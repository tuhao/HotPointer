package com.apesRise.hotPointer.core.byes;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.Constant;
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.WordCount;

public class ByesLearn {
	
	private static ThriftClient client = ThriftClient.getInstance();
	
	public static Map<String,Double> propMap = new HashMap<String,Double>();
	
	static{
		learnFromLocal();
	}
	
	private static void learnFromRemote(){
		List<Message> unApprovedMsgs = client.getAllUnRelated();
		Map<String,Double> primaryMap = wordFrequece(wordCount(unApprovedMsgs),unApprovedMsgs.size(),1);
		
		List<Message> approvedMsgs = client.getAllSyncApproved();
		Map<String,Double> aidMap = wordFrequece(wordCount(approvedMsgs),approvedMsgs.size(),2);
		
		propMap = wordProp(primaryMap,aidMap);
	}
	
	private static void learnFromLocal(){
		List<Message> unApprovedMsgs = new LinkedList<Message>();
		File unrelated = new File(Constant.UNRELATED_DIR);
		for(File item :unrelated.listFiles()){
			Message msg = new Message();
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			unApprovedMsgs.add(msg);
		}
		Map<String,Double> primaryMap = wordFrequece(wordCount(unApprovedMsgs),unApprovedMsgs.size(),1);
		
		List<Message> approvedMsgs = new LinkedList<Message>();
		File approve = new File(Constant.APPROVE_DIR);
		for(File item :approve.listFiles()){
			Message msg = new Message();
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			approvedMsgs.add(msg);
		}
		Map<String,Double> aidMap = wordFrequece(wordCount(approvedMsgs),approvedMsgs.size(),2);
		propMap = wordProp(primaryMap,aidMap);
	}
	
	/**
	 * 
	 * @param messages
	 * @return
	 */
	private static Map<String,Integer> wordCount(List<Message> messages){
		Map<String,Integer> wordCountMap = new HashMap<String,Integer>();
		for(Message msg : messages){
			WordCount.chineseCharacterWordCount(wordCountMap, msg.getContent());
		}
		return wordCountMap;
	}
	
	/**
	 * 
	 * @param wordCountMap
	 * @param multy
	 * @return
	 */
	private static Map<String,Double> wordFrequece(Map<String,Integer> wordCountMap,int sum,int multy){
		Map<String,Double> wordFrequenceMap = new HashMap<String, Double>();
		BigDecimal clusterSum = new BigDecimal(sum);
		for(Iterator<Entry<String, Integer>> it = wordCountMap.entrySet().iterator();it.hasNext();){
			Entry<String, Integer> entry = (Entry<String, Integer>)it.next();
			String word = (String)entry.getKey();
			BigDecimal divide = new BigDecimal((Integer)entry.getValue() * multy);
			double result = divide.divide(clusterSum,4,BigDecimal.ROUND_HALF_UP).doubleValue();
			result = result > 1.0 ? 1.0 : result;
			wordFrequenceMap.put(word, result);
		}
		return wordFrequenceMap;
	}
	
	/**
	 * 
	 * @param primaryMap
	 * @param aidMap
	 * @return
	 */
	private static Map<String,Double> wordProp(Map<String,Double> primaryMap,Map<String,Double> aidMap){
		Map<String,Double> propMap = new HashMap<String,Double>();
		
		for(Iterator<Entry<String, Double>> it = aidMap.entrySet().iterator();it.hasNext();){
			Entry<String, Double> entry = (Entry<String, Double>)it.next();
			String word = (String)entry.getKey();
			double frequence = (Double)entry.getValue();
			double result = 0.01;
			if (primaryMap.get(word) != null){
				BigDecimal numerator = new BigDecimal(primaryMap.get(word));
				BigDecimal denominator = new BigDecimal(frequence + primaryMap.get(word));
				denominator = denominator.doubleValue() == 0 ? new BigDecimal(0.0001) : denominator;
				result = numerator.divide(denominator,4,BigDecimal.ROUND_HALF_UP).doubleValue();
				result = result > 0.99 ? 0.99 : result;
				result = result > 0.01 ? result : 0.01;
			}
			propMap.put(word,result);
		}
		
		for(Iterator<Entry<String, Double>> it = primaryMap.entrySet().iterator();it.hasNext();){
			Entry<String, Double> entry = (Entry<String, Double>)it.next();
			String word = (String)entry.getKey();
			if (propMap.get(word) != null) continue;
			double frequence = (Double)entry.getValue();
			double result = 0.01;
			if (aidMap.get(word) != null){
				BigDecimal numerator = new BigDecimal(frequence);
				BigDecimal denominator = new BigDecimal(frequence + aidMap.get(word));
				denominator = denominator.doubleValue() == 0 ? new BigDecimal(0.0001) : denominator;
				result = numerator.divide(denominator,4,BigDecimal.ROUND_HALF_UP).doubleValue();
				result = result > 0.99 ? 0.99 : result;
				result = result > 0.01 ? result : 0.01;
			}
			propMap.put(word,result);
		}
		
		return propMap;
	}

}

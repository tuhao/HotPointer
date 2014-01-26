package com.apesRise.hotPointer.core.knn;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.Constant;
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.ReadByLine;
import com.apesRise.hotPointer.util.WordCount;

public class KnnModel {
	
	private static List<String> properties = new LinkedList<String>();
	private static List<KNN> metric = new LinkedList<KNN>();
	
	static class KNN{
		int msgId;
		List<Map<String,Integer>> valueMaps;
		boolean result = false;
		
		public KNN(int msgId,List<Map<String,Integer>> valueMaps,boolean result){
			this.msgId = msgId;
			this.valueMaps = valueMaps;
			this.result = result;
		}
	}
	
	private static List<Message> approvedMsgs = new LinkedList<Message>();
	private static List<Message> unApprovedMsgs = new LinkedList<Message>();
	
	static {
		learnFromLocal();
	}
	
	private static void learnFromLocal(){
		properties = ReadByLine.readByLine(Constant.KNN_PROPERTY_FILE, "utf-8");
		
		File unrelated = new File(Constant.UNRELATED_DIR);
		for(File item :unrelated.listFiles()){
			Message msg = new Message();
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			unApprovedMsgs.add(msg);
		}
		File approve = new File(Constant.APPROVE_DIR);
		for(File item :approve.listFiles()){
			Message msg = new Message();
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			approvedMsgs.add(msg);
		}
		initKnnModel(metric,properties,approvedMsgs,true);
		initKnnModel(metric,properties,unApprovedMsgs,false);
	}
	
	private static void initKnnModel(List<KNN> metric,List<String> properties,List<Message> msgs,boolean result){
		for(Message msg:msgs){
			List<Map<String,Integer>> valueMaps = new LinkedList<Map<String,Integer>>();
			Map<String,Integer> wordCountMap = new HashMap<String,Integer>();
			WordCount.chineseCharacterWordCount(wordCountMap, msg.getContent());
			for(String property : properties){
				Map<String,Integer> valueMap = new HashMap<String,Integer>();
				if (wordCountMap.get(property) == null){
					valueMap.put(property,0);
				}else{
					//计算值
					
					valueMap.put(property,wordCountMap.get(property));
				}
				valueMaps.add(valueMap);
			}
			metric.add(new KNN(msg.getId(),valueMaps,result));
		}
	}
	
	
	
	private static void preProcess(){
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
//		List<Message> messages = ThriftClient.getInstance().getAllUnRelated();
		for(Message msg : approvedMsgs){
			WordCount.chineseCharacterWordCount(wordCount, msg.getContent());
		}
		Map<Integer,List<String>> sortMap = new HashMap<Integer,List<String>>();
		for(Iterator it = wordCount.entrySet().iterator();it.hasNext();){
			Entry entry = (Entry)it.next();
			String word = (String)entry.getKey();
			int count = (Integer)entry.getValue();
			if(sortMap.get(count) != null){
				sortMap.get(count).add(word);
			}else{
				List<String> list = new LinkedList<String>();
				list.add(word);
				sortMap.put(count, list);
			}
		}
		Integer[] counts = new Integer[sortMap.size()];
		sortMap.keySet().toArray(counts);
		Arrays.sort(counts);
		for(int i = counts.length - 1;i >=0;i --){
			System.out.println(counts[i] + " : " + sortMap.get(counts[i]));
		}
	}
	
	public static void main(String[] args) {
//		preProcess();
	}

}

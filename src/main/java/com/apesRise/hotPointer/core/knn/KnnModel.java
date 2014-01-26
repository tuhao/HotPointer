package com.apesRise.hotPointer.core.knn;

import java.io.File;
import java.util.Arrays;
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

public class KnnModel {
	
	private static List<Message> approvedMsgs = new LinkedList<Message>();
	private static List<Message> unApprovedMsgs = new LinkedList<Message>();
	
	static {
		learnFromLocal();
	}
	
	private static void learnFromLocal(){
		
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
		preProcess();
	}

}

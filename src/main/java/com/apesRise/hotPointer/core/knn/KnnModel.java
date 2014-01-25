package com.apesRise.hotPointer.core.knn;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.CharacterEncoding;
import com.apesRise.hotPointer.util.WordCount;

public class KnnModel {
	
	
	
	private static void preProcess(){
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
		List<Message> messages = ThriftClient.getInstance().pullBySort(200, 1);
		for(Message msg : messages){
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

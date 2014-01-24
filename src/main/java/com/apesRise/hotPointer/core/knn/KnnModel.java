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

import com.apesRise.hotPointer.core.simhash.CharacterEncoding;
import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;

public class KnnModel {
	
	
	
	private static void preProcess(){
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
		List<Message> messages = ThriftClient.getInstance().pullBySort(200, 1);
		for(Message msg : messages){
			StringReader line = new StringReader(msg.getContent());
			IKSegmenter segment = new IKSegmenter(line,true);
			try {
				for(Lexeme lexeme = segment.next();lexeme != null;lexeme=segment.next()){
					String word = lexeme.getLexemeText();
					if (!CharacterEncoding.isChinese(word)) continue;
					if(wordCount.get(word) != null){
						wordCount.put(word, wordCount.get(word) + 1);
					}else{
						wordCount.put(word, 1);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		for(int count:counts){
			System.out.println(count + " : " + sortMap.get(count));
		}
		
	}
	
	public static void main(String[] args) {
		preProcess();
	}

}

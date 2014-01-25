package com.apesRise.hotPointer.core.byes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.apesRise.hotPointer.util.WordCount;

public class ByesFilter {
	
	private static int tokenNum = 7;
	private static double defaultProp = 0.40;
	
	public static double prop(String text){
		BigDecimal numerator = new BigDecimal(1);
		BigDecimal temp = new BigDecimal(1);
		for(String token : getTokens(text)){
			double p = tokenProp(token);
			numerator = numerator.multiply(new BigDecimal(p),MathContext.DECIMAL32) ;
			temp = temp.multiply(new BigDecimal(1 - p), MathContext.DECIMAL32);
		}
		BigDecimal denominator = numerator.add(temp);
		return numerator.divide(denominator, 4, BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	
	private static double tokenProp(String token){
		if(ByesLearn.propMap.get(token) == null){
			return defaultProp;
		}
		return ByesLearn.propMap.get(token);
	}
	
	private static List<String> getTokens(String text){
		List<String> tokens = new LinkedList<String>();
		Map<String,Integer> wordCountMap = new HashMap<String,Integer>();
		WordCount.chineseCharacterWordCount(wordCountMap, text);
		
		Map<Integer,List<String>> sortMap = new HashMap<Integer,List<String>>();
		for(Iterator<Entry<String, Integer>> it = wordCountMap.entrySet().iterator();it.hasNext();){
			Entry<String, Integer> entry = (Entry<String, Integer>)it.next();
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
		int count = 0;
		for(int i = counts.length - 1;i >=0;i --){
			for(String word : sortMap.get(counts[i])){
				tokens.add(word);
				if(++count >= tokenNum) break;
			}
//			System.out.println(counts[i] + " : " + sortMap.get(counts[i]));
		}
		return tokens;
	}
	

}

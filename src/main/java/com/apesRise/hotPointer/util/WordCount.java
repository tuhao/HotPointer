package com.apesRise.hotPointer.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;


public class WordCount {
	
	static{
		Configuration conf = DefaultConfig.getInstance();
		Dictionary.initial(conf);
		Dictionary dictionary = Dictionary.getSingleton();
		List<String> properties = new LinkedList<String>();
		properties.addAll(ReadFileByLine.getAllLine2Array(Constant.KNN_COOK_PROPERTY_FILE,"utf-8"));
		properties.addAll(ReadFileByLine.getAllLine2Array(Constant.KNN_DELICIOUS_PROPERTY_FILE,"utf-8"));
		properties.addAll(ReadFileByLine.getAllLine2Array(Constant.KNN_HEALTHY_PROPERTY_FILE,"utf-8"));
		dictionary.addWords(properties);
	}
	
	public static void chineseCharacterWordCount(Map<String,Integer> wordCountMap,String text){
		StringReader line = new StringReader(text);
		IKSegmenter segment = new IKSegmenter(line,true);
		try {
			for(Lexeme lexeme = segment.next();lexeme != null;lexeme=segment.next()){
				String word = lexeme.getLexemeText();
				if (!CharacterEncoding.isChinese(word)) continue;
				if(wordCountMap.get(word) != null){
					wordCountMap.put(word, wordCountMap.get(word) + 1);
				}else{
					wordCountMap.put(word, 1);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}

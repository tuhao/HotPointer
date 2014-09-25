package com.apesRise.hotPointer.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.cfg.DefaultConfig;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.dic.Dictionary;


public class WordCount {
	
	static {
		Configuration conf = DefaultConfig.getInstance();
		Dictionary.initial(conf);
		Dictionary dictionary = Dictionary.getSingleton();
		List<String> properties = ReadFileByLine.getAllLine2Array("train/train_attributes.txt", "utf-8");
		dictionary.addWords(properties);
	}
	public static void chineseCharacterWordCount(Map<String,Integer> wordCountMap,String text){
		StringReader line = new StringReader(text);
		IKSegmenter segment = new IKSegmenter(line,false);
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

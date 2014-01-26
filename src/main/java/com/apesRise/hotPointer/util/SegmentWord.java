package com.apesRise.hotPointer.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

public class SegmentWord {
	
	public static LinkedList<String> segment(String content,int skiplength){
		StringReader text = new StringReader(content);
		IKSegmenter segment = new IKSegmenter(text,true);
		LinkedList<String> result = new LinkedList<String>();
		try {
			for(Lexeme lexeme = segment.next();lexeme != null;lexeme=segment.next()){
				String word = lexeme.getLexemeText();
				if (!CharacterEncoding.isChinese(word)) continue;
				if (skiplength>=word.length()) continue;
				result.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

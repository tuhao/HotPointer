package com.apesRise.hotPointer.core.simhash;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;

public class Deduplicate {
	
	private static void dedupDB(){
		ThriftClient client = ThriftClient.getInstance();
		List<Message> list = client.pullBySort(100, 2);
		for (Message msg:list){
			StringReader line = new StringReader(msg.getContent());
			IKSegmenter segment = new IKSegmenter(line, false);
			StringBuffer sb = new StringBuffer();
			try {
				for(Lexeme lexeme = segment.next();lexeme != null;segment.next()){
					String word = lexeme.getLexemeText();
					sb.append(word).append(" ");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

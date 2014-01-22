package com.apesRise.hotPointer.core.simhash;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;

public class Deduplicate {
	
	public static Map<Integer, Map<String, List<SimHash>>> simHashMap = new HashMap<Integer,Map<String,List<SimHash>>>();
	static{
		for(int i =0;i < SimHash.DIVIDED;i++){
			Map<String, List<SimHash>> map = new HashMap<String,List<SimHash>>();
			simHashMap.put(i, map);
		}
	}
	
	public static void main(String[] args) {
		dedupDB();
	}
	
	private static void dedupDB(){
		ThriftClient client = ThriftClient.getInstance();
		List<Message> msgs = client.pullBySort(200, 1);
		Map<Integer,Message> msgMap = new HashMap<Integer,Message>();
		for (Message msg:msgs){
			msgMap.put(msg.getId(), msg);
			SimHash similarOne = null;
			boolean hasSimilar = false;
			StringReader line = new StringReader(msg.getContent());
			IKSegmenter segment = new IKSegmenter(line, false);
			StringBuffer sb = new StringBuffer();
			try {
				for(Lexeme lexeme = segment.next();lexeme != null;lexeme = segment.next()){
					String word = lexeme.getLexemeText();
					if(CharacterEncoding.isChinese(word)){
						sb.append(word).append(" ");
					}
				}
				SimHash simHash = new SimHash(msg.getId(),sb.toString());
				List<String> subs = new LinkedList<String>();
				for(int i =0;i < 64;i=i+16){
					subs.add(simHash.getStrSimHash().substring(i, i + 16));
				}
				
//				System.out.println(subs.size());
				for(int i = 0;i < SimHash.DIVIDED;i++){
					String sub = subs.get(i);
					Map<String, List<SimHash>> LinkedMap= simHashMap.get(i);
					if (LinkedMap.get(sub) == null){
						List<SimHash> simHashList  = new LinkedList<SimHash>();
						simHashList.add(simHash);
						LinkedMap.put(sub, simHashList);
					}else{
						for(SimHash item:LinkedMap.get(sub)){
							if(simHash.hammingDistance(item) <= SimHash.DISTANCE){
								hasSimilar = true;
								similarOne = item;
								System.out.println("================================");
								System.out.println("Older one msg: " + msgMap.get(similarOne.getMsgId()).getContent());
								System.out.println("Older one tokens: " + similarOne.getTokens());
								System.out.println("Similar input: " + msgMap.get(simHash.getMsgId()).getContent());
								System.out.println("Similar tokens : " + simHash.getTokens());
								System.out.println("hammingDistance:" + simHash.hammingDistance(item));
								System.out.println("================================");
								break;
							}
						}
						LinkedMap.get(sub).add(simHash);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception er){
				er.printStackTrace();
			}
		}
	}
	
	private static void delete(int msgId){
		System.out.println(msgId);
	}
	
	

}

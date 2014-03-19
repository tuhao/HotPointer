package com.apesRise.hotPointer.core.simhash;

import java.io.IOException;
import java.io.StringReader;
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
import com.apesRise.hotPointer.util.CharacterEncoding;

public class Deduplicate {
	
	public static ThriftClient client = ThriftClient.getInstance();
		
	public Map<Integer, Map<String, List<SimHash>>> simHashMapInit(){
		
		Map<Integer, Map<String, List<SimHash>>> simHashMap = new HashMap<Integer,Map<String,List<SimHash>>>();
		for(int i =0;i < SimHash.DIVIDED;i++){
			Map<String, List<SimHash>> map = new HashMap<String,List<SimHash>>();
			simHashMap.put(i, map);
		}
		return simHashMap;
	}
	
	/**
	 * 元数据表去重
	 */
	public void dedupMetaDB(){
		List<Message> msgs = client.getAllMsg();
		List<Integer> duplicates = dedup(simHashMapInit(),msgs);
		if(client.deleteMeta(duplicates)){
			System.out.println(duplicates.size() + " duplicates messages deleted from approve_metadata");
		}else{
			System.out.println("duplicates messages delete trasaction fail..");
		}
	}
	
	/**
	 * 推荐信息表去重
	 
	public void dedupMessageDB(){
		Deduplicate dedup = new Deduplicate();
		List<Message> msgs = client.getAllSyncApproved();
		List<Integer> duplicates = dedup.dedup(dedup.simHashMapInit(),msgs);
		client.deleteMsgs(duplicates);
		System.out.println(duplicates.size() + " duplicates items deleted from signature_message");
	}
	*/
	
	/**
	 * 
	 */
	public void syncApproved(List<Message> approveResult,List<Message> approvedMsgs){
		List<Message> result = new LinkedList<Message>();
		List<Integer> duplicates = dedup(approveResult,approvedMsgs,result);
		int count = client.pushApprove(result);
		System.out.println(result.size() + " approve messages ,pushed " + count);
		if(client.deleteMeta(duplicates)){
			System.out.println(duplicates.size() + " duplicates approve messages deleted from approve_metadata");
		}else{
			System.out.println("Approve duplicates messages delete trasaction fail..");
		}
	}
	
	
	/**
	 * 
	 * @param deliciousResult
	 * @param deliciousMsgs
	 */
	public void syncDelicious(List<Message> deliciousResult,List<Message> deliciousMsgs){
		List<Message> result = new LinkedList<Message>();
		List<Integer> duplicates = dedup(deliciousResult,deliciousMsgs,result);
		int count = client.pushDelicious(result);
		System.out.println(result.size() + " delicious messages ,pushed " + count);
		if(client.deleteMeta(duplicates)){
			System.out.println(duplicates.size() + " duplicates delicious messages deleted from approve_metadata");
		}else{
			System.out.println("Delicious duplicates messages delete trasaction fail..");
		}
		
	}
	
	/**
	 * 
	 * @param newMsgs
	 * @param passed
	 * @param result
	 * @return
	 */
	private List<Integer> dedup(List<Message> newMsgs,List<Message> passed,List<Message> result){
			
		Map<Integer, Map<String, List<SimHash>>> simHashMap = simHashMapInit();
		dedup(simHashMap,passed);

		List<Integer> duplicates = dedup(simHashMap, newMsgs);
		
		Set<Integer> cache = new HashSet<Integer>();
		for(int msgId : duplicates){
			cache.add(msgId);
		}
		for(Message msg : newMsgs){
			if(cache.add(msg.getId())){
				result.add(msg);
			}
		}
		for(Message msg:result){
			duplicates.add(msg.getId());
			System.out.println("push new message:" + msg.getContent());
		}
		return duplicates;
	}
	
	
	/**
	 * simHash 去重
	 * @param simHashMap
	 * @param msgs
	 * @return
	 */
	private List<Integer> dedup(Map<Integer, Map<String, List<SimHash>>> simHashMap,List<Message> msgs){
		List<Integer> dupicateIds = new LinkedList<Integer>();
		for (Message msg:msgs){
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
				boolean similar = false;
				for(int i = 0;i < SimHash.DIVIDED;i++){
					String sub = subs.get(i);
					Map<String, List<SimHash>> LinkedMap = simHashMap.get(i);
					if (LinkedMap.get(sub) == null){
						List<SimHash> simHashList  = new LinkedList<SimHash>();
						simHashList.add(simHash);
						LinkedMap.put(sub, simHashList);
					}else{
						for(SimHash item:LinkedMap.get(sub)){
							if(simHash.hammingDistance(item) <= SimHash.DISTANCE){
								dupicateIds.add(simHash.getMsgId());
								similar = true;
								break;
							}
						}
						LinkedMap.get(sub).add(simHash);
						if(similar) break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch(Exception er){
				er.printStackTrace();
			}
		}
		return dupicateIds;
	}
	

}

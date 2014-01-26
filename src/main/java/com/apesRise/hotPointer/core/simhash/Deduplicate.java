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
	 * 信息采集表去重
	 */
	public void dedupMetaDB(){
		Deduplicate dedup = new Deduplicate();
		List<Message> msgs = client.getAllMsg();
		List<Integer> duplicates = dedup.dedup(dedup.simHashMapInit(),msgs);
		client.deleteIds(duplicates);
		for(int id:duplicates){
			System.out.println("delete " + id);
		}
		System.out.println(duplicates.size() + " duplicates items deleted");
	}
	
	
	/**
	 * 将审核通过的信息更新到信息查询表
	 */
	public void syncApproved(){
		Deduplicate dedup = new Deduplicate();
		List<Message> msgs = client.getAllSyncApproved();
		
		Map<Integer, Map<String, List<SimHash>>> simHashMap = dedup.simHashMapInit();
		dedup.dedup(simHashMap,msgs);
		
		msgs = client.getAllUnSyncApproved();
		List<Integer> duplicates = dedup.dedup(simHashMap, msgs);
		
		Set<Integer> cache = new HashSet<Integer>();
		for(int msgId : duplicates){
			cache.add(msgId);
		}
		List<Message> pushApproveList = new LinkedList<Message>();
		for(Message msg : msgs){
			if(cache.add(msg.getId())){
				pushApproveList.add(msg);
			}
		}
		client.pushApprove(pushApproveList);
		for(Message msg:pushApproveList){
			duplicates.add(msg.getId());
			System.out.println("push approved message:" + msg.getContent());
		}
		System.out.println(pushApproveList.size() + " approved messages pushed");
		client.deleteIds(duplicates);
		System.out.println(duplicates.size() + " duplicates messages deleted");
	}
	
	/**
	 * simHash 去重
	 * @param simHashMap
	 * @param msgs
	 * @return
	 */
	public List<Integer> dedup(Map<Integer, Map<String, List<SimHash>>> simHashMap,List<Message> msgs){
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
		return dupicateIds;
	}
	

	public static void main(String[] args) {
		Deduplicate dedup = new Deduplicate();
		dedup.dedupMetaDB();
		
		dedup = new Deduplicate();
		dedup.syncApproved();
		
	}

}

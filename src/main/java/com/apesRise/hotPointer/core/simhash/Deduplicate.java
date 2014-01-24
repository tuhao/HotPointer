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

public class Deduplicate {
	
	public static ThriftClient client = ThriftClient.getInstance();
	
	private static int itemNum = 500;
	private static int meta = 1;
	private static int approved = 2;
	
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
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = client.getMsgCount();
		for (int i =0;i < msgSum + itemNum;i = i + itemNum){
			msgs.addAll(client.pullPaginateMsg(i,itemNum));
		}
		client.deleteIds(dedup.dedup(dedup.simHashMapInit(),msgs));
	}
	
	public static void main(String[] args) {
		Deduplicate dedup = new Deduplicate();
		dedup.dedupMetaDB();
		
		dedup = new Deduplicate();
		dedup.syncApproved();
		
	}
	
	/**
	 * 将审核通过的信息更新到信息查询表
	 */
	public void syncApproved(){
		Deduplicate dedup = new Deduplicate();
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = client.getApproveCount();
		for (int i =0;i < msgSum + itemNum;i = i + itemNum){
			msgs.addAll(client.pullPaginateApprove(i,itemNum));
		}
		Map<Integer, Map<String, List<SimHash>>> simHashMap = dedup.simHashMapInit();
		dedup.dedup(simHashMap,msgs);
		
		msgs = new LinkedList<Message>();
		msgSum = client.getMsgCountBySort(approved);
		for (int i =0;i < msgSum + itemNum;i = i + itemNum){
			msgs.addAll(client.pullPaginateMsgBySort(i,itemNum,approved));
		}
		List<Integer> duplicates = dedup.dedup(simHashMap, msgs);
		client.deleteIds(duplicates);
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
	}
	
	
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

}

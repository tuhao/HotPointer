package com.apesRise.hotPointer.main;

import java.util.LinkedList;
import java.util.List;

import com.apesRise.hotPointer.core.knn.KnnModel;
import com.apesRise.hotPointer.core.simhash.Deduplicate;
import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.Constant;
import com.apesRise.hotPointer.util.ReadByLine;

public class SyncMessage {
	
	private static ThriftClient client = ThriftClient.getInstance();

	public static void main(String[] args) {
		
		Deduplicate dedup = new Deduplicate();
		List<Integer> unPassedIds = new LinkedList<Integer>();
		List<Message> newMsgs = client.getAllMsgBySort(Constant.META, 0);
		List<Message> unPassed = client.getAllMsgBySort(Constant.UNRELATED, 2000);
		
		/**菜谱信息**/
		List<Message> approvedMsgs = client.getAllSyncApproved(2000);
		List<String> approveProperties = ReadByLine.readByLine(Constant.KNN_APPROVE_PROPERTY_FILE, "utf-8");
		List<Message> approveResult = client.getAllMsgBySort(Constant.APPROVED, 0);  //经过页面审批为菜谱但未同步的数据
		unPassedIds.addAll(judge(newMsgs, approvedMsgs, unPassed, approveProperties, approveResult));
		dedup.syncApproved(approveResult,approvedMsgs);
		
		/**美食信息
		List<Message> deliciousMsgs = client.getAllDelicious(0);
		List<String> deliciousProperties = ReadByLine.readByLine(Constant.KNN_DELICIOUS_PROPERTY_FILE, "utf-8");
		List<Message> deliciousResult = client.getAllMsgBySort(Constant.DELICIOUS, 0); //经过页面审批为美食但未同步的数据
		unPassedIds.addAll(judge(newMsgs, deliciousMsgs, unPassed, deliciousProperties, deliciousResult));
		dedup.syncDelicious(deliciousResult, deliciousMsgs);
		
		/**标记未通过审核为无关**/
		markUnpassed(unPassedIds);
		
	}
	
	/**
	 * Knn分类计算
	 * @param newMsgs   新输入数据列表
	 * @param passed    通过的训练集列表
	 * @param unPassed  未通过的训练集列表
	 * @param properties 分类属性列表
	 * @param result    通过的数据列表
	 * @return          未通过数据id列表
	 */
	private static List<Integer> judge(List<Message> newMsgs,List<Message> passed,List<Message> unPassed,List<String> properties,List<Message> result){
		List<Integer> unPassedIds = new LinkedList<Integer>();
		KnnModel knnModel = new KnnModel(passed,unPassed,properties);
		for(Message msg : newMsgs){
			if(knnModel.judge(msg.getContent())){
				result.add(msg);
			}else{
				unPassedIds.add(msg.getId());
			}
		}
		return unPassedIds;
	}
	
	private static void markUnpassed(List<Integer> unPassedIds){
		System.out.println("sortMark :" + unPassedIds.size() + " items => " + client.msgSortMark(unPassedIds, Constant.UNRELATED));
	}
	
	/**
	 * 对元数据表数据去重
	 */
	private static void dedupMetaDB(){
		Deduplicate dedup = new Deduplicate();
		dedup.dedupMetaDB();
	}
	
	

}

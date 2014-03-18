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
		
		List<Integer> unPassedIds = new LinkedList<Integer>();
		unPassedIds.addAll(approveMsg());
		unPassedIds.addAll(deliciousMsg());
		markUnpassed(unPassedIds);
	}
	
	/**
	 * 对新进元数据进行分类，筛选出推荐数据入库
	 * @return 未通过的id列表
	 */
	private static List<Integer> approveMsg(){
		Deduplicate dedup = new Deduplicate();
		List<Message> newMsgs = client.getAllMsgBySort(Constant.META, 0);
		List<Message> approveResult = new LinkedList<Message>();
		List<Message> approvedMsgs = client.getAllSyncApproved();
		List<Message> unApprovedMsgs = client.getAllMsgBySort(Constant.UNRELATED, 0);
		List<String> properties = ReadByLine.readByLine(Constant.KNN_PROPERTY_FILE, "utf-8");
		KnnModel knnModel = new KnnModel(approvedMsgs,unApprovedMsgs,properties);
		List<Integer> unPassedIds = new LinkedList<Integer>();
		for(Message msg : newMsgs){
			if(knnModel.judge(msg.getContent())){
				approveResult.add(msg);
			}else{
				unPassedIds.add(msg.getId());
			}
		}
		//同步经过审批的数据
		approveResult.addAll(client.getAllMsgBySort(Constant.APPROVED, 0));
		dedup.syncApproved(approveResult);
		return unPassedIds;
	}
	
	private static List<Integer> deliciousMsg(){
		List<Integer> unPassedIds = new LinkedList<Integer>();
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

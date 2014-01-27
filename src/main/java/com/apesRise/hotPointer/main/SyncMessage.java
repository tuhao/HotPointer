package com.apesRise.hotPointer.main;

import java.util.LinkedList;
import java.util.List;

import com.apesRise.hotPointer.core.knn.KnnModel;
import com.apesRise.hotPointer.core.simhash.Deduplicate;
import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.Constant;

public class SyncMessage {
	
	private static ThriftClient client = ThriftClient.getInstance();

	public static void main(String[] args) {
		//对元数据表中数据去重
		Deduplicate dedup = new Deduplicate();
//		dedup.dedupMetaDB();
		
		List<Message> newMsgs = client.getAllMetaMsg();
		List<Message> approvedMsgs = new LinkedList<Message>();
		List<Integer> ids = new LinkedList<Integer>();
		KnnModel knnModel = new KnnModel();
		for(Message msg : newMsgs){
			if(knnModel.judge(msg.getContent())){
				approvedMsgs.add(msg);
			}else{
				ids.add(msg.getId());
			}
		}
		//同步经过审批的数据
		approvedMsgs.addAll(client.getAllUnSyncApproved());
		dedup.syncApproved(approvedMsgs);
		System.out.println("sortMark :" + ids.size() + " items => " + client.msgSortMark(ids, Constant.UNRELATED));
		
	}

}
package com.apesRise.hotPointer.main;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.apesRise.hotPointer.core.knn.KnnModel;
import com.apesRise.hotPointer.core.simhash.Deduplicate;
import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.Constant;
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.ReadByLine;

public class SyncMessage {
	
	private static ThriftClient client = ThriftClient.getInstance();

	private static void cookbook(){

		List<Message> trainPassedMsgs = new LinkedList<Message>();
		List<Message> trainUnPassedMsgs = new LinkedList<Message>();
		List<String> properties = ReadByLine.readByLine(Constant.KNN_COOK_PROPERTY_FILE,"utf-8");
		
		final String trainPassedPath = "train/cookbook";
		final String trainUnPassedPath = "train/unrelated_cook";
		
		File file = new File(trainPassedPath);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			trainPassedMsgs.add(msg);
		}

		file = new File(trainUnPassedPath);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,
					item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			trainUnPassedMsgs.add(msg);
		}
		List<Message> newMsgs = client.getAllMsgBySort(Constant.META, 0);
		List<Message> approveResult = client.getAllMsgBySort(Constant.APPROVED, 0);  //经过页面审批为菜谱但未同步的数据
		List<Message> unPassedMsgs = judge(newMsgs, trainPassedMsgs, trainUnPassedMsgs, properties, approveResult);
		Deduplicate dedup = new Deduplicate();
		dedup.syncApproved(approveResult);
		dedup.syncUnRelated(unPassedMsgs);
	}
	
	
	public static void main(String[] args) {
		cookbook();
	}
	
	/**
	 * Knn分类计算
	 * @param newMsgs   新输入数据列表
	 * @param passed    通过的训练集列表
	 * @param unPassed  未通过的训练集列表
	 * @param properties 分类属性列表
	 * @param result    通过的数据列表
	 * @return          未通过数据
	 */
	private static List<Message> judge(List<Message> newMsgs,List<Message> passed,List<Message> unPassed,List<String> properties,List<Message> result){
		List<Message> unPassedMsgs = new LinkedList<Message>();
		KnnModel knnModel = new KnnModel(passed,unPassed,properties);
		for(Message msg : newMsgs){
			if(knnModel.judge(msg.getContent())){
				result.add(msg);
			}else{
				unPassedMsgs.add(msg);
			}
		}
		return unPassedMsgs;
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

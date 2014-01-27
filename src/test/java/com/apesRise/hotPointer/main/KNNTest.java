package com.apesRise.hotPointer.main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.apesRise.hotPointer.core.knn.KnnModel;
import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.Constant;
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.WFile;

public class KNNTest {
	
	static ThriftClient client  = ThriftClient.getInstance();
	
	public static void main(String[] args) {
		String content = "哈哈，菜菜搜罗的超级详细的教学贴，垂涎欲滴的小蛋糕，" +
				"你完全可以轻松驾驭啊！转发=>【手把手教你平底锅小蛋糕】不要以为" +
				"糕点那都是大师级才能驾驭得了的，跟着步骤做，你一样可以做出让人垂" +
				"涎欲滴的小蛋糕哟，而且不用烤箱做蛋糕哦，还不快快学起来! [din推撞]" +
				" | photo by DIY达人http://ww1.sinaimg.cn/bmiddle/80891114gw1ecart676xqj20c80wajv0.jpg";
		KnnModel knnModel = new KnnModel();
		knnModel.DEBUG = true;
		System.out.println(knnModel.judge(content));
	}
	
	
	private static void knnFilterTest(){
		List<Message> msgs = new LinkedList<Message>();
		File file = new File(Constant.META_DIR);
		for(File item:file.listFiles()){
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0, item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			msgs.add(msg);
		}
		String approveDir = "train/delta_approve/";
		String unrelatedDir = "train/delta_unrelated/";
		KnnModel knnModel = new KnnModel();
		for(Message msg:msgs){
			boolean result = knnModel.judge(msg.getContent());
			String output = unrelatedDir;
			if(result){
				output = approveDir;
			}
			WFile.wf(output + msg.getId() + ".txt", msg.getContent(), false);
//			System.out.println(KnnModel.judge(msg.getContent()) + " " + msg.getContent());
		}
	}
	
	/*
	private static void knnPropertyTest(){
		List<Message> msgs = new LinkedList<Message>();
		File file = new File("train/unrelated/");
		for(File item:file.listFiles()){
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0, item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			msg.result = false;
			msgs.add(msg);
		}
		file = new File("train/approve/");
		for(File item:file.listFiles()){
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0, item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			msg.result = true;
			msgs.add(msg);
		}
		System.out.println(msgs.size() + " messages inputed...");
		List<String> properties = ReadByLine.readByLine(Constant.KNN_PROPERTY_FILE, "utf-8");
		List<String> temp = new LinkedList<String>();
		
		for (int i = 0;i < properties.size(); i++){
			int wrong = 0;
			temp.add(properties.get(i));
			KnnModel knnModel = new KnnModel();
			knnModel.learnFromLocal(temp);
			for(Message msg : msgs){
				if(knnModel.judge(msg.getContent()) != msg.result){
					wrong ++;
				}
			}
			System.out.println("properties is " + i + " and wrong is " + wrong);
		}
		
	}
	*/
	

	/*
	private static void preProcess(){
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
//		List<Message> messages = ThriftClient.getInstance().getAllUnRelated();
		for(Message msg : approvedMsgs){
			WordCount.chineseCharacterWordCount(wordCount, msg.getContent());
		}
		Map<Integer,List<String>> sortMap = new HashMap<Integer,List<String>>();
		for(Iterator it = wordCount.entrySet().iterator();it.hasNext();){
			Entry entry = (Entry)it.next();
			String word = (String)entry.getKey();
			int count = (Integer)entry.getValue();
			if(sortMap.get(count) != null){
				sortMap.get(count).add(word);
			}else{
				List<String> list = new LinkedList<String>();
				list.add(word);
				sortMap.put(count, list);
			}
		}
		Integer[] counts = new Integer[sortMap.size()];
		sortMap.keySet().toArray(counts);
		Arrays.sort(counts);
		for(int i = counts.length - 1;i >=0;i --){
			System.out.println(counts[i] + " : " + sortMap.get(counts[i]));
		}
	}
	*/

}

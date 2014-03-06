package com.apesRise.hotPointer.main;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.apesRise.hotPointer.core.knn.KnnModel;
import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.Constant;
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.WFile;
import com.apesRise.hotPointer.util.WordCount;

public class KNNTest {
	
	static ThriftClient client  = ThriftClient.getInstance();
	
	public static void main(String[] args) {
		String content = "【金针菇牛肉卷】材料：肥牛肉十五片，金针菇一把。做法：用生抽，蒜末，耗油，蜂蜜，水，混在一起，腌牛肉大约20分钟。烤箱预热375华氏。将牛肉包在金针菇外面，在烤箱烤10分钟。拿出后可直接食用，也可洒一点鳗鱼汁，或者用一勺酱油，一勺蜂蜜，一勺水，一勺汤，混和烧开后勾芡做汁。#美食#http://app.qpic.cn/mblogpic/5a98254b9565e3425512/460.jpg";
		KnnModel knnModel = new KnnModel();
		knnModel.DEBUG = true;
		System.out.println(knnModel.judge(content));
//		wordCount();
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
	

	private static void wordCount(){
		Map<String,Integer> wordCount = new HashMap<String,Integer>();
		List<Message> messages = ThriftClient.getInstance().getAllSyncApproved();
		for(Message msg : messages){
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

}

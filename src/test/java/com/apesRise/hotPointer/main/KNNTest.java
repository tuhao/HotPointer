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
import com.apesRise.hotPointer.util.ReadByLine;
import com.apesRise.hotPointer.util.WFile;
import com.apesRise.hotPointer.util.WordCount;

public class KNNTest {

	static ThriftClient client = ThriftClient.getInstance();

	public static void main(String[] args) {
		/*
		String content = "【 “舌尖上的新疆”新鲜出炉！】网友：“对于我们这些吃货来说，任何妄图把大盘鸡拉条子羊肉串烤包子馕包肉哈密瓜葡萄干从祖国分离出去成为进口食品的阴谋都是痴心妄想！！”戳图↓↓50款精选新疆美食，哪一样是你的心头最爱？";
		List<Message> approvedMsgs = client.getAllSyncApproved(0);
		List<Message> unApprovedMsgs = client.getAllMsgBySort(Constant.UNRELATED, 0);
		List<String> properties = ReadByLine.readByLine(Constant.KNN_APPROVE_PROPERTY_FILE, "utf-8");
		KnnModel knnModel = new KnnModel(approvedMsgs,unApprovedMsgs,properties);
		knnModel.DEBUG = true;
		System.out.println(knnModel.judge(content));
		// wordCount();
		 * */
		knnPropertyTest();
	}

	private static void knnPropertyTest() {
		List<Message> newMsgs = new LinkedList<Message>();
		File file = new File( "data_sets/approve_delta/");
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,
					item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			newMsgs.add(msg);
		}
		
		List<Message> passed = new LinkedList<Message>();
		List<Message> unPassed = new LinkedList<Message>();
		String passedPath = "data_sets/approve_delta/";
		String unPassedPath = "data_sets/unrelated_delta/";
		file = new File(passedPath);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,
					item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			passed.add(msg);
		}
		int count =0;
		file = new File(unPassedPath);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,
					item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			unPassed.add(msg);
//			if(count ++ > passed.size()) break;
		}
		List<String> properties = ReadByLine.readByLine("KnnProperties.txt.bak", "utf-8");
		KnnModel knnModel = new KnnModel(passed,unPassed,properties);
		for (Message msg : newMsgs) {
			boolean result = knnModel.judge(msg.getContent());
			System.out.println( result+ " " +	 msg.getContent());
			if(!result) count ++;
//			WFile.wf(output + msg.getId() + ".txt", msg.getContent(), false);
//			 System.out.println(KnnModel.judge(msg.getContent()) + " " +
			// msg.getContent());
		}
		System.out.println("false count:" + count);
	}

	/*
	 * private static void knnPropertyTest(){ List<Message> msgs = new
	 * LinkedList<Message>(); File file = new File("train/unrelated/"); for(File
	 * item:file.listFiles()){ Message msg = new Message();
	 * msg.setId(Integer.parseInt(item.getName().substring(0,
	 * item.getName().lastIndexOf(".txt"))));
	 * msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
	 * msg.result = false; msgs.add(msg); } file = new File("train/approve/");
	 * for(File item:file.listFiles()){ Message msg = new Message();
	 * msg.setId(Integer.parseInt(item.getName().substring(0,
	 * item.getName().lastIndexOf(".txt"))));
	 * msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
	 * msg.result = true; msgs.add(msg); } System.out.println(msgs.size() +
	 * " messages inputed..."); List<String> properties =
	 * ReadByLine.readByLine(Constant.KNN_PROPERTY_FILE, "utf-8"); List<String>
	 * temp = new LinkedList<String>();
	 * 
	 * for (int i = 0;i < properties.size(); i++){ int wrong = 0;
	 * temp.add(properties.get(i)); KnnModel knnModel = new KnnModel();
	 * knnModel.learnFromLocal(temp); for(Message msg : msgs){
	 * if(knnModel.judge(msg.getContent()) != msg.result){ wrong ++; } }
	 * System.out.println("properties is " + i + " and wrong is " + wrong); }
	 * 
	 * }
	 */

	private static void wordCount() {
		Map<String, Integer> wordCount = new HashMap<String, Integer>();
		List<Message> messages = ThriftClient.getInstance().getAllSyncApproved(0);
		for (Message msg : messages) {
			WordCount.chineseCharacterWordCount(wordCount, msg.getContent());
		}
		Map<Integer, List<String>> sortMap = new HashMap<Integer, List<String>>();
		for (Iterator it = wordCount.entrySet().iterator(); it.hasNext();) {
			Entry entry = (Entry) it.next();
			String word = (String) entry.getKey();
			int count = (Integer) entry.getValue();
			if (sortMap.get(count) != null) {
				sortMap.get(count).add(word);
			} else {
				List<String> list = new LinkedList<String>();
				list.add(word);
				sortMap.put(count, list);
			}
		}
		Integer[] counts = new Integer[sortMap.size()];
		sortMap.keySet().toArray(counts);
		Arrays.sort(counts);
		for (int i = counts.length - 1; i >= 0; i--) {
			System.out.println(counts[i] + " : " + sortMap.get(counts[i]));
		}
	}

}

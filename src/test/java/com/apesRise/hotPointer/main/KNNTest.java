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
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.ReadByLine;
import com.apesRise.hotPointer.util.WFile;
import com.apesRise.hotPointer.util.WordCount;

public class KNNTest {


	List<Message> newMsgs = new LinkedList<Message>();
	List<Message> passed = new LinkedList<Message>();
	List<Message> unPassed = new LinkedList<Message>();
	List<String> properties = new LinkedList<String>();
	
	Map<String,Integer> propertyMap = new HashMap<String, Integer>();

	final String passedPath = "train/cookbook";
	final String newsPath = "train/cookbook";
	final String unPassedPath = "train/unrelated_cook";
	
	private KNNTest() {
		

		File file = new File(newsPath);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			newMsgs.add(msg);
		}

		file = new File(passedPath);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			passed.add(msg);
		}

		file = new File(unPassedPath);
		for (File item : file.listFiles()) {
			Message msg = new Message();
			msg.setId(Integer.parseInt(item.getName().substring(0,
					item.getName().lastIndexOf(".txt"))));
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			unPassed.add(msg);
			// if(count ++ > passed.size()) break;
		}
		properties = ReadByLine.readByLine("train/train_attributes.txt","utf-8");
//		properties = ReadByLine.readByLine("KnnApproveProperties.txt", "utf-8");
		for(String word:properties){
			propertyMap.put(word,1);
		}
		System.out.println("passed count:" + passed.size());
		System.out.println("unPassed count:" + unPassed.size());
		System.out.println("newMsg count:" + newMsgs.size());
	}

	public static void main(String[] args) {
		KNNTest test = new KNNTest();
		
//		String text = "[话筒]转发=>【年底到了，小心人贩子】现在丢一个孩子会要了一个家庭的命，有网友提出修改刑法，以买卖为营利的拐卖妇女儿童一律死刑。而户籍方面，儿童入户口时一律采集指纹入电脑，保证被偷后无法入户并能及时找到。支持的请为了儿童转发！在发。。。[话筒]http://ww2.sinaimg.cn/bmiddle/9647a31egw1ecdib0tzx0j20go0bt74q.jpg";
//		String text = "【简版寿司】1.准备热的米饭;2.加白醋,糖,盐,芝麻油拌匀;3.鸡蛋摊成蛋皮;4.将黄瓜,胡萝卜,蛋皮切条;5.海苔上铺满米饭,放黄瓜,胡萝卜,蛋皮;6.卷紧卷起;7.切段吃起来吧。#美食#http://app.qpic.cn/mblogpic/6cb58b67cf8f869167ac/460.jpg";
//		String text = "【电饭锅版可乐鸡翅】1.鸡翅洗净,沸水汆下,再冷水洗净沥干;2.鸡翅和可.蒜粒.干辣椒.生姜片.酱油入盆中略抓几下使每块鸡翅都裹上颜色;3.鸡翅和调料入电饭煲中,盖盖,按下煮饭键;4.出气口有蒸气冒出时开盖,拌一下鸡翅;5.30min再开盖,用筷子插下鸡翅,能插进去即可。#美食#http://app.qpic.cn/mblogpic/a819dd3aacfe165ec6d0/460.jpg";
//		String text = "【椒盐排骨】1.排骨切段，青红辣椒，洋葱切末。用葱姜，盐，糖，酱油，料酒把排骨腌制一小时;2.鸡蛋和淀粉搅成糊状。排骨裹上蛋糊放入五成热的油中炸酥，捞起滤干油;3.把辣椒，洋葱末加适量的椒盐略炒，放入排骨翻炒均匀。http://ww2.sinaimg.cn/bmiddle/88ffde5fjw1ecuyf0wt3bj20c909zgmh.jpg";
//		String text = "【韩国料理：拌菠菜】1.锅里加水，放粗盐烧开。2.水开后放菠菜，菠菜断生后立刻捞出来，用凉水过凉后，用手挤干水分。3.挤干水分的菠菜装到容器里放细盐，蒜末，香油，味精，酱油均匀的搅拌后装盘，撒上芝麻即可。@kuaizishuo ★分享自#筷子说#❤#美食#http://app.qpic.cn/mblogpic/b9544fd469bfce117fa2/460.jpg";
//		String text = "高大上滴搅拌全家福到了〜还有漂亮滴锅垫和心形调味碟〜欢迎加入S老师美食烹饪大家族〜/鼓掌/鼓掌http://app.qpic.cn/mblogpic/97dbb88f66c65fe64f32/460.jpg";
//		String text = "【干锅麻辣鸡翅尖】鸡翅尖一直都是我家宝贝最钟爱的一道美食。怎样做都是百吃不厌。这个干锅麻辣翅尖，口味鲜香，麻辣适中，是...做法：1、准备好所有的食材。2、胡萝卜和西兰花梗切片用开水焯烫过...详细：http://app.qpic.cn/mblogpic/c06e2e0ad858c56662b6/460.jpg";
//		
//		String text = "海南美食煎罗非鱼：罗非鱼原产于非洲，又叫“非洲鲫鱼”。是海南餐桌上最常见的淡水鱼菜品，两广一带也常吃到。海南最家常做法—煎罗非鱼，肉质肥美细嫩，味道好极了。http://app.qpic.cn/mblogpic/3e7df90d928fd3bde2ae/460.jpg";
//		String text = "【川椒排骨#美食#】1、将猪肋排剁成17厘米长的段，将花椒小火炒香后磨碎待用。2、把葱、姜、味精、鸡精、生抽、美极鲜酱油、东古一品鲜酱油、料酒、花椒粉放一起调匀，然后下猪肋排腌1小时待用。3、炒锅下油，七成热时下排骨微火炸6分钟至熟，摆入垫有生菜的盘内即可。http://app.qpic.cn/mblogpic/e168b1a868358b1f7cf8/460.jpg";
//		String text = "【京味家常肉饼】肉馅中加入盐、姜粉、白胡椒粉、生抽、料酒搅打上劲；揉一面团擀开；在面皮上铺满肉馅；肉馅上撒上葱花；用面片将肉馅包裹烙熟即可。更多美食:更多美食:http://app.qpic.cn/mblogpic/4db2316a109ab0471890/460.jpg";
//		test.segmentHitTest(text);
//		test.singelTest(text);
		test.knnPropertyBatchTest();
	}

	private void singelTest(String text) {
		
		KnnModel knnModel = new KnnModel(passed, unPassed, properties);
		knnModel.DEBUG = true;
		System.out.println("result:" + knnModel.judge(text));
	}

	private void segmentHitTest(String text) {
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		WordCount.chineseCharacterWordCount(wordCountMap, text);
		System.out.println("命中属性:");
		for(String word :wordCountMap.keySet()){
			if(propertyMap.get(word) != null){
				System.out.print(word + " ");
			}
		}
		System.out.println();
	}

	private void knnPropertyBatchTest() {
		int count = 0;
		KnnModel knnModel = new KnnModel(passed, unPassed, properties);
		
		for (Message msg : newMsgs) {
			boolean result = knnModel.judge(msg.getContent());
			if (!result){
				
				System.out.println(result + " " + msg.getId() + " " + msg.getContent());
//				move(msg);
				count++;
			}
			// WFile.wf(output + msg.getId() + ".txt", msg.getContent(), false);
			// System.out.println(KnnModel.judge(msg.getContent()) + " " +
			// msg.getContent());
		}
		System.out.println("total count:" + count);
	}

	private void move(Message msg){
		String fileName = passedPath + "/" + msg.getId() + ".txt";
		WFile.wf(fileName, msg.getContent(), false);
		File file = new File(newsPath + "/" + msg.getId() + ".txt");
		file.delete();
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
		List<Message> messages = ThriftClient.getInstance().getAllSyncApproved(
				0);
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

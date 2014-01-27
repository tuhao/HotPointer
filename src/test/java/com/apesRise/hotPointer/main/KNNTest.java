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
//		String content = "哈哈，菜菜搜罗的超级详细的教学贴，垂涎欲滴的小蛋糕，你完全可以轻松驾驭啊！转发=>【手把手教你平底锅小蛋糕】不要以为糕点那都是大师级才能驾驭得了的，跟着步骤做，你一样可以做出让人垂涎欲滴的小蛋糕哟，而且不用烤箱做蛋糕哦，还不快快学起来! [din推撞] | photo by DIY达人http://ww1.sinaimg.cn/bmiddle/80891114gw1ecart676xqj20c80wajv0.jpg";
//		KnnModel.judge(content);
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
		for(Message msg:msgs){
			boolean result = KnnModel.judge(msg.getContent());
			if(result){
				WFile.wf(approveDir + msg.getId() + ".txt", msg.getContent(), false);
			}else{
				WFile.wf(unrelatedDir + msg.getId() + ".txt", msg.getContent(), false);
			}
//			System.out.println(KnnModel.judge(msg.getContent()) + " " + msg.getContent());
		}
//		int value = 0;
//		int x = 3;
//		System.out.println((value - x) * (value - x));
	}

}

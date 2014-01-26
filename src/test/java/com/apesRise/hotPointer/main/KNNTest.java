package com.apesRise.hotPointer.main;

import java.util.List;

import com.apesRise.hotPointer.core.knn.KnnModel;
import com.apesRise.hotPointer.thrift.ThriftClient;
import com.apesRise.hotPointer.thrift.push_gen.Message;

public class KNNTest {
	
	static ThriftClient client  = ThriftClient.getInstance();
	
	public static void main(String[] args) {
//		String content = "哈哈，菜菜搜罗的超级详细的教学贴，垂涎欲滴的小蛋糕，你完全可以轻松驾驭啊！转发=>【手把手教你平底锅小蛋糕】不要以为糕点那都是大师级才能驾驭得了的，跟着步骤做，你一样可以做出让人垂涎欲滴的小蛋糕哟，而且不用烤箱做蛋糕哦，还不快快学起来! [din推撞] | photo by DIY达人http://ww1.sinaimg.cn/bmiddle/80891114gw1ecart676xqj20c80wajv0.jpg";
//		KnnModel.judge(content);
		List<Message> msgs = client.getAllMetaMsg();
		for(Message msg:msgs){
			System.out.println(KnnModel.judge(msg.getContent()) + " " + msg.getContent() );
		}
//		int value = 0;
//		int x = 3;
//		System.out.println((value - x) * (value - x));
	}

}

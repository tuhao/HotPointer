package com.apesRise.hotPointer.thrift;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.apesRise.hotPointer.core.toper.MaxTopo;
import com.apesRise.hotPointer.core.toper.weibo.WeiboMsg;
import com.apesRise.hotPointer.main.Config;
import com.apesRise.hotPointer.thrift.crawler_gen.Data;
import com.apesRise.hotPointer.thrift.crawler_gen.Operate;
import com.apesRise.hotPointer.thrift.crawler_gen.Request;
import com.apesRise.hotPointer.thrift.crawler_gen.Type;

public class TestRankByThriftConn {
	
	public static void main(String []a){
		
		Config.init();
		
		Pusher pusher = new Pusher();
		Request request = new Request();
		request.setOperate(Operate.ROLLBACK);
		request.setType(Type.Weibo);
		request.setScope(1);
		request.setStart(131227);
		Puller puller = new Puller();
		List<Data> list = puller.pull(request);
		
		if(list==null) return;
		MaxTopo<WeiboMsg> toper = new MaxTopo<WeiboMsg>(3);
		for(Data cur:list){
			String[] lists = cur.getData().split("\\}\\{");
			for(String curStr :lists){
				if(!curStr.endsWith("}")){
					curStr=curStr+"}";
				}
				if(!curStr.startsWith("{")){
					curStr="{"+curStr;
				}
//				System.out.println(curStr+"\n\n\n\n\n");
				WeiboMsg msg = JSON.parseObject(curStr, WeiboMsg.class);
//				System.out.println(msg);
				toper.push(msg);
				
			}
			
		}
		
		pusher.push(toper.getResult());
		
		for (WeiboMsg cur : toper.getResult()) {
			System.out.println("score:"+cur.getTime());
			System.out.println("user:"+cur.getUser().getName()+"  "+cur.getUser().getFollowers_count()+"  "+cur.getUser().getFriends_count());
			System.out.println("weibo:"+cur.getText());
			if(cur.getRetweeted_status()!=null){
				System.out.println("reuser:"+cur.getRetweeted_status().getUser().getName()+"  "+cur.getRetweeted_status().getUser().getFollowers_count()+"  "+cur.getRetweeted_status().getUser().getFriends_count());
				System.out.println("reweibo:"+cur.getRetweeted_status().getText());
			}
			System.out.println("\n\n");
		}
		
	}
	
}

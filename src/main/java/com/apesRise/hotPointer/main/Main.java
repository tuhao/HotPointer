package com.apesRise.hotPointer.main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.apesRise.hotPointer.core.toper.MaxTopo;
import com.apesRise.hotPointer.core.toper.weibo.WeiboMsg;
import com.apesRise.hotPointer.thrift.Puller;
import com.apesRise.hotPointer.thrift.gen.Data;
import com.apesRise.hotPointer.thrift.gen.Operate;
import com.apesRise.hotPointer.thrift.gen.Request;
import com.apesRise.hotPointer.thrift.gen.Type;

public class Main {
public static void main(String []a){
		
		Request request = new Request();
		request.setOperate(Operate.GET);
		request.setType(Type.Weibo);
		request.setScope(2);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date date = calendar.getTime();
		request.setStart(((date.getYear()%100)*10000)+((date.getMonth()+1)*100)+date.getDate());
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
				WeiboMsg msg = JSON.parseObject(curStr, WeiboMsg.class);
				toper.push(msg);
				
			}
			
		}
		
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

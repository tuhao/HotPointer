package com.apesRise.hotPointer.main;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.apesRise.hotPointer.core.toper.weibo.WeiboMsg;
import com.apesRise.hotPointer.thrift.Pusher;
import com.apesRise.hotPointer.util.BloomFilter;
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.WFile;

public class ReSend {
	
	public static void main(String []a){
		if(a.length!=1){
			return ;
		}
		Config.init();
		
		BloomFilter pushedfilter = new BloomFilter(Config.BASEDIR+"cache/pushed.data");
		
		String text = ReadAll.readAll(Config.BASEDIR+"cache/"+a[0], "utf-8");
		String[] jsons = text.split("\n\n\n\n\n");
		List<WeiboMsg> result = new LinkedList<WeiboMsg>();
		for(String json:jsons){
			WeiboMsg msg = JSON.parseObject(json, WeiboMsg.class);
			result.add(msg);
		}
		
		Pusher pusher = new Pusher();
		boolean isfinish = pusher.push(result);
		System.out.println("push finish and  push sucess == "+isfinish);
		int tryTime = 0;
		while (!isfinish && tryTime < 5) {
			System.out.println("satart retry... and try time is "+tryTime);
			try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
			}

			tryTime++;
			System.out.println("parse finish and start push ... ");
			isfinish = pusher.push(result);
			System.out.println("push finish and  push sucess == "+isfinish);

		}
		
		if(isfinish){
			System.out.println("push sucess");
			System.out.println("--------------------------------time:"+new Date()+"-----------------------------------");
			
			for (WeiboMsg cur : result) {
				pushedfilter.add(cur.getID());
				WFile.wf(Config.BASEDIR+"cache/pushed.data", cur.getID()+"\n", true);
				System.out.println("score:"+cur.getTime());
				System.out.println("user:"+cur.getUser().getName()+"  "+cur.getUser().getFollowers_count()+"  "+cur.getUser().getFriends_count());
				System.out.println("weibo:"+cur.getText()+cur.getBmiddle_pic());
				if(cur.getRetweeted_status()!=null){
					System.out.println("reuser:"+cur.getRetweeted_status().getUser().getName()+"  "+cur.getRetweeted_status().getUser().getFollowers_count()+"  "+cur.getRetweeted_status().getUser().getFriends_count());
					System.out.println("reweibo:"+cur.getRetweeted_status().getText()+cur.getRetweeted_status().getBmiddle_pic());
				}
				System.out.println("\n\n");
			}
			
			System.out.println("push ok and write cache file");
			long curTime = System.currentTimeMillis();
			for (WeiboMsg cur : result) {
				WFile.wf(Config.BASEDIR+"cache/pushok_"+curTime, JSON.toJSONString(cur)+"\n\n\n\n\n",true);
			}
			
		}else{
			System.out.println("push fail !!");
		}
	}
	
}

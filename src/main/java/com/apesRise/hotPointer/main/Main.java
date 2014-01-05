package com.apesRise.hotPointer.main;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.apesRise.hotPointer.core.toper.MaxTopo;
import com.apesRise.hotPointer.core.toper.weibo.WeiboMsg;
import com.apesRise.hotPointer.thrift.Puller;
import com.apesRise.hotPointer.thrift.Pusher;
import com.apesRise.hotPointer.thrift.crawler_gen.Data;
import com.apesRise.hotPointer.thrift.crawler_gen.Operate;
import com.apesRise.hotPointer.thrift.crawler_gen.Request;
import com.apesRise.hotPointer.thrift.crawler_gen.Type;
import com.apesRise.hotPointer.util.BloomFilter;
import com.apesRise.hotPointer.util.LogHelper;
import com.apesRise.hotPointer.util.WFile;

public class Main {
	public static void main(String[] a) {

		Config.init();
		BloomFilter pushedfilter = new BloomFilter("cache/pushed.data");
		BloomFilter parsedfilter = new BloomFilter("cache/parsed.data");

		Request request = new Request();
		request.setOperate(Operate.GET);
		request.setType(Type.Weibo);
		request.setScope(2);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Date date = calendar.getTime();
		request.setStart(((date.getYear() % 100) * 10000)
				+ ((date.getMonth() + 1) * 100) + date.getDate());
		Puller puller = new Puller();
		List<Data> list = puller.pull(request);
		Pusher pusher = new Pusher();

		if (list == null)
			return;
		MaxTopo<WeiboMsg> toper = new MaxTopo<WeiboMsg>(100);
		for (Data cur : list) {
			LogHelper.info(cur.data);
			String[] lists = cur.getData().split("\\}\\{");
			for (String curStr : lists) {
				if (!curStr.endsWith("}")) {
					curStr = curStr + "}";
				}
				if (!curStr.startsWith("{")) {
					curStr = "{" + curStr;
				}
				WeiboMsg msg = JSON.parseObject(curStr, WeiboMsg.class);
				
				
				if(!parsedfilter.contains(msg.getID())){
					parsedfilter.add(msg.getID());
					WFile.wf("cache/parsed.data", msg.getID()+"\n", true);
					if(!pushedfilter.contains(msg.getID())){
						toper.push(msg);
					}
				}
				
			}

		}

		boolean isfinish = pusher.push(toper.getResult());
		int tryTime = 0;
		while (!isfinish && tryTime < 5) {
			try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
			}

			tryTime++;
			isfinish = pusher.push(toper.getResult());

		}
		
		if(isfinish){
			System.out.println("--------------------------------time:"+new Date()+"-----------------------------------");
			
			for (WeiboMsg cur : toper.getResult()) {
				pushedfilter.add(cur.getID());
				WFile.wf("cache/pushed.data", cur.getID()+"\n", true);
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
}

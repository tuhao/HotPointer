package com.apesRise.hotPointer.thrift;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.apesRise.hotPointer.core.toper.weibo.WeiboMsg;
import com.apesRise.hotPointer.thrift.gen.Data;
import com.apesRise.hotPointer.thrift.gen.Operate;
import com.apesRise.hotPointer.thrift.gen.Request;
import com.apesRise.hotPointer.thrift.gen.Type;

public class TestThriftConn {
	
	public static void main(String []a){
		
		Request request = new Request();
		request.setOperate(Operate.ROLLBACK);
		request.setType(Type.Weibo);
		request.setScope(1);
		request.setStart(131227);
		Puller puller = new Puller();
		List<Data> list = puller.pull(request);
		
		if(list==null) return;
		for(Data cur:list){
			String[] lists = cur.getData().split("\\}\\{");
			for(String curStr :lists){
				if(!curStr.endsWith("}")){
					curStr=curStr+"}";
				}
				if(!curStr.startsWith("{")){
					curStr="{"+curStr;
				}
				System.out.println(curStr+"\n\n\n\n\n");
			}
		}
		
	}
	
}

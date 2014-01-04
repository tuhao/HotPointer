package com.apesRise.hotPointer.thrift;

import java.util.List;

import com.apesRise.hotPointer.thrift.crawler_gen.Data;
import com.apesRise.hotPointer.thrift.crawler_gen.Operate;
import com.apesRise.hotPointer.thrift.crawler_gen.Request;
import com.apesRise.hotPointer.thrift.crawler_gen.Type;
import com.apesRise.hotPointer.util.LogHelper;

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
			LogHelper.info(cur.data);
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

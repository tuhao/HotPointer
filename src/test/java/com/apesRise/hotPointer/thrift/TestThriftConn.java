package com.apesRise.hotPointer.thrift;

import java.util.List;

import com.apesRise.hotPointer.thrift.gen.Data;
import com.apesRise.hotPointer.thrift.gen.Operate;
import com.apesRise.hotPointer.thrift.gen.Request;
import com.apesRise.hotPointer.thrift.gen.Type;

public class TestThriftConn {
	
	public static void main(String []a){
		
		Request request = new Request();
		request.setOperate(Operate.GET);
		request.setType(Type.Weibo);
		request.setScope(1);
		request.setStart(140102);
		Puller puller = new Puller();
		List<Data> list = puller.pull(request);
		
		if(list==null) return;
		for(Data cur:list){
			System.out.println(cur.getData());
		}
		
	}
	
}

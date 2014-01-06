package com.apesRise.hotPointer.thrift;

import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.apesRise.hotPointer.main.Config;
import com.apesRise.hotPointer.thrift.crawler_gen.Data;
import com.apesRise.hotPointer.thrift.crawler_gen.DataService.Client;
import com.apesRise.hotPointer.thrift.crawler_gen.Request;

public class Puller {

	/**
	 * pull info form crawler server
	 * */
	public List<Data> pull(Request request) {
		TTransport transport = new TFramedTransport(new TSocket(Config.PULL_IP, Config.PULL_PORT,20000),50 * 1024 * 1024);
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
			return null;
		}
		try {
			TProtocol protocol = new TBinaryProtocol(transport);
			Client.Factory factory = new Client.Factory();
			Client client = factory.getClient(protocol);
			
			List<Data> dataList = null;
			try {
				dataList = client.pull(request);
			} catch (TException e) {
				e.printStackTrace();
				return null;
			}
			
			return dataList;
			
		} finally {
			transport.close();
		}
		
	}

}

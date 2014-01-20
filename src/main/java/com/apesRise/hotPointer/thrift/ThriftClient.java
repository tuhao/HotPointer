package com.apesRise.hotPointer.thrift;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.apesRise.hotPointer.thrift.push_gen.DataService.Client;
import com.apesRise.hotPointer.thrift.push_gen.Message;

public class ThriftClient {
	
	private static ThriftClient instance = null;
	public static ThriftClient getInstance(){
		if (instance == null){
			instance = new ThriftClient();
		}
		return instance;
	}
	
	private Client client = null;
	private TTransport transport = null;
	
	private ThriftClient(){
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("thrift.properties");
			properties.load(fis);
			String host = properties.getProperty("weixin.host");
			String port = properties.getProperty("weixin.port");
			transport = new TFramedTransport(new TSocket(host, Integer.parseInt(port)));
			TProtocol protocol = new TBinaryProtocol(transport);
			Client.Factory factory = new Client.Factory();
			client = factory.getClient(protocol);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @param size
	 * @param sortId 1:meta_data 2:approve_data
	 * @return
	 */
	public List<Message> pullBySort(int size,int sortId){
		List<Message> result = new LinkedList<Message>();
		try {
			transport.open();
			result = client.pullMsgBySort(size, sortId);
			transport.close();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
		ThriftClient client = ThriftClient.getInstance();
		System.out.println(client.pullBySort(20, 1));
	}

}

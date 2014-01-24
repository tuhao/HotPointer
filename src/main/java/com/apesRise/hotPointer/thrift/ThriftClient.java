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
			
			transport = new TFramedTransport(new TSocket(host, Integer.parseInt(port),30000),50 * 1024 * 1024);
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
	
	public List<Message> pullMsg(int size){
		List<Message> result = new LinkedList<Message>();
		try {
			transport.open();
			result = client.pullMsg(size);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return result;
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
			result = client.pullMsgBySort(size,sortId);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return result;
	}
	
	/**
	 * 
	 * @param ids
	 * @return
	 */
	public boolean deleteIds(List<Integer> ids){
			try {
				transport.open();
				System.out.println(client.deleteMsgs(ids));
				return true;
			} catch (TTransportException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				transport.close();
			}
			return false;
	}
	
	/**
	 * 
	 * @param startIndex
	 * @param itemNum
	 * @return
	 */
	public List<Message> pullPaginateMsg(int startIndex,int itemNum){
		List<Message> result = new LinkedList<Message>();
		try {
			transport.open();
			result = client.pullPaginateMsg(startIndex, itemNum);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return result;
	}
	
	/**
	 * 
	 * @param startIndex
	 * @param itemNum
	 * @param sortId
	 * @return
	 */
	public List<Message> pullPaginateMsgBySort(int startIndex,int itemNum,int sortId){
		List<Message> result = new LinkedList<Message>();
		try {
			transport.open();
			result = client.pullPaginateMsgBySort(startIndex, itemNum,sortId);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return result;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMsgCount(){
		try {
			transport.open();
			return client.getMsgCount();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param sort_id
	 * @return
	 */
	public int getMsgCountBySort(int sort_id){
		try {
			transport.open();
			return client.getMsgCountBySort(sort_id);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return 0;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getApproveCount(){
		try {
			transport.open();
			return client.getApproveCount();
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return 0;
	}
	
	/**
	 * 
	 * @param startIndex
	 * @param itemNum
	 * @return
	 */
	public List<Message> pullPaginateApprove(int startIndex,int itemNum){
		List<Message> result = new LinkedList<Message>();
		try {
			transport.open();
			result = client.pullApprove(startIndex, itemNum);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return result;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	public int pushApprove(List<Message> list){
		int count = 0;
		try {
			transport.open();
			count = client.pushApprove(list);
		} catch (TTransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			transport.close();
		}
		return count;
	}
	
	public static void main(String[] args) {
		ThriftClient client = ThriftClient.getInstance();
		System.out.println(client.pullPaginateMsgBySort(0, 5,1));
	}

}

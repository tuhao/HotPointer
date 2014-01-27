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
import com.apesRise.hotPointer.util.Constant;
import com.apesRise.hotPointer.util.WFile;

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
	private static int itemNum = 200;
	
	private ThriftClient(){
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("thrift.properties");
			properties.load(fis);
			String host = properties.getProperty("weixin.host");
			String port = properties.getProperty("weixin.port");
			
			transport = new TFramedTransport(new TSocket(host, Integer.parseInt(port),3000),50 * 1024 * 1024);
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
	
	@Deprecated
	private List<Message> pullMsg(int size){
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
	 * @param sortId 1:meta_data 2:approve_data 3:unrelated_data
	 * @return
	 */
	@Deprecated
	private List<Message> pullBySort(int size,int sortId){
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
	 * 删除元数据
	 * @param ids 元数据id列表
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
	 * 根据分类查询元数据
	 * @param startIndex 开始索引
	 * @param itemNum	item个数
	 * @return
	 */
	private List<Message> pullPaginateMsg(int startIndex,int itemNum){
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
	 * 分页形式根据分类查询元数据
	 * @param startIndex 开始索引
	 * @param itemNum item个数
	 * @param sortId 1:meta_data 2:approve_data 3:unrelated_data
	 * @return
	 */
	private List<Message> pullPaginateMsgBySort(int startIndex,int itemNum,int sortId){
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
	 * 取得元数据总数
	 * @return
	 */
	private int getMsgCount(){
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
	 * 根据分类查询元数据
	 * @param sort_id
	 * @return
	 */
	private int getMsgCountBySort(int sort_id){
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
	 * 取得索引总数
	 * @return
	 */
	private int getApproveCount(){
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
	 * 分页形式拉取索引数据
	 * @param startIndex
	 * @param itemNum
	 * @return
	 */
	private List<Message> pullPaginateApprove(int startIndex,int itemNum){
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
	 * 将已审核通过的信息推送入索引
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
	

	/**
	 * 拉取所有索引数据
	 * @return List<Message>
	 */
	public List<Message> getAllSyncApproved(){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getApproveCount();
		for (int i =0;i < msgSum;i = i + itemNum){
			msgs.addAll(pullPaginateApprove(i,itemNum));
		}
		return msgs;
	}
	
	/**
	 * 拉取所有已通过审核但未同步到索引的元数据
	 * @return List<Message>
	 */
	public List<Message> getAllUnSyncApproved(){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getMsgCountBySort(Constant.APPROVED);
		for (int i =0;i < msgSum;i = i + itemNum){
			msgs.addAll(pullPaginateMsgBySort(i,itemNum,Constant.APPROVED));
		}
		return msgs;
	}
	
	/**
	 * 拉取所有审核判定为无关的元数据
	 * @return
	 */
	public List<Message> getAllUnRelated(){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getMsgCountBySort(Constant.UNRELATED);
		for (int i =0;i < msgSum;i = i + itemNum){
//			System.out.println(msgs.size());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(msgs.size());
			msgs.addAll(pullPaginateMsgBySort(i,itemNum,Constant.UNRELATED));
		}
		return msgs;
	}
	
	/**
	 * 拉取所有数据
	 * @return
	 */
	public List<Message> getAllMsg(){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getMsgCount();
		for (int i =0;i < msgSum;i = i + itemNum){
			msgs.addAll(pullPaginateMsg(i,itemNum));
		}
		return msgs;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Message> getAllMetaMsg(){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getMsgCountBySort(Constant.META);
		for (int i =0;i < msgSum;i = i + itemNum){
			msgs.addAll(pullPaginateMsgBySort(i,itemNum,Constant.META));
		}
		return msgs;
	}
	
	
	public static void main(String[] args) {
//		String path = "data_sets/unrelated/";
		String path = "data_sets/approve/";
		ThriftClient client = ThriftClient.getInstance();
//		List<Message> unRelated = client.getAllSyncApproved();
//		for(Message msg:unRelated){
//			String filename = path + msg.getId() + ".txt";
//			WFile.wf(filename, msg.getContent(), false);
//		}
		System.out.println(client.getApproveCount());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

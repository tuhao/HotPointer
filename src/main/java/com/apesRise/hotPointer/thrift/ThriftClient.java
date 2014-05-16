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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static int itemNum = 500;
	
	private ThriftClient(){
		Properties properties = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream("thrift.properties");
			properties.load(fis);
			String host = properties.getProperty("weixin.host");
			String port = properties.getProperty("weixin.port");
			try{
				itemNum = Integer.parseInt(properties.getProperty("thrift.itemNum"));
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
			transport = new TFramedTransport(new TSocket(host, Integer.parseInt(port),10000),50 * 1024 * 1024);
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
	 * 拉取元数据表中所有数据
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
	 * 拉取推荐表中所有数据
	 * @return List<Message>
	 */
	public List<Message> getAllSyncApproved(int count){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getApproveCount();
		if (count > 0){
			for( int i = 0;i<msgSum && i <= count;i = i + itemNum){
				msgs.addAll(pullPaginateApprove(i,itemNum));
			}
		}else{
			for (int i =0;i < msgSum;i = i + itemNum){
				msgs.addAll(pullPaginateApprove(i,itemNum));
			}
		}
		return msgs;
	}
	
	/**
	 * 拉取美食表中所有数据
	 * @return
	 */
	public List<Message> getAllDelicious(int count){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getDeliciousCount();
		if(count > 0){
			for( int i = 0;i < msgSum && i <= count;i = i + itemNum){
				msgs.addAll(pullPaginateDelicious(i,itemNum));
			}
		}else{
			for(int i =0;i < msgSum;i = i+ itemNum){
				msgs.addAll(pullPaginateDelicious(i,itemNum));
			}
		}
		return msgs;
	}
	
	/**
	 * 拉取健康饮食表中所有数据
	 * @return
	 */
	public List<Message> getAllHealthy(int count){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getHealthyCount();
		if(count > 0){
			for( int i = 0;i < msgSum && i <= count;i = i + itemNum){
				msgs.addAll(pullPaginateHealthy(i,itemNum));
			}
		}else{
			for(int i =0;i < msgSum;i = i+ itemNum){
				msgs.addAll(pullPaginateHealthy(i,itemNum));
			}
		}
		return msgs;
	}


	
	/**
	 * 根据分类拉取元数据表中信息
	 * @param sortId 类型ID
	 * @param count  取数据总数限制 (0为不限制)
	 * @return
	 */
	public List<Message> getAllMsgBySort(int sortId,int count){
		List<Message> msgs = new LinkedList<Message>();
		int msgSum = getMsgCountBySort(sortId);
		if(count > 0){
			for (int i =0;i < msgSum && i <= count;i = i + itemNum){
				msgs.addAll(pullPaginateMsgBySort(i,itemNum,sortId));
			}
		}else{
			for (int i =0;i < msgSum;i = i + itemNum){
				msgs.addAll(pullPaginateMsgBySort(i,itemNum,sortId));
			}
		}
		return msgs;
	}
	
	
	/**
	 * 标记数据类型
	 * @param ids
	 * @param sortId
	 * @return
	 */
	public boolean msgSortMark(List<Integer> ids,int sortId){
		if (ids.size() == 0) return true;
		try {
			transport.open();
			return client.msgSortMark(ids, sortId);
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
	 * 删除元数据表中数据
	 * @param ids 元数据id列表
	 * @return
	 */
	public boolean deleteMeta(List<Integer> ids){
		   if(ids.size() == 0) return true;
			try {
				transport.open();
				return client.deleteMeta(ids);
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
	 * 删除推荐表中数据
	 * @param ids
	 * @return
	 */
	public boolean deleteMsgs(List<Integer> ids){
		   if(ids.size() == 0) return true;
			try {
				transport.open();
				return client.deleteMsgs(ids);
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
	 * 删除健康饮食表中数据
	 * @param ids
	 * @return
	 */
	public boolean deleteHealthy(List<Integer> ids){
		   if(ids.size() == 0) return true;
			try {
				transport.open();
				return client.deleteHealthy(ids);
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
	 * 将已审核通过的信息推送入推荐表
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
	 * 
	 * @param list
	 * @return
	 */
	public int pushDelicious(List<Message> list){
		int count = 0;
		try {
			transport.open();
			count = client.pushDelicious(list);
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
	 * 
	 * @param list
	 * @return
	 */
	public int pushHealthy(List<Message> list){
		int count = 0;
		try {
			transport.open();
			count = client.pushHealthy(list);
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
	 * 分页拉取元数据表中数据
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
	 * 根据分类，分页拉取元数据表中数据
	 * @param startIndex 开始索引
	 * @param itemNum item个数
	 * @param sortId 1:meta_data 2:approve_data 3:unrelated_data 4:delicious_data
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
	 * 取得元数据表中数据总数
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
	 * 根据分类查询元数据表中分类数据数
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
	 * 取得推荐表数据总数
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
	 * 取得美食数据总数
	 * @return
	 */
	private int getDeliciousCount() {
		// TODO Auto-generated method stub
		try {
			transport.open();
			return client.getDeliciousCount();
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
	 * 取得健康饮食表数据总数
	 * @return
	 */
	private int getHealthyCount(){
		try {
			transport.open();
			return client.getHealthyCount();
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
	 * 分页形式拉取推荐表中数据
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
	 * 分页形式拉取美食表中数据
	 * @param startIndex
	 * @param itemNum
	 * @return
	 */
	private List<Message> pullPaginateDelicious(int startIndex,int itemNum) {
		// TODO Auto-generated method stub
		List<Message> result = new LinkedList<Message>();
		try {
			transport.open();
			result = client.pullDelicious(startIndex, itemNum);
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
	 * 分页形式拉取健康饮食表数据
	 * @param startIndex
	 * @param itemNum
	 * @return
	 */
	private List<Message> pullPaginateHealthy(int startIndex,int itemNum) {
		// TODO Auto-generated method stub
		List<Message> result = new LinkedList<Message>();
		try {
			transport.open();
			result = client.pullHealthy(startIndex, itemNum);
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
	
	
	public static void main(String[] args) {
		String deliciousPath = "data_sets/approve_delta/";
		ThriftClient client = ThriftClient.getInstance();
		List<Message> deliciousMsgs = client.getAllDelicious(0);
		deliciousMsgs.addAll(client.getAllMsgBySort(Constant.APPROVED, 2000));
		for(Message msg:deliciousMsgs){
			String filename = deliciousPath + msg.getId() + ".txt";
			WFile.wf(filename, msg.getContent(), false);
			System.out.println(msg.getId() + " " + msg.getContent() + "\n");
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

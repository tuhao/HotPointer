package com.apesRise.hotPointer.thrift;

import java.util.LinkedList;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.apesRise.hotPointer.core.toper.weibo.WeiboMsg;
import com.apesRise.hotPointer.main.Config;
import com.apesRise.hotPointer.thrift.push_gen.DataService.Client;
import com.apesRise.hotPointer.thrift.push_gen.Message;


public class Pusher {

	private List<Message> convertWeiboMsgToMessage(List<WeiboMsg> request){
		List<Message> result = new LinkedList<Message>();
		for(WeiboMsg cur:request){
			Message msg = new Message();
			msg.setTitle("热门美食");
			if(cur.getRetweeted_status()!=null){
				String m = cur.getText()+cur.getBmiddle_pic()+"转发=>"+cur.getRetweeted_status().getText()+cur.getRetweeted_status().getBmiddle_pic();
				msg.setContent(m);
			}else{
				msg.setContent(cur.getText()+cur.getBmiddle_pic());
			}
			result.add(msg);
		}
		return result;
	}
	
	/**
	 * push info to view server
	 * */
	public boolean push(List<WeiboMsg> request) {
		
		TTransport transport = new TFramedTransport(new TSocket(Config.PUSH_IP, Config.PUSH_PORT,10000));
		try {
			transport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
			return false;
		}
		try {
			TProtocol protocol = new TBinaryProtocol(transport);
			Client.Factory factory = new Client.Factory();
			Client client = factory.getClient(protocol);

			try {
				List<Message> result = convertWeiboMsgToMessage(request);
				if(result!=null){
					return client.pushMsg(result);
//					for(Message cur:result){
//						client.pushString(cur.getContent());					
//					}
					
					
				}
				return false;
			} catch (TException e) {
				e.printStackTrace();
				return false;
			}
			
		} finally {
			transport.close();
		}
		
	}

}

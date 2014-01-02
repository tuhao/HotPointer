package com.apesRise.hotPointer.thrift.hello;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.apesRise.hotPointer.thrift.hello.SayHello.Client;


public class TestHello {


	public static void main(String []a) {
		
		TTransport transport = new TFramedTransport(new TSocket("localhost", 19090));
		TTransport tt = new TFramedTransport.Factory().getTransport(transport);

		try {
			TProtocol protocol = new TBinaryProtocol(transport);
			SayHello.Client client = new SayHello.Client(protocol);

			try {
				transport.open();
			} catch (TTransportException e) {

			}
			
			try {
				client.say(new Print("123"));
			} catch (TException e) {
				e.printStackTrace();
			}
			
			
		} finally {
			transport.close();
		}
		
	}

}

package com.apesRise.hotPointer.main;

import java.io.FileInputStream;
import java.util.Properties;

public class Config {
	public static String PULL_IP = "127.0.0.1";
	public static int PULL_PORT = 9090;
	public static String PUSH_IP = "127.0.0.1";
	public static int PUSH_PORT = 9090;
	
	public static void init(){
		Properties prop = new Properties();
		try{
			FileInputStream fileIn = new FileInputStream("conf/config");
			prop.load(fileIn);
		}catch(Exception e){
			
		}
		PULL_IP = prop.getProperty("PULL_IP","127.0.0.1");
		PULL_PORT = Integer.parseInt(prop.getProperty("PULL_PORT","9090"));
		PUSH_IP = prop.getProperty("PUSH_IP","127.0.0.1");
		PUSH_PORT = Integer.parseInt(prop.getProperty("PUSH_PORT","9090"));
	}
	
}

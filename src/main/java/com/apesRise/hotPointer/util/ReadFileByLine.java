package com.apesRise.hotPointer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class ReadFileByLine {

	/**
	 * 传入文件地址,得到�?��数组 里面是以行分割的字符�?	 */
	public static ArrayList<String> getAllLine2Array(String file,String encoding) {
		ArrayList<String> res = new ArrayList<String>();
		BufferedReader readConf = null;
		FileInputStream in = null;
		InputStreamReader redar = null;
		try {
			in = new FileInputStream(file);
			redar = new InputStreamReader(in,encoding);
			readConf = new BufferedReader(redar);
//			FileReader conFile = new FileReader(file);
			String line;
			while (null != (line = readConf.readLine())) {
				if (!line.startsWith("#") && !line.trim().equalsIgnoreCase("")) {
					res.add(line);
				}
			}
		} catch (Exception e) {
			return null;
		}finally{
			try {
				if(readConf!=null)
				readConf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(redar!=null)
				redar.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(in!=null)
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	public static ArrayList<String> getAllLine2Array(String file) {
		ArrayList<String> res = new ArrayList<String>();
		BufferedReader readConf = null;
		FileInputStream in = null;
		InputStreamReader redar = null;
		try {
			in = new FileInputStream(file);
			redar = new InputStreamReader(in,"utf-8");
			readConf = new BufferedReader(redar);
//			FileReader conFile = new FileReader(file);
			String line;
			while (null != (line = readConf.readLine())) {
				if (!line.startsWith("#") && !line.trim().equalsIgnoreCase("")) {
					res.add(line);
				}
			}
		} catch (Exception e) {
			return null;
		}finally{
			try {
				if(readConf!=null)
				readConf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(redar!=null)
				redar.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(in!=null)
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}
	
	public static ArrayList<String> getAllLine2Array2(String file) {
		ArrayList<String> res = new ArrayList<String>();
		BufferedReader readConf = null;
		
		InputStreamReader redar = null;
		try {
			
			redar = new InputStreamReader(ReadFileByLine.class.getResourceAsStream(file));
			readConf = new BufferedReader(redar);
//			FileReader conFile = new FileReader(file);
			String line;
			while (null != (line = readConf.readLine())) {
				if (!line.startsWith("#") && !line.trim().equalsIgnoreCase("")) {
					res.add(line);
				}
			}
		} catch (Exception e) {
			return null;
		}finally{
			try {
				if(readConf!=null)
				readConf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(redar!=null)
				redar.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return res;
	}
	
}

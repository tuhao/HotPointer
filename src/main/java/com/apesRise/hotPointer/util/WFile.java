package com.apesRise.hotPointer.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class WFile {
	
	public static void wf(String file,String content){
		PrintWriter out = null;
		FileOutputStream fo = null;
		OutputStreamWriter os = null;
		BufferedWriter bf = null;
		try {
			fo = new FileOutputStream(file);
			os = new OutputStreamWriter(fo,"utf-8");
			bf = new BufferedWriter(os);
			out = new PrintWriter(bf);
			out.write(content);
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			out.close();
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				fo.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static void wf(String file,String content,boolean isAdd){
		PrintWriter out = null;
		FileOutputStream fo = null;
		OutputStreamWriter os = null;
		BufferedWriter bf = null;
		try {
			fo = new FileOutputStream(file,isAdd);
			os = new OutputStreamWriter(fo,"utf-8");
			bf = new BufferedWriter(os);
			out = new PrintWriter(bf);
			out.write(content);
			
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(bf!=null){
					bf.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(os!=null){
					os.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(fo!=null){
					fo.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
}

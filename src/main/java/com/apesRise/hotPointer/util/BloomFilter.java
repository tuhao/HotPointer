package com.apesRise.hotPointer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.BitSet;

//传统的Bloom filter 不支持从集合中删除成员。
//Counting Bloom filter由于采用了计数，因此支持remove操作。
//基于BitSet来实现，性能上可能存在问题
public class BloomFilter {
	
	
	// DEFAULT_SIZE为2的25次方
	private static final int DEFAULT_SIZE = 2<<28;
	/* 不同哈希函数的种子，一般应取质数,seeds数据共有7个值，则代表采用8种不同的HASH算法 */
	private static final int[] seeds = new int[] { 5, 7, 11, 13, 31, 37, 61 };
	
	// BitSet实际是由“二进制位”构成的一个Vector。假如希望高效率地保存大量“开－关”信息，就应使用BitSet.
	// BitSet的最小长度是一个长整数（Long）的长度：64位
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	
	/* 哈希函数对象 */
	private SimpleHash[] func = new SimpleHash[seeds.length];
	private String div = "";
	// 构造函数
	public BloomFilter(String div) {
		for (int i = 0; i < seeds.length; i++) {
			// 给出所有的hash值，共计seeds.length个hash值。共8位。
			// 通过调用SimpleHash.hash(),可以得到根据7种hash函数计算得出的hash值。
			// 传入DEFAULT_SIZE(最终字符串的长度），seeds[i](一个指定的质数)即可得到需要的那个hash值的位置。
			func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}
		this.div = div;
	}

	// 将字符串标记到bits中，即设置字符串的8个hash值函数为1
	public synchronized void add(String value) {
		for (SimpleHash f : func) {
			bits.set(f.hash(value), true);
		}
		
	}

	// 判断字符串是否已经被bits标记
	public synchronized boolean contains(String value) {
		// 确保传入的不是空值
		if (value == null) {
			return false;
		}

		// 计算7种hash算法下各自对应的hash值，并判断
		for (SimpleHash f : func) {
			if(!bits.get(f.hash(value))) return false;
		}
		return true;
	}
	
	public void init(){
		File f = new File(div);
		FileInputStream in = null;
		try {
			in = new FileInputStream(f);
			long lt = System.currentTimeMillis();
			read(in);
			System.out.println(System.currentTimeMillis()-lt);
			System.out.println(Runtime.getRuntime().totalMemory());
		}catch(Exception e){
			try {
				if(in!=null)
				in.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}
	}

	/* 哈希函数类 */
	public static class SimpleHash {
		// cap为DEFAULT_SIZE的值，即用于结果的最大的字符串长度。
		// seed为计算hash值的一个给定key，具体对应上面定义的seeds数组
		private int cap;
		private int seed;

		public SimpleHash(int cap, int seed) {
			this.cap = cap;
			this.seed = seed;
		}

		public int hash(String value) {
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++) {
				result = seed * result + value.charAt(i);
			}
			return (cap - 1) & result;
		}

	}
	
	private void read(InputStream is){
		if(is == null){
        	return ;
        }
		int i = 0;
		InputStreamReader redar = null;
		try {
			redar = new InputStreamReader(is , "UTF-8");
			BufferedReader br = new BufferedReader(redar, 512);
			String theWord = null;
			do {
				i++;
				theWord = br.readLine();
				if (theWord != null && !theWord.trim().equals("")) {
					add(theWord.trim());
				}
				if(i%100000==0){
					System.out.println(i);
				}
			} while (theWord != null);
			
		} catch (IOException ioe) {
			System.err.println("读入失败.");
			ioe.printStackTrace();
			
		}finally{
			try {
				if(redar!=null){
					redar.close();
					redar = null;
				}
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return;
	}
	
}
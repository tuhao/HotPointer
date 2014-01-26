package com.apesRise.hotPointer.core.C45;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.apesRise.hotPointer.core.C45.dataset.KV;
import com.apesRise.hotPointer.core.C45.tree.DecisionTree;
import com.apesRise.hotPointer.core.C45.util.TreeView;
import com.apesRise.hotPointer.util.ReadAll;
import com.apesRise.hotPointer.util.ReadFileByLine;
import com.apesRise.hotPointer.util.ScanFile;
import com.apesRise.hotPointer.util.SegmentWord;

public class C45Test2{

	public static void main(String []a) throws URISyntaxException{
		
		ScanFile scaner = new ScanFile();
		ArrayList<String> appfiles = scaner.seanFile("train/approve");
		
		ArrayList<String> keys = ReadFileByLine.getAllLine2Array("train/train_attributes.txt", "utf-8");
		
		LinkedList<HashMap<String,Integer>> appcontents = new LinkedList<HashMap<String,Integer>>();
		LinkedList<HashMap<String,Integer>> unrelatedcontents = new LinkedList<HashMap<String,Integer>>();
		for(String cur:appfiles){
			String content = ReadAll.readAll(cur, "utf-8");
			LinkedList<String> temps = SegmentWord.segment(content,0);
			HashMap<String, Integer> cmap = new HashMap<String, Integer>();
			for(String curword:temps){
				Integer v = cmap.get(curword);
				if(v==null){
					cmap.put(curword, 1);
				}else{
					cmap.put(curword, v+1);
				}
			}
			appcontents.add(cmap);
		}
		
		ArrayList<String> unrelated = scaner.seanFile("train/unrelated");
		for(String cur:unrelated){
			String content = ReadAll.readAll(cur, "utf-8");
			LinkedList<String> temps = SegmentWord.segment(content,0);
			HashMap<String, Integer> cmap = new HashMap<String, Integer>();
			for(String curword:temps){
				Integer v = cmap.get(curword);
				if(v==null){
					cmap.put(curword, 1);
				}else{
					cmap.put(curword, v+1);
				}
			}
			unrelatedcontents.add(cmap);
		}
		
		System.out.println("init finish!");
		String[] classnames = new String[]{"approve","unrelated"};
		String[] cattributes = keys.toArray(new String[keys.size()]);
		
		C45 c45 = new C45(classnames,cattributes,null);
		
		for(HashMap<String, Integer> cur:appcontents){
			ArrayList<KV> kvs = new ArrayList<KV>();
			for(String key:cattributes){
				Integer v = cur.get(key);
				if(v==null) v = 0;
				kvs.add(new KV(key,v.intValue()+""));
			}
			c45.addTrain("approve", kvs);
			
		}
		
		for(HashMap<String, Integer> cur:unrelatedcontents){
			ArrayList<KV> kvs = new ArrayList<KV>();
			for(String key:cattributes){
				Integer v = cur.get(key);
				if(v==null) v = 0;
				kvs.add(new KV(key,v.intValue()+""));
			}
			c45.addTrain("unrelated", kvs);

		}
		System.out.println("add finish!");
		
		c45.build();
		System.out.println("build finish!");
		DecisionTree tree = c45.getDecisionTree();
//		tree.prune();
		
		TreeView view = new TreeView(tree);

//		System.out.println(view);
		
		int aerr = 0;
		int uerr = 0;
		for(HashMap<String, Integer> cur:appcontents){
			String[] text = new String[cattributes.length];
			int i = 0;
			for(String key:cattributes){
				Integer v = cur.get(key);
				if(v==null) v = 0;
				text[i++] = v.intValue()+"";
			}
			String test = tree.classify(text);
			if(!test.equals("approve")){
//				System.out.println("a err!");
				aerr++;
			}
			
		}
		
		for(HashMap<String, Integer> cur:unrelatedcontents){
			String[] text = new String[cattributes.length];
			int i = 0;
			for(String key:cattributes){
				Integer v = cur.get(key);
				if(v==null) v = 0;
				text[i++] = v.intValue()+"";
			}
			String test = tree.classify(text);
			if(!test.equals("unrelated")){
//				System.out.println("u err!");
				uerr++;
			}
		}
		
		System.out.println(aerr+"   "+uerr+"    "+(appfiles.size()+unrelated.size()));
		
	}
}

package com.apesRise.hotPointer.core.C45;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import com.apesRise.hotPointer.core.C45.dataset.KV;
import com.apesRise.hotPointer.core.C45.tree.DecisionTree;
import com.apesRise.hotPointer.core.C45.util.TreeView;

public class C45Test
{
//	@Test
	
	public static void main(String []a) throws URISyntaxException
	{

		/**
			A1	text=a and source > 10  
			A2	text=a and source < 10
			B	text != a  
		*/
		String[] classnames = new String[]{"A1","A2","B"};
		String[] cattributes = new String[]{"text","source"};

		HashMap<String,String[]> hh = new HashMap<String,String[]>();
		hh.put("text", new String[]{"a","b"});
		
		C45 c45 = new C45(classnames,cattributes,hh);
		
		ArrayList<KV> kvs = new ArrayList<KV>();
		kvs.add(new KV("text","a"));
		kvs.add(new KV("source","11"));
		c45.addTrain("A1", kvs);
		kvs.add(new KV("text","a"));
		kvs.add(new KV("source","21"));
		c45.addTrain("A1", kvs);
		kvs.add(new KV("text","a"));
		kvs.add(new KV("source","1151"));
		c45.addTrain("A1", kvs);
		
		
		kvs.add(new KV("text","a"));
		kvs.add(new KV("source","10"));
		c45.addTrain("A2", kvs);
		kvs.add(new KV("text","a"));
		kvs.add(new KV("source","1"));
		c45.addTrain("A2", kvs);
		kvs.add(new KV("text","a"));
		kvs.add(new KV("source","9"));
		c45.addTrain("A2", kvs);
		
		
		kvs.add(new KV("text","b"));
		kvs.add(new KV("source","11"));
		c45.addTrain("B", kvs);
		kvs.add(new KV("text","b"));
		kvs.add(new KV("source","123"));
		c45.addTrain("B", kvs);
		kvs.add(new KV("text","b"));
		kvs.add(new KV("source","10"));
		c45.addTrain("B", kvs);
		kvs.add(new KV("text","b"));
		kvs.add(new KV("source","9"));
		c45.addTrain("B", kvs);
		
		c45.build();


		DecisionTree tree = c45.getDecisionTree();
		tree.prune();
		TreeView view = new TreeView(tree);

		System.out.println(view);
		
		String[] testData = new String[]{"a","1.977398","-1"};
		String test = tree.classify(testData);
		System.out.println(test);
		
	}
}

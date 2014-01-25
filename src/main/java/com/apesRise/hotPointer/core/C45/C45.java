package com.apesRise.hotPointer.core.C45;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.apesRise.hotPointer.core.C45.dataset.Attribute;
import com.apesRise.hotPointer.core.C45.dataset.DataSet;
import com.apesRise.hotPointer.core.C45.dataset.KV;
import com.apesRise.hotPointer.core.C45.tree.DecisionTree;

/**
 * The main class of Fast C4.5.
 */
public class C45 {
	/** The c45 decision tree **/
	private DecisionTree tree;
	private DataSet trainner = new DataSet("trainner");
	private HashMap<String,List<String>> attributeMap = new HashMap<String,List<String>>();
	private HashMap<String,String[]> discreteAttribute = new HashMap<String,String[]>();
	
//	public C45(DataSet dataSet) {
//		this.tree = new DecisionTree(dataSet);
//	}
	
	public C45(String[] classNames,String[] continuousAttributes,HashMap<String,String[]> discreteAttribute) {
		trainner.setClassValues(classNames);
		trainner.setAttributeNames(continuousAttributes);
		
		for(String cur:continuousAttributes){
			attributeMap.put(cur, new LinkedList<String>());
		}
		
		if(discreteAttribute!=null){
			this.discreteAttribute = discreteAttribute;
		}

		attributeMap.put("Result$$Violetgo", new LinkedList<String>());
	}
	
	//确定分类
	//确定属性集合
	//确定训练集中每个属性对应的记录及最终结果
	//生成C4.5树
	public void addTrain(String className,List<KV> content){
		attributeMap.get("Result$$Violetgo").add(className);
		for(KV cur:content){
			attributeMap.get(cur.getKey()).add(cur.getValue());
		}
		trainner.addCaseCount();
		content.clear();
	}
	
	public void build(){

		Attribute[] atts = new Attribute[attributeMap.keySet().size()];
		int i = 0;
		for(String key:attributeMap.keySet()){
			List<String> temp = attributeMap.get(key);
			String[] values = temp.toArray(new String[temp.size()]);
			if(key.equals("Result$$Violetgo")){
				atts[atts.length-1] = new Attribute("class",trainner.getClassValues(),values);
			}else{
				String[] normalvalues = discreteAttribute.get(key);
				if(normalvalues==null){
					atts[i] = new Attribute(key,values);
				}else{
					atts[i] = new Attribute(key,normalvalues,values);
				}
				
				i++;
			}
		}
		
		trainner.setAttributes(atts);
		this.tree = new DecisionTree(trainner);
	}
	
	/**
	 * Get the decision tree.
	 */
	public DecisionTree getDecisionTree() {
		return this.tree;
	}

}

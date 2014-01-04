package com.apesRise.hotPointer.core.toper;

import java.util.HashSet;
import java.util.LinkedList;

public class MaxTopo<T extends MaxTopInterface> {
	
	private int num;
	public MaxTopo(int num){
		this.num = num;
		this.list4sort = new LinkedList<T>();
	}

	private HashSet<String> ids = new HashSet<String>();
	private LinkedList<T> list4sort = null;
	private int index = 0;
	
	public void push(T max){
		if(!ids.contains(max.getID())){
			index = 0;
			for(T cur:list4sort){
				if(cur.getTime().compareTo(max.getTime())>0){
					index++;
				}else{
					if(list4sort.size()>=num){
						list4sort.removeLast();
					}
					list4sort.add(index, max);
					ids.add(max.getID());
					break;
				}
			}
			if(index==list4sort.size() && list4sort.size()<num){
				list4sort.add(index, max);
				ids.add(max.getID());
			}
		}
	}
	
	public LinkedList<T> getResult(){
		return list4sort;
	}

	
}

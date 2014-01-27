package com.apesRise.hotPointer.main;

import com.apesRise.hotPointer.core.byes.ByesFilter;

public class ByesTest {
	
	public static void main(String[] args) {
		ByesFilter byesFiler = new ByesFilter();
		double prop = byesFiler.prop("获得 ");
		System.out.println(prop);
	}

}

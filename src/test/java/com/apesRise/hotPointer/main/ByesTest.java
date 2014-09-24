package com.apesRise.hotPointer.main;

import com.apesRise.hotPointer.core.byes.ByesFilter;

public class ByesTest {

	public static void main(String[] args) {
		String text = "纳智捷无锡南华升隆汽车生活馆：#小纳美食##【大虾厚蛋烧】1.选一个大个洋葱，中间横断剖开，切两个洋葱圈，并切适量洋葱末；2.青甜椒切碎，大虾去皮和虾线；3.甜椒，洋葱末，和鸡蛋打散，加盐调味；4.洋葱圈放入锅中，倒少许橄榄油在洋葱圈内；5.倒鸡蛋液入洋葱圈；6.把一只虾放入，待到蛋液基本凝固，翻面略煎；7.煎熟后剥去洋葱圈 http://ww4.sinaimg.cn/bmiddle/bced6637jw1ehyt7etfjcj20c908y0u9.jpg";
		ByesFilter byesFiler = new ByesFilter();
		double prop = byesFiler.prop(text);
		System.out.println(prop);
	}

}

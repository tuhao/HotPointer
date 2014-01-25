package com.apesRise.hotPointer.main;

import com.apesRise.hotPointer.core.byes.ByesFilter;
import com.apesRise.hotPointer.core.byes.ByesLearn;

public class ByesTest {
	
	public static void main(String[] args) {
		
		double prop = ByesFilter.prop("适合电脑族的3大花茶，多喝一点哦！" +
				"转发=>【适合电脑族的3大花茶】①保护视力：菊花茶。" +
				"菊花10朵，金莲花5朵，决明子5克，枸杞子3克。②抗辐射" +
				"：玫瑰花茶。玫瑰花8朵，玫瑰茄(又名洛神花)2朵，绞股蓝2克" +
				"，绿茶2克。③排毒美容：金银花茶。金银花5克，薰衣草3克，" +
				"甘草10克，蜂蜜5克，温水冲泡10分钟即可饮用" +
				"http://ww3.sinaimg.cn/bmiddle/7fd40773jw1ecrkqb4hvoj20d10hdgo9.jpg");
		System.out.println(prop);
	}

}

package com.apesRise.hotPointer.main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestJSON {

	public static class User {
		private Long age;
		private String name;

		public Long getAge() {
			return age;
		}

		public void setAge(Long age) {
			this.age = age;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		
	}

	public static void main(String[] a) {
		String text = "{\"name\":\"张老头\", \"age\":66}";
		JSONObject json = JSON.parseObject(text);
		System.out.println(json);
		
		User user = JSON.parseObject(text, User.class);
		System.out.println(user.name+"   "+user.age);
	}
}

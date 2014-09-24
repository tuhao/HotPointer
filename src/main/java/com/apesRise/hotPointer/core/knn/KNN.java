package com.apesRise.hotPointer.core.knn;

import java.util.List;
import java.util.Map;

public class KNN {
	
	int msgId;
	String content;
	List<Map<String, Integer>> valueMaps;
	public int distance;
	boolean result = false;

	public KNN(int msgId, String content,
			List<Map<String, Integer>> valueMaps, boolean result) {
		this.msgId = msgId;
		this.content = content;
		this.valueMaps = valueMaps;
		this.result = result;
	}

}

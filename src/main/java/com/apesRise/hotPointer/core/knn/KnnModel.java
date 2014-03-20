package com.apesRise.hotPointer.core.knn;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.apesRise.hotPointer.thrift.push_gen.Message;
import com.apesRise.hotPointer.util.WordCount;

public class KnnModel {

	public boolean DEBUG = false;
	long start = 0;

	private List<KNN> metric = new LinkedList<KNN>();

	private static int k = 7;

	public static class KNN {
		int msgId;
		List<Map<String, Integer>> valueMaps;
		boolean result = false;
		String content;
		public int distance;

		public KNN(int msgId, String content,
				List<Map<String, Integer>> valueMaps, boolean result) {
			this.msgId = msgId;
			this.content = content;
			this.valueMaps = valueMaps;
			this.result = result;
		}
	}


	/**
	 * 
	 * @param passedMsgs
	 * @param unPasseddMsgs
	 * @param properties
	 */
	public KnnModel(List<Message> passedMsgs,List<Message> unPasseddMsgs,List<String> properties) {
		initKnnModel(metric, properties, passedMsgs, true);
		initKnnModel(metric, properties, unPasseddMsgs, false);
	}

	public boolean judge(String content) {
//		Map<KNN, Integer> distanceMap = new HashMap<KNN,Integer>();
//		distanceMap(distanceMap, content, metric);
//		return findKNeibor(distanceMap);
		
		KNN[] data = new KNN[metric.size()];
		buildData(data, content, metric);
		HeapSort.buildMaxHeapify(data);
		HeapSort.heapSort(data);
		return findKNeibor(data);
	}

	private void initKnnModel(List<KNN> metric, List<String> properties,
			List<Message> msgs, boolean result) {
		for (Message msg : msgs) {
			List<Map<String, Integer>> valueMaps = new LinkedList<Map<String, Integer>>();
			Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
			WordCount.chineseCharacterWordCount(wordCountMap, msg.getContent());
			for (String property : properties) {
				Map<String, Integer> valueMap = new HashMap<String, Integer>();
				if (wordCountMap.get(property) == null) {
					valueMap.put(property, 0);
				} else {
					valueMap.put(property, wordCountMap.get(property));
				}
				valueMaps.add(valueMap);
			}
			metric.add(new KNN(msg.getId(), msg.getContent(), valueMaps, result));
		}
	}

	private void buildData(KNN[] data, String content, List<KNN> metric) {
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		WordCount.chineseCharacterWordCount(wordCountMap, content);
		for (int i = 0; i < metric.size(); i++) {
			KNN knn = metric.get(i);
			int distance = 0;
			for (Map<String, Integer> valueMap : knn.valueMaps) {
				for (Iterator<Entry<String, Integer>> it = valueMap.entrySet()
						.iterator(); it.hasNext();) {
					Entry<String, Integer> entry = (Entry<String, Integer>) it
							.next();
					String property = entry.getKey();
					int value = entry.getValue();
					Integer x = wordCountMap.get(property);
					x = x == null ? 0 : x.intValue();
					distance += (value - x) * (value - x);
				}
			}
			knn.distance = distance;
			data[i] = knn;
		}
	}

	private boolean findKNeibor(KNN[] data) {
		// TODO Auto-generated method stub
		int right = 0, wrong = 0;
		for (int i = 0; i < k && i < data.length; i++) {
			KNN knn = data[i];
			if (knn.result) {
				right++;
			} else {
				wrong++;
			}
			if (DEBUG) {
				System.out.println(knn.distance + " : " + knn.result + " "
						+ knn.content);
			}
		}
//		System.out.println(System.currentTimeMillis() - start);
		return right > wrong ? true : false;
	}
	

	/*
	private void learnFromLocal() {
		List<String> properties = ReadByLine.readByLine(
				Constant.KNN_PROPERTY_FILE, "utf-8");
		File unrelated = new File(Constant.UNRELATED_DIR);
		for (File item : unrelated.listFiles()) {
			Message msg = new Message();
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			unApprovedMsgs.add(msg);
		}
		File approve = new File(Constant.APPROVE_DIR);
		for (File item : approve.listFiles()) {
			Message msg = new Message();
			msg.setContent(ReadAll.readAll(item.getAbsolutePath(), "utf-8"));
			approvedMsgs.add(msg);
		}
		initKnnModel(metric, properties, approvedMsgs, true);
		initKnnModel(metric, properties, unApprovedMsgs, false);
	}
	*/

	/*
	private void distanceMap(Map<KNN, Integer> distanceMap, String content,
			List<KNN> metric) {
		Map<String, Integer> wordCountMap = new HashMap<String, Integer>();
		WordCount.chineseCharacterWordCount(wordCountMap, content);
		for (KNN knn : metric) {
			int distance = 0;
			for (Map<String, Integer> valueMap : knn.valueMaps) {
				for (Iterator<Entry<String, Integer>> it = valueMap.entrySet()
						.iterator(); it.hasNext();) {
					Entry<String, Integer> entry = (Entry<String, Integer>) it
							.next();
					String property = entry.getKey();
					int value = entry.getValue();
					Integer x = wordCountMap.get(property);
					x = x == null ? 0 : x.intValue();
					distance += (value - x) * (value - x);
				}
			}
			distanceMap.put(knn, distance);
		}
	}

	private boolean findKNeibor(Map<KNN, Integer> distanceMap) {
		Map<Integer, List<KNN>> sortMap = new HashMap<Integer, List<KNN>>();
		for (Iterator<Entry<KNN, Integer>> it = distanceMap.entrySet()
				.iterator(); it.hasNext();) {
			Entry<KNN, Integer> entry = (Entry<KNN, Integer>) it.next();
			KNN knn = (KNN) entry.getKey();
			int distance = (Integer) entry.getValue();
			if (sortMap.get(distance) != null) {
				sortMap.get(distance).add(knn);
			} else {
				List<KNN> list = new LinkedList<KNN>();
				list.add(knn);
				sortMap.put(distance, list);
			}
		}
		Integer[] counts = new Integer[sortMap.size()];
		sortMap.keySet().toArray(counts);
		Arrays.sort(counts);
		int right = 0;
		int wrong = 0;
		for (int i = 0; i < counts.length; i++) {
			for (KNN knn : sortMap.get(counts[i])) {
				if (knn.result) {
					right++;
				} else {
					wrong++;
				}
				if (DEBUG) {
					System.out.println(counts[i] + " : " + knn.result + " "
							+ knn.content);
				}

				if (right + wrong >= k)
					break;
			}
			if (right + wrong >= k)
				break;
		}
		return right > wrong ? true : false;
	}
	
	*/

}

package com.apesRise.hotPointer.core.C45.tree;

import java.util.Formatter;

public class TreeNodeContent {
	private float trainWeight;
	private float[] trainClassDistribution;
	private String classification;

	public TreeNodeContent(float trainWeight, float[] trainClassDistribution, String classification, float errorAsLeafNode) {
		this.trainWeight = trainWeight;
		this.trainClassDistribution = trainClassDistribution;
		this.classification = classification;
	}

	public float getTrainWeight() {
		return this.trainWeight;
	}

	public float[] getTrainClassDistribution() {
		return this.trainClassDistribution;
	}

	public String getClassification() {
		return this.classification;
	}

	public void setTrainWeight(float trainWeight) {
		this.trainWeight = trainWeight;
	}

	public void setTrainClassDistribution(float[] trainClassDistribution) {
		this.trainClassDistribution = trainClassDistribution;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(classification).append("(");
		Formatter formatter = new Formatter(sb);
		formatter.format("%.1f", trainWeight);
		sb.append(")");
		formatter.close();
		return sb.toString();
	}
}
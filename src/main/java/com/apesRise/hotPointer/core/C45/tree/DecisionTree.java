package com.apesRise.hotPointer.core.C45.tree;

import java.util.Arrays;

import com.apesRise.hotPointer.core.C45.AttributeDelegate;
import com.apesRise.hotPointer.core.C45.Constant;
import com.apesRise.hotPointer.core.C45.TreeBuilder;
import com.apesRise.hotPointer.core.C45.TreePruner;
import com.apesRise.hotPointer.core.C45.dataset.Attribute;
import com.apesRise.hotPointer.core.C45.dataset.DataSet;

public class DecisionTree {
	private TreeNode root;
	private DataSet dataSet;
	private AttributeDelegate[] attributeDelegates;

	public DecisionTree(DataSet dataSet) {
		this.dataSet = dataSet;
		build();
		this.root.setName(dataSet.getName());
	}

	public int size() {
		return treeSize(root);
	}

	private int treeSize(TreeNode root) {
		if (root instanceof LeafNode)
			return 1;

		int sum = 0;
		int childrenCount = root.getChildrenCount();
		for (int i = 0; i < childrenCount; i++) {
			sum += treeSize(root.getChildAt(i));
		}
		return sum + 1;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public String classify(String[] testData) {

		int numberOfClasses = dataSet.getClassCount();
		String[] classValues = dataSet.getClassValues();

		// Initialize the test error
		float[] testClassDistribution = new float[numberOfClasses];

		Arrays.fill(testClassDistribution, 0.0f);

		// Classify a single test data from top to bottom
		classifyDownward(root, testData, testClassDistribution, 1.0f);

		// Select the branch whose probability is the greatest as the
		// classification of the test data
		float max = -1.0f;
		int maxIndex = -1;
		for (int i = 0; i < testClassDistribution.length; i++) {
			if (testClassDistribution[i] > max) {
				maxIndex = i;
				max = testClassDistribution[i];
			}
		}
		return classValues[maxIndex];

	}

	/**
	 * Classify a test data from top to bottom from one tree node to its
	 * offspring (if there is any).
	 * 
	 * @param node
	 *            The current tree node classify the test data
	 * @param record
	 *            The test data with its attribute values extracted
	 * @param testClassDistribution
	 *            Actually the output of this method, recording the weight
	 *            distribution of the test data in different class values.
	 * @param weight
	 *            The weight of the test data on the current tree node
	 */
	private void classifyDownward(TreeNode node, String[] record, float[] testClassDistribution, float weight) {
		TreeNodeContent content = node.getContent();
		if (node instanceof LeafNode) {
			// If there is no train data distributed on this tree node,
			// then add the weight of the test data to its corresponding class
			// branch
			if (content.getTrainWeight() <= 0) {
				// Get the branch index of the tree node's classification
				int classificationIndex = indexOf(content.getClassification(), dataSet.getClassValues());
				testClassDistribution[classificationIndex] += weight;
			}
			// Otherwise, distribute the weight of the test data with the
			// coefficient
			// of trainClassDistri[classValueIndex]/trainWeight
			else {
				float[] trainClassDistribution = content.getTrainClassDistribution();
				for (int i = 0; i < testClassDistribution.length; i++) {
					testClassDistribution[i] += weight * trainClassDistribution[i] / content.getTrainWeight();
				}
			}
		}
		// If the current tree node is an InternalNode
		else {
			// if the test attribute value of the test data is not missing, then
			// pass it to its child tree node for classification.
			Attribute testAttribute = ((InternalNode) node).getTestAttribute();
			int testAttributeIndex = indexOf(testAttribute.getName(), dataSet.getAttributeNames());
			if (!record[testAttributeIndex].equals("?")) {
				int branchIndex = findChildBranch(record[testAttributeIndex], (InternalNode) node);
				classifyDownward(node.getChildAt(branchIndex), record, testClassDistribution, weight);
			}
			/*
			 * If the test attribute value of the test data is missing or not
			 * exists in declaration, the test data is then passed to all the
			 * children tree nodes with the partitioned weight of
			 * (weight*children[childindex].getTrainWeight()/trainWeight)
			 */
			else {
				TreeNode[] children = node.getChildren();
				for (int i = 0; i < children.length; i++) {
					TreeNodeContent childContent = children[i].getContent();
					float childWeight = (float) weight * childContent.getTrainWeight() / content.getTrainWeight();
					classifyDownward(children[i], record, testClassDistribution, childWeight);
				}
			}
		}
	}

	/**
	 * Find the branch index of the child tree node to which the parent tree
	 * node should classify the test data to.
	 * 
	 * @param value
	 *            The attribute value of the test data on the parent tree node's
	 *            test attribute
	 * @param node
	 *            The parent tree node which need to classify the test data to
	 *            its offspring.
	 */
	private int findChildBranch(String value, InternalNode node) {
		Attribute testAttribute = node.getTestAttribute();
		// If the test attribute is continuous, find the branch of the test data
		// belong to by comparing its test attribute value and the cut value.
		if (!testAttribute.isDiscrete()) {
			float continValue = Float.parseFloat(value);
			return (continValue < (node.getCut() + Constant.PRECISION)) ? 0 : 1;
		} else {
			// If the test attribute is discrete, find the branch whose value is
			// the same as the test attribute value of the test data
			String[] nominalValues = testAttribute.getNominalValues();
			for (int i = 0; i < nominalValues.length; i++) {
				if (nominalValues[i].equals(value))
					return i;
			}
			// Not Found the test attribute value
			return -1;
		}
	}

	/**
	 * Build a decision tree.
	 */
	public void build() {
		TreeBuilder tb = new TreeBuilder(this.dataSet, this.attributeDelegates);

		this.dataSet = tb.getDataSet();
		this.attributeDelegates = tb.getAttributeDelegates();
		this.root = tb.getRootNode();
	}

	/**
	 * Prune the built decision tree.
	 */
	public void prune() {
		TreePruner tp = new TreePruner(this.dataSet, this.attributeDelegates, this.root, this);

		this.dataSet = tp.getDataSet();
		this.attributeDelegates = tp.getAttributeDelegates();
		this.root = tp.getRootNode();
	}

	/**
	 * Find the index of a String value in a String array.
	 */
	private int indexOf(String target, String[] from) {
		for (int i = 0; i < from.length; i++) {
			if (from[i].equals(target))
				return i;
		}
		return -1;
	}
}

package com.apesRise.hotPointer.core.C45.util;

import com.apesRise.hotPointer.core.C45.tree.DecisionTree;
import com.apesRise.hotPointer.core.C45.tree.LeafNode;
import com.apesRise.hotPointer.core.C45.tree.TreeNode;

public class TreeView {

	protected StringBuilder head = new StringBuilder();;
	protected StringBuilder body = new StringBuilder();;
	protected StringBuilder tail = new StringBuilder();;

	public static String LEVEL_PREFIX = "";
	public static String LEVEL_GAP = "  ";
	public static final String CR = System.getProperty("line.separator");

	private TreeNode root;

	public TreeView(DecisionTree tree) {
		this(tree.getRoot());
	}

	public TreeView(TreeNode root) {
		this.root = root;
		initHead();
		initBody();
		initTail();
	}

	public void initHead() {
		StringBuilder buffer = new StringBuilder();
		setHead(buffer.toString());
	}

	public void initBody() {
		StringBuilder buffer = new StringBuilder();
		preorderToBuffer(root, buffer, TreeView.LEVEL_PREFIX);
		setBody(buffer.toString());
	}

	public void initTail() {
		StringBuilder buffer = new StringBuilder();
		setTail(buffer.toString());
	}

	private static void preorderToBuffer(TreeNode node, StringBuilder buffer, String prefix) {
		StringBuilder blank = new StringBuilder(80);
		buffer.append(prefix).append(node);

		if (node instanceof LeafNode) {
			buffer.append(CR);
			return;
		}

		buffer.append(TreeView.CR);
		blank.setLength(node.toString().length());

		for (int i = 0; i < node.getChildrenCount(); i++) {
			preorderToBuffer(node.getChildAt(i), buffer, prefix + blank.toString());
		}
	}

	public String getHead() {
		return head.toString();
	}

	public String getTail() {
		return tail.toString();
	}

	public String getBody() {
		return body.toString();
	}

	public void setHead(String newHead) {
		head.setLength(0);
		head.append(newHead);
	}

	public void setBody(String newBody) {
		body.setLength(0);
		body.append(newBody);
	}

	public void setTail(String newTail) {
		tail.setLength(0);
		tail.append(newTail);
	}

	public TreeView insert(String line) {
		body.insert(0, line + TreeView.CR);
		return this;
	}

	public TreeView append(String line) {
		body.append(line + TreeView.CR);
		return this;
	}

	public TreeView union(TreeView treeView) {
		if (this.getClass() != treeView.getClass()) {
			throw new UnsupportedOperationException();
		}

		body.append(treeView.getBody());
		return this;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(head);
		buffer.append(body);
		buffer.append(tail);
		return buffer.toString();
	}

}
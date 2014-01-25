package com.apesRise.hotPointer.core.C45.tree;

public class TreeNode {

	protected TreeNodeContent content;

	protected String name;
	protected TreeNode parent;
	protected TreeNode[] children;
	protected int childCount;

	protected TreeNode(TreeNodeContent content) {
		this.content = content;
		this.childCount = 0;
	}

	public TreeNodeContent getContent() {
		return this.content;
	}

	public void setContent(TreeNodeContent content) {
		this.content = content;
	}

	public String getName() {
		if (name != null)
			return this.name;
		else
			return this.toString();
	}

	public void setName(String name) {
		this.name = name;
	}

	public TreeNode getParent() {
		return this.parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public void addChild(TreeNode aChild) {
		children[childCount++] = aChild;
		aChild.setParent(this);
	}

	public TreeNode[] getChildren() {
		return this.children;
	}

	public void setChildren(TreeNode[] children) {
		this.children = children;
	}

	public TreeNode getChildAt(int index) {
		return children[index];
	}

	public void setChildAt(int index, TreeNode child) {
		children[index] = child;
		child.setParent(this);
	}

	public int indexOfChild(TreeNode child) {
		for (int i = 0; i < children.length; i++) {
			if (children[i] == child)
				return i;
		}
		return -1;
	}

	public int getChildrenCount() {
		return children.length;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return (children == null || children.length == 0);
	}
}

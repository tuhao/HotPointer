package com.apesRise.hotPointer.core.C45.tree;

import com.apesRise.hotPointer.core.C45.dataset.Attribute;

public class InternalNode extends TreeNode {

	private Attribute testAttribute;
	private float cut;
	private int cutRank;

	public InternalNode(TreeNodeContent content, Attribute testAttribute) {
		super(content);

		this.testAttribute = testAttribute;
		if (!testAttribute.isDiscrete())
			this.children = new TreeNode[2];
		else
			this.children = new TreeNode[testAttribute.getNominalValuesCount()];

	}

	public float getCut() {
		return this.cut;
	}

	public void setCut(float cut) {
		this.cut = cut;
	}

	public int getCutRank() {
		return this.cutRank;
	}

	public void setCutRank(int cutRank) {
		this.cutRank = cutRank;
	}

	public Attribute getTestAttribute() {
		return this.testAttribute;
	}

	public void setTestAttribute(Attribute attribute) {
		this.testAttribute = attribute;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (!isRoot()) {
			Attribute parentTestAttribute = ((InternalNode) parent).getTestAttribute();
			int childIndex = parent.indexOfChild(this);

			sb.append(parentTestAttribute.getName());
			if (!parentTestAttribute.isDiscrete()) {
				sb.append(childIndex == 0 ? " <= " : " > ").append(((InternalNode) parent).getCut());
			} else {
				sb.append(" = ").append(parentTestAttribute.getNominalValues()[childIndex]);
			}
		} else
			sb.append(name);

		return sb.toString();
	}
}
package com.apesRise.hotPointer.core.C45.tree;

import com.apesRise.hotPointer.core.C45.dataset.Attribute;

public class LeafNode extends TreeNode {

	public LeafNode(TreeNodeContent content) {
		super(content);
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
			sb.append(" : ");
		} else
			sb.append(name);

		sb.append(content.getClassification());

		return sb.toString();
	}
}
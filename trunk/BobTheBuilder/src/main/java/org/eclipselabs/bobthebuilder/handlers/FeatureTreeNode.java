package org.eclipselabs.bobthebuilder.handlers;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;

public class FeatureTreeNode implements TreeNode<String, Feature, IField> {

  private final Set<FieldTreeNode> children = new HashSet<FieldTreeNode>();

  private final BobTheBuilderTreeNode parent;

  private final Feature data;

  private final String text;

  @Override
  public Set<FieldTreeNode> getChildren() {
    return children;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public Feature getData() {
    return data;
  }

  @SuppressWarnings("unchecked")
  @Override
  public BobTheBuilderTreeNode getParent() {
    return parent;
  }

  private FeatureTreeNode(Builder builder) {
    this.data = builder.data;
    this.parent = builder.parent;
    this.text = builder.text;
  }

  public FeatureTreeNode addChild(FieldTreeNode node) {
    Validate.notNull(node, "node may not be null");
    children.add(node);
    return this;
  }
  
  public static class Builder {

    private Feature data;

    private BobTheBuilderTreeNode parent;

    private String text;

    public Builder withData(Feature data) {
      this.data = data;
      return this;
    }

    public Builder withParent(BobTheBuilderTreeNode parent) {
      this.parent = parent;
      return this;
    }

    public Builder withText(String text) {
      this.text = text;
      return this;
    }

    public FeatureTreeNode build() {
      return new FeatureTreeNode(this);
    }
  }

}

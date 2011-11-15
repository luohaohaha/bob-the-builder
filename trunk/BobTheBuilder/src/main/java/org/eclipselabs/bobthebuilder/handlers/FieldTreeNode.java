package org.eclipselabs.bobthebuilder.handlers;

import java.util.Set;

import org.eclipse.jdt.core.IField;

public class FieldTreeNode implements TreeNode<Feature, IField, Void> {

  private final FeatureTreeNode parent;

  private final IField data;

  private final String text;

  @Override
  public Set<NoChildTreeNode> getChildren() {
    return null;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public IField getData() {
    return data;
  }

  @SuppressWarnings("unchecked")
  @Override
  public FeatureTreeNode getParent() {
    return parent;
  }

  private FieldTreeNode(Builder builder) {
    this.data = builder.data;
    this.parent = builder.parent;
    this.text = builder.text;
  }

  public static class Builder {

    private IField data;

    private FeatureTreeNode parent;

    private String text;

    public Builder withData(IField data) {
      this.data = data;
      return this;
    }

    public Builder withParent(FeatureTreeNode parent) {
      this.parent = parent;
      return this;
    }

    public Builder withText(String text) {
      this.text = text;
      return this;
    }

    public FieldTreeNode build() {
      return new FieldTreeNode(this);
    }
  }

}

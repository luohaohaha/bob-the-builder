package org.eclipselabs.bobthebuilder;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipselabs.bobthebuilder.model.Field;

public class FeatureTreeNode implements TreeNode<String, Feature, Field> {

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

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}

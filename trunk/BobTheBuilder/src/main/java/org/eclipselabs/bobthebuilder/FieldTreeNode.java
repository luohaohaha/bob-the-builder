package org.eclipselabs.bobthebuilder;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipselabs.bobthebuilder.model.Field;

public class FieldTreeNode implements TreeNode<Feature, Field, Void> {

  private final FeatureTreeNode parent;

  private final Field data;

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
  public Field getData() {
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

    private Field data;

    private FeatureTreeNode parent;

    private String text;

    public Builder withData(Field data) {
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

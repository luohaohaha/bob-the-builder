package org.eclipselabs.bobthebuilder;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BobTheBuilderTreeNode implements TreeNode<Void, String, Feature> {

  private final Set<FeatureTreeNode> children =
      new HashSet<FeatureTreeNode>();

  private final String data;

  private final String text;

  @Override
  public Set<FeatureTreeNode> getChildren() {
    return children;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public String getData() {
    return data;
  }

  @SuppressWarnings("unchecked")
  @Override
  public NoParentTreeNode getParent() {
    return null;
  }

  public BobTheBuilderTreeNode addChild(FeatureTreeNode node) {
    Validate.notNull(node, "node may not be null");
    children.add(node);
    return this;
  }

  private BobTheBuilderTreeNode(Builder builder) {
    this.text = builder.text;
    this.data = builder.data;
  }

  public static class Builder {

    private String text;

    private String data;

    public Builder withText(String text) {
      this.text = text;
      return this;
    }

    public Builder withData(String data) {
      this.data = data;
      return this;
    }

    public BobTheBuilderTreeNode build() {
      return new BobTheBuilderTreeNode(this);
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

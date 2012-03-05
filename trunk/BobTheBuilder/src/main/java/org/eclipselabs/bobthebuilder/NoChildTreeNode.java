package org.eclipselabs.bobthebuilder;

import java.util.Set;

import org.eclipselabs.bobthebuilder.model.Field;

public class NoChildTreeNode implements TreeNode<Field, Void, Void> {

  @Override
  public Set<? extends TreeNode<Void, Void, ?>> getChildren() {
    return null;
  }

  @Override
  public String getText() {
    return null;
  }

  @Override
  public Void getData() {
    return null;
  }

  @Override
  public <PT extends TreeNode<?, Field, Void>> PT getParent() {
    return null;
  }

}

package org.eclipselabs.bobthebuilder.handlers;

import java.util.Set;

public class NoParentTreeNode implements TreeNode<Void, Void, String> {

  @Override
  public Set<? extends TreeNode<Void, String, ?>> getChildren() {
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
  public <PT extends TreeNode<?, Void, Void>> PT getParent() {
    return null;
  }

}

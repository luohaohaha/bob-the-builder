package org.eclipselabs.bobthebuilder.handlers;

import java.util.Set;

import org.eclipse.jdt.core.IField;

public class NoChildTreeNode implements TreeNode<IField, Void, Void> {

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
  public <PT extends TreeNode<?, IField, Void>> PT getParent() {
    return null;
  }

}

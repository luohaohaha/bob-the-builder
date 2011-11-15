package org.eclipselabs.bobthebuilder.handlers;

import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class FieldTreeContentProvider implements ITreeContentProvider {

  private TreeNode<?, ?, ?> parentNode;

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

  @Override
  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    this.parentNode = (TreeNode<?, ?, ?>)arg2;
  }

  @Override
  public Object[] getChildren(Object arg0) {
    TreeNode<?, ?, ?> node = (TreeNode<?, ?, ?>)arg0;
    if (node != null) {
      Set<?> children = node.getChildren();
      return children.toArray();
    }
    return null;
  }

  @Override
  public Object[] getElements(Object arg0) {
    return getChildren(parentNode);
  }

  @Override
  public Object getParent(Object arg0) {
    TreeNode<?, ?, ?> node = (TreeNode<?, ?, ?>)arg0;
    if (node != null) {
      return node.getParent();
    }
    return null;
  }

  @Override
  public boolean hasChildren(Object arg0) {
    TreeNode<?, ?, ?> node = (TreeNode<?, ?, ?>)arg0;
    if (node != null && node.getChildren() != null) {
      return !node.getChildren().isEmpty();
    }
    return false;
  }

}

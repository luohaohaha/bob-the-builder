package org.eclipselabs.bobthebuilder;

import java.util.Set;

/**
 * Organize data in a tree fashion way
 *
 * @param <C> type of the children
 * @param <P> type of the parent
 * @param <T> type of this node
 */
public interface TreeNode<P, T, C> {

  /**
   * Returns all the children nodes associated with a node.
   */
  Set<? extends TreeNode<T, C, ?>> getChildren();

  /*
   * Returns the text of associated with a node.
   */
  String getText();

  /**
   * Returns the data associated with a node.
   */
  T getData();

  /**
   * Returns the parent node.
   */
  <PT extends TreeNode<?, P, T>> PT getParent();
  
}

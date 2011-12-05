package org.eclipselabs.bobthebuilder;

import org.apache.commons.lang.Validate;

public class DialogContent {

  private final BobTheBuilderTreeNode tree;
  
  public DialogContent(BobTheBuilderTreeNode tree) {
    Validate.notNull(tree, "Analyzed may not be null");
    this.tree = tree;
  }

  public BobTheBuilderTreeNode getTree() {
    return tree;
  }


}

package org.eclipselabs.bobthebuilder;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;


public class DialogTreeEntryComparator extends ViewerComparator {

  @Override
  public int compare(Viewer viewer, Object e1, Object e2) {
    TreeNode<?, ?, ?> node1 = (TreeNode<?, ?, ?>)e1;
    TreeNode<?, ?, ?> node2 = (TreeNode<?, ?, ?>)e2;
    Object data1 = node1.getData();
    Object data2 = node2.getData();
    if (data1 instanceof Feature) {
      return ((Feature)data1).getOrdering().compareTo(((Feature)data2).getOrdering());
    }
    else {
      return node1.getText().compareTo(node2.getText());
    }
  }

}

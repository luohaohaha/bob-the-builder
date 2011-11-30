package org.eclipselabs.bobthebuilder;

import org.eclipse.swt.graphics.Image;


public class FieldTreeLabelProvider extends org.eclipse.jface.viewers.LabelProvider {

  @Override
  public Image getImage(Object element) {
    return super.getImage(element);
  }

  @Override
  public String getText(Object element) {
    return ((TreeNode<?, ?, ?>)element).getText();
  }

}

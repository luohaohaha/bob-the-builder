package org.eclipselabs.bobthebuilder;

import java.util.EnumSet;

import org.eclipse.jface.viewers.ICheckStateProvider;

public class DialogTreeCheckStateProvider implements ICheckStateProvider {

  /*
   * There is no point on disabling/un-checking these in the UI, they should always be activated
   */
  private EnumSet<Feature> unCheckedFeatures = 
    EnumSet.of(
      Feature.NO_MISSING_VALIDATE,
      Feature.NO_MISSING_CONSTRUCTOR,  
      Feature.NO_MISSING_BUILD, 
      Feature.MISSING_BUILDER, 
      Feature.MISSING_CONSTRUCTOR,
      Feature.MISSING_BUILD);

  @Override
  public boolean isChecked(Object arg0) {
    return true;
  }

  @Override
  public boolean isGrayed(Object arg0) {
    TreeNode<?, ?, ?> node = (TreeNode<?, ?, ?>)arg0;
    Object data = node.getData();
    if (data instanceof Feature && node.getChildren() != null && !node.getChildren().isEmpty()) {
      return true;
    }
    else if (data instanceof Feature && unCheckedFeatures.contains((Feature) data)) {
      return true;
    }
    else {
      return false;
    }
  }

}

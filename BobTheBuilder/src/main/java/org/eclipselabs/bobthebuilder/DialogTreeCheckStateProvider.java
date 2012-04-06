package org.eclipselabs.bobthebuilder;

import java.util.EnumSet;

import org.eclipse.jface.viewers.ICheckStateProvider;

public class DialogTreeCheckStateProvider implements ICheckStateProvider {

  @Override
  public boolean isChecked(Object arg0) {
    return true;
  }

  @Override
  public boolean isGrayed(Object arg0) {
    TreeNode<?, ?, ?> node = (TreeNode<?, ?, ?>)arg0;
    Object data = node.getData();
    EnumSet<Feature> unCheckedFeatures = 
      EnumSet.of(Feature.NONE, Feature.MISSING_BUILDER, Feature.MISSING_CONSTRUCTOR);
    if (data instanceof Feature && node.getChildren() != null) {
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

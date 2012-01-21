package org.eclipselabs.bobthebuilder.complement;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.MainType;

public class BuildMethodComplementProvider {

  public BuildMethodComplement complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    BuilderType builderType = mainType.getBuilderType();
    if (builderType == null || builderType.getBuildMethod() == null) {
      return BuildMethodComplement.ENTIRE_METHOD;
    }
    else {
      boolean validateMethodInvocation = builderType.getBuildMethod().getValidateMethodInvocation();
      if (validateMethodInvocation) {
        return BuildMethodComplement.NOTHING_TO_DO;
      }
      else {
        return BuildMethodComplement.NEEDS_VALIDATE_INVOCATION;
      }
    }
  }

}

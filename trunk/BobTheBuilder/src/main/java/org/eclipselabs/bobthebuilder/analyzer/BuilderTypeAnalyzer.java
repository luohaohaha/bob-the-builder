package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class BuilderTypeAnalyzer {

  static final String BUILDER_CLASS_NAME = "Builder";

  private IType mainType;

  public BuilderTypeAnalyzer(IType mainType) {
    Validate.notNull(mainType, "main type may not be null");
    this.mainType = mainType;
  }

  public AnalyzerResult.ForType analyze() throws JavaModelException {
    for (IType each : mainType.getTypes()) {
      if (each.getElementName().equals(BUILDER_CLASS_NAME)) {
        return AnalyzerResult.ForType.getPresentInstance(each);
      }
    }
    return AnalyzerResult.ForType.NOT_PRESENT;
  }
}

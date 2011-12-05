package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class BuilderTypeAnalyzer {

  static final String BUILDER_CLASS_NAME = "Builder";

  public BuilderTypeAnalyzer() {}

  public AnalyzerResult.ForType analyze(IType mainType) throws JavaModelException {
    Validate.notNull(mainType, "main type may not be null");
    for (IType each : mainType.getTypes()) {
      if (each.getElementName().equals(BUILDER_CLASS_NAME)) {
        return AnalyzerResult.ForType.getPresentInstance(each);
      }
    }
    return AnalyzerResult.ForType.NOT_PRESENT;
  }
}

package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class BuilderTypeAnalyzer {

  public static final String BUILDER_CLASS_NAME = "Builder";

  private IType type;

  public BuilderTypeAnalyzer(IType type) {
    Validate.notNull(type, "type may not be null");
    this.type = type;
  }

  public AnalyzerResult.Type analyze() throws JavaModelException {
    boolean present = false;
    IType builderType = null;
    for (IType each : type.getTypes()) {
      if (each.getElementName().equals(BUILDER_CLASS_NAME)) {
        builderType = each;
        present = true;
        break;
      }
    }
    return new AnalyzerResult.Type(present, builderType);
  }
}

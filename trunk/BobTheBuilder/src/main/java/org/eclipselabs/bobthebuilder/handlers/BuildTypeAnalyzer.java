package org.eclipselabs.bobthebuilder.handlers;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class BuildTypeAnalyzer {

  public static final String BUILDER_CLASS_NAME = "Builder";
  
  private IType type;

  BuildTypeAnalyzer(IType type) {
    Validate.notNull(type, "type may not be null");
    this.type = type;
  }
  
  Result analyze() throws JavaModelException {
    boolean present = false;
    IType builderType = null;
    for (IType each : type.getTypes()) {
      if (each.getElementName().equals(BUILDER_CLASS_NAME)) {
        builderType = each;
        present = true;
        break;
      }
    }
    return new Result(present, builderType);
  }
  
  static class Result {
    private final boolean present;
    private final IType builderType;
    
    Result(boolean present, IType builderType) {
      this.present = present;
      this.builderType = builderType;
    }

    public boolean isPresent() {
      return present;
    }

    public IType getBuilderType() {
      return builderType;
    }
    
  }
}

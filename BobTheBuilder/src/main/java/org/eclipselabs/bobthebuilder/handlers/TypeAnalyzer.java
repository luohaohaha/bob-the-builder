package org.eclipselabs.bobthebuilder.handlers;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class TypeAnalyzer {

  private final ICompilationUnit compilationUnit;

  TypeAnalyzer(ICompilationUnit compilationUnit) {
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    this.compilationUnit = compilationUnit;
  }
  
  IType analyze() throws JavaModelException {
    IType type = null;
    IType[] topLevelTypes = compilationUnit.getTypes();
    if (topLevelTypes.length > 1) {
      throw new IllegalStateException(
          "Compilation units with more than one type are not supported. Compilation unit: " +
            compilationUnit.getElementName());
    }
    type = topLevelTypes[0];
    if (!type.isClass()) {
      throw new IllegalStateException("The main type has to be a class."
        + type.getElementName());
    }
    if (type.isBinary()) {
      throw new IllegalStateException("Binary types are not supported." + type.getElementName());
    }
    return type;
  }
}

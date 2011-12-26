package org.eclipselabs.bobthebuilder.mapper.eclipse;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class MainTypeSelector {

  public IType map(ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    IType type = null;
    IType[] topLevelTypes = compilationUnit.getTypes();
    if (topLevelTypes.length > 1) {
      throw new IllegalStateException(
        "Compilation units with more than one type are not supported. Compilation unit: " +
        compilationUnit.getElementName());
    }
    type = topLevelTypes[0];
    return type;
  }

}

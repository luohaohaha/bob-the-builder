package org.eclipselabs.bobthebuilder.mapper.eclipse;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;

public class ValidationFrameworkMapper {

  public ValidationFramework map(IMethod validateMethod,
    ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(validateMethod, "validateMethod may not be null");
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    String methodSource = validateMethod.getSource();
    if (methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckNotNull())) {
      return ValidationFramework.GOOGLE_GUAVA;
    }
    if (methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckNotNull())) {
      for (IImportDeclaration each : compilationUnit.getImports()) {
        if (each.getElementName().equals(ValidationFramework.COMMONS_LANG2.fullClassName)) {
          return ValidationFramework.COMMONS_LANG2;
        }
        else if (each.getElementName().equals(ValidationFramework.COMMONS_LANG3.fullClassName)) {
          return ValidationFramework.COMMONS_LANG3;
        }
        else {
          return ValidationFramework.COMMONS_LANG2;
        }
      }
    }
    return null;
  }
}

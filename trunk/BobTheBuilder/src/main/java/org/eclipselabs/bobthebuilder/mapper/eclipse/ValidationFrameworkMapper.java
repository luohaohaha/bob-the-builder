package org.eclipselabs.bobthebuilder.mapper.eclipse;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.model.Imports;

public class ValidationFrameworkMapper {

  public ValidationFramework map(IMethod validateMethod,
    Imports imports) throws JavaModelException {
    Validate.notNull(validateMethod, "validateMethod may not be null");
    Validate.notNull(imports, "compilationUnit may not be null");
    String methodSource = validateMethod.getSource();
    if (methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckNotNull())) {
      return ValidationFramework.GOOGLE_GUAVA;
    }
    if (methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckNotNull())) {
      if (imports.isCommonsLang2()) {
        return ValidationFramework.COMMONS_LANG2;
      }
      else if (imports.isCommonsLang3()) {
        return ValidationFramework.COMMONS_LANG3;
      }
      else {
        return ValidationFramework.COMMONS_LANG2;
      }
    }
    return null;
  }
}

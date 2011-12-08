package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForMethod;

public class ValidationFrameworkAnalyzer {

  Collection<ValidationFramework> analyze(ForMethod analyzedValidateResult,
    ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(analyzedValidateResult, "analyzedValidateResult may not be null");
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    if (!analyzedValidateResult.isPresent()) {
      return Arrays.asList(ValidationFramework.values());
    }

    String methodSource = analyzedValidateResult.getElement().getSource();
    if (methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckNotNull())) {
      return Arrays.asList(ValidationFramework.GOOGLE_GUAVA);
    }
    if (methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckNotNull())) {
      for (IImportDeclaration each : compilationUnit.getImports()) {
        if (each.getElementName().equals(ValidationFramework.COMMONS_LANG2.fullClassName)) {
          return Arrays.asList(ValidationFramework.COMMONS_LANG2);
        }
        else if (each.getElementName().equals(ValidationFramework.COMMONS_LANG3.fullClassName)) {
          return Arrays.asList(ValidationFramework.COMMONS_LANG3);
        }
        else {
          // Fall-back strategy
          return Arrays.asList(ValidationFramework.COMMONS_LANG2);
        }
      }
    }
    return Arrays.asList(ValidationFramework.values());
  }

}

package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.ValidationFramework;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;

public class ValidationFrameworkAnalyzer {
  private final AnalyzerResult.ForMethod analyzedValidateResult;

  private final ICompilationUnit compilationUnit;

  public ValidationFrameworkAnalyzer(ForMethod analyzedValidateResult, ICompilationUnit compilationUnit) {
    this.analyzedValidateResult = analyzedValidateResult;
    this.compilationUnit = compilationUnit;
  }

  Collection<ValidationFramework> analyze() throws JavaModelException {
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

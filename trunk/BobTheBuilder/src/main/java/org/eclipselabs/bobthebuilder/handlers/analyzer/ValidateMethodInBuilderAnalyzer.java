package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;

public class ValidateMethodInBuilderAnalyzer {

  private final MethodAnalyzer methodAnalyzer;

  public ValidateMethodInBuilderAnalyzer(ForType analyzedTypeResult) {
    methodAnalyzer = new MethodAnalyzer(analyzedTypeResult, getPredicate());
  }

  private MethodPredicate getPredicate() {
    return new MethodPredicate.ValidateInBuilder();
  }

  public AnalyzerResult.ForMethod analyze() throws JavaModelException {
    return methodAnalyzer.analyze();
  }

}

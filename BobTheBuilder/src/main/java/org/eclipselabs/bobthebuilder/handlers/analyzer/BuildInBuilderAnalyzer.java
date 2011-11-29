package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.JavaModelException;

//TODO delete and call MethodAnalyzer directly
public class BuildInBuilderAnalyzer {

  private final MethodAnalyzer methodAnalyzer;

  public BuildInBuilderAnalyzer(AnalyzerResult.ForType analyzedBuilderTypeResult) {
    methodAnalyzer = new MethodAnalyzer(analyzedBuilderTypeResult, getPredicate());
  }

  private MethodPredicate getPredicate() {
    return new MethodPredicate.BuildInBuilder();
  }

  public AnalyzerResult.ForMethod analyze() throws JavaModelException {
    return methodAnalyzer.analyze();
  }
}

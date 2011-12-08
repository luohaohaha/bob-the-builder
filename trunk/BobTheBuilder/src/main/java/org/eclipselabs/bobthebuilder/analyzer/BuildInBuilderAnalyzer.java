package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForType;

// TODO delete and call MethodAnalyzer directly
public class BuildInBuilderAnalyzer {

  private MethodPredicate getPredicate() {
    return new MethodPredicate.BuildInBuilder();
  }

  public AnalyzerResult.ForMethod analyze(ForType analyzedBuilderTypeResult) throws JavaModelException {
    Validate.notNull(analyzedBuilderTypeResult, "analyzedBuilderTypeResult may not be null");
    MethodAnalyzer methodAnalyzer = new MethodAnalyzer();
    return methodAnalyzer.analyze(analyzedBuilderTypeResult, getPredicate());
  }
}

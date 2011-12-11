package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.JavaModelException;

// TODO delete and call MethodAnalyzer directly
public class BuildInBuilderAnalyzer {

  private MethodPredicate getPredicate() {
    return new MethodPredicate.BuildInBuilder();
  }

  public MethodResult analyze(TypeResult analyzedBuilderTypeResult) throws JavaModelException {
    Validate.notNull(analyzedBuilderTypeResult, "analyzedBuilderTypeResult may not be null");
    MethodAnalyzer methodAnalyzer = new MethodAnalyzer();
    return methodAnalyzer.analyze(analyzedBuilderTypeResult, getPredicate());
  }
}

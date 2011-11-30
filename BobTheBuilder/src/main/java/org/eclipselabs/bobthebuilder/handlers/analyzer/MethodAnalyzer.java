package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class MethodAnalyzer {

  private final AnalyzerResult.ForType analyzedTypeResult;

  private final MethodPredicate methodPredicate;

  protected MethodAnalyzer(AnalyzerResult.ForType analyzedTypeResult,
      MethodPredicate methodPredicate) {
    Validate.notNull(analyzedTypeResult, "analyzedTypeResult may not be null");
    this.analyzedTypeResult = analyzedTypeResult;
    Validate.notNull(methodPredicate, "methodPredicate might not be null");
    this.methodPredicate = methodPredicate;
  }

  public AnalyzerResult.ForMethod analyze() throws JavaModelException {
    if (!analyzedTypeResult.isPresent()) {
      return AnalyzerResult.ForMethod.NOT_PRESENT;
    }
    for (IMethod each : analyzedTypeResult.getElement().getMethods()) {
      if (methodPredicate.match(each)) {
        return AnalyzerResult.ForMethod.getPresentInstance(each);
      }
    }
    return AnalyzerResult.ForMethod.NOT_PRESENT;
  }
}

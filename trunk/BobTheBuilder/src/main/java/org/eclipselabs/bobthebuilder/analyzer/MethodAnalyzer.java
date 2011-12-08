package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForType;

public class MethodAnalyzer {

  public AnalyzerResult.ForMethod analyze(
    ForType analyzedTypeResult,
    MethodPredicate methodPredicate) throws JavaModelException {
    Validate.notNull(analyzedTypeResult, "analyzedTypeResult may not be null");
    Validate.notNull(methodPredicate, "methodPredicate might not be null");
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

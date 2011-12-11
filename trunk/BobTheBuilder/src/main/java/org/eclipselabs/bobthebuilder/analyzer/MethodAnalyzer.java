package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class MethodAnalyzer {

  public MethodResult analyze(
    TypeResult analyzedTypeResult,
    MethodPredicate methodPredicate) throws JavaModelException {
    Validate.notNull(analyzedTypeResult, "analyzedTypeResult may not be null");
    Validate.notNull(methodPredicate, "methodPredicate might not be null");
    if (!analyzedTypeResult.isPresent()) {
      return MethodResult.NOT_PRESENT;
    }
    for (IMethod each : analyzedTypeResult.getElement().getMethods()) {
      if (methodPredicate.match(each)) {
        return MethodResult.getPresentInstance(each);
      }
    }
    return MethodResult.NOT_PRESENT;
  }
}

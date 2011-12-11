package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class ConstructorWithBuilderAnalyzer {

  public MethodResult analyze(TypeResult analyzedBuilderTypeResult, IType mainType) throws JavaModelException {
    Validate.notNull(analyzedBuilderTypeResult, "analyzedBuilderTypeResult may not be null");
    Validate.notNull(mainType, "main type may not be null");
    MethodAnalyzer methodAnalyzer = new MethodAnalyzer();

    if (!analyzedBuilderTypeResult.isPresent()) {
      return MethodResult.NOT_PRESENT;
    }
    return methodAnalyzer.analyze(TypeResult.getPresentInstance(mainType), getPredicate());
  }

  private MethodPredicate getPredicate() {
    return new MethodPredicate.ConstructorWithBuilder();
  }

}

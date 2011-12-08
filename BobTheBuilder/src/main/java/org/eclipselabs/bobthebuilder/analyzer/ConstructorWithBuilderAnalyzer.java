package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForMethod;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForType;

public class ConstructorWithBuilderAnalyzer {

  public ForMethod analyze(ForType analyzedBuilderTypeResult, IType mainType) throws JavaModelException {
    Validate.notNull(analyzedBuilderTypeResult, "analyzedBuilderTypeResult may not be null");
    Validate.notNull(mainType, "main type may not be null");
    MethodAnalyzer methodAnalyzer = new MethodAnalyzer();

    if (!analyzedBuilderTypeResult.isPresent()) {
      return AnalyzerResult.ForMethod.NOT_PRESENT;
    }
    return methodAnalyzer.analyze(AnalyzerResult.ForType.getPresentInstance(mainType), getPredicate());
  }

  private MethodPredicate getPredicate() {
    return new MethodPredicate.ConstructorWithBuilder();
  }

}

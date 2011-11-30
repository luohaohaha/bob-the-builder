package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForMethod;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForType;

public class ConstructorWithBuilderAnalyzer {

  private ForType analyzedBuilderTypeResult;

  private final MethodAnalyzer methodAnalyzer;

  protected ConstructorWithBuilderAnalyzer(ForType analyzedBuilderTypeResult, IType mainType) {
    methodAnalyzer = new MethodAnalyzer(AnalyzerResult.ForType.getPresentInstance(mainType),
        getPredicate());
    Validate.notNull(analyzedBuilderTypeResult, "analyzedBuilderTypeResult may not be null");
    this.analyzedBuilderTypeResult = analyzedBuilderTypeResult;
  }

  public ForMethod analyze() throws JavaModelException {
    if (!analyzedBuilderTypeResult.isPresent()) {
      return AnalyzerResult.ForMethod.NOT_PRESENT;
    }
    return methodAnalyzer.analyze();
  }

  private MethodPredicate getPredicate() {
    return new MethodPredicate.ConstructorWithBuilder();
  }

}

package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;

public class WithMethodInBuilderAnalyzer {

  private final MethodAnalyzer methodAnalyzer;

  private final IField field;

  protected WithMethodInBuilderAnalyzer(ForType analyzedTypeResult, IField field) {
    Validate.notNull(field, "field may not be null");
    this.field = field;
    this.methodAnalyzer = new MethodAnalyzer(analyzedTypeResult, getPredicate());
  }

  private MethodPredicate getPredicate() {
    return new MethodPredicate.WithMethodInBuilder(field);
  }

  public AnalyzerResult.ForMethod analyze() throws JavaModelException {
    return methodAnalyzer.analyze();
  }
}

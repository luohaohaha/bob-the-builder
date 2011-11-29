package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;

public class ValidateInBuilderAnalyzer {
  private final MethodContentAnalyzer methodContentAnalyzer;

  public ValidateInBuilderAnalyzer(Set<IField> mainTypeFields, ForMethod validateInBuilderResult) {
    methodContentAnalyzer =
        new MethodContentAnalyzer(mainTypeFields, validateInBuilderResult, getPredicate());
  }

  // TODO use ioc to add behavior
  private FieldPredicate getPredicate() {
    return new FieldPredicate.FieldValidation();
  }

  public Set<IField> analyze() throws JavaModelException {
    return methodContentAnalyzer.analyze();
  }

}

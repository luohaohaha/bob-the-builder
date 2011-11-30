package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForMethod;

public class ConstructorWithBuilderInMainTypeAnalyzer {

  private final MethodContentAnalyzer methodContentAnalyzer;

  public ConstructorWithBuilderInMainTypeAnalyzer(
      Set<IField> mainTypeFields, ForMethod constructorWithBuilderResult) {
    this.methodContentAnalyzer =
        new MethodContentAnalyzer(mainTypeFields, constructorWithBuilderResult, getPredicate());
  }

  // TODO use ioc to add behavior
  private FieldPredicate getPredicate() {
    return new FieldPredicate.FieldAssignment();
  }

  public Set<IField> analyze() throws JavaModelException {
    return methodContentAnalyzer.analyze();
  }
}

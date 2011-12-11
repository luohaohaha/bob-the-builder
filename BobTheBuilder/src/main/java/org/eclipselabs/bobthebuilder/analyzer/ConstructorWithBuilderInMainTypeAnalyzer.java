package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

public class ConstructorWithBuilderInMainTypeAnalyzer {

  // TODO use ioc to add behavior
  private FieldPredicate getPredicate() {
    return new FieldPredicate.FieldAssignment();
  }

  public Set<IField> analyze(Set<IField> mainTypeFields, MethodResult constructorWithBuilderResult) throws JavaModelException {
    Validate.notNull(mainTypeFields, "mainTypeFields may not be null");
    Validate.noNullElements(mainTypeFields, "There may not be nulls in mainTypeFields");
    Validate.notNull(constructorWithBuilderResult, "constructorWithBuilderResult may not be null");
    MethodContentAnalyzer methodContentAnalyzer =
        new MethodContentAnalyzer();
    return methodContentAnalyzer.analyze(
      mainTypeFields, constructorWithBuilderResult, getPredicate());
  }
}

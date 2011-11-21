package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;

public abstract class MethodContentAnalyzer {

  private final Set<IField> typeFields;

  private final AnalyzerResult.ForMethod analyzedMethodResult;

  public MethodContentAnalyzer(
      Set<IField> typeFields, AnalyzerResult.ForMethod analyzedMethodResult) {
    Validate.notNull(typeFields, "type fields set may not be null");
    Validate.isTrue(!typeFields.isEmpty(), "fields may not be empty");
    Validate.noNullElements(typeFields, "type fields set may not contain nulls");
    Validate.notNull(analyzedMethodResult, "analyzed method result may not be null");
    this.typeFields = Collections.unmodifiableSet(typeFields);
    this.analyzedMethodResult = analyzedMethodResult;
  }

  public Set<IField> analyze() throws JavaModelException {
    if (!analyzedMethodResult.isPresent()) {
      return typeFields;
    }
    if (analyzedMethodResult.getElement().getSource() == null) {
      return typeFields;
    }
    Set<IField> result = new HashSet<IField>();
    for (IField each : typeFields) {
      String fieldName = each.getElementName();
      // TODO use ioc to add behavior
      boolean found = getPredicate().match(
        fieldName, analyzedMethodResult.getElement().getSource(), each.getTypeSignature());
      if (!found) {
        result.add(each);
      }
    }
    return Collections.unmodifiableSet(result);
  }

  protected abstract FieldPredicate getPredicate();

  public static class ValidateInBuilder extends MethodContentAnalyzer {

    public ValidateInBuilder(Set<IField> mainTypeFields, ForMethod validateInBuilderResult) {
      super(mainTypeFields, validateInBuilderResult);
    }

    @Override
    protected FieldPredicate getPredicate() {
      return new FieldPredicate.FieldValidation();
    }

  }

  public static class ConstructorWithBuilderInMainType extends MethodContentAnalyzer {

    public ConstructorWithBuilderInMainType(
        Set<IField> mainTypeFields, ForMethod constructorWithBuilderResult) {
      super(mainTypeFields, constructorWithBuilderResult);
    }

    @Override
    protected FieldPredicate getPredicate() {
      return new FieldPredicate.FieldAssignment();
    }

  }
}

package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;

public abstract class MissingInstructionsInMethodAnalyzer {

  private final Set<IField> typeFields;

  private final AnalyzerResult.ForMethod analyzedMethodResult;

  public MissingInstructionsInMethodAnalyzer(
      Set<IField> typeFields, AnalyzerResult.ForMethod analyzedMethodResult) {
    Validate.notNull(typeFields, "type fields set may not be null");
    Validate.noNullElements(typeFields, "type fields set may not contain nulls");
    Validate.notNull(analyzedMethodResult, "analyzed method result may not be null");
    this.typeFields = typeFields;
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
      boolean found = getPredicate().match(
        fieldName, analyzedMethodResult.getElement().getSource(), each.getTypeSignature());
      if (!found) {
        result.add(each);
      }
    }
    return result;
  }
  protected abstract FieldPredicate getPredicate();
  
  public static class ValidateInBuilder extends MissingInstructionsInMethodAnalyzer {

    public ValidateInBuilder(Set<IField> mainTypeFields, ForMethod validateInBuilderResult) {
      super(mainTypeFields, validateInBuilderResult);
    }

    @Override
    protected FieldPredicate getPredicate() {
      return new FieldPredicate.FieldValidation();
    }
    
  }
  
  public static class ConstructorWithBuilderInMainType extends MissingInstructionsInMethodAnalyzer {

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

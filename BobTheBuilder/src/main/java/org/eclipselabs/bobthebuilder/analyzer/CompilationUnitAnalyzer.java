package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForMethod;

public class CompilationUnitAnalyzer {
  public static final String BUILDER_CLASS_NAME = "Builder";

  public CompilationUnitAnalyzer() {}

  public Analyzed analyze(final ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(compilationUnit, "Compilation Unit cannot be null");
    Set<IField> fields = new HashSet<IField>();
    Set<IField> copyOfFields = new HashSet<IField>();
    Set<IField> builderFields = new HashSet<IField>();
    Set<IField> copyOfBuilderFields = new HashSet<IField>();
    Set<IField> anotherCopyOfBuilderFields = new HashSet<IField>();
    Set<IField> missingFieldsInBuilder = null;
    Set<IField> extraFieldsInBuilder = new HashSet<IField>();
    Set<IField> missingWithMethodsForFields = new HashSet<IField>();
    Set<IField> missingFieldsInConstructorWithBuilder = new HashSet<IField>();
    Set<IField> missingFieldValidationsInBuilder = new HashSet<IField>();
    IMethod validationMethod = null;
    IMethod constructorWithBuilder = null;
    IType type = null;
    IType builderType = null;
    boolean missingConstructorWithBuilder = false;
    boolean missingBuilder = true;
    boolean missingBuildMethodInBuilder = true;
    boolean missingValidateMethodInBuilder = true;
    Collection<ValidationFramework> validationFrameworks = null;
    try {
      type = new TypeAnalyzer().analyze(compilationUnit);
      fields = new MainTypeFieldAnalyzer(type).analyze();
      // TODO if we have our own representation of the IField, we can do fancier set operations.
      copyOfFields.addAll(fields);
      AnalyzerResult.ForType builderAnalyzerResult = new BuilderTypeAnalyzer().analyze(type);
      builderType = builderAnalyzerResult.getElement();
      missingBuilder = !builderAnalyzerResult.isPresent();
      builderFields = new BuilderTypeFieldAnalyzer().analyze(builderAnalyzerResult);
      copyOfBuilderFields.addAll(builderFields);
      anotherCopyOfBuilderFields.addAll(builderFields);
      missingFieldsInBuilder =
          new DifferenceBetweenFieldSetsAnalyzer().analyze(copyOfFields, builderFields);
      // flip-flop collections so that the subtraction of sets works
      extraFieldsInBuilder = new DifferenceBetweenFieldSetsAnalyzer()
          .analyze(copyOfBuilderFields, fields);
      missingWithMethodsForFields = new WithMethodsInBuilderAnalyzer().analyze(
            anotherCopyOfBuilderFields, missingFieldsInBuilder, extraFieldsInBuilder, builderAnalyzerResult);
      ForMethod constructorWithBuilderResult =
          new ConstructorWithBuilderAnalyzer(builderAnalyzerResult, type).analyze();
      missingConstructorWithBuilder = !constructorWithBuilderResult.isPresent();
      constructorWithBuilder = constructorWithBuilderResult.getElement();
      missingFieldsInConstructorWithBuilder =
          new ConstructorWithBuilderInMainTypeAnalyzer(
              fields, constructorWithBuilderResult).analyze();
      missingBuildMethodInBuilder =
          !new BuildInBuilderAnalyzer(builderAnalyzerResult).analyze().isPresent();
      ForMethod analyzedValidateInBuilder =
          new MethodAnalyzer(builderAnalyzerResult, new MethodPredicate.ValidateInBuilder())
              .analyze();
      missingValidateMethodInBuilder = !analyzedValidateInBuilder.isPresent();
      validationMethod = analyzedValidateInBuilder.getElement();
      missingFieldValidationsInBuilder =
          new MethodContentAnalyzer(
              fields, analyzedValidateInBuilder, new FieldPredicate.FieldValidation()).analyze();
      validationFrameworks =
          new ValidationFrameworkAnalyzer(analyzedValidateInBuilder, compilationUnit).analyze();
    }
    catch (JavaModelException e) {
      new IllegalStateException("Something went really wrong: " + e.getMessage());
    }
    return new Analyzed(
        compilationUnit,
        Collections.unmodifiableSet(fields),
        Collections.unmodifiableSet(builderFields),
        type,
        Collections.unmodifiableSet(missingFieldsInBuilder),
        Collections.unmodifiableSet(extraFieldsInBuilder),
        missingBuilder,
        Collections.unmodifiableSet(missingWithMethodsForFields),
        builderType,
        missingConstructorWithBuilder,
        Collections.unmodifiableSet(missingFieldsInConstructorWithBuilder),
        constructorWithBuilder,
        missingBuildMethodInBuilder,
        missingValidateMethodInBuilder,
        Collections.unmodifiableSet(missingFieldValidationsInBuilder),
        validationMethod,
        Collections.unmodifiableCollection(validationFrameworks));
  }

}

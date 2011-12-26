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
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate.FieldValidation;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate.ValidateInBuilder;

import com.google.inject.Inject;

// TODO break down this piece so that it is not so massive:
// eg. MainTypeAnalyer and BuilderTypeAnalyzer and each are composed of a bunch of analyzers
public class CompilationUnitAnalyzer {
  public static final String BUILDER_CLASS_NAME = "Builder";

  private final TypeAnalyzer typeAnalyzer;

  private final BuilderTypeAnalyzer builderTypeAnalyzer;

  private final BuilderTypeFieldAnalyzer builderTypeFieldAnalyzer;

  private final MainTypeFieldAnalyzer mainTypeFieldAnalyzer;

  private final DifferenceBetweenFieldSetsAnalyzer differenceBetweenFieldSetsAnalyzer;

  private final WithMethodsInBuilderAnalyzer withMethodsInBuilderAnalyzer;

  private final ConstructorWithBuilderAnalyzer constructorWithBuilderAnalyzer;

  private final ConstructorWithBuilderInMainTypeAnalyzer constructorWithBuilderInMainTypeAnalyzer;

  private final BuildInBuilderAnalyzer buildInBuilderAnalyzer;

  private final MethodAnalyzer methodAnalyzer;

  private final MethodPredicate.ValidateInBuilder methodPredicate;

  private final MethodContentAnalyzer methodContentAnalyzer;

  private final FieldPredicate.FieldValidation fieldPredicate;

  private final ValidationFrameworkAnalyzer validationFrameworkAnalyzer;

  @Inject
  public CompilationUnitAnalyzer(
      TypeAnalyzer typeAnalyzer,
      BuilderTypeAnalyzer builderTypeAnalyzer,
      BuilderTypeFieldAnalyzer builderTypeFieldAnalyzer,
      MainTypeFieldAnalyzer mainTypeFieldAnalyzer,
      DifferenceBetweenFieldSetsAnalyzer differenceBetweenFieldSetsAnalyzer,
      WithMethodsInBuilderAnalyzer withMethodsInBuilderAnalyzer,
      ConstructorWithBuilderAnalyzer constructorWithBuilderAnalyzer,
      ConstructorWithBuilderInMainTypeAnalyzer constructorWithBuilderInMainTypeAnalyzer,
      BuildInBuilderAnalyzer buildInBuilderAnalyzer, MethodAnalyzer methodAnalyzer,
      ValidateInBuilder methodPredicate, MethodContentAnalyzer methodContentAnalyzer,
      FieldValidation fieldPredicate, ValidationFrameworkAnalyzer validationFrameworkAnalyzer) {
    this.typeAnalyzer = typeAnalyzer;
    this.builderTypeAnalyzer = builderTypeAnalyzer;
    this.builderTypeFieldAnalyzer = builderTypeFieldAnalyzer;
    this.mainTypeFieldAnalyzer = mainTypeFieldAnalyzer;
    this.differenceBetweenFieldSetsAnalyzer = differenceBetweenFieldSetsAnalyzer;
    this.withMethodsInBuilderAnalyzer = withMethodsInBuilderAnalyzer;
    this.constructorWithBuilderAnalyzer = constructorWithBuilderAnalyzer;
    this.constructorWithBuilderInMainTypeAnalyzer = constructorWithBuilderInMainTypeAnalyzer;
    this.buildInBuilderAnalyzer = buildInBuilderAnalyzer;
    this.methodAnalyzer = methodAnalyzer;
    this.methodPredicate = methodPredicate;
    this.methodContentAnalyzer = methodContentAnalyzer;
    this.fieldPredicate = fieldPredicate;
    this.validationFrameworkAnalyzer = validationFrameworkAnalyzer;
  }

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
    TypeResult type = null;
    IType builderType = null;
    boolean missingConstructorWithBuilder = false;
    boolean missingBuilder = true;
    boolean missingBuildMethodInBuilder = true;
    boolean missingValidateMethodInBuilder = true;
    Collection<ValidationFramework> validationFrameworks = null;
    // TODO if we have our own representation of the IField, we can do fancier set operations.
    type = typeAnalyzer.analyze(compilationUnit);
    fields = mainTypeFieldAnalyzer.analyze(type.getElement());
    copyOfFields.addAll(fields);
    TypeResult builderAnalyzerResult = builderTypeAnalyzer.analyze(type.getElement());
    builderType = builderAnalyzerResult.getElement();
    missingBuilder = !builderAnalyzerResult.isPresent();
    builderFields = builderTypeFieldAnalyzer.analyze(builderAnalyzerResult);
    copyOfBuilderFields.addAll(builderFields);
    anotherCopyOfBuilderFields.addAll(builderFields);
    missingFieldsInBuilder =
          differenceBetweenFieldSetsAnalyzer.analyze(copyOfFields, builderFields);
    // flip-flop collections so that the subtraction of sets works
    extraFieldsInBuilder = differenceBetweenFieldSetsAnalyzer
          .analyze(copyOfBuilderFields, fields);
    missingWithMethodsForFields = withMethodsInBuilderAnalyzer.analyze(
            anotherCopyOfBuilderFields, missingFieldsInBuilder, extraFieldsInBuilder,
        builderAnalyzerResult);
    MethodResult constructorWithBuilderResult =
          constructorWithBuilderAnalyzer.analyze(builderAnalyzerResult, type.getElement());
    missingConstructorWithBuilder = !constructorWithBuilderResult.isPresent();
    constructorWithBuilder = constructorWithBuilderResult.getElement();
    missingFieldsInConstructorWithBuilder =
          constructorWithBuilderInMainTypeAnalyzer.analyze(fields, constructorWithBuilderResult);
    missingBuildMethodInBuilder =
          !buildInBuilderAnalyzer.analyze(builderAnalyzerResult).isPresent();
    MethodResult analyzedValidateInBuilder =
          methodAnalyzer.analyze(builderAnalyzerResult, methodPredicate);
    missingValidateMethodInBuilder = !analyzedValidateInBuilder.isPresent();
    validationMethod = analyzedValidateInBuilder.getElement();
    missingFieldValidationsInBuilder =
          methodContentAnalyzer.analyze(fields, analyzedValidateInBuilder, fieldPredicate);
    validationFrameworks =
          validationFrameworkAnalyzer.analyze(analyzedValidateInBuilder, compilationUnit);
    return new Analyzed(
        compilationUnit,
        Collections.unmodifiableSet(fields),
        Collections.unmodifiableSet(builderFields),
        type.getElement(),
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

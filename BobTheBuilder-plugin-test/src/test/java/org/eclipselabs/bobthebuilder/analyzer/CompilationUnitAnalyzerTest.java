package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class CompilationUnitAnalyzerTest {

  @Mock
  private TypeAnalyzer typeAnalyzer;

  @Mock
  private BuilderTypeAnalyzer builderTypeAnalyzer;

  @Mock
  private BuilderTypeFieldAnalyzer builderTypeFieldAnalyzer;

  @Mock
  private MainTypeFieldAnalyzer mainTypeFieldAnalyzer;

  @Mock
  private DifferenceBetweenFieldSetsAnalyzer differenceBetweenFieldSetsAnalyzer;

  @Mock
  private WithMethodsInBuilderAnalyzer withMethodsInBuilderAnalyzer;

  @Mock
  private ConstructorWithBuilderAnalyzer constructorWithBuilderAnalyzer;

  @Mock
  private ConstructorWithBuilderInMainTypeAnalyzer constructorWithBuilderInMainTypeAnalyzer;

  @Mock
  private BuildInBuilderAnalyzer buildInBuilderAnalyzer;

  @Mock
  private MethodAnalyzer methodAnalyzer;

  @Mock
  private MethodPredicate.ValidateInBuilder methodPredicate;

  @Mock
  private MethodContentAnalyzer methodContentAnalyzer;

  @Mock
  private FieldPredicate.FieldValidation fieldPredicate;

  @Mock
  private ValidationFrameworkAnalyzer validationFrameworkAnalyzer;

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IType type;

  @Mock
  private IField field0;
  
  private Set<IField> fields;

  @Mock
  private TypeResult builderAnalyzerResult;

  @Mock
  private IType builderType;

  @Mock
  private IField field1;
  
  private Set<IField> builderFields;

  @Mock
  private IField field2;
  
  private Set<IField> missingFieldsInBuilder;

  @Mock
  private IField field3;

  private Set<IField> extraFieldsInBuilder;

  @Mock
  private IField field4;

  private Set<IField> missingWithMethods;

  @Mock
  private MethodResult constructorWithBuilderResult;

  @Mock
  private IMethod constructorWithBuilder;

  @Mock
  private IField field5;

  private Set<IField> missingFieldsInConstructor;

  @Mock
  private MethodResult buildMethodResult;

  @Mock
  private MethodResult analyzedValidateResult;

  @Mock
  private IMethod validateMethod;

  @Mock
  private IField field6;

  private Set<IField> missingFieldValidations;

  private Collection<ValidationFramework> validationFrameworks = 
    Sets.newHashSet(ValidationFramework.COMMONS_LANG2);

  private CompilationUnitAnalyzer compilationUnitAnalyzer;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    fields = Sets.newHashSet(field0);
    builderFields = Sets.newHashSet(field1);
    missingFieldsInBuilder = Sets.newHashSet(field2);
    extraFieldsInBuilder = Sets.newHashSet(field3);
    missingWithMethods = Sets.newHashSet(field4);
    missingFieldsInConstructor = Sets.newHashSet(field5);
    missingFieldValidations = Sets.newHashSet(field6);
    Mockito.when(typeAnalyzer.analyze(compilationUnit)).thenReturn(type);
    Mockito.when(mainTypeFieldAnalyzer.analyze(type)).thenReturn(fields);
    Mockito.when(builderTypeAnalyzer.analyze(type)).thenReturn(builderAnalyzerResult);
    Mockito.when(builderAnalyzerResult.isPresent()).thenReturn(true);
    Mockito.when(builderAnalyzerResult.getElement()).thenReturn(builderType);
    Mockito.when(builderTypeFieldAnalyzer.analyze(builderAnalyzerResult)).thenReturn(builderFields);
    Mockito.when(differenceBetweenFieldSetsAnalyzer.analyze(fields, builderFields))
        .thenReturn(missingFieldsInBuilder);
    Mockito.when(differenceBetweenFieldSetsAnalyzer.analyze(builderFields, fields)).thenReturn(
      extraFieldsInBuilder);
    Mockito.when(
      withMethodsInBuilderAnalyzer.analyze(builderFields, missingFieldsInBuilder,
        extraFieldsInBuilder, builderAnalyzerResult)).thenReturn(missingWithMethods);
    Mockito.when(constructorWithBuilderAnalyzer.analyze(builderAnalyzerResult, type)).thenReturn(
      constructorWithBuilderResult);
    Mockito.when(constructorWithBuilderResult.isPresent()).thenReturn(true);
    Mockito.when(constructorWithBuilderResult.getElement()).thenReturn(constructorWithBuilder);
    Mockito.when(
      constructorWithBuilderInMainTypeAnalyzer.analyze(fields, constructorWithBuilderResult))
        .thenReturn(missingFieldsInConstructor);
    Mockito.when(buildInBuilderAnalyzer.analyze(builderAnalyzerResult)).thenReturn(
      buildMethodResult);
    Mockito.when(buildMethodResult.isPresent()).thenReturn(true);
    Mockito.when(methodAnalyzer.analyze(builderAnalyzerResult, methodPredicate)).thenReturn(
      analyzedValidateResult);
    Mockito.when(analyzedValidateResult.isPresent()).thenReturn(true);
    Mockito.when(analyzedValidateResult.getElement()).thenReturn(validateMethod);
    Mockito.when(methodContentAnalyzer.analyze(fields, analyzedValidateResult, fieldPredicate))
        .thenReturn(missingFieldValidations);
    Mockito.when(validationFrameworkAnalyzer.analyze(analyzedValidateResult, compilationUnit))
        .thenReturn(validationFrameworks);
    compilationUnitAnalyzer = new CompilationUnitAnalyzer(typeAnalyzer, builderTypeAnalyzer,
        builderTypeFieldAnalyzer, mainTypeFieldAnalyzer, differenceBetweenFieldSetsAnalyzer,
        withMethodsInBuilderAnalyzer, constructorWithBuilderAnalyzer,
        constructorWithBuilderInMainTypeAnalyzer, buildInBuilderAnalyzer, methodAnalyzer,
        methodPredicate, methodContentAnalyzer, fieldPredicate, validationFrameworkAnalyzer);
  }

  @Test
  public void testAnalyze() throws JavaModelException {
    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);
    assertEquals(builderFields, actual.getBuilderFields());
    assertEquals(builderType, actual.getBuilderType());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertEquals(constructorWithBuilder, actual.getConstructorWithBuilder());
    assertEquals(extraFieldsInBuilder, actual.getExtraFieldsInBuilder());
    assertEquals(fields, actual.getFields());
    assertEquals(missingFieldsInBuilder, actual.getMissingFieldsInBuilder());
    assertEquals(missingFieldsInConstructor, actual.getMissingFieldsInConstructorWithBuilder());
    assertEquals(missingFieldValidations, actual.getMissingFieldValidationsInBuilder());
    assertEquals(missingWithMethods, actual.getMissingWithMethodsForFields());
    assertTrue(validationFrameworks.containsAll(actual.getPossibleValidationFrameworks()));
    assertEquals(type, actual.getType());
    assertEquals(validateMethod, actual.getValidateMethodInBuilder());
  }
}

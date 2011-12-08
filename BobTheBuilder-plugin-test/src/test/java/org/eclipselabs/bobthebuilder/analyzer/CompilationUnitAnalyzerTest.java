package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.Signature;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate.FieldValidation;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CompilationUnitAnalyzerTest {
  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IField field1;

  @Mock
  private IField field2;

  @Mock
  private IField field3;

  @Mock
  private IField finalStaticField;

  @Mock
  private IType mainType;

  @Mock
  private IType builderType;

  @Mock
  private IType otherType;

  @Mock
  private IType interfaceType;

  @Mock
  private IType binaryType;

  @Mock
  private IMethod withField1Method;

  @Mock
  private IMethod withField2Method;

  @Mock
  private IMethod withField3Method;

  @Mock
  private IMethod constructorWithBuilder;

  @Mock
  private IMethod constructorWithNoBuilder;

  @Mock
  private IMethod mainTypeMethod;

  private IType[] types;

  @Mock
  private IMethod buildMethod;

  @Mock
  private IMethod validateMethod;

  @Mock
  private IImportDeclaration commonsLang2ValidateImport;

  @Mock
  private IImportDeclaration commonsLang3ValidateImport;

  @Mock
  private IImportDeclaration googlePreconditionsImport;

  @Mock
  private IImportDeclaration someRandomImport;

  private CompilationUnitAnalyzer compilationUnitAnalyzer;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    types = new IType[] { mainType };
    when(compilationUnit.getTypes()).thenReturn(types);
    when(compilationUnit.getImports()).thenReturn(new IImportDeclaration[] {});
    when(mainType.isClass()).thenReturn(true);
    when(mainType.isBinary()).thenReturn(false);
    when(mainType.getMethods()).thenReturn(new IMethod[] {});
    when(field1.getElementName()).thenReturn("field1");
    when(field1.getFlags()).thenReturn(Flags.AccPrivate);
    String type1 = Signature.SIG_INT;
    when(field1.getTypeSignature()).thenReturn(type1);
    when(withField1Method.getElementName()).thenReturn("withField1");
    when(withField1Method.getParameterTypes()).thenReturn(new String[] { type1 });
    when(field2.getElementName()).thenReturn("field2");
    when(field2.getFlags()).thenReturn(Flags.AccPrivate);
    String type2 = Signature.SIG_CHAR;
    when(field2.getTypeSignature()).thenReturn(type2);
    when(withField2Method.getElementName()).thenReturn("withField2");
    when(withField2Method.getParameterTypes()).thenReturn(new String[] { type2 });
    when(field3.getElementName()).thenReturn("field3");
    when(field3.getFlags()).thenReturn(Flags.AccPrivate);
    String type3 = Signature.SIG_BYTE;
    when(field3.getTypeSignature()).thenReturn(type3);
    when(withField3Method.getElementName()).thenReturn("withField3");
    when(withField3Method.getParameterTypes()).thenReturn(new String[] { type3 });
    when(builderType.getMethods()).thenReturn(new IMethod[] {});
    when(otherType.getElementName()).thenReturn("OtherType");
    when(builderType.getElementName()).thenReturn(CompilationUnitAnalyzer.BUILDER_CLASS_NAME);
    when(constructorWithBuilder.isConstructor()).thenReturn(true);
    when(constructorWithBuilder.getSignature()).thenReturn(
      MethodPredicate.ConstructorWithBuilder.CONSTRUCTOR_WITH_BUILDER_SIGNATURE);
    when(constructorWithBuilder.isConstructor()).thenReturn(true);
    when(constructorWithNoBuilder.getSignature()).thenReturn("AnotherSignature");
    when(mainTypeMethod.isConstructor()).thenReturn(false);
    when(finalStaticField.getFlags()).thenReturn(Flags.AccFinal | Flags.AccStatic);
    when(buildMethod.getElementName()).thenReturn(
      MethodPredicate.BuildInBuilder.BUILD_METHOD_NAME);
    when(validateMethod.getElementName()).thenReturn(
      MethodPredicate.ValidateInBuilder.VALIDATE_METHOD_NAME);
    when(validateMethod.getParameterTypes()).thenReturn(new String[] {});
    when(validateMethod.getSource()).thenReturn("private void validate(){}");
    when(commonsLang2ValidateImport.getElementName())
        .thenReturn(ValidationFramework.COMMONS_LANG2.getFullClassName());
    when(commonsLang3ValidateImport.getElementName())
        .thenReturn(ValidationFramework.COMMONS_LANG3.getFullClassName());
    when(googlePreconditionsImport.getElementName())
        .thenReturn(ValidationFramework.GOOGLE_GUAVA.getFullClassName());
    when(someRandomImport.getElementName()).thenReturn("blah.blah.blah");
    compilationUnitAnalyzer = new CompilationUnitAnalyzer(new BuilderTypeAnalyzer(),
        new BuilderTypeFieldAnalyzer(), new MainTypeFieldAnalyzer(),
        new DifferenceBetweenFieldSetsAnalyzer(), new WithMethodsInBuilderAnalyzer(),
        new ConstructorWithBuilderAnalyzer(), new ConstructorWithBuilderInMainTypeAnalyzer(),
        new BuildInBuilderAnalyzer(), new MethodAnalyzer(),
        new MethodPredicate.ValidateInBuilder(), new MethodContentAnalyzer(),
        new FieldValidation(), new ValidationFrameworkAnalyzer());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRequest() throws Exception {
    ICompilationUnit compilationUnit = null;
    compilationUnitAnalyzer.analyze(compilationUnit);
  }

  @Test(expected = IllegalStateException.class)
  public void testInterfaceType() throws Exception {
    types = new IType[] { interfaceType };
    when(compilationUnit.getTypes()).thenReturn(types);
    when(interfaceType.isClass()).thenReturn(false);
    compilationUnitAnalyzer.analyze(compilationUnit);
  }

  @Test(expected = IllegalStateException.class)
  public void testBinaryType() throws Exception {
    types = new IType[] { binaryType };
    when(compilationUnit.getTypes()).thenReturn(types);
    when(interfaceType.isClass()).thenReturn(true);
    when(interfaceType.isBinary()).thenReturn(true);
    compilationUnitAnalyzer.analyze(compilationUnit);
  }

  @Test
  public void testNoBuilder() throws Exception {
    IField[] fields = new IField[] { field1, field2 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] {});

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.isMissingBuilder());
    assertTrue(actual.getExtraFieldsInBuilder().isEmpty());
    Set<IField> expectedFieldsInBuilder = new HashSet<IField>();
    expectedFieldsInBuilder.add(field1);
    expectedFieldsInBuilder.add(field2);
    assertEquals(expectedFieldsInBuilder, actual.getMissingFieldsInBuilder());
    assertEquals(expectedFieldsInBuilder, actual.getMissingWithMethodsForFields());
    assertEquals(expectedFieldsInBuilder, actual.getFields());
    assertTrue(actual.getBuilderFields().isEmpty());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertTrue(actual.isMissingConstructorWithBuilder());
    assertEquals(expectedFieldsInBuilder, actual.getMissingFieldsInConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testNoBuilderButAnotherType() throws Exception {
    IField[] fields = new IField[] { field1, field2 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { otherType });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.isMissingBuilder());
    assertTrue(actual.getExtraFieldsInBuilder().isEmpty());
    Set<IField> expectedFieldsInBuilder = new HashSet<IField>();
    expectedFieldsInBuilder.add(field1);
    expectedFieldsInBuilder.add(field2);
    assertEquals(expectedFieldsInBuilder, actual.getMissingFieldsInBuilder());
    assertEquals(expectedFieldsInBuilder, actual.getMissingWithMethodsForFields());
    assertEquals(expectedFieldsInBuilder, actual.getFields());
    assertTrue(actual.getBuilderFields().isEmpty());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertTrue(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testBuilderWithNoFields() throws Exception {
    IField[] fields = new IField[] { field1, field2 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(new IField[] {});
    when(builderType.getMethods()).thenReturn(new IMethod[] {});

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingBuilder());
    assertTrue(actual.getExtraFieldsInBuilder().isEmpty());
    Set<IField> expectedFieldsInBuilder = new HashSet<IField>();
    expectedFieldsInBuilder.add(field1);
    expectedFieldsInBuilder.add(field2);
    assertEquals(expectedFieldsInBuilder, actual.getMissingFieldsInBuilder());
    assertEquals(expectedFieldsInBuilder, actual.getMissingWithMethodsForFields());
    assertEquals(expectedFieldsInBuilder, actual.getFields());
    assertTrue(actual.getBuilderFields().isEmpty());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertTrue(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testBuilderWithSomeMatchingFields() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3, finalStaticField };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(new IField[] { field1, field2 });
    when(builderType.getMethods()).thenReturn(new IMethod[] { withField1Method, withField2Method });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingBuilder());
    assertTrue(actual.getExtraFieldsInBuilder().isEmpty());
    Set<IField> expectedFieldsInBuilder = new HashSet<IField>();
    expectedFieldsInBuilder.add(field3);
    assertEquals(expectedFieldsInBuilder, actual.getMissingFieldsInBuilder());
    assertEquals(expectedFieldsInBuilder, actual.getMissingWithMethodsForFields());
    Set<IField> expectedFields = new HashSet<IField>();
    expectedFields.add(field1);
    expectedFields.add(field2);
    expectedFields.add(field3);
    assertEquals(expectedFields, actual.getFields());
    assertFalse(actual.getBuilderFields().isEmpty());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertTrue(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testBuilderWithExtraFields() throws Exception {
    IField[] fields = new IField[] { field1, field2 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(
      new IField[] { field1, field2, field3, finalStaticField });
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, withField3Method });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingBuilder());
    Set<IField> expectedExtraFields = new HashSet<IField>();
    expectedExtraFields.add(field3);
    assertEquals(expectedExtraFields, actual.getExtraFieldsInBuilder());
    assertTrue(actual.getMissingFieldsInBuilder().isEmpty());
    assertTrue(actual.getMissingWithMethodsForFields().isEmpty());
    Set<IField> expectedFields = new HashSet<IField>();
    expectedFields.add(field1);
    expectedFields.add(field2);
    assertEquals(expectedFields, actual.getFields());
    Set<IField> expectedBuilderFields = new HashSet<IField>();
    expectedBuilderFields.add(field1);
    expectedBuilderFields.add(field2);
    expectedBuilderFields.add(field3);
    assertEquals(expectedBuilderFields, actual.getBuilderFields());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertTrue(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testBuilderWithExtraAndMissingFields() throws Exception {
    IField[] fields = new IField[] { field1, field2 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(new IField[] { field1, field3 /* extra field */});
    when(builderType.getMethods()).thenReturn(new IMethod[] { withField1Method });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingBuilder());
    Set<IField> expectedExtraFields = new HashSet<IField>();
    expectedExtraFields.add(field3);
    assertEquals(expectedExtraFields, actual.getExtraFieldsInBuilder());
    Set<IField> expectedMissingFields = new HashSet<IField>();
    expectedMissingFields.add(field2);
    assertEquals(expectedMissingFields, actual.getMissingFieldsInBuilder());
    assertEquals(expectedMissingFields, actual.getMissingWithMethodsForFields());
    Set<IField> expectedFields = new HashSet<IField>();
    expectedFields.add(field1);
    expectedFields.add(field2);
    assertEquals(expectedFields, actual.getFields());
    Set<IField> expectedBuilderFields = new HashSet<IField>();
    expectedBuilderFields.add(field1);
    expectedBuilderFields.add(field3);
    assertEquals(expectedBuilderFields, actual.getBuilderFields());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertTrue(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testBuilderWithAllMatchingFields() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, withField3Method });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingBuilder());
    assertTrue(actual.getExtraFieldsInBuilder().isEmpty());
    Set<IField> expectedFields = new HashSet<IField>();
    expectedFields.add(field1);
    expectedFields.add(field2);
    expectedFields.add(field3);
    assertTrue(actual.getMissingFieldsInBuilder().isEmpty());
    assertTrue(actual.getMissingWithMethodsForFields().isEmpty());
    assertEquals(expectedFields, actual.getFields());
    assertEquals(expectedFields, actual.getBuilderFields());
    assertEquals(compilationUnit, actual.getCompilationUnit());
    assertTrue(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testConstructorWithBuilderAndEmptyConstructor() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    Set<IField> expectedFields = new HashSet<IField>();
    expectedFields.add(field1);
    expectedFields.add(field2);
    expectedFields.add(field3);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, withField3Method });
    when(mainType.getMethods())
        .thenReturn(new IMethod[] { constructorWithBuilder, mainTypeMethod });
    when(constructorWithBuilder.getSource()).thenReturn(null);

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingConstructorWithBuilder());
    assertEquals(expectedFields, actual.getMissingFieldsInConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testConstructorWithBuilderAndHalfEmptyConstructor() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    Set<IField> expectedFields = new HashSet<IField>();
    expectedFields.add(field2);
    expectedFields.add(field3);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, withField3Method });
    when(mainType.getMethods())
        .thenReturn(new IMethod[] { constructorWithBuilder, mainTypeMethod });
    when(constructorWithBuilder.getSource()).thenReturn("this.field1 = builder.field1;");

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingConstructorWithBuilder());
    assertEquals(expectedFields, actual.getMissingFieldsInConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testConstructorWithBuilderAndFieldAssignments() throws Exception {
    IField[] fields = new IField[] { field1, field2 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(new IMethod[] { withField1Method, withField2Method });
    when(mainType.getMethods())
        .thenReturn(new IMethod[] { constructorWithBuilder, mainTypeMethod });
    when(constructorWithBuilder.getSource()).thenReturn(
      "this.field1 = builder.field1; this.field2 = builder.field2;");

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.getMissingFieldsInConstructorWithBuilder().isEmpty());
    assertTrue(actual.isMissingValidateMethodInBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testConstructorWithNoBuilder() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, withField3Method });
    when(mainType.getMethods()).thenReturn(
      new IMethod[] { constructorWithNoBuilder, mainTypeMethod });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.isMissingConstructorWithBuilder());
    assertTrue(actual.isThereAnythingToDo());
  }

  @Test
  public void testBuilderWithMissingWithMethods() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(new IMethod[] { withField1Method, withField2Method });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    Set<IField> expectedMissingWithMethodForFields = new HashSet<IField>();
    expectedMissingWithMethodForFields.add(field3);
    assertEquals(expectedMissingWithMethodForFields, actual.getMissingWithMethodsForFields());
    assertTrue(actual.isThereAnythingToDo());
    assertTrue(actual.isMissingBuildMethodInBuilder());
  }

  @Test
  public void testBuilderWithBuildMethod() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.isThereAnythingToDo());
    assertFalse(actual.isMissingBuildMethodInBuilder());
    assertTrue(Arrays.asList(ValidationFramework.values()).containsAll(
      actual.getPossibleValidationFrameworks()));
    assertTrue(actual.getPossibleValidationFrameworks().containsAll(
      Arrays.asList(ValidationFramework.values())));
  }

  @Test
  public void testBuilderWithValidateMethod() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod, validateMethod });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.isThereAnythingToDo());
    assertFalse(actual.isMissingValidateMethodInBuilder());
    assertTrue(Arrays.asList(ValidationFramework.values()).containsAll(
      actual.getPossibleValidationFrameworks()));
    assertTrue(actual.getPossibleValidationFrameworks().containsAll(
      Arrays.asList(ValidationFramework.values())));
  }

  @Test
  public void testBuilderWithMissingFieldsInValidateMethod() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod, validateMethod });
    when(validateMethod.getSource()).thenReturn("Validate.notNull(field1); ");
    when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { commonsLang2ValidateImport });
    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.isThereAnythingToDo());
    assertFalse(actual.isMissingValidateMethodInBuilder());
    HashSet<IField> expected = new HashSet<IField>();
    expected.add(field2);
    expected.add(field3);
    assertEquals(expected, actual.getMissingFieldValidationsInBuilder());
    assertTrue(actual.getPossibleValidationFrameworks().contains(ValidationFramework.COMMONS_LANG2));
    assertEquals(1, actual.getPossibleValidationFrameworks().size());
  }

  @Test
  public void testBuilderWithGuavaValidation() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod, validateMethod });
    when(validateMethod.getSource()).thenReturn("Preconditions.checkNotNull(field1); ");
    when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { googlePreconditionsImport });
    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.getPossibleValidationFrameworks().contains(ValidationFramework.GOOGLE_GUAVA));
    assertEquals(1, actual.getPossibleValidationFrameworks().size());
  }

  @Test
  public void testBuilderWithCommonsLang3Validation() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod, validateMethod });
    when(validateMethod.getSource()).thenReturn("Validate.notNull(field1); ");
    when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { commonsLang3ValidateImport });
    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.getPossibleValidationFrameworks().contains(ValidationFramework.COMMONS_LANG3));
    assertEquals(1, actual.getPossibleValidationFrameworks().size());
  }

  @Test
  public void testBuilderWithNonIdentifiedValidation() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod, validateMethod });
    when(validateMethod.getSource()).thenReturn("MyFancyValidation.makeSureThatNotNull(field1); ");
    when(compilationUnit.getImports()).thenReturn(new IImportDeclaration[] { someRandomImport });
    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(Arrays.asList(ValidationFramework.values()).containsAll(
      actual.getPossibleValidationFrameworks()));
    assertTrue(actual.getPossibleValidationFrameworks().containsAll(
      Arrays.asList(ValidationFramework.values())));
  }

  @Test
  public void testBuilderWithMissingABooleanFieldInValidateMethod() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(field3.getTypeSignature()).thenReturn(Signature.SIG_BOOLEAN);
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod, validateMethod });
    when(validateMethod.getSource()).thenReturn(
      "Validate.notNull(field1); Validate.notNull(field2);");
    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);
    assertTrue(actual.getMissingFieldValidationsInBuilder().isEmpty());
  }

  @Test
  public void testBuilderWithAnotherValidateMethod() throws Exception {
    IField[] fields = new IField[] { field1, field2, field3 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    IMethod anotherValidateMethod = mock(IMethod.class);
    when(anotherValidateMethod.getElementName()).thenReturn(
      MethodPredicate.ValidateInBuilder.VALIDATE_METHOD_NAME);
    when(anotherValidateMethod.getParameterTypes()).thenReturn(new String[] { "newParam" });
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, buildMethod, anotherValidateMethod });

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertTrue(actual.isThereAnythingToDo());
    assertTrue(actual.isMissingValidateMethodInBuilder());
  }

  @Test
  public void testNothingToDo() throws Exception {
    IField[] fields = new IField[] { field1, field2 };
    when(mainType.getFields()).thenReturn(fields);
    when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    when(builderType.getFields()).thenReturn(fields);
    when(builderType.getMethods()).thenReturn(
      new IMethod[] { withField1Method, withField2Method, validateMethod });
    when(mainType.getMethods())
        .thenReturn(new IMethod[] { constructorWithBuilder, mainTypeMethod });
    when(constructorWithBuilder.getSource()).thenReturn(
      "this.field1 = builder.field1; this.field2 = builder.field2;");
    when(validateMethod.getSource()).thenReturn(
      "Validate.notNull(field1); Validate.notNull(field2);");

    Analyzed actual = compilationUnitAnalyzer.analyze(compilationUnit);

    assertFalse(actual.isThereAnythingToDo());
  }
}

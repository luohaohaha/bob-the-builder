package org.eclipselabs.bobthebuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipselabs.bobthebuilder.handlers.Composer;
import org.eclipselabs.bobthebuilder.handlers.ComposerRequest;
import org.eclipselabs.bobthebuilder.handlers.DialogRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ComposerTest {

  @Mock
  private IType type;

  @Mock
  private IType builderType;

  @Mock
  private DialogRequest dialogRequest;

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IField field1;

  private String field1Signature = Signature.SIG_INT;

  private String field1Name = "field1";

  private ComposerRequest.Builder composerRequestBuilder =
      new ComposerRequest.Builder();

  @Mock
  private IField field2;

  private String field2Name = "field2";

  private String field2Signature = Signature.SIG_LONG;

  @Mock
  private IMethod withField2;

  @Mock
  private IField field3;

  private String field3Name = "field3";

  private String field3Signature = "QString;";

  private String field3Type = "String";

  @Captor
  private ArgumentCaptor<String> constructorWithBuilderCaptor;

  @Captor
  private ArgumentCaptor<String> builderFieldCaptor;

  private Set<IField> builderFields = new HashSet<IField>();

  private Set<IField> missingBuilderFields = new HashSet<IField>();

  private Set<IField> extraBuilderFields = new HashSet<IField>();

  @Captor
  private ArgumentCaptor<String> withMethodCaptor;

  private String builderTypeName = "Builder";

  @Captor
  private ArgumentCaptor<String> builderTypeCaptor;

  @Captor
  private ArgumentCaptor<String> buildMethodCaptor;

  @Mock
  private IMethod constructorWithBuilder;

  @Mock
  private ISourceRange constructorSourceRange;

  @Mock
  private IMethod withField1;

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    when(dialogRequest.getType()).thenReturn(type);
    when(dialogRequest.getBuilderType()).thenReturn(builderType);
    when(dialogRequest.getCompilationUnit()).thenReturn(compilationUnit);
    when(dialogRequest.getBuilderFields()).thenReturn(builderFields);
    when(field1.getElementName()).thenReturn(field1Name);
    when(field2.getElementName()).thenReturn(field2Name);
    when(field3.getElementName()).thenReturn(field3Name);
    when(field1.getTypeSignature()).thenReturn(field1Signature);
    when(field2.getTypeSignature()).thenReturn(field2Signature);
    when(field3.getTypeSignature()).thenReturn(field3Signature);
    when(builderType.getElementName()).thenReturn(builderTypeName);
    when(withField1.getElementName()).thenReturn("withField1");
    when(withField2.getElementName()).thenReturn("withField2");
  }

  /**
   * Builder is present. Constructor with builder is missing. BuilderType is missing: Field3 and
   * withField3 method.
   * 
   * @throws JavaModelException
   */
  @Test
  public void testCreateConstructorWithBuilder() throws JavaModelException {
    composerRequestBuilder
        .withConstructorWithBuilder()
        .addMissingFieldInBuilder(field3)
        .addMissingWithMethodInBuilder(field3);
    builderFields.add(field1);
    builderFields.add(field2);
    missingBuilderFields.add(field3);
    when(dialogRequest.getConstructorWithBuilder()).thenReturn(null);
    when(dialogRequest.isMissingBuilder()).thenReturn(false);
    new Composer.Impl().compose(composerRequestBuilder.build(), dialogRequest);
    verify(type).createMethod(
      constructorWithBuilderCaptor.capture(), eq(builderType), eq(true),
      any(IProgressMonitor.class));
    assertTrue(constructorWithBuilderCaptor.getValue().contains(field1Name));
    assertTrue(constructorWithBuilderCaptor.getValue().contains(field2Name));
    assertTrue(constructorWithBuilderCaptor.getValue().contains(field3Name));
    verify(builderType).createField(
      builderFieldCaptor.capture(), any(IJavaElement.class), eq(true), any(IProgressMonitor.class));
    assertEquals("private " + field3Type + " " + field3Name + ";", builderFieldCaptor.getValue());
    verify(builderType).createMethod(
      withMethodCaptor.capture(), any(IJavaElement.class), eq(true), any(IProgressMonitor.class));
    assertTrue(withMethodCaptor.getValue()
        .contains("this." + field3Name + " = " + field3Name + ";"));
    assertFalse(withMethodCaptor.getValue()
        .matches("this." + field1Name + " = " + field1Name + ";"));
  }

  /**
   * Builder is missing. The one field, the with- method and the method are missing as well
   * 
   * @throws JavaModelException
   */
  @Test
  public void testMissingBuilder() throws JavaModelException {
    composerRequestBuilder
        .withConstructorWithBuilder()
        .withBuildMethodInBuilder()
        .addMissingFieldInBuilder(field3)
        .addMissingWithMethodInBuilder(field3);
    missingBuilderFields.add(field1);
    when(dialogRequest.getConstructorWithBuilder()).thenReturn(null);
    when(dialogRequest.isMissingBuilder()).thenReturn(true);
    when(type.getTypes()).thenReturn(new IType[] { builderType });
    new Composer.Impl().compose(
      composerRequestBuilder.build(), dialogRequest);
    verify(type).createMethod(anyString(), eq(builderType), eq(true), any(IProgressMonitor.class));
    verify(builderType).createField(
      anyString(), any(IJavaElement.class), eq(true), any(IProgressMonitor.class));
    verify(type).createType(
      builderTypeCaptor.capture(), any(IJavaElement.class), eq(true), any(IProgressMonitor.class));
    assertTrue(builderTypeCaptor.getValue().contains("public static class Builder"));
    verify(builderType, times(2) /* One time for the with- method and another for the build method */)
        .createMethod(
          buildMethodCaptor.capture(), any(IJavaElement.class), eq(true),
          any(IProgressMonitor.class));
    // The last invocation for the build method is the one we care about
    assertTrue(buildMethodCaptor.getValue().contains("build() {"));
  }

  @Test
  public void testExistingConstructorWithBuilderAndMissingAndExtraFields() throws JavaModelException {
    builderFields.add(field1);
    builderFields.add(field2);
    extraBuilderFields.add(field2);
    missingBuilderFields.add(field3);
    when(builderType.getFields()).thenReturn(new IField[] { field1, field2 });
    when(builderType.getMethods()).thenReturn(new IMethod[] { withField1, withField2 });
    when(dialogRequest.getConstructorWithBuilder()).thenReturn(constructorWithBuilder);
    when(dialogRequest.getBuilderType()).thenReturn(builderType);
    when(dialogRequest.isMissingBuilder()).thenReturn(false);
    when(constructorWithBuilder.getSourceRange()).thenReturn(constructorSourceRange);
    String originalSource =
        "public Type(Builder builder) { this.field2 = builder.field2; this.field1 = builder.field1; }";
    int length = originalSource.length();
    when(constructorSourceRange.getLength()).thenReturn(length);
    when(constructorWithBuilder.getSource()).thenReturn(originalSource);
    new Composer.Impl().compose(
      composerRequestBuilder
          .withConstructorWithBuilder()
          .addMissingAssignmentInConstructor(field3)
          .addExtraFieldInBuilder(field2)
          .addMissingWithMethodInBuilder(field3)
          .addMissingFieldInBuilder(field3)
          .build(),
        dialogRequest);
    verify(constructorWithBuilder).delete(true, null);
    verify(type).createMethod(
      constructorWithBuilderCaptor.capture(), eq(builderType), eq(true),
      any(IProgressMonitor.class));
    assertTrue(constructorWithBuilderCaptor.getValue().contains(field3Name));
    assertFalse(constructorWithBuilderCaptor.getValue().contains(field2Name));
    verify(builderType).createField(
      anyString(), any(IJavaElement.class), eq(true), any(IProgressMonitor.class));
    verify(builderType).createMethod(
      anyString(), any(IJavaElement.class), eq(true), any(IProgressMonitor.class));
    verify(withField2).delete(true, null);
  }
}

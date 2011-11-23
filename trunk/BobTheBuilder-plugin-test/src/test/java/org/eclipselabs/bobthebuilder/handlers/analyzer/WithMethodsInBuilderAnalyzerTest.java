package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;
/**
 * To test {@link WithMethodsInBuilderAnalyzer}
 */
//TODO parameterize the positive tests
public class WithMethodsInBuilderAnalyzerTest {

  private AnalyzerResult.ForType builderPresent;

  private AnalyzerResult.ForType builderMissing;

  private Set<IField> builderFields;

  private Set<IField> missingBuilderFields;

  private Set<IField> extraBuilderFields;

  @Mock
  private IType builderType;

  @Mock
  private IField field1;

  private String field1Name = "field1";

  private String field1Signature = "sign1";

  @Mock
  private IField field2;

  private String field2Name = "field2";

  private String field2Signature = "sign2";

  @Mock
  private IField field3;

  private String field3Name = "field3";

  private String field3Signature = "sign3";

  @Mock
  private IField field4;

  private String field4Name = "field4";

  private String field4Signature = "sign4";

  @Mock
  private IMethod method1;

  private String method1Name = "withField1";

  private String method1Param = field1Signature;

  @Mock
  private IMethod method2;

  private String method2Name = "withField2";

  private String method2Param = field2Signature;

  @Mock
  private IMethod method3;

  private String method3Name = "withField3";

  private String method3Param = field3Signature;

  @Mock
  private IMethod method4;

  private String method4Name = "withField4";

  private String method4Param = field4Signature;

  private Set<IField> setOfFieldsWithANull = Sets.newHashSet(field1, null);

  private Set<IField> emptyFieldSet = Sets.newHashSet();

  private Set<IField> actual;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    builderPresent = AnalyzerResult.ForType.getPresentInstance(builderType);
    builderMissing = AnalyzerResult.ForType.NOT_PRESENT;
    builderFields = Sets.newHashSet(field1, field2);
    missingBuilderFields = Sets.newHashSet(field3);
    extraBuilderFields = Sets.newHashSet(field4);
    when(field1.getElementName()).thenReturn(field1Name);
    when(field2.getElementName()).thenReturn(field2Name);
    when(field3.getElementName()).thenReturn(field3Name);
    when(field4.getElementName()).thenReturn(field4Name);
    when(field1.getTypeSignature()).thenReturn(field1Signature);
    when(field2.getTypeSignature()).thenReturn(field2Signature);
    when(field3.getTypeSignature()).thenReturn(field3Signature);
    when(field4.getTypeSignature()).thenReturn(field4Signature);
    when(method1.getElementName()).thenReturn(method1Name);
    when(method2.getElementName()).thenReturn(method2Name);
    when(method3.getElementName()).thenReturn(method3Name);
    when(method4.getElementName()).thenReturn(method4Name);
    when(method1.getParameterTypes()).thenReturn(new String[] { method1Param });
    when(method2.getParameterTypes()).thenReturn(new String[] { method2Param });
    when(method3.getParameterTypes()).thenReturn(new String[] { method3Param });
    when(method4.getParameterTypes()).thenReturn(new String[] { method4Param });
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzerWithNullBuilderFields() {
    new WithMethodsInBuilderAnalyzer(
        null, missingBuilderFields, builderPresent, extraBuilderFields);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzerWithNullAmongBuilderFields() {
    new WithMethodsInBuilderAnalyzer(
        setOfFieldsWithANull, missingBuilderFields, builderPresent, extraBuilderFields);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzerWithNullMissingBuilderFields() {
    new WithMethodsInBuilderAnalyzer(
        builderFields, null, builderPresent, extraBuilderFields);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzerWithNullAmongMissingBuilderFields() {
    new WithMethodsInBuilderAnalyzer(
        builderFields, setOfFieldsWithANull, builderPresent, extraBuilderFields);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzerWithNullAnalyzed() {
    new WithMethodsInBuilderAnalyzer(
        builderFields, missingBuilderFields, null, extraBuilderFields);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzerWithNullExtraFields() {
    new WithMethodsInBuilderAnalyzer(
        builderFields, missingBuilderFields, builderPresent, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzerWithNullAmongExtraFields() {
    new WithMethodsInBuilderAnalyzer(
        builderFields, missingBuilderFields, builderPresent, setOfFieldsWithANull);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeBuilderMissingButBuilderFieldsNotEmpty() throws JavaModelException {
    builderFields = Sets.newHashSet(field1, field2);
    new WithMethodsInBuilderAnalyzer(
        builderFields, emptyFieldSet, builderMissing, emptyFieldSet).analyze();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeExtraFieldsNotEmptyButBuilderFieldsEmpty() throws JavaModelException {
    extraBuilderFields = Sets.newHashSet(field1, field2);
    new WithMethodsInBuilderAnalyzer(
        emptyFieldSet, emptyFieldSet, builderPresent, extraBuilderFields).analyze();
  }

  @Test
  public void testAnalyzeBuilderIsMissing() throws JavaModelException {
    missingBuilderFields = Sets.newHashSet(field1, field2);
    actual = new WithMethodsInBuilderAnalyzer(
        emptyFieldSet, missingBuilderFields, builderMissing, emptyFieldSet).analyze();
    assertEquals(builderFields, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAnalyzeExtraFieldsNotFoundInBuilderFields() throws JavaModelException {
    extraBuilderFields = Sets.newHashSet(field1, field2);
    builderFields = Sets.newHashSet(field1, field3);
    new WithMethodsInBuilderAnalyzer(
        builderFields, emptyFieldSet, builderPresent, extraBuilderFields);
  }

  @Test
  public void testAnalyzeBuilderPresentMissing1Withs() throws JavaModelException {
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] { method1, method2 });
    builderFields = Sets.newHashSet(field1, field2, field4);
    missingBuilderFields = Sets.newHashSet(field3);
    extraBuilderFields = Sets.newHashSet(field4);
    actual = new WithMethodsInBuilderAnalyzer(
        builderFields, missingBuilderFields, builderPresent, extraBuilderFields).analyze();
    assertEquals(missingBuilderFields, actual);
  }

  @Test
  public void testAnalyzeBuilderPresentMissingAllWiths() throws JavaModelException {
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] {});
    builderFields = Sets.newHashSet(field1, field2, field4);
    missingBuilderFields = Sets.newHashSet(field3);
    extraBuilderFields = Sets.newHashSet(field4);
    actual = new WithMethodsInBuilderAnalyzer(
        builderFields, missingBuilderFields, builderPresent, extraBuilderFields).analyze();
    builderFields.addAll(missingBuilderFields);
    builderFields.removeAll(extraBuilderFields);
    assertEquals(builderFields, actual);
  }
}

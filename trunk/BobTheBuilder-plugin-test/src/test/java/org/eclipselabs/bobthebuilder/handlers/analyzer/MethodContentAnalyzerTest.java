package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

/**
 * To test {@link MethodContentAnalyzer}
 */
public abstract class MethodContentAnalyzerTest {

  private AnalyzerResult.ForMethod analyzedMethodResult;

  private Set<IField> fields;

  private Set<IField> expected;

  private Set<IField> actual;

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
  private IMethod method;

  protected abstract MethodContentAnalyzer getMethodContentAnalyzer(
    Set<IField> fields, ForMethod analyzedMethodResult);

  protected abstract String getSourceToMakeAllFieldsPassPredicate(Set<IField> fields);

  protected abstract String getSourceToMakeAllFieldsFailPredicate(Set<IField> fields);

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    fields = Sets.newHashSet(field1, field2, field3);
    analyzedMethodResult = AnalyzerResult.ForMethod.getPresentInstance(method);
    Mockito.when(field1.getElementName()).thenReturn(field1Name);
    Mockito.when(field1.getTypeSignature()).thenReturn(field1Signature);
    Mockito.when(field2.getElementName()).thenReturn(field2Name);
    Mockito.when(field2.getTypeSignature()).thenReturn(field2Signature);
    Mockito.when(field3.getElementName()).thenReturn(field3Name);
    Mockito.when(field3.getTypeSignature()).thenReturn(field3Signature);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFieldSet() {
    getMethodContentAnalyzer(null, analyzedMethodResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyFieldSet() {
    getMethodContentAnalyzer(Sets.<IField> newHashSet(), analyzedMethodResult);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFieldInSet() {
    getMethodContentAnalyzer(Sets.<IField> newHashSet(null, field1, field2), analyzedMethodResult);
  }

  @Test
  public void testMethodIsNotPresent() throws JavaModelException {
    analyzedMethodResult = AnalyzerResult.ForMethod.NOT_PRESENT;
    expected = fields;
    actual = getMethodContentAnalyzer(fields, analyzedMethodResult).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testMethodHasNoSource() throws JavaModelException {
    Mockito.when(method.getSource()).thenReturn(null);
    expected = fields;
    actual = getMethodContentAnalyzer(fields, analyzedMethodResult).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testAllFieldsPassPredicate() throws JavaModelException {
    String sourceToMakeAllFieldsPassPredicate = getSourceToMakeAllFieldsPassPredicate(fields);
    Mockito.when(method.getSource()).thenReturn(sourceToMakeAllFieldsPassPredicate);
    expected = Sets.newHashSet();
    actual = getMethodContentAnalyzer(fields, analyzedMethodResult).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testAllFieldsFailPredicate() throws JavaModelException {
    String sourceToMakeAllFieldsFailPredicate = getSourceToMakeAllFieldsFailPredicate(fields);
    Mockito.when(method.getSource()).thenReturn(sourceToMakeAllFieldsFailPredicate);
    expected = fields;
    actual = getMethodContentAnalyzer(fields, analyzedMethodResult).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testOneFieldsPassesPredicate() throws JavaModelException {
    HashSet<IField> expected = Sets.newHashSet(field2, field3);
    String sourceToMakeOneFieldPassPredicate =
        getSourceToMakeOneFieldPassPredicate(field1, expected);
    Mockito.when(method.getSource()).thenReturn(
      sourceToMakeOneFieldPassPredicate);
    actual = getMethodContentAnalyzer(fields, analyzedMethodResult).analyze();
    assertEquals(expected, actual);
  }

  private String getSourceToMakeOneFieldPassPredicate(
    IField fieldThatPasses, HashSet<IField> fieldsThatFail) {
    return getSourceToMakeAllFieldsPassPredicate(Sets.newHashSet(field1)) +
      getSourceToMakeAllFieldsFailPredicate(Sets.newHashSet(fieldsThatFail));
  }
}

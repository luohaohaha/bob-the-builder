package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.analyzer.MethodContentAnalyzer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

/**
 * To test {@link MethodContentAnalyzer}
 */
public class MethodContentAnalyzerTest {

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

  @Mock
  private FieldPredicate predicate;

  private MethodContentAnalyzer getMethodContentAnalyzer() {
    return new MethodContentAnalyzer();
  }

  // protected abstract String getSourceToMakeAllFieldsFailPredicate(Set<IField> fields);

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
    Mockito.when(method.getSource()).thenReturn("anything, really");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFieldSet() throws JavaModelException {
    new MethodContentAnalyzer().analyze(null, analyzedMethodResult, predicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyFieldSet() throws JavaModelException {
    new MethodContentAnalyzer().analyze(Sets.<IField>newHashSet(), analyzedMethodResult, predicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFieldInSet() throws JavaModelException {
    new MethodContentAnalyzer().analyze(
      Sets.<IField>newHashSet(field1, null), analyzedMethodResult, predicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPredicate() throws JavaModelException {
    new MethodContentAnalyzer().analyze(fields, analyzedMethodResult, null);
  }

  @Test
  public void testMethodIsNotPresent() throws JavaModelException {
    analyzedMethodResult = AnalyzerResult.ForMethod.NOT_PRESENT;
    expected = fields;
    actual = getMethodContentAnalyzer().analyze(fields, analyzedMethodResult, predicate);
    assertEquals(expected, actual);
  }

  @Test
  public void testMethodHasNoSource() throws JavaModelException {
    Mockito.when(method.getSource()).thenReturn(null);
    expected = fields;
    actual = getMethodContentAnalyzer().analyze(fields, analyzedMethodResult, predicate);
    assertEquals(expected, actual);
  }

  @Test
  public void testAllFieldsPassPredicate() throws JavaModelException {
    Mockito.when(
      predicate.match(Matchers.anyString(), Matchers.anyString(), Matchers.anyString()))
        .thenReturn(true);
    expected = Sets.newHashSet();
    actual = getMethodContentAnalyzer().analyze(fields, analyzedMethodResult, predicate);
    assertEquals(expected, actual);
  }

  @Test
  public void testAllFieldsFailPredicate() throws JavaModelException {
    Mockito.when(
      predicate.match(Matchers.anyString(), Matchers.anyString(), Matchers.anyString()))
        .thenReturn(false);
    expected = fields;
    actual = getMethodContentAnalyzer().analyze(fields, analyzedMethodResult, predicate);
    assertEquals(expected, actual);
  }

  @Test
  public void testOneFieldsPassesPredicate() throws JavaModelException {
    Mockito.when(
      predicate.match(Matchers.eq(field1Name), Matchers.anyString(), Matchers.anyString()))
        .thenReturn(true);
    expected = fields;
    Mockito.when(
      predicate.match(Matchers.eq(field2Name), Matchers.anyString(), Matchers.anyString()))
        .thenReturn(false);
    Mockito.when(
      predicate.match(Matchers.eq(field3Name), Matchers.anyString(), Matchers.anyString()))
        .thenReturn(false);
    expected = fields;
    HashSet<IField> expected = Sets.newHashSet(field2, field3);
    actual = getMethodContentAnalyzer().analyze(fields, analyzedMethodResult, predicate);
    assertEquals(expected, actual);
  }

}

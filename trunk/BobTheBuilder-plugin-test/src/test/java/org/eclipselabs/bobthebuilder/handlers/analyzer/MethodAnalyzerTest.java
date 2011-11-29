package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link MethodAnalyzer}
 */
public class MethodAnalyzerTest {

  protected AnalyzerResult.ForType analyzedTypeResult;

  @Mock
  protected IType type;

  @Mock
  protected IMethod targetMethod;

  protected AnalyzerResult.ForMethod actual;

  protected AnalyzerResult.ForMethod expected;

  @Mock
  private IMethod anotherMethod;

  @Mock
  private MethodPredicate methodPredicate;

  private MethodAnalyzer getMethodAnalyzer() {
    return new MethodAnalyzer(analyzedTypeResult, methodPredicate);
  }

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    analyzedTypeResult = AnalyzerResult.ForType.getPresentInstance(type);
  }

  @Test
  public void testTypeNotPresent() throws JavaModelException {
    analyzedTypeResult = AnalyzerResult.ForType.NOT_PRESENT;
    expected = AnalyzerResult.ForMethod.NOT_PRESENT;
    actual = getMethodAnalyzer().analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testPredicatePasses() throws JavaModelException {
    when(type.getMethods()).thenReturn(new IMethod[] { targetMethod, anotherMethod });
    Mockito.when(methodPredicate.match(targetMethod)).thenReturn(true);
    expected = AnalyzerResult.ForMethod.getPresentInstance(targetMethod);
    actual = getMethodAnalyzer().analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testPredicateFails() throws JavaModelException {
    Mockito.when(methodPredicate.match(anotherMethod)).thenReturn(false);
    when(type.getMethods()).thenReturn(new IMethod[] { anotherMethod });
    expected = AnalyzerResult.ForMethod.NOT_PRESENT;
    actual = getMethodAnalyzer().analyze();
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnalyzedResult() {
    new MethodAnalyzer(null, methodPredicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPredicate() {
    new MethodAnalyzer(analyzedTypeResult, null);
  }
}

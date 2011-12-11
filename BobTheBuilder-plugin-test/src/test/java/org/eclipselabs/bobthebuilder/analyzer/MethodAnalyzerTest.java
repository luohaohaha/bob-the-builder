package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MethodAnalyzer;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link MethodAnalyzer}
 */
public class MethodAnalyzerTest {

  protected TypeResult analyzedTypeResult;

  @Mock
  protected IType type;

  @Mock
  protected IMethod targetMethod;

  protected MethodResult actual;

  protected MethodResult expected;

  @Mock
  private IMethod anotherMethod;

  @Mock
  private MethodPredicate methodPredicate;

  private MethodAnalyzer getMethodAnalyzer() {
    return new MethodAnalyzer();
  }

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    analyzedTypeResult = TypeResult.getPresentInstance(type);
  }

  @Test
  public void testTypeNotPresent() throws JavaModelException {
    analyzedTypeResult = TypeResult.NOT_PRESENT;
    expected = MethodResult.NOT_PRESENT;
    actual = getMethodAnalyzer().analyze(analyzedTypeResult, methodPredicate);
    assertEquals(expected, actual);
  }

  @Test
  public void testPredicatePasses() throws JavaModelException {
    when(type.getMethods()).thenReturn(new IMethod[] { targetMethod, anotherMethod });
    Mockito.when(methodPredicate.match(targetMethod)).thenReturn(true);
    expected = MethodResult.getPresentInstance(targetMethod);
    actual = getMethodAnalyzer().analyze(analyzedTypeResult, methodPredicate);
    assertEquals(expected, actual);
  }

  @Test
  public void testPredicateFails() throws JavaModelException {
    Mockito.when(methodPredicate.match(anotherMethod)).thenReturn(false);
    when(type.getMethods()).thenReturn(new IMethod[] { anotherMethod });
    expected = MethodResult.NOT_PRESENT;
    actual = getMethodAnalyzer().analyze(analyzedTypeResult, methodPredicate);
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnalyzedResult() throws JavaModelException {
    new MethodAnalyzer().analyze(null, methodPredicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullPredicate() throws JavaModelException {
    new MethodAnalyzer().analyze(analyzedTypeResult, null);
  }
}

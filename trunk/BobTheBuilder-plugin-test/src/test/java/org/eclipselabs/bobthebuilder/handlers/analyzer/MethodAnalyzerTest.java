package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link MethodAnalyzer}
 */
public abstract class MethodAnalyzerTest {

  protected AnalyzerResult.ForType analyzedTypeResult;

  @Mock
  protected IType type;

  @Mock
  protected IMethod targetMethod;

  protected AnalyzerResult.ForMethod actual;

  protected AnalyzerResult.ForMethod expected;

  @Mock
  private IMethod anotherMethod;

  protected abstract MethodAnalyzer getMethodAnalyzer(ForType analyzedTypeResult);

  protected abstract void forceFailPredicate(IMethod anotherMethod) throws JavaModelException;

  protected abstract void forcePassPredicate(IMethod targetMethod) throws JavaModelException;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    analyzedTypeResult = AnalyzerResult.ForType.getPresentInstance(type);
    forceFailPredicate(anotherMethod);
  }

  @Test
  public void testTypeNotPresent() throws JavaModelException {
    analyzedTypeResult = AnalyzerResult.ForType.NOT_PRESENT;
    expected = AnalyzerResult.ForMethod.NOT_PRESENT;
    actual = getMethodAnalyzer(analyzedTypeResult).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testPredicatePasses() throws JavaModelException {
    when(type.getMethods()).thenReturn(new IMethod[] { targetMethod, anotherMethod });
    forcePassPredicate(targetMethod);
    expected = AnalyzerResult.ForMethod.getPresentInstance(targetMethod);
    actual = getMethodAnalyzer(analyzedTypeResult).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testPredicateFails() throws JavaModelException {
    when(type.getMethods()).thenReturn(new IMethod[] { anotherMethod });
    expected = AnalyzerResult.ForMethod.NOT_PRESENT;
    actual = getMethodAnalyzer(analyzedTypeResult).analyze();
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnalyzedResult() {
    getMethodAnalyzer(null);
  }
}

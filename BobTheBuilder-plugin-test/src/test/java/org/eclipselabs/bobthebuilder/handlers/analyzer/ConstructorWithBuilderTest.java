package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * To test {@link MethodAnalyzer.ValidateInBuilder} and
 * {@link MethodPredicate.ConstructorWithBuilder}
 */
public class ConstructorWithBuilderTest extends MethodAnalyzerTest {

  @Mock
  private IType mainType;

  private ForType analyzedBuilderTypeResult;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    type = mainType;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnalyzedResult() {
    new MethodAnalyzer.ConstructorWithBuilder(null, mainType);
  }

  @Test
  public void testNotPresentBuilder() throws JavaModelException {
    analyzedBuilderTypeResult = AnalyzerResult.ForType.NOT_PRESENT;
    ForMethod actual =
        new MethodAnalyzer.ConstructorWithBuilder(analyzedBuilderTypeResult, mainType).analyze();
    expected = AnalyzerResult.ForMethod.NOT_PRESENT;
    assertEquals(expected, actual);

  }

  @Override
  protected MethodAnalyzer getMethodAnalyzer(ForType analyzedTypeResult) {
    return new MethodAnalyzer.ConstructorWithBuilder(analyzedTypeResult, mainType);
  }

  @Override
  protected void forceFailPredicate(IMethod anotherMethod) throws JavaModelException {
    Mockito.when(anotherMethod.isConstructor()).thenReturn(false);
  }

  @Override
  protected void forcePassPredicate(IMethod targetMethod) throws JavaModelException {
    Mockito.when(targetMethod.isConstructor()).thenReturn(true);
    Mockito.when(targetMethod.getSignature()).thenReturn(
      MethodPredicate.ConstructorWithBuilder.CONSTRUCTOR_WITH_BUILDER_SIGNATURE);
  }

}

package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.ConstructorWithBuilderAnalyzer;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.junit.Test;
import org.mockito.Mock;

/**
 * To test {@link ValidateMethodInBuilderAnalyzer} and
 * {@link MethodPredicate.ConstructorWithBuilder}
 */
//TODO fix these tests
public class ConstructorWithBuilderTest extends MethodAnalyzerTest {

  @Mock
  private IType mainType;

  private TypeResult analyzedBuilderTypeResult;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    type = mainType;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnalyzedResult() throws JavaModelException {
    new ConstructorWithBuilderAnalyzer().analyze(null, mainType);
  }

  @Test
  public void testNotPresentBuilder() throws JavaModelException {
    analyzedBuilderTypeResult = TypeResult.NOT_PRESENT;
    MethodResult actual =
        new ConstructorWithBuilderAnalyzer().analyze(analyzedBuilderTypeResult, mainType);
    expected = MethodResult.NOT_PRESENT;
    assertEquals(expected, actual);

  }

  @Override
  public void testPredicatePasses() throws JavaModelException {
    // TODO Auto-generated method stub
  }

  @Override
  public void testPredicateFails() throws JavaModelException {
    // TODO Auto-generated method stub
  }

}

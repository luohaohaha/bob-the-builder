package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.IMethod;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;
import org.mockito.Mockito;

/**
 * To test {@link MethodAnalyzer.ValidateInBuilder} and {@link MethodPredicate.ValidateInBuilder}
 */
public class ValidateInBuilderTest extends MethodAnalyzerTest {

  @Override
  protected MethodAnalyzer getMethodAnalyzer(ForType analyzedTypeResult) {
    return new MethodAnalyzer.ValidateInBuilder(analyzedTypeResult);
  }

  @Override
  protected void forceFailPredicate(IMethod anotherMethod) {
    Mockito.when(anotherMethod.getElementName()).thenReturn("whatever");
  }

  @Override
  protected void forcePassPredicate(IMethod targetMethod) {
    Mockito.when(targetMethod.getElementName()).thenReturn(
      MethodPredicate.ValidateInBuilder.VALIDATE_METHOD_NAME);
    Mockito.when(targetMethod.getParameterTypes()).thenReturn(new String[] {});
  }

}

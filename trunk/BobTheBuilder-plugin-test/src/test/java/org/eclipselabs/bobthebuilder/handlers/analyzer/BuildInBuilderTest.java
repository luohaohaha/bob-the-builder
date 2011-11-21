package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.IMethod;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;
import org.mockito.Mockito;

/**
 * To test {@link MethodAnalyzer.BuildInBuilder} and {@link MethodPredicate.BuildInBuilder}
 */
public class BuildInBuilderTest extends MethodAnalyzerTest {

  @Override
  protected MethodAnalyzer getMethodAnalyzer(ForType analyzedTypeResult) {
    return new MethodAnalyzer.BuildInBuilder(analyzedTypeResult);
  }

  @Override
  protected void forceFailPredicate(IMethod anotherMethod) {
    Mockito.when(anotherMethod.getElementName()).thenReturn("whatever");
  }

  @Override
  protected void forcePassPredicate(IMethod targetMethod) {
    Mockito.when(targetMethod.getElementName()).thenReturn(
      MethodPredicate.BuildInBuilder.BUILD_METHOD_NAME);
  }

}

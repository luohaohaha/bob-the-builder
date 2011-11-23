package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * To test {@link MethodAnalyzer.WithMethodInBuilder} and
 * {@link MethodPredicate.WithMethodInBuilderInBuilder}
 */
public class WithMethodInBuilderTest extends MethodAnalyzerTest {

  @Mock
  private IField field;

  private String fieldName = "fieldName";

  private String fieldSignature = "fieldSignature";

  @Override
  public void setUp() throws Exception {
    super.setUp();
    Mockito.when(field.getElementName()).thenReturn(fieldName);
    Mockito.when(field.getTypeSignature()).thenReturn(fieldSignature);
  }

  @Override
  protected MethodAnalyzer getMethodAnalyzer(ForType analyzedTypeResult) {
    return new MethodAnalyzer.WithMethodInBuilder(analyzedTypeResult, field);
  }

  @Override
  protected void forceFailPredicate(IMethod anotherMethod) {
    Mockito.when(anotherMethod.getElementName()).thenReturn("whatever");
  }

  @Override
  protected void forcePassPredicate(IMethod targetMethod) {
    Mockito.when(targetMethod.getElementName()).thenReturn("withFieldName");
    Mockito.when(targetMethod.getParameterTypes()).thenReturn(new String[] { fieldSignature });
  }

}

package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.IField;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * To test {@link WithMethodInBuilderAnalyzer} and
 * {@link MethodPredicate.WithMethodInBuilderInBuilder}
 */
//TODO fix this
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

}

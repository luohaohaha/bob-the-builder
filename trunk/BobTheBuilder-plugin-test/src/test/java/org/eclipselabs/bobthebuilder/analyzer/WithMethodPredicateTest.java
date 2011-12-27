package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class WithMethodPredicateTest {

  private WithMethodPredicate withMethodPredicate;

  @Mock
  private IField field;

  @Mock
  private IMethod method;

  private String fieldName = "fieldName";

  private String fieldSignature = "fieldSignature";
    
  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    withMethodPredicate = new WithMethodPredicate();
    Mockito.when(field.getElementName()).thenReturn(fieldName);
    Mockito.when(field.getTypeSignature()).thenReturn(fieldSignature);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullField() throws JavaModelException {
    withMethodPredicate.match(null, method);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMethod() throws JavaModelException {
    withMethodPredicate.match(field, null);
  }

  @Test
  public void testWithMethodBuilderFailsWrongName() throws JavaModelException {
    Mockito.when(method.getElementName()).thenReturn("withFieldBlahName");
    boolean actual = withMethodPredicate.match(field, method);
    assertFalse(actual);
  }

  @Test
  public void testWithMethodBuilderFailsWrongSignature() throws JavaModelException {
    Mockito.when(method.getElementName()).thenReturn("withFieldName");
    Mockito.when(method.getParameterTypes()).thenReturn(new String[]{"whatever params"});
    boolean actual = withMethodPredicate.match(field, method);
    assertFalse(actual);
  }
  
  @Test
  public void testMatch() throws JavaModelException {
    Mockito.when(method.getElementName()).thenReturn("withFieldName");
    Mockito.when(method.getParameterTypes()).thenReturn(new String[]{fieldSignature});
    boolean actual = withMethodPredicate.match(field, method);
    assertTrue(actual);
  }
}

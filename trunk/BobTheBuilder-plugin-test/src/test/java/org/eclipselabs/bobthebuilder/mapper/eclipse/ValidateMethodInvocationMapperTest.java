package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ValidateMethodInvocationMapperTest {

  private ValidateMethodInvocationMapper validateMethodInvocationMapper;

  @Mock
  private IMethod buildMethod;

  private String source = "private class build() {validate(); return new Class();}";

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    validateMethodInvocationMapper = new ValidateMethodInvocationMapper();
    Mockito.when(buildMethod.getSource()).thenReturn(source);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullBuildMethod() throws JavaModelException {
    validateMethodInvocationMapper.map(null);
  }

  @Test
  public void testNoValidateInvocation() throws JavaModelException {
    Mockito.when(buildMethod.getSource()).thenReturn("");
    boolean actual = validateMethodInvocationMapper.map(buildMethod);
    assertFalse(actual);
  }

  @Test
  public void testValidateInvocation() throws JavaModelException {
    boolean actual = validateMethodInvocationMapper.map(buildMethod);
    assertTrue(actual);
  }
  
  

}

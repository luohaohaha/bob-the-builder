package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipselabs.bobthebuilder.AbstractResult;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link AbstractResult}
 */
public class AnalyzerResultTest {

  @Mock
  private IType type;
  
  @Mock
  private IMethod method;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);  
  }
  
  @Test
  public void testAnalyzerResultForType() {
    TypeResult actual = TypeResult.getPresentInstance(type);
    assertEquals(type, actual.getElement());
    assertTrue(actual.isPresent());
  }

  @Test
  public void testAnalyzerResultForMethod() {
    MethodResult actual = MethodResult.getPresentInstance(method);
    assertEquals(method, actual.getElement());
    assertTrue(actual.isPresent());
  }

  @Test
  public void testTypeIsNotPresent() {
    TypeResult actual = TypeResult.NOT_PRESENT;
    assertNull(actual.getElement());
    assertFalse(actual.isPresent());
  }

  @Test
  public void testMethodIsNotPresent() {
    MethodResult actual = MethodResult.NOT_PRESENT;
    assertNull(actual.getElement());
    assertFalse(actual.isPresent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullType() {
    TypeResult.getPresentInstance(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullMethod() {
    MethodResult.getPresentInstance(null);
  }

}

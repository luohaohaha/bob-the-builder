package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForMethod;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link AnalyzerResult}
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
    ForType actual = AnalyzerResult.ForType.getPresentInstance(type);
    assertEquals(type, actual.getElement());
    assertTrue(actual.isPresent());
  }

  @Test
  public void testAnalyzerResultForMethod() {
    ForMethod actual = AnalyzerResult.ForMethod.getPresentInstance(method);
    assertEquals(method, actual.getElement());
    assertTrue(actual.isPresent());
  }

  @Test
  public void testTypeIsNotPresent() {
    ForType actual = AnalyzerResult.ForType.NOT_PRESENT;
    assertNull(actual.getElement());
    assertFalse(actual.isPresent());
  }

  @Test
  public void testMethodIsNotPresent() {
    ForMethod actual = AnalyzerResult.ForMethod.NOT_PRESENT;
    assertNull(actual.getElement());
    assertFalse(actual.isPresent());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullType() {
    AnalyzerResult.ForType.getPresentInstance(null);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullMethod() {
    AnalyzerResult.ForMethod.getPresentInstance(null);
  }

}

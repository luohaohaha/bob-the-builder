package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult;
import org.eclipselabs.bobthebuilder.analyzer.BuilderTypeAnalyzer;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link BuilderTypeAnalyzer}
 */
public class BuilderTypeAnalyzerTest {

  @Mock
  private IType builderType;

  @Mock
  private IType mainType;

  @Mock
  private IType anotherType;

  private AnalyzerResult.ForType expected;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(builderType.getElementName()).thenReturn(BuilderTypeAnalyzer.BUILDER_CLASS_NAME);
    Mockito.when(anotherType.getElementName()).thenReturn("YEEHA");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() throws JavaModelException {
    new BuilderTypeAnalyzer().analyze(null);
  }

  @Test
  public void testNoBuilderType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[] {});
    ForType actual = new BuilderTypeAnalyzer().analyze(mainType);
    expected = AnalyzerResult.ForType.NOT_PRESENT;
    assertEquals(expected, actual);
  }

  @Test
  public void testBuilderType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    ForType actual = new BuilderTypeAnalyzer().analyze(mainType);
    expected = AnalyzerResult.ForType.getPresentInstance(builderType);
    assertEquals(expected, actual);
  }

  @Test
  public void testMainTypeContainsBuilderTypeAndAnotherType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[] { builderType, anotherType });
    ForType actual = new BuilderTypeAnalyzer().analyze(mainType);
    expected = AnalyzerResult.ForType.getPresentInstance(builderType);
    assertEquals(expected, actual);
  }

  @Test
  public void testMainTypeContainsAnotherType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[] { anotherType });
    expected = AnalyzerResult.ForType.NOT_PRESENT;
    ForType actual = new BuilderTypeAnalyzer().analyze(mainType);
    assertEquals(expected, actual);
  }

}

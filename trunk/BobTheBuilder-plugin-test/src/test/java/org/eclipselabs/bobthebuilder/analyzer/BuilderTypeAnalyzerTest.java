package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.*;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.BuilderTypeAnalyzer;
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

  private TypeResult expected;

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
    TypeResult actual = new BuilderTypeAnalyzer().analyze(mainType);
    expected = TypeResult.NOT_PRESENT;
    assertEquals(expected, actual);
  }

  @Test
  public void testBuilderType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[] { builderType });
    TypeResult actual = new BuilderTypeAnalyzer().analyze(mainType);
    expected = TypeResult.getPresentInstance(builderType);
    assertEquals(expected, actual);
  }

  @Test
  public void testMainTypeContainsBuilderTypeAndAnotherType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[] { builderType, anotherType });
    TypeResult actual = new BuilderTypeAnalyzer().analyze(mainType);
    expected = TypeResult.getPresentInstance(builderType);
    assertEquals(expected, actual);
  }

  @Test
  public void testMainTypeContainsAnotherType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[] { anotherType });
    expected = TypeResult.NOT_PRESENT;
    TypeResult actual = new BuilderTypeAnalyzer().analyze(mainType);
    assertEquals(expected, actual);
  }

}

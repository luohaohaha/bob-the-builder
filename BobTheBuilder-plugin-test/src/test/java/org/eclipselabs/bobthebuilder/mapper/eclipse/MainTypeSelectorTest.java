package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MainTypeSelectorTest {

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IType mainType;

  private MainTypeSelector mainTypeSelector;

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    Mockito.when(compilationUnit.getTypes()).thenReturn(new IType[] { mainType });
    mainTypeSelector = new MainTypeSelector();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() throws JavaModelException {
    mainTypeSelector.map(null);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testMultipleTypes() throws JavaModelException {
    IType anotherType = Mockito.mock(IType.class);
    Mockito.when(compilationUnit.getTypes()).thenReturn(new IType[] { mainType, anotherType });
    mainTypeSelector.map(compilationUnit);
  }
  @Test
  public void testSuccess() throws JavaModelException {
    IType actual = mainTypeSelector.map(compilationUnit);
    assertEquals(mainType, actual);
  }
}

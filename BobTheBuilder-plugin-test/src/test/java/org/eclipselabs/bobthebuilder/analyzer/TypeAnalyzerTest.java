package org.eclipselabs.bobthebuilder.analyzer;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.TypeAnalyzer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class TypeAnalyzerTest {

  @Mock
  private IType mainType;
  
  @Mock
  private IType interfaceType;
  
  @Mock
  private ICompilationUnit compilationUnit;

  private IType actual;
  
  private IType expected;

  @Mock
  private IType binaryType;
  
  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    Mockito.when(mainType.isClass()).thenReturn(true);
    Mockito.when(mainType.isBinary()).thenReturn(false);
    Mockito.when(interfaceType.isClass()).thenReturn(false);
    Mockito.when(binaryType.isClass()).thenReturn(false);
    Mockito.when(binaryType.isBinary()).thenReturn(true);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() {
    new TypeAnalyzer(null);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testNoTypesInCompilationUnit() throws JavaModelException {
    Mockito.when(compilationUnit.getTypes()).thenReturn(new IType[]{});
    new TypeAnalyzer(compilationUnit).analyze();
  }
  
  @Test(expected = IllegalStateException.class)
  public void testManyTypesInCompilationUnit() throws JavaModelException {
    Mockito.when(compilationUnit.getTypes()).thenReturn(new IType[]{mainType, interfaceType});
    new TypeAnalyzer(compilationUnit).analyze();
  }

  @Test(expected = IllegalStateException.class)
  public void testNotAClassTypeInCompilationUnit() throws JavaModelException {
    Mockito.when(compilationUnit.getTypes()).thenReturn(new IType[]{interfaceType});
    new TypeAnalyzer(compilationUnit).analyze();
  }

  @Test(expected = IllegalStateException.class)
  public void testBinaryTypeInCompilationUnit() throws JavaModelException {
    Mockito.when(compilationUnit.getTypes()).thenReturn(new IType[]{binaryType});
    new TypeAnalyzer(compilationUnit).analyze();
  }

  @Test
  public void testMainTypeInCompilationUnit() throws JavaModelException {
    Mockito.when(compilationUnit.getTypes()).thenReturn(new IType[]{mainType});
    actual = new TypeAnalyzer(compilationUnit).analyze();
    expected = mainType;
    assertEquals(expected, actual);
  }
  
}

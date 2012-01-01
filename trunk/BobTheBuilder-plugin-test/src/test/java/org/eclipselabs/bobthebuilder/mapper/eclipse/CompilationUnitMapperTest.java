package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.Imports;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CompilationUnitMapperTest {

  @Mock
  private MainTypeMapper mainTypeMapper;

  @Mock
  private MainTypeSelector mainTypeSelector;

  @Mock
  private ImportStatementMapper importStatementMapper;

  private CompilationUnitMapper compilationUnitMapper;

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IType type;

  @Mock
  private MainType mainType;

  private String mainTypeName = "main type";

  @Mock
  private Imports imports;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    compilationUnitMapper = new CompilationUnitMapper(mainTypeMapper, mainTypeSelector,
        importStatementMapper);
    Mockito.when(mainTypeSelector.map(compilationUnit)).thenReturn(type);
    Mockito.when(mainTypeMapper.map(type, imports)).thenReturn(mainType);
    Mockito.when(type.getElementName()).thenReturn(mainTypeName);
    Mockito.when(importStatementMapper.map(compilationUnit)).thenReturn(imports);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() throws JavaModelException {
    compilationUnitMapper.map(null);
  }

  @Test
  public void testMapCompilationUnit() throws JavaModelException {
    JavaClassFile actual = compilationUnitMapper.map(compilationUnit);
    assertEquals(mainType, actual.getMainType());
    assertEquals(mainTypeName, actual.getName());
    assertEquals(imports, actual.getImports());
  }

}

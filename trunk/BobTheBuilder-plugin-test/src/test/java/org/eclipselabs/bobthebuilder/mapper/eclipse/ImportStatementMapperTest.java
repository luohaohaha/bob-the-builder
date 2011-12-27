package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.ImportStatement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class ImportStatementMapperTest {

  private ImportStatementMapper importStatementMapper;

  @Mock
  private ICompilationUnit compilationUnit;

  private IImportDeclaration[] importDeclarations;

  @Mock
  private IImportDeclaration importDeclaration;

  private String importName = "import name";

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(importDeclaration.getElementName()).thenReturn(importName);
    importDeclarations = new IImportDeclaration[] { importDeclaration };
    Mockito.when(compilationUnit.getImports()).thenReturn(importDeclarations);
    importStatementMapper = new ImportStatementMapper();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() throws JavaModelException {
    importStatementMapper.map(null);
  }

  @Test
  public void testCompilationUnit() throws JavaModelException {
    Set<ImportStatement> actual = importStatementMapper.map(compilationUnit);
    assertEquals(Sets.newHashSet(new ImportStatement(importName)), actual);
  }

  @Test
  public void testCompilationUnitNoImport() throws JavaModelException {
    Mockito.when(compilationUnit.getImports()).thenReturn(new IImportDeclaration[]{});
    Set<ImportStatement> actual = importStatementMapper.map(compilationUnit);
    assertTrue(actual.isEmpty());
  }
}

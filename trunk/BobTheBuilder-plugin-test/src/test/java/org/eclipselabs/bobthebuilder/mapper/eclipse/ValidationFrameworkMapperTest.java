package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ValidationFrameworkMapperTest {

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IMethod validateMethod;

  private ValidationFramework expected;

  private ValidationFramework actual;

  @Mock
  private IImportDeclaration commonsLang2Import;

  @Mock
  private IImportDeclaration commonsLang3Import;

  @Mock
  private IImportDeclaration anotherImport;

  private ValidationFrameworkMapper validationFrameworkMapper;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(commonsLang2Import.getElementName()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getFullClassName());
    Mockito.when(commonsLang3Import.getElementName()).thenReturn(
      ValidationFramework.COMMONS_LANG3.getFullClassName());
    Mockito.when(anotherImport.getElementName()).thenReturn(
      "org.eclipselabs.bobthebuilder.rocks");
    validationFrameworkMapper = new ValidationFrameworkMapper();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() throws JavaModelException {
    validationFrameworkMapper.map(validateMethod, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullmapdResult() throws JavaModelException {
    validationFrameworkMapper.map(null, compilationUnit);
  }

  @Test
  public void testGuavaPresent() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.GOOGLE_GUAVA.getCheckArgument());
    actual = validationFrameworkMapper.map(validateMethod, compilationUnit);
    expected = ValidationFramework.GOOGLE_GUAVA;
    assertEquals(expected, actual);
  }

  @Test
  public void testCommonsLang2Present() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getCheckArgument());
    Mockito.when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { commonsLang2Import });
    actual = validationFrameworkMapper.map(validateMethod, compilationUnit);
    expected = ValidationFramework.COMMONS_LANG2;
    assertEquals(expected, actual);
  }

  @Test
  public void testCommonsLang3Present() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG3.getCheckArgument());
    Mockito.when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { commonsLang3Import });
    actual = validationFrameworkMapper.map(validateMethod, compilationUnit);
    expected = ValidationFramework.COMMONS_LANG3;
    assertEquals(expected, actual);
  }

  @Test
  public void testDefaultCommonsLang2() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getCheckArgument());
    Mockito.when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { anotherImport });
    actual = validationFrameworkMapper.map(validateMethod, compilationUnit);
    expected = ValidationFramework.COMMONS_LANG2;
    assertEquals(expected, actual);
  }

  @Test
  public void testValidatePresentButNoFrameworkUsed() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn("whatever");
    actual = validationFrameworkMapper.map(validateMethod, compilationUnit);
    assertNull(actual);
  }
}

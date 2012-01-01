package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.model.Imports;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ValidationFrameworkMapperTest {

  @Mock
  private Imports imports;

  @Mock
  private IMethod validateMethod;

  private ValidationFramework expected;

  private ValidationFramework actual;

  private ValidationFrameworkMapper validationFrameworkMapper;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(imports.isCommonsLang2()).thenReturn(true);
    Mockito.when(imports.isCommonsLang3()).thenReturn(true);
    validationFrameworkMapper = new ValidationFrameworkMapper();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() throws JavaModelException {
    validationFrameworkMapper.map(validateMethod, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullmapdResult() throws JavaModelException {
    validationFrameworkMapper.map(null, imports);
  }

  @Test
  public void testGuavaPresent() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.GOOGLE_GUAVA.getCheckArgument());
    actual = validationFrameworkMapper.map(validateMethod, imports);
    expected = ValidationFramework.GOOGLE_GUAVA;
    assertEquals(expected, actual);
  }

  @Test
  public void testCommonsLang2Present() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getCheckArgument());
    actual = validationFrameworkMapper.map(validateMethod, imports);
    expected = ValidationFramework.COMMONS_LANG2;
    assertEquals(expected, actual);
  }

  @Test
  public void testCommonsLang3Present() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG3.getCheckArgument());
    Mockito.when(imports.isCommonsLang2()).thenReturn(false);
    actual = validationFrameworkMapper.map(validateMethod, imports);
    expected = ValidationFramework.COMMONS_LANG3;
    assertEquals(expected, actual);
  }

  @Test
  public void testDefaultCommonsLang2() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getCheckArgument());
    Mockito.when(imports.isCommonsLang2()).thenReturn(false);
    Mockito.when(imports.isCommonsLang3()).thenReturn(false);
    actual = validationFrameworkMapper.map(validateMethod, imports);
    expected = ValidationFramework.COMMONS_LANG2;
    assertEquals(expected, actual);
  }

  @Test
  public void testValidatePresentButNoFrameworkUsed() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn("whatever");
    actual = validationFrameworkMapper.map(validateMethod, imports);
    assertNull(actual);
  }
}

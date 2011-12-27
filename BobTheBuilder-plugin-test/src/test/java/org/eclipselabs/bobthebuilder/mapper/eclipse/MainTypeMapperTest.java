package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MainTypeMapperTest {
  @Mock
  private BuilderTypeMapper builderTypeMapper;

  private MainTypeMapper mainTypeMapper;

  @Mock
  private IType mainType;

  @Mock
  private BuilderType builderType;

  private String typeName;

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    mainTypeMapper = new MainTypeMapper(builderTypeMapper);
    Mockito.when(builderTypeMapper.map(mainType)).thenReturn(builderType);
    Mockito.when(mainType.isClass()).thenReturn(true);
    Mockito.when(mainType.isBinary()).thenReturn(false);
    Mockito.when(mainType.getElementName()).thenReturn(typeName);
  }
  @Test(expected = IllegalArgumentException.class)
  public void testNullType() throws JavaModelException {
    mainTypeMapper.map(null);
  }

  @Test(expected = IllegalStateException.class)
  public void testNotClassFile() throws JavaModelException {
    Mockito.when(mainType.isClass()).thenReturn(false);
    mainTypeMapper.map(mainType);
  }

  @Test(expected = IllegalStateException.class)
  public void testBinaryFile() throws JavaModelException {
    Mockito.when(mainType.isBinary()).thenReturn(true);
    mainTypeMapper.map(mainType);
  }
  
  @Ignore
  @Test
  public void testSuccess() throws JavaModelException {
    MainType actual = mainTypeMapper.map(mainType);
    assertEquals(typeName, actual.getName());
    assertEquals(builderType, actual.getBuilderType());
  }
}

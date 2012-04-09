package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilder;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.Imports;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class MainTypeMapperTest {
  @Mock
  private BuilderTypeMapper builderTypeMapper;

  @Mock
  private MainTypeMapper mainTypeMapper;

  @Mock
  private IType mainType;

  @Mock
  private BuilderType builderType;

  private String typeName;

  @Mock
  private Imports imports;

  @Mock
  private FieldMapper fieldMapper;

  @Mock
  private ConstructorWithBuilderMapper constructorWithBuilderMapper;

  private Field field1 = new Field.Builder()
      .withName("field1")
      .withSignature("signature1")
      .withPosition(1)
      .build();

  private Field field2 = new Field.Builder()
      .withName("field2")
      .withSignature("signature2")
      .withPosition(2)
      .build();

  private Set<Field> fields = Sets.newHashSet(field1, field2);

  @Mock
  private ConstructorWithBuilder constructorWithBuilder;

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    mainTypeMapper = new MainTypeMapper(builderTypeMapper, fieldMapper,
        constructorWithBuilderMapper);
    when(fieldMapper.map(mainType)).thenReturn(fields);
    when(builderTypeMapper.map(mainType, imports, fields)).thenReturn(builderType);
    when(mainType.isClass()).thenReturn(true);
    when(mainType.isBinary()).thenReturn(false);
    when(mainType.getElementName()).thenReturn(typeName);
    when(constructorWithBuilderMapper.map(mainType, fields)).thenReturn(constructorWithBuilder);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullType() throws JavaModelException {
    mainTypeMapper.map(null, imports);
  }

  @Test(expected = IllegalStateException.class)
  public void testNotClassFile() throws JavaModelException {
    Mockito.when(mainType.isClass()).thenReturn(false);
    mainTypeMapper.map(mainType, imports);
  }

  @Test(expected = IllegalStateException.class)
  public void testBinaryFile() throws JavaModelException {
    Mockito.when(mainType.isBinary()).thenReturn(true);
    mainTypeMapper.map(mainType, imports);
  }

  @Test
  public void testSuccess() throws JavaModelException {
    MainType actual = mainTypeMapper.map(mainType, imports);
    assertEquals(typeName, actual.getName());
    assertEquals(builderType, actual.getBuilderType());
  }
}

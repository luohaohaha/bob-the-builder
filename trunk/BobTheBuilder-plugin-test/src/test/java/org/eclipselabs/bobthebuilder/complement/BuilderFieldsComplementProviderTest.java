package org.eclipselabs.bobthebuilder.complement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class BuilderFieldsComplementProviderTest {

  @Mock
  private Field field1;

  @Mock
  private Field field2;

  @Mock
  private Field field3;

  @Mock
  private MainType mainType;

  @Mock
  private BuilderType builderType;

  private BuilderFieldsComplementProvider builderFieldsComplementProvider;

  private Set<Field> mainTypeFields;

  private Set<Field> builderTypeFields;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    builderFieldsComplementProvider = new BuilderFieldsComplementProvider();
    mainTypeFields = Sets.newHashSet(field1, field2, field3);
    builderTypeFields = Sets.newHashSet(field1);
    Mockito.when(mainType.getFields()).thenReturn(mainTypeFields);
    Mockito.when(builderType.getBuilderFields()).thenReturn(builderTypeFields);
    Mockito.when(mainType.getBuilderType()).thenReturn(builderType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() {
    builderFieldsComplementProvider.complement(null);
  }

  @Test
  public void testNullBuilderType() {
    Mockito.when(mainType.getBuilderType()).thenReturn(null);
    Set<Field> actual = builderFieldsComplementProvider.complement(mainType);
    assertEquals(mainTypeFields, actual);
  }

  @Test
  public void testEmptyBuilderFields() {
    Mockito.when(builderType.getBuilderFields()).thenReturn(Sets.<Field> newHashSet());
    Set<Field> actual = builderFieldsComplementProvider.complement(mainType);
    assertEquals(mainTypeFields, actual);
  }

  @Test
  public void testNoComplement() {
    Mockito.when(builderType.getBuilderFields()).thenReturn(mainTypeFields);
    Set<Field> actual = builderFieldsComplementProvider.complement(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testComplement() {
    Set<Field> actual = builderFieldsComplementProvider.complement(mainType);
    assertEquals(Sets.newHashSet(field2, field3), actual);
  }
}

package org.eclipselabs.bobthebuilder.supplement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.mapper.eclipse.BuilderTypeMapper;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class BuilderFieldsSupplementProviderTest {
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

  private BuilderFieldsSupplementProvider builderFieldsSupplementProvider;

  private Set<Field> mainTypeFields;

  private Set<Field> builderTypeFields;

  @Mock
  private BuilderTypeMapper builderTypeMapper;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    builderFieldsSupplementProvider = new BuilderFieldsSupplementProvider(builderTypeMapper);
    mainTypeFields = Sets.newHashSet(field1);
    builderTypeFields = Sets.newHashSet(field1, field2, field3);
    Mockito.when(mainType.getFields()).thenReturn(mainTypeFields);
    Mockito.when(builderType.getBuilderFields()).thenReturn(builderTypeFields);
    Mockito.when(mainType.getBuilderType()).thenReturn(builderType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() throws JavaModelException {
    builderFieldsSupplementProvider.supplement((MainType) null);
  }

  @Test
  public void testNullBuilderType() throws JavaModelException {
    Mockito.when(mainType.getBuilderType()).thenReturn(null);
    Collection<Field> actual = builderFieldsSupplementProvider.supplement(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testEmptyBuilderFields() throws JavaModelException {
    Mockito.when(builderType.getBuilderFields()).thenReturn(Sets.<Field> newHashSet());
    Collection<Field> actual = builderFieldsSupplementProvider.supplement(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testNoSupplement() throws JavaModelException {
    Mockito.when(builderType.getBuilderFields()).thenReturn(mainTypeFields);
    Collection<Field> actual = builderFieldsSupplementProvider.supplement(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testSupplement() throws JavaModelException {
    Collection<Field> actual = builderFieldsSupplementProvider.supplement(mainType);
    assertTrue(Sets.newHashSet(field2, field3).containsAll(actual));
  }
}

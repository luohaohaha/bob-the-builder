package org.eclipselabs.bobthebuilder.complement;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;
import org.eclipselabs.bobthebuilder.model.ValidateMethodComplement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class ValidateMethodComplementProviderTest {

  private ValidateMethodComplementProvider validateMethodComplementProvider;

  @Mock
  private MainType mainType;

  @Mock
  private BuilderType builderType;

  @Mock
  private ValidateMethod validateMethod;

  @Mock
  private Field field1;

  @Mock
  private Field field2;

  @Mock
  private Field field3;

  private String field1Name = "field1";

  private String field2Name = "field2";

  private String field3Name = "field3";

  private FieldAssignment fieldAssignment1;

  private FieldAssignment fieldAssignment2;

  private Set<FieldAssignment> fieldAssignments;

  private Set<Field> fields;

  private FieldAssignment fieldAssignment3;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    fieldAssignment1 = new FieldAssignment(field1Name);
    fieldAssignment2 = new FieldAssignment(field2Name);
    fieldAssignment3 = new FieldAssignment(field3Name);
    fieldAssignments = Sets.newHashSet(fieldAssignment1, fieldAssignment2);
    validateMethodComplementProvider = new ValidateMethodComplementProvider();
    Mockito.when(mainType.getBuilderType()).thenReturn(builderType);
    Mockito.when(field1.getName()).thenReturn(field1Name);
    Mockito.when(field2.getName()).thenReturn(field2Name);
    Mockito.when(field3.getName()).thenReturn(field3Name);
    fields = Sets.newHashSet(field1, field2, field3);
    Mockito.when(mainType.getFields()).thenReturn(fields);
    Mockito.when(builderType.getValidateMethod()).thenReturn(validateMethod);
    Mockito.when(validateMethod.getValidatedFields()).thenReturn(fieldAssignments);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() {
    validateMethodComplementProvider.complement(null);
  }

  @Test
  public void testNullBuilderType() {
    Mockito.when(mainType.getBuilderType()).thenReturn(null);
    ValidateMethodComplement actual = validateMethodComplementProvider.complement(mainType);
    assertEquals(
      Sets.newHashSet(fieldAssignment1, fieldAssignment2, fieldAssignment3),
      actual.getFieldAssignments());
  }

  @Test
  public void testNullValidateMethod() {
    Mockito.when(builderType.getValidateMethod()).thenReturn(null);
    ValidateMethodComplement actual = validateMethodComplementProvider.complement(mainType);
    assertEquals(
      Sets.newHashSet(fieldAssignment1, fieldAssignment2, fieldAssignment3),
      actual.getFieldAssignments());
  }

  @Test
  public void testComplement() {
    ValidateMethodComplement actual = validateMethodComplementProvider.complement(mainType);
    assertEquals(Sets.newHashSet(fieldAssignment3), actual.getFieldAssignments());
  }

}

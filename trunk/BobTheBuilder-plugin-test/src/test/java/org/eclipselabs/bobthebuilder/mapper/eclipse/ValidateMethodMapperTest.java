package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.eclipselabs.bobthebuilder.model.Imports;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class ValidateMethodMapperTest {

  @Mock
  private ValidateFieldsMethodMapper validatedFieldsMapper;

  @Mock
  private MethodPredicate.ValidateInBuilder predicate;

  @Mock
  private ValidationFrameworkMapper validationFrameworkMapper;

  private ValidateMethodMapper validateMethodMapper;

  @Mock
  private IType builderType;

  @Mock
  private IMethod validateMethod;

  private String source = "private void validate() {/*do something*/}";

  private Set<Field> fields;

  @Mock
  private Field field1;

  @Mock
  private Field field2;

  @Mock
  private Imports imports;

  private Set<FieldAssignment> fieldAssignments = Sets.newHashSet(new FieldAssignment("field1"),
    new FieldAssignment("field2"));

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(predicate.match(validateMethod)).thenReturn(true);
    Mockito.when(validateMethod.getSource()).thenReturn(source);
    fields = Sets.newHashSet(field1, field2);
    Mockito.when(validatedFieldsMapper.map(validateMethod, fields)).thenReturn(fieldAssignments);
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] { validateMethod });
    validateMethodMapper = new ValidateMethodMapper(validatedFieldsMapper, predicate,
        validationFrameworkMapper);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBuilderTyper() throws JavaModelException {
    validateMethodMapper.map(null, imports, fields);
  }

  @Test
  public void testNoValidateMethod() throws JavaModelException {
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] {});
    ValidateMethod actual = validateMethodMapper.map(builderType, imports, fields);
    assertNull(actual);
  }

  @Test
  public void testMapValidateMethod() throws JavaModelException {
    ValidateMethod actual = validateMethodMapper.map(builderType, imports, fields);
    assertNotNull(actual);
    assertEquals(source, actual.getSource());
    assertEquals(fieldAssignments, actual.getValidatedFields());
    assertEquals(null, actual.getValidationFramework());
  }

}

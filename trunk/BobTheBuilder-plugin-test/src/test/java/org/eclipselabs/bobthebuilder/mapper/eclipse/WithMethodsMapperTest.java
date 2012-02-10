package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.WithMethodPredicate;
import org.eclipselabs.bobthebuilder.model.WithMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class WithMethodsMapperTest {

  @Mock
  private WithMethodPredicate withMethodPredicate;

  @Mock
  private IField field1;

  @Mock
  private IField field2;

  @Mock
  private IMethod method1;

  @Mock
  private IMethod method3;

  @Mock
  private IType builderType;

  private WithMethodsMapper withMethodsMapper;

  private String method1Name = "withField1";

  @Mock
  private FieldMapper fieldMapper;

  @Mock
  private BuilderTypeMapper builderTypeMapper;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(withMethodPredicate.match(field1, method1)).thenReturn(true);
    Mockito.when(withMethodPredicate.match(field2, method3)).thenReturn(false);
    Mockito.when(withMethodPredicate.match(field1, method3)).thenReturn(false);
    Mockito.when(withMethodPredicate.match(field2, method1)).thenReturn(false);
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] { method1, method3 });
    Mockito.when(method1.getElementName()).thenReturn(method1Name);
    Mockito.when(fieldMapper.findFields(builderType)).thenReturn(Sets.newHashSet(field1, field2));
    withMethodsMapper = new WithMethodsMapper(withMethodPredicate, fieldMapper, builderTypeMapper);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullType() throws JavaModelException {
    withMethodsMapper.map(null);
  }

  @Test
  public void testNoMatches() throws JavaModelException {
    Mockito.when(builderType.getFields()).thenReturn(new IField[] { field2 });
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] { method3 });
    Set<WithMethod> actual = withMethodsMapper.map(builderType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testMatches() throws JavaModelException {
    Set<WithMethod> actual = withMethodsMapper.map(builderType);
    assertFalse(actual.isEmpty());
    Set<WithMethod> expected = Sets.newHashSet(
        new WithMethod.Builder().withName(method1Name).build());
    assertEquals(expected, actual);
  }
}

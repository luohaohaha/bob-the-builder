package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.WithMethodPredicate;
import org.eclipselabs.bobthebuilder.model.WithMethod;
import org.eclipselabs.bobthebuilder.supplement.BuilderFieldsSupplementProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class WithMethodsSupplementProviderTest {

  @Mock
  private BuilderTypeMapper builderTypeMapper;

  @Mock
  private WithMethodPredicate withMethodPredicate;

  @Mock
  private BuilderFieldsSupplementProvider builderFieldsSupplementProvider;

  private WithMethodsSupplementProvider withMethodsSupplementProvider;

  @Mock
  private IType mainType;

  private HashSet<IField> extraFields;

  @Mock
  private IField field1;

  @Mock
  private IField field2;

  @Mock
  private IType builderType;

  private IMethod[] methods;

  @Mock
  private IMethod withMethod1;

  @Mock
  private IMethod anotherMethod;

  private String method1Name = "withMethod1";

  private String field1Name = "field1";

  private String field1Signature = "field1Signature";

  @Mock
  private ISourceRange sourceRange1;

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    withMethodsSupplementProvider = new WithMethodsSupplementProvider(
        builderTypeMapper, withMethodPredicate, builderFieldsSupplementProvider);
    extraFields = Sets.<IField> newHashSet(field1, field2);
    when(builderFieldsSupplementProvider.supplement(mainType)).thenReturn(extraFields);
    when(builderTypeMapper.findBuilderType(mainType)).thenReturn(builderType);
    when(withMethod1.getElementName()).thenReturn(method1Name);
    methods = new IMethod[] { withMethod1, anotherMethod };
    when(builderType.getMethods()).thenReturn(methods);
    when(withMethodPredicate.match(field1, withMethod1)).thenReturn(true);
    when(withMethodPredicate.match(field1, anotherMethod)).thenReturn(false);
    when(withMethodPredicate.match(field2, withMethod1)).thenReturn(false);
    when(withMethodPredicate.match(field2, anotherMethod)).thenReturn(false);
    when(field1.getElementName()).thenReturn(field1Name);
    when(field1.getTypeSignature()).thenReturn(field1Signature);
    when(field1.getSourceRange()).thenReturn(sourceRange1);
    when(sourceRange1.getOffset()).thenReturn(1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() throws JavaModelException {
    withMethodsSupplementProvider.findExtra(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMappedNullMainType() throws JavaModelException {
    withMethodsSupplementProvider.findMappedExtra(null);
  }

  @Test
  public void testEmptyExtraFields() throws Exception {
    when(builderFieldsSupplementProvider.supplement(mainType)).thenReturn(
      Sets.<IField> newHashSet());
    Map<IField, IMethod> actual = withMethodsSupplementProvider.findExtra(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testMappedEmptyExtraFields() throws Exception {
    when(builderFieldsSupplementProvider.supplement(mainType)).thenReturn(
      Sets.<IField> newHashSet());
    Set<WithMethod> actual = withMethodsSupplementProvider.findMappedExtra(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testSomeExtraFields() throws Exception {
    Map<IField, IMethod> actual = withMethodsSupplementProvider.findExtra(mainType);
    assertEquals(1, actual.size());
    assertEquals(withMethod1, actual.values().iterator().next());
  }

  @Test
  public void testMappedSomeExtraFields() throws Exception {
    Set<WithMethod> actual = withMethodsSupplementProvider.findMappedExtra(mainType);
    assertEquals(1, actual.size());
    assertEquals(withMethod1.getElementName(), actual.iterator().next().getName());
  }
}

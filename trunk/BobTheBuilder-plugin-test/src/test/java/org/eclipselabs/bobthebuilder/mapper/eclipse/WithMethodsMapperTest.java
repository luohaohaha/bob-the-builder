package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.WithMethodPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
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

  private String field1Name = "field1";

  private String field1Signature = "QString;";

  private String field2Name = "field1";

  private String field2Signature = "Qlong;";

  @Mock
  private ISourceRange sourceRange1;

  @Mock
  private ISourceRange sourceRange2;

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
    Mockito.when(field1.getElementName()).thenReturn(field1Name);
    Mockito.when(field1.getTypeSignature()).thenReturn(field1Signature);
    Mockito.when(field1.getSourceRange()).thenReturn(sourceRange1);
    Mockito.when(field2.getElementName()).thenReturn(field2Name);
    Mockito.when(field2.getTypeSignature()).thenReturn(field2Signature);
    Mockito.when(field2.getSourceRange()).thenReturn(sourceRange2);
    Mockito.when(sourceRange1.getOffset()).thenReturn(1);
    Mockito.when(sourceRange2.getOffset()).thenReturn(1);
    withMethodsMapper = new WithMethodsMapper(withMethodPredicate, fieldMapper);
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
        new WithMethod.Builder()
            .withName(method1Name)
            .withField(
              new Field.Builder()
                  .withName(field1Name)
                  .withSignature("String")
                  .withPosition(1)
                  .build())
            .build());
    assertEquals(expected, actual);
  }
}

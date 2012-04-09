package org.eclipselabs.bobthebuilder.supplement;

import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.mapper.eclipse.BuilderTypeMapper;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FieldMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

//TODO this feels a lot like BuilderFieldsSupplementProviderTest
public class RawFieldSupplementorTest {
  @Mock
  private IField field1;

  @Mock
  private IField field2;

  @Mock
  private IField field3;

  @Mock
  private IType mainType;

  @Mock
  private IType builderType;

  private RawFieldSupplementor builderFieldsSupplementProvider;

  private IField[] mainTypeFields;

  private IField[] builderTypeFields;

  @Mock
  private BuilderTypeMapper builderTypeMapper;

  @Mock
  private FieldMapper fieldMapper;

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    builderFieldsSupplementProvider = new RawFieldSupplementor(builderTypeMapper, fieldMapper);
    mainTypeFields = new IField[]{field1};
    builderTypeFields = new IField[]{field1, field2, field3};
    Mockito.when(mainType.getFields()).thenReturn(mainTypeFields);
    Mockito.when(builderType.getFields()).thenReturn(builderTypeFields);
    Mockito.when(mainType.getTypes()).thenReturn(new IType[]{builderType});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() throws JavaModelException {
    builderFieldsSupplementProvider.findSupplement((IType) null);
  }

  @Test
  public void testNoBuilderType() throws JavaModelException {
    Mockito.when(mainType.getTypes()).thenReturn(new IType[]{});
    Collection<IField> actual = builderFieldsSupplementProvider.findSupplement(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testEmptyBuilderFields() throws JavaModelException {
    Mockito.when(builderType.getFields()).thenReturn(new IField[]{});
    Collection<IField> actual = builderFieldsSupplementProvider.findSupplement(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testNoSupplement() throws JavaModelException {
    Mockito.when(builderType.getFields()).thenReturn(mainTypeFields);
    Collection<IField> actual = builderFieldsSupplementProvider.findSupplement(mainType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testSupplement() throws JavaModelException {
    Collection<IField> actual = builderFieldsSupplementProvider.findSupplement(mainType);
    assertTrue(Sets.newHashSet(field2, field3).containsAll(actual));
  }
}

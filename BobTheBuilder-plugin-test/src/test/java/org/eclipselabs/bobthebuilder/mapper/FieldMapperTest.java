package org.eclipselabs.bobthebuilder.mapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FieldMapper;
import org.eclipselabs.bobthebuilder.model.Field;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class FieldMapperTest {

  @Mock
  private IField field1;

  private String field1Name = "field1";

  private String field1Signature = "field1Signature";

  @Mock
  private IField finalField2;

  private String field2Name = "field2";

  private String field2Signature = "field2Signature";

  @Mock
  private IField staticFinalField3;

  private Set<Field> expected;

  @Mock
  private IType mainType;

  private FieldMapper fieldMapper;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(field1.getFlags()).thenReturn(Flags.AccDefault);
    Mockito.when(field1.getElementName()).thenReturn(field1Name);
    Mockito.when(field1.getTypeSignature()).thenReturn(field1Signature);
    Mockito.when(finalField2.getFlags()).thenReturn(Flags.AccFinal);
    Mockito.when(finalField2.getElementName()).thenReturn(field2Name);
    Mockito.when(finalField2.getTypeSignature()).thenReturn(field2Signature);
    Mockito.when(staticFinalField3.getFlags()).thenReturn(Flags.AccFinal | Flags.AccStatic);
    Mockito.when(mainType.getFields()).thenReturn(
      new IField[] { field1, finalField2, staticFinalField3 });
    expected = Sets.newHashSet();
    fieldMapper = new FieldMapper();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullType() throws Exception {
    fieldMapper.map(null);
  }

  @Test
  public void testMainTypeFields() throws JavaModelException {
    expected.addAll(Sets.newHashSet(
      new Field.Builder().withName(field1Name).withSignature(field1Signature).build(),
      new Field.Builder().withName(field2Name).withSignature(field2Signature).build()
        ));
    Set<Field> actual = fieldMapper.map(mainType);
    assertEquals(expected, actual);
  }

  @Test
  public void testMainTypeNoFields() throws JavaModelException {
    Mockito.when(mainType.getFields()).thenReturn(new IField[] {});
    Set<Field> actual = fieldMapper.map(mainType);
    assertTrue(actual.isEmpty());
  }

}

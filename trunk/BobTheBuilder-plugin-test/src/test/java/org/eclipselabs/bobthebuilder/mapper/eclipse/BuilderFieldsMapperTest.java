package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MainTypeFieldAnalyzer;
import org.eclipselabs.bobthebuilder.model.Field;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class BuilderFieldsMapperTest {

  @Mock
  private IType builderType;

  @Mock
  private MainTypeFieldAnalyzer mainTypeFieldAnalyzer;

  private Set<IField> fields;

  @Mock
  private IField field1;

  @Mock
  private IField field2;

  @Mock
  private IField field3;

  private String field1Name = "field1";

  private String field2Name = "field2";

  private String field3Name = "field3";

  private String field1Signature = "signature1";

  private String field2Signature = "signature2";

  private String field3Signature = "signature3";

  private BuilderFieldsMapper builderFieldsMapper;

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    fields = Sets.newHashSet(field1, field2, field3);
    Mockito.when(field1.getElementName()).thenReturn(field1Name);
    Mockito.when(field1.getTypeSignature()).thenReturn(field1Signature);
    Mockito.when(field2.getElementName()).thenReturn(field2Name);
    Mockito.when(field2.getTypeSignature()).thenReturn(field2Signature);
    Mockito.when(field3.getElementName()).thenReturn(field3Name);
    Mockito.when(field3.getTypeSignature()).thenReturn(field3Signature);
    Mockito.when(mainTypeFieldAnalyzer.analyze(builderType)).thenReturn(fields);
    builderFieldsMapper = new BuilderFieldsMapper(mainTypeFieldAnalyzer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBuilderType() throws JavaModelException {
    builderFieldsMapper.map(null);
  }
  @Test
  public void testMapNoFields() throws JavaModelException {
    Mockito.when(mainTypeFieldAnalyzer.analyze(builderType)).thenReturn(Sets.<IField>newHashSet());
    Set<Field> actual = builderFieldsMapper.map(builderType);
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testMap() throws JavaModelException {
    Set<Field> actual = builderFieldsMapper.map(builderType);
    assertFalse(actual.isEmpty());
    Set<Field> expected = Sets.newHashSet(
      new Field.Builder().withName(field1Name).withSignature(field1Signature).build(), 
      new Field.Builder().withName(field2Name).withSignature(field2Signature).build(), 
      new Field.Builder().withName(field3Name).withSignature(field3Signature).build());
    assertEquals(expected, actual);
  }
}

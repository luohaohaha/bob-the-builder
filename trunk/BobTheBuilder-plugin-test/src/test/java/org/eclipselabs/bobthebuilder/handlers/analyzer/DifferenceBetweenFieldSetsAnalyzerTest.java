package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

/**
 * To test {@link DifferenceBetweenFieldSetsAnalyzer}
 */
public class DifferenceBetweenFieldSetsAnalyzerTest {

  @Mock
  private IField field1;

  private String field1Name = "field1";

  private String field1Signature = "signature1";

  @Mock
  private IField field2;

  private String field2Name = "field2";

  private String field2Signature = "signature2";

  @Mock
  private IField field3;

  private String field3Name = "field3";

  private String field3Signature = "signature3";

  @Mock
  private IField field4SimilarToField3;

  private String field4Name = field3Name;

  private String field4Signature = "signature4";

  private Set<IField> mainTypeFields;

  private Set<IField> builderFields;

  private Set<IField> expected;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(field1.getElementName()).thenReturn(field1Name);
    Mockito.when(field1.getTypeSignature()).thenReturn(field1Signature);
    Mockito.when(field2.getElementName()).thenReturn(field2Name);
    Mockito.when(field2.getTypeSignature()).thenReturn(field2Signature);
    Mockito.when(field3.getElementName()).thenReturn(field3Name);
    Mockito.when(field3.getTypeSignature()).thenReturn(field3Signature);
    Mockito.when(field4SimilarToField3.getElementName()).thenReturn(field4Name);
    Mockito.when(field4SimilarToField3.getTypeSignature()).thenReturn(field4Signature);
    mainTypeFields = Sets.newHashSet(field1, field2, field3);
    builderFields = Sets.newHashSet(field1, field2, field3);
    expected = Sets.newHashSet();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBuilderFieldSet() {
    new DifferenceBetweenFieldSetsAnalyzer(mainTypeFields, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainTypeFieldSet() {
    new DifferenceBetweenFieldSetsAnalyzer(null, builderFields);
  }

  @Test
  public void testEmptyBuilderFields() throws JavaModelException {
    builderFields = Collections.emptySet();
    expected.addAll(mainTypeFields);
    Set<IField> actual =
        new DifferenceBetweenFieldSetsAnalyzer(mainTypeFields, builderFields).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testEmptyMainTypeFields() throws JavaModelException {
    builderFields = Collections.emptySet();
    mainTypeFields = Collections.emptySet();
    Set<IField> actual =
        new DifferenceBetweenFieldSetsAnalyzer(mainTypeFields, builderFields).analyze();
    assertTrue(actual.isEmpty());
  }

  @Test
  public void testOneFieldDifference() throws JavaModelException {
    builderFields = Sets.newHashSet(field1, field2);
    expected = Sets.newHashSet(field3);
    Set<IField> actual =
        new DifferenceBetweenFieldSetsAnalyzer(mainTypeFields, builderFields).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testOneFieldWithDifferentSignature() throws JavaModelException {
    mainTypeFields = Sets.newHashSet(field1, field2, field4SimilarToField3);
    builderFields = Sets.newHashSet(field1, field2, field3);
    expected = Sets.newHashSet(field4SimilarToField3);
    Set<IField> actual =
        new DifferenceBetweenFieldSetsAnalyzer(mainTypeFields, builderFields).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testNoDiffBetweenSets() throws JavaModelException {
    Set<IField> actual =
        new DifferenceBetweenFieldSetsAnalyzer(mainTypeFields, builderFields).analyze();
    assertTrue(actual.isEmpty());
  }
}

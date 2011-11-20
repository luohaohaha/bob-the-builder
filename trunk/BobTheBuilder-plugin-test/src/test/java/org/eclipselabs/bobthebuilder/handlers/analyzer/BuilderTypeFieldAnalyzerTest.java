package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableSet;

/**
 * To test {@link BuilderTypeFieldAnalyzer} and {@link MainTypeFieldAnalyzer}
 */
public class BuilderTypeFieldAnalyzerTest {

  private AnalyzerResult.ForType resultFixture;

  @Mock
  private IField field1;

  @Mock
  private IField finalField2;

  @Mock
  private IField staticFinalField3;
  
  private Set<IField> expected;

  @Mock
  private IType builderType;

  @Mock
  private IType mainType;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(field1.getFlags()).thenReturn(Flags.AccDefault);
    Mockito.when(finalField2.getFlags()).thenReturn(Flags.AccFinal);
    Mockito.when(staticFinalField3.getFlags()).thenReturn(Flags.AccFinal|Flags.AccStatic);
    Mockito.when(builderType.getFields()).thenReturn(new IField[]{field1, finalField2, staticFinalField3});
    Mockito.when(mainType.getFields()).thenReturn(new IField[]{field1, finalField2, staticFinalField3});
    expected = ImmutableSet.of(field1, finalField2);
  }

  @Test
  public void testBuilderTypeNotPresent() throws JavaModelException {
    resultFixture = AnalyzerResult.ForType.NOT_PRESENT;
    Set<IField> actual = new BuilderTypeFieldAnalyzer(resultFixture).analyze();
    assertTrue(actual.isEmpty());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullAnalyzedResult() throws Exception {
    new BuilderTypeFieldAnalyzer(null).analyze();
  }
  
  @Test
  public void testBuilderIsPresent() throws JavaModelException {
    resultFixture = AnalyzerResult.ForType.getPresentInstance(builderType);
    Set<IField> actual = new BuilderTypeFieldAnalyzer(resultFixture).analyze();
    assertEquals(expected, actual);
  }

  @Test
  public void testMainTypeFields() throws JavaModelException {
    Set<IField> actual = new MainTypeFieldAnalyzer(mainType).analyze();
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() throws JavaModelException {
    new MainTypeFieldAnalyzer(null).analyze();
  }
}

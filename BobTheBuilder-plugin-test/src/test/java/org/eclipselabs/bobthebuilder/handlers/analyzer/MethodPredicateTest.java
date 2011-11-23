package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MethodPredicateTest {

  @Mock
  private IMethod methodFixture;

  @Mock
  private IField fieldFixture;

  private String fieldName = "fieldName";

  private String fieldSignature = "fieldSignature";

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    Mockito.when(fieldFixture.getElementName()).thenReturn(fieldName);
    Mockito.when(fieldFixture.getTypeSignature()).thenReturn(fieldSignature);
  }

  @Test
  public void testValidateInBuilderFails() {
    Mockito.when(methodFixture.getElementName()).thenReturn("pedro");
    boolean actual = new MethodPredicate.ValidateInBuilder().match(methodFixture);
    assertFalse(actual);
  }

  @Test
  public void testValidateInBuilderPasses() {
    Mockito.when(methodFixture.getElementName()).thenReturn(
      MethodPredicate.ValidateInBuilder.VALIDATE_METHOD_NAME);
    Mockito.when(methodFixture.getParameterTypes()).thenReturn(new String[] {});
    boolean actual = new MethodPredicate.ValidateInBuilder().match(methodFixture);
    assertTrue(actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMethodPassedToValidateInBuilder() {
    new MethodPredicate.ValidateInBuilder().match(null);
  }

  @Test
  public void testBuildInBuilderFails() {
    Mockito.when(methodFixture.getElementName()).thenReturn("pedro");
    boolean actual = new MethodPredicate.BuildInBuilder().match(methodFixture);
    assertFalse(actual);
  }

  @Test
  public void testBuildInBuilderPasses() {
    Mockito.when(methodFixture.getElementName()).thenReturn(
      MethodPredicate.BuildInBuilder.BUILD_METHOD_NAME);
    boolean actual = new MethodPredicate.BuildInBuilder().match(methodFixture);
    assertTrue(actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMethodPassedToBuildInBuilder() {
    new MethodPredicate.BuildInBuilder().match(null);
  }

  @Test
  public void testConstructorWithBuilderFails() throws JavaModelException {
    Mockito.when(methodFixture.isConstructor()).thenReturn(false);
    boolean actual = new MethodPredicate.ConstructorWithBuilder().match(methodFixture);
    assertFalse(actual);
  }

  @Test
  public void testConstructorWithBuilderFailsWrongSignature() throws JavaModelException {
    Mockito.when(methodFixture.isConstructor()).thenReturn(true);
    Mockito.when(methodFixture.getSignature()).thenReturn("whatever signature");
    boolean actual = new MethodPredicate.ConstructorWithBuilder().match(methodFixture);
    assertFalse(actual);
  }

  @Test
  public void testConstructorWithBuilderPasses() throws JavaModelException {
    Mockito.when(methodFixture.isConstructor()).thenReturn(true);
    Mockito.when(methodFixture.getSignature()).thenReturn(
      MethodPredicate.ConstructorWithBuilder.CONSTRUCTOR_WITH_BUILDER_SIGNATURE);
    boolean actual = new MethodPredicate.ConstructorWithBuilder().match(methodFixture);
    assertTrue(actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMethodPassedToConstructorWithBuilder() throws JavaModelException {
    new MethodPredicate.ConstructorWithBuilder().match(null);
  }

  @Test
  public void testWithMethodBuilderFailsWrongName() throws JavaModelException {
    Mockito.when(methodFixture.getElementName()).thenReturn("withFieldBlahName");
    boolean actual = new MethodPredicate.WithMethodInBuilder(fieldFixture).match(methodFixture);
    assertFalse(actual);
  }

  @Test
  public void testWithMethodBuilderFailsWrongSignature() throws JavaModelException {
    Mockito.when(methodFixture.getElementName()).thenReturn("withFieldName");
    Mockito.when(methodFixture.getParameterTypes()).thenReturn(new String[]{"whatever params"});
    boolean actual = new MethodPredicate.WithMethodInBuilder(fieldFixture).match(methodFixture);
    assertFalse(actual);
  }
  
  @Test
  public void testWithMethodBuilderPasses() throws JavaModelException {
    Mockito.when(methodFixture.getElementName()).thenReturn("withFieldName");
    Mockito.when(methodFixture.getParameterTypes()).thenReturn(new String[]{fieldSignature});
    boolean actual = new MethodPredicate.WithMethodInBuilder(fieldFixture).match(methodFixture);
    assertTrue(actual);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullMethodPassedToWithInBuilder() throws JavaModelException {
    new MethodPredicate.WithMethodInBuilder(fieldFixture).match(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFieldPassedToWithInBuilder() throws JavaModelException {
    new MethodPredicate.WithMethodInBuilder(null);
  }
}

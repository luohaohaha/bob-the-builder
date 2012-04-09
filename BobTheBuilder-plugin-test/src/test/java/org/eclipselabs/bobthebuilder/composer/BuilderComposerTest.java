package org.eclipselabs.bobthebuilder.composer;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class BuilderComposerTest {

  private BuilderComposer builderComposer;

  private String signature = "String";

  private String name = "fieldName";

  private Field.Builder fieldBuilder;

  private FieldPredicate fieldPredicate;

  private ValidateMethod.Builder validateMethodBuilder;

  private String source;

  private ComposerRequest.Builder composerRequestBuilder;

  private Field missingFieldValidation;

  private Field extraField;

  private String extraFieldSignature = "Vector";

  private String missingFieldValidationSignature = "Object";

  private String missingFieldValidationName = "fieldWithMissingValidation";

  @Mock
  private MainType mainType;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    fieldPredicate = new FieldPredicate.FieldValidation();
    builderComposer = new BuilderComposer(fieldPredicate);
    fieldBuilder = 
      new Field.Builder().withName(name).withSignature(signature).withPosition(3);
    source =
        "private void validate() {\n" +
          "  Preconditions.checkArgument(" +
          "!StringUtils.isBlank(fieldName), \"fieldName may not be blank\");\n" +
          "  Preconditions.checkNotNull(extraField), \"extraField may not be null\");\n" +
          "}";
    Set<FieldAssignment> fieldAssignments = Sets.newHashSet();
    validateMethodBuilder = new ValidateMethod.Builder()
        .withSource(source)
        .withValidationFramework(ValidationFramework.GOOGLE_GUAVA)
        .withValidatedFields(fieldAssignments);
    extraField =
        new Field.Builder()
          .withName("extraField")
          .withSignature(extraFieldSignature)
          .withPosition(1)
          .build();
    missingFieldValidation = new Field.Builder()
        .withName(missingFieldValidationName)
        .withSignature(missingFieldValidationSignature)
        .withPosition(2)
        .build();
    composerRequestBuilder = new ComposerRequest.Builder()
        .withBuildMethodInBuilder()
        .withConstructorWithBuilder()
        .withValidateMethodInBuilder()
        .withValidationFramework(ValidationFramework.GOOGLE_GUAVA)
        .addMissingValidationInBuild(missingFieldValidation)
        .addExtraFieldInBuilder(extraField);
    Mockito.when(mainType.getName()).thenReturn("MainType");
  }

  @Test
  public void testComposeBuilderSkeleton() {
    String actual = builderComposer.composeSkeleton();
    String expected = "public static class Builder{\n}";
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeDeclarationNullField() {
    builderComposer.composeFieldDeclaration(null);
  }

  @Test
  public void testComposeFieldDeclaration() {
    String actual = builderComposer.composeFieldDeclaration(fieldBuilder.build());
    String expected = "private String fieldName;";
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeWithMethodNullField() {
    builderComposer.composeFieldDeclaration(null);
  }

  @Test
  public void testComposeWithMethod() {
    String actual = builderComposer.composeWithMethod(fieldBuilder.build());
    String expected =
        "public Builder withFieldName(String fieldName) {\n" +
          "  this.fieldName = fieldName;\n" +
          "  return this;\n" +
          "}";
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeValidateNullFields() {
    builderComposer.composeValidateMethodFromScratch(null, ValidationFramework.GOOGLE_GUAVA);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeValidateNullValidation() {
    builderComposer.composeValidateMethodFromScratch(Sets.<Field> newHashSet(), null);
  }

  @Test
  public void testComposeValidateMethod() {
    String actual = builderComposer.composeValidateMethodFromScratch(
      Sets.<Field> newHashSet(fieldBuilder.build()), ValidationFramework.GOOGLE_GUAVA);

    String expected = "private void validate() {\n" +
    "  Preconditions.checkArgument(" +
    "!StringUtils.isBlank(fieldName), \"fieldName may not be blank\");\n" +
    "}";;
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeValidateFromExistingNullRequest() {
    builderComposer.composeValidateMethodFromExisting(null, validateMethodBuilder.build());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeValidateFromExistingNullMethod() {
    builderComposer.composeValidateMethodFromExisting(composerRequestBuilder.build(), null);
  }

  @Test
  public void testComposeValidateFromExisting() {
    String actual = builderComposer.composeValidateMethodFromExisting(
      composerRequestBuilder.build(), validateMethodBuilder.build());
    String expected =
        "private void validate() {\n" +
          "  Preconditions.checkArgument(" +
          "!StringUtils.isBlank(fieldName), \"fieldName may not be blank\");\n" +
          "  Preconditions.checkNotNull(" +
          "fieldWithMissingValidation, \"fieldWithMissingValidation may not be null\");\n" +
          "}";
    assertEquals(expected, actual);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testComposeBuilderNullType() {
    builderComposer.composeBuilderMethod(null, true);
  }

  @Test
  public void testComposeBuilderWithValidateInvocation() {
    String actual = builderComposer.composeBuilderMethod(mainType, true);
    String expected = "public MainType build() {\n" +
    "  validate();\n" +
    "  return new MainType(this);\n" +
    "}";
    assertEquals(expected, actual);
  }

  @Test
  public void testComposeBuilderNoValidateInvocation() {
    String actual = builderComposer.composeBuilderMethod(mainType, false);
    String expected = "public MainType build() {\n" +
    "  return new MainType(this);\n" +
    "}";
    assertEquals(expected, actual);
  }
  
  
}

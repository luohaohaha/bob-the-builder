package org.eclipselabs.bobthebuilder.composer;

import static org.junit.Assert.assertEquals;

import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilder;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class ConstructorComposerTest {

  private ConstructorComposer constructorComposer;

  private String mainTypeName = "MainType";

  private ComposerRequest.Builder composerRequestBuilder;

  private ValidationFramework validationFramework = ValidationFramework.GOOGLE_GUAVA;

  private Field missingAssignment1;

  private Field missingAssignment2;

  private Field extraField1;

  private Field missingAssignment3;

  private String signature = "String";

  private ConstructorWithBuilder.Builder constructorWithBuilder;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    constructorComposer = new ConstructorComposer(new FieldPredicate.FieldAssignment());
    missingAssignment1 =
        new Field.Builder().withName("missingAssignment1").withSignature(signature).build();
    missingAssignment2 =
        new Field.Builder().withName("missingAssignment2").withSignature(signature).build();
    missingAssignment3 =
        new Field.Builder().withName("missingAssignment3").withSignature(signature).build();
    extraField1 = new Field.Builder().withName("extraField1").withSignature(signature).build();
    composerRequestBuilder = new ComposerRequest.Builder()
        .withBuildMethodInBuilder()
        .withConstructorWithBuilder()
        .withValidateMethodInBuilder()
        .withValidationFramework(validationFramework)
        .addMissingAssignmentInConstructor(missingAssignment1)
        .addMissingAssignmentInConstructor(missingAssignment2)
        .addExtraFieldInBuilder(extraField1)
        .addExtraFieldInBuilder(missingAssignment3);
    String source = "private MainType(Builder builder) {" + "\n" +
      "  this.extraField1 = builder.extraField1;" + "\n" +
      "  this.missingAssignment3 = builder.missingAssignment3;" + "\n" +
      "  this.assignment3 = builder.assignment3;" + "\n" +
      "}";
    constructorWithBuilder = new ConstructorWithBuilder.Builder()
        .withName(mainTypeName)
        .withFieldAssignment(Sets.<FieldAssignment> newHashSet())
        .withSource(source);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeFromScratchNullRequest() {
    constructorComposer.composeFromScratch(null, mainTypeName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeFromScratchNullMainTypeName() {
    constructorComposer.composeFromScratch(composerRequestBuilder.build(), null);
  }

  @Test
  public void testComposeFromScratch() throws Exception {
    String actual =
        constructorComposer.composeFromScratch(composerRequestBuilder.build(),
          mainTypeName);
    String expected =
        "private MainType(Builder builder) {" + "\n" +
          "  this.missingAssignment2 = builder.missingAssignment2;" + "\n" +
          "  this.missingAssignment1 = builder.missingAssignment1;" + "\n" +
          "}";
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeFromExistingNullRequest() {
    constructorComposer.composeFromExisting(null, constructorWithBuilder.build());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testComposeFromExistingNullMainTypeName() {
    constructorComposer.composeFromExisting(composerRequestBuilder.build(), null);
  }

  @Test
  public void testComposeFromExisting() throws Exception {
    String actual =
      constructorComposer.composeFromExisting(
        composerRequestBuilder.build(), constructorWithBuilder.build());
    String expected =
      "private MainType(Builder builder) {" + "\n" +
      "  this.assignment3 = builder.assignment3;" + "\n" +
      "  this.missingAssignment2 = builder.missingAssignment2;" + "\n" +
      "  this.missingAssignment1 = builder.missingAssignment1;" + "\n" +
      "}";
    assertEquals(expected, actual);
  }
}

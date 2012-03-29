package org.eclipselabs.bobthebuilder.composer;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.model.Field;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    constructorComposer = new ConstructorComposer();
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
        .addMissingAssignmentInConstructor(missingAssignment3)
        .addExtraFieldInBuilder(extraField1)
        .addExtraFieldInBuilder(missingAssignment3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRequest() {
    constructorComposer.composeFromScratch(null, mainTypeName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainTypeName() {
    constructorComposer.composeFromScratch(composerRequestBuilder.build(), null);
  }

  @Test
  public void testCompose() throws Exception {
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
}

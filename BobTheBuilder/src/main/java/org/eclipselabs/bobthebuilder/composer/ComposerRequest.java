package org.eclipselabs.bobthebuilder.composer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.model.Field;

public class ComposerRequest {

  private final Set<Field> missingFieldsInBuilder;

  private final Set<Field> extraFieldsInBuilder;

  private final Set<Field> missingWithMethodsInBuilder;

  private final boolean createConstructorWithBuilder;

  private final Set<Field> missingAssignmentsInConstructor;

  private final boolean createBuildMethodInBuilder;

  private final Set<Field> missingFieldValidationsInBuild;

  private final boolean createValidateMethodInBuilder;

  private final ValidationFramework validationFramework;

  private ComposerRequest(Builder builder) {
    this.missingFieldsInBuilder = builder.missingFieldsInBuilder;
    this.extraFieldsInBuilder = builder.extraFieldsInBuilder;
    this.missingWithMethodsInBuilder = builder.missingWithMethodsInBuilder;
    this.createConstructorWithBuilder = builder.createConstructorWithBuilder;
    this.missingAssignmentsInConstructor = builder.missingAssignmentsInConstructor;
    this.createBuildMethodInBuilder = builder.createBuildMethodInBuilder;
    this.missingFieldValidationsInBuild = builder.missingFieldValidationsInBuild;
    this.createValidateMethodInBuilder = builder.createValidateMethodInBuilder;
    this.validationFramework = builder.validationFramework;
  }

  public static class Builder {

    public Set<Field> missingFieldsInBuilder = new HashSet<Field>();

    private Set<Field> extraFieldsInBuilder = new HashSet<Field>();

    private Set<Field> missingWithMethodsInBuilder = new HashSet<Field>();

    private boolean createConstructorWithBuilder = false;

    private Set<Field> missingAssignmentsInConstructor = new HashSet<Field>();

    private boolean createBuildMethodInBuilder = false;

    private Set<Field> missingFieldValidationsInBuild = new HashSet<Field>();

    private boolean createValidateMethodInBuilder = false;

    private ValidationFramework validationFramework;

    public ComposerRequest build() {
      return new ComposerRequest(this);
    }

    public Builder addMissingFieldInBuilder(Field missingField) {
      Validate.notNull(missingField, "missing Field cannot be null");
      missingFieldsInBuilder.add(missingField);
      return this;
    }

    public Builder addExtraFieldInBuilder(Field extraField) {
      Validate.notNull(extraField, "extra Field cannot be null");
      extraFieldsInBuilder.add(extraField);
      return this;
    }

    public Builder addMissingWithMethodInBuilder(Field missingWithMethod) {
      Validate.notNull(missingWithMethod, "missingwithmethod for field cannot be null");
      missingWithMethodsInBuilder.add(missingWithMethod);
      return this;
    }

    public Builder withConstructorWithBuilder() {
      this.createConstructorWithBuilder = true;
      return this;
    }

    public Builder addMissingAssignmentInConstructor(Field missingAssignment) {
      Validate.notNull(missingAssignment, "missing field for field cannot be null");
      missingAssignmentsInConstructor.add(missingAssignment);
      return this;
    }

    public Builder withBuildMethodInBuilder() {
      this.createBuildMethodInBuilder = true;
      return this;
    }

    public Builder addMissingValidationInBuild(Field missingFieldValidation) {
      Validate.notNull(missingFieldValidation, "missing field validation may not be null");
      missingFieldValidationsInBuild.add(missingFieldValidation);
      return this;
    }

    public Builder withValidateMethodInBuilder() {
      this.createValidateMethodInBuilder = true;
      return this;
    }

    public Builder withValidationFramework(ValidationFramework validationFramework) {
      Validate.notNull(validationFramework, "validationFramework may not be null");
      this.validationFramework = validationFramework;
      return this;
    }

  }

  public Set<Field> getMissingFieldsInBuilder() {
    return Collections.unmodifiableSet(missingFieldsInBuilder);
  }

  public Set<Field> getExtraFieldsInBuilder() {
    return Collections.unmodifiableSet(extraFieldsInBuilder);
  }

  public Set<Field> getMissingWithMethodsInBuilder() {
    return Collections.unmodifiableSet(missingWithMethodsInBuilder);
  }

  public boolean isCreateConstructorWithBuilder() {
    return createConstructorWithBuilder;
  }

  public Set<Field> getMissingAssignmentsInConstructor() {
    return Collections.unmodifiableSet(missingAssignmentsInConstructor);
  }

  public boolean isCreateBuildMethodInBuilder() {
    return createBuildMethodInBuilder;
  }

  public Set<Field> getMissingFieldValidationsInBuild() {
    return Collections.unmodifiableSet(missingFieldValidationsInBuild);
  }

  public boolean isCreateValidateMethodInBuilder() {
    return createValidateMethodInBuilder;
  }

  public ValidationFramework getValidationFramework() {
    return validationFramework;
  }

}

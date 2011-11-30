package org.eclipselabs.bobthebuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;

public class ComposerRequest {

  private final Set<IField> missingFieldsInBuilder;

  private final Set<IField> extraFieldsInBuilder;

  private final Set<IField> missingWithMethodsInBuilder;

  private final boolean createConstructorWithBuilder;

  private final Set<IField> missingAssignmentsInConstructor;

  private final boolean createBuildMethodInBuilder;

  private final Set<IField> missingFieldValidationsInBuild;

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

    public Set<IField> missingFieldsInBuilder = new HashSet<IField>();

    private Set<IField> extraFieldsInBuilder = new HashSet<IField>();

    private Set<IField> missingWithMethodsInBuilder = new HashSet<IField>();

    private boolean createConstructorWithBuilder = false;

    private Set<IField> missingAssignmentsInConstructor = new HashSet<IField>();

    private boolean createBuildMethodInBuilder = false;

    private Set<IField> missingFieldValidationsInBuild = new HashSet<IField>();

    private boolean createValidateMethodInBuilder = false;

    private ValidationFramework validationFramework;

    public ComposerRequest build() {
      return new ComposerRequest(this);
    }

    public Builder addMissingFieldInBuilder(IField missingField) {
      Validate.notNull(missingField, "missing Field cannot be null");
      missingFieldsInBuilder.add(missingField);
      return this;
    }

    public Builder addExtraFieldInBuilder(IField extraField) {
      Validate.notNull(extraField, "extra Field cannot be null");
      extraFieldsInBuilder.add(extraField);
      return this;
    }

    public Builder addMissingWithMethodInBuilder(IField missingWithMethod) {
      Validate.notNull(missingWithMethod, "missingwithmethod for field cannot be null");
      missingWithMethodsInBuilder.add(missingWithMethod);
      return this;
    }

    public Builder withConstructorWithBuilder() {
      this.createConstructorWithBuilder = true;
      return this;
    }

    public Builder addMissingAssignmentInConstructor(IField missingAssignment) {
      Validate.notNull(missingAssignment, "missing field for field cannot be null");
      missingAssignmentsInConstructor.add(missingAssignment);
      return this;
    }

    public Builder withBuildMethodInBuilder() {
      this.createBuildMethodInBuilder = true;
      return this;
    }

    public Builder addMissingValidationInBuild(IField missingFieldValidation) {
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

  public Set<IField> getMissingFieldsInBuilder() {
    return Collections.unmodifiableSet(missingFieldsInBuilder);
  }

  public Set<IField> getExtraFieldsInBuilder() {
    return Collections.unmodifiableSet(extraFieldsInBuilder);
  }

  public Set<IField> getMissingWithMethodsInBuilder() {
    return Collections.unmodifiableSet(missingWithMethodsInBuilder);
  }

  public boolean isCreateConstructorWithBuilder() {
    return createConstructorWithBuilder;
  }

  public Set<IField> getMissingAssignmentsInConstructor() {
    return Collections.unmodifiableSet(missingAssignmentsInConstructor);
  }

  public boolean isCreateBuildMethodInBuilder() {
    return createBuildMethodInBuilder;
  }

  public Set<IField> getMissingFieldValidationsInBuild() {
    return Collections.unmodifiableSet(missingFieldValidationsInBuild);
  }

  public boolean isCreateValidateMethodInBuilder() {
    return createValidateMethodInBuilder;
  }

  public ValidationFramework getValidationFramework() {
    return validationFramework;
  }

}

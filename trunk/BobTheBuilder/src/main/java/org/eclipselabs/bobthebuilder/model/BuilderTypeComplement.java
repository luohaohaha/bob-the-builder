package org.eclipselabs.bobthebuilder.model;

import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipselabs.bobthebuilder.complement.BuildMethodComplement;

public class BuilderTypeComplement {

  private final Set<Field> builderFieldsComplement;

  private final Set<WithMethod> withMethodsComplement;

  private final BuildMethodComplement buildMethodComplement;

  private final ValidateMethodComplement validateMethodComplement;

  private final boolean completeComplement;

  public BuilderTypeComplement(Builder builder) {
    this.builderFieldsComplement = builder.builderFieldsComplement;
    this.withMethodsComplement = builder.withMethodsComplement;
    this.buildMethodComplement = builder.buildMethodComplement;
    this.validateMethodComplement = builder.validateMethodComplement;
    this.completeComplement = builder.completeComplement;
  }

  public static class Builder {
    private Set<Field> builderFieldsComplement;

    private Set<WithMethod> withMethodsComplement;

    private BuildMethodComplement buildMethodComplement;

    private ValidateMethodComplement validateMethodComplement;

    private boolean completeComplement = false;

    public Builder withBuilderFieldsComplement(Set<Field> builderFieldsComplement) {
      this.builderFieldsComplement = builderFieldsComplement;
      return this;

    }

    public Builder withWithMethodsComplement(Set<WithMethod> withMethodsComplement) {
      this.withMethodsComplement = withMethodsComplement;
      return this;
    }

    public Builder withBuildMethodComplement(BuildMethodComplement buildMethodComplement) {
      this.buildMethodComplement = buildMethodComplement;
      return this;
    }

    public Builder withValidateMethodComplement(ValidateMethodComplement validateMethodComplement) {
      this.validateMethodComplement = validateMethodComplement;
      return this;
    }

    public Builder withCompleteComplement() {
      this.completeComplement = true;
      return this;
    }

    public BuilderTypeComplement build() {
      validate();
      return new BuilderTypeComplement(this);
    }

    private void validate() {
      Validate.notNull(builderFieldsComplement, "builderFieldsComplement may not be null");
      Validate.noNullElements(builderFieldsComplement,
        "builderFieldsComplement may not contain null elements");
      Validate.notNull(withMethodsComplement, "withMethodsComplement may not be null");
      Validate.noNullElements(withMethodsComplement,
        "withMethodsComplement may not contain null elements");
      Validate.notNull(buildMethodComplement, "buildMethodComplement may not be null");
      Validate.notNull(validateMethodComplement, "validateMethodComplement may not be null");
    }

  }

  public Set<Field> getBuilderFieldsComplement() {
    return builderFieldsComplement;
  }

  public Set<WithMethod> getWithMethodsComplement() {
    return withMethodsComplement;
  }

  public BuildMethodComplement getBuildMethodComplement() {
    return buildMethodComplement;
  }

  public ValidateMethodComplement getValidateMethodComplement() {
    return validateMethodComplement;
  }

  public boolean isCompleteComplement() {
    return completeComplement;
  }

  public boolean isEmptyComplement() {
    return builderFieldsComplement.isEmpty()
      && withMethodsComplement.isEmpty()
      && buildMethodComplement.isEmptyComplement()
      && validateMethodComplement.isEmptyComplement();
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}

package org.eclipselabs.bobthebuilder.model;

import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BuilderTypeComplement {

  // May be empty
  private final Set<Field> builderFieldsComplement;

  // May be empty
  private final Set<WithMethod> withMethodsComplement;

  // May be null
  private final BuildMethod buildMethodComplement;

  // May be null
  private final ValidateMethod validateMethodComplement;

  public BuilderTypeComplement(Builder builder) {
    this.builderFieldsComplement = builder.builderFieldsComplement;
    this.withMethodsComplement = builder.withMethodsComplement;
    this.buildMethodComplement = builder.buildMethodComplement;
    this.validateMethodComplement = builder.validateMethodComplement;
  }

  public static class Builder {
    private Set<Field> builderFieldsComplement;

    private Set<WithMethod> withMethodsComplement;

    private BuildMethod buildMethodComplement;

    private ValidateMethod validateMethodComplement;

    public Builder withBuilderFieldsComplement(Set<Field> builderFieldsComplement) {
      this.builderFieldsComplement = builderFieldsComplement;
      return this;

    }

    public Builder withWithMethodsComplement(Set<WithMethod> withMethodsComplement) {
      this.withMethodsComplement = withMethodsComplement;
      return this;
    }

    public Builder withBuildMethodComplement(BuildMethod buildMethodComplement) {
      this.buildMethodComplement = buildMethodComplement;
      return this;
    }

    public Builder withValidateMethodComplement(ValidateMethod validateMethodComplement) {
      this.validateMethodComplement = validateMethodComplement;
      return this;
    }

    public BuilderTypeComplement build() {
      return new BuilderTypeComplement(this);
    }

  }

  public Set<Field> getBuilderFieldsComplement() {
    return builderFieldsComplement;
  }

  public Set<WithMethod> getWithMethodsComplement() {
    return withMethodsComplement;
  }

  public BuildMethod getBuildMethodComplement() {
    return buildMethodComplement;
  }

  public ValidateMethod getValidateMethodComplement() {
    return validateMethodComplement;
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

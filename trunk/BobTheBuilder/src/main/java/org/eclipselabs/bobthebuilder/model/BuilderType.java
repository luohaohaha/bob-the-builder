package org.eclipselabs.bobthebuilder.model;

import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BuilderType {
  // May be empty
  private final Set<Field> builderFields;

  // May be empty
  private final Set<WithMethod> WithMethods;

  // May be null
  private final BuildMethod buildMethod;

  //May be null
  private final ValidateMethod validateMethod;

  private BuilderType(Builder builder) {
    this.buildMethod = builder.buildMethod;
    this.builderFields = builder.builderFields;
    this.WithMethods = builder.WithMethods;
    this.validateMethod = builder.validateMethod;
  }

  public static class Builder {

    private Set<WithMethod> WithMethods;

    private BuildMethod buildMethod = null;

    private Set<Field> builderFields;

    private ValidateMethod validateMethod = null;

    public Builder withWithMethods(Set<WithMethod> WithMethods) {
      this.WithMethods = WithMethods;
      return this;
    }

    public Builder withBuildMethod(BuildMethod buildMethod) {
      this.buildMethod = buildMethod;
      return this;
    }

    public Builder withBuilderFields(Set<Field> builderFields) {
      this.builderFields = builderFields;
      return this;
    }

    public Builder withValidateMethod(ValidateMethod validateMethod) {
      this.validateMethod = validateMethod;
      return this;
    }
    
    public BuilderType build() {
      validate();
      return new BuilderType(this);
    }

    private void validate() {
      Validate.notNull(WithMethods, "WithMethods may not be null");
      Validate.isTrue(!WithMethods.isEmpty(), "WithMethods may not be empty");
      //TODO can you have withMethod if there is no corresponding field?
      Validate.notNull(buildMethod, "buildMethod may not be null");
      Validate.notNull(builderFields, "builderFields may not be null");
      Validate.isTrue(!builderFields.isEmpty(), "builderFields may not be empty");
    }


  }

  public BuildMethod getBuildMethod() {
    return buildMethod;
  }

  public ValidateMethod getValidateMethod() {
    return validateMethod;
  }

  public Set<Field> getBuilderFields() {
    return builderFields;
  }

  public Set<WithMethod> getWithMethods() {
    return WithMethods;
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

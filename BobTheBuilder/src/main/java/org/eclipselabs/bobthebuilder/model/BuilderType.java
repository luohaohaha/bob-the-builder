package org.eclipselabs.bobthebuilder.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BuilderType {
  // May be empty
  private final Set<Field> builderFields;

  // May be empty
  private final Set<WithMethod> withMethods;

  // May be null
  private final BuildMethod buildMethod;

  //May be null
  private final ValidateMethod validateMethod;

  private BuilderType(Builder builder) {
    this.buildMethod = builder.buildMethod;
    this.builderFields = builder.builderFields;
    this.withMethods = builder.withMethods;
    this.validateMethod = builder.validateMethod;
  }

  public static class Builder {

    private Set<WithMethod> withMethods = new HashSet<WithMethod>();

    private BuildMethod buildMethod = null;

    private Set<Field> builderFields = new HashSet<Field>();

    private ValidateMethod validateMethod = null;

    public Builder withWithMethods(Set<WithMethod> withMethods) {
      this.withMethods.addAll(withMethods);
      return this;
    }

    public Builder withBuildMethod(BuildMethod buildMethod) {
      this.buildMethod = buildMethod;
      return this;
    }

    public Builder withBuilderFields(Set<Field> builderFields) {
      this.builderFields.addAll(builderFields);
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
      Validate.notNull(withMethods, "withMethods may not be null");
      Validate.noNullElements(withMethods, "withMethods may not contain null elements");
      //TODO can you have withMethod if there is no corresponding field?
      Validate.notNull(builderFields, "builderFields may not be null");
      Validate.noNullElements(builderFields, "builderFields may not contain null elements");
    }
  }

  public BuildMethod getBuildMethod() {
    return buildMethod;
  }

  public ValidateMethod getValidateMethod() {
    return validateMethod;
  }

  public Set<Field> getBuilderFields() {
    return Collections.unmodifiableSet(builderFields);
  }

  public Set<WithMethod> getWithMethods() {
    return Collections.unmodifiableSet(withMethods);
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

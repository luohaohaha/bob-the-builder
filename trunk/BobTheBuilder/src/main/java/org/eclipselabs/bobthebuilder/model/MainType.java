package org.eclipselabs.bobthebuilder.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MainType {

  private final String name;

  private final Set<Field> fields;

  // This could be null
  private BuilderType builderType;

  // This could be null
  private ConstructorWithBuilder constructorWithBuilder;

  private MainType(Builder builder) {
    this.constructorWithBuilder = builder.constructorWithBuilder;
    this.builderType = builder.builderType;
    this.name = builder.name;
    this.fields = builder.fields;
  }

  public static class Builder {

    private ConstructorWithBuilder constructorWithBuilder;

    private BuilderType builderType;

    private String name;

    private Set<Field> fields = new HashSet<Field>();

    public Builder withConstructorWithBuilder(ConstructorWithBuilder constructorWithBuilder) {
      this.constructorWithBuilder = constructorWithBuilder;
      return this;
    }

    public Builder withBuilderType(BuilderType builderType) {
      this.builderType = builderType;
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withFields(Set<Field> fields) {
      this.fields.addAll(fields);
      return this;
    }

    public MainType build() {
      validate();
      return new MainType(this);
    }

    private void validate() {
      Validate.notNull(constructorWithBuilder, "constructorWithBuilder may not be null");
      Validate.notNull(builderType, "builderType may not be null");
      Validate.notNull(fields, "fields may not be null");
      Validate.noNullElements(fields, "fields may not contain null elements");
    }
  }

  public String getName() {
    return name;
  }

  public Set<Field> getFields() {
    return fields;
  }

  public BuilderType getBuilderType() {
    return builderType;
  }

  public ConstructorWithBuilder getConstructorWithBuilder() {
    return constructorWithBuilder;
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

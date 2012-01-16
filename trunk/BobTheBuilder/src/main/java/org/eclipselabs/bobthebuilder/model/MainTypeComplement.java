package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class MainTypeComplement {

  private final ConstructorWithBuilderComplement constructorWithBuilderComplement;

  // TODO getters and pojomatics

  private MainTypeComplement(Builder builder) {
    this.constructorWithBuilderComplement = builder.constructorWithBuilderComplement;
  }

  public static class Builder {

    private ConstructorWithBuilderComplement constructorWithBuilderComplement;

    public Builder withConstructorWithBuilderComplement(
      ConstructorWithBuilderComplement constructorWithBuilderComplement) {
      this.constructorWithBuilderComplement = constructorWithBuilderComplement;
      return this;
    }

    public MainTypeComplement build() {
      validate();
      return new MainTypeComplement(this);
    }

    private void validate() {
      Validate.notNull(constructorWithBuilderComplement,
        "constructorWithBuilderComplement may not be null");
    }
  }
  
  public ConstructorWithBuilderComplement getConstructorWithBuilderComplement() {
    return constructorWithBuilderComplement;
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

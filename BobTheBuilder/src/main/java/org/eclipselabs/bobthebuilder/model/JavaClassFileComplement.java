package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JavaClassFileComplement {

  private final MainTypeComplement mainTypeComplement;

  private final BuilderTypeComplement builderTypeComplement;

  private JavaClassFileComplement(Builder builder) {
    this.mainTypeComplement = builder.mainTypeComplement;
    this.builderTypeComplement = builder.builderTypeComplement;
  }

  public static class Builder {

    private MainTypeComplement mainTypeComplement;

    private BuilderTypeComplement builderTypeComplement;

    public Builder withMainTypeComplement(MainTypeComplement mainTypeComplement) {
      this.mainTypeComplement = mainTypeComplement;
      return this;
    }

    public Builder withBuilderTypeComplement(BuilderTypeComplement builderTypeComplement) {
      this.builderTypeComplement = builderTypeComplement;
      return this;

    }

    public JavaClassFileComplement build() {
      validate();
      return new JavaClassFileComplement(this);
    }

    private void validate() {
      Validate.notNull(builderTypeComplement, "builderTypeComplement may not be null");
      Validate.notNull(mainTypeComplement, "mainTypeComplement may not be null");
    }

  }

  public MainTypeComplement getMainTypeComplement() {
    return mainTypeComplement;
  }

  public BuilderTypeComplement getBuilderTypeComplement() {
    return builderTypeComplement;
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

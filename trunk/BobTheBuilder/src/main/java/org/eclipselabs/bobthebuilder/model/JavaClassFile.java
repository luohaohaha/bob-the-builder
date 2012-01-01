package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JavaClassFile {
  private final String name;

  private final MainType mainType;

  private final Imports imports;

  private JavaClassFile(Builder builder) {
    this.name = builder.name;
    this.mainType = builder.mainType;
    this.imports = builder.imports;
  }

  public static class Builder {

    private String name;

    private MainType mainType;

    private Imports imports;

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withMainType(MainType mainType) {
      this.mainType = mainType;
      return this;
    }

    public Builder withImports(Imports imports) {
      this.imports = imports;
      return this;
    }

    public JavaClassFile build() {
      validate();
      return new JavaClassFile(this);
    }

    private void validate() {
      Validate.isTrue(!StringUtils.isBlank(name), "name may not be blank");
      Validate.notNull(mainType, "mainType may not be null");
      Validate.notNull(imports, "imports may not be null");
    }
  }

  public String getName() {
    return name;
  }

  public MainType getMainType() {
    return mainType;
  }

  public Imports getImports() {
    return imports;
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

package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class WithMethod {

  private final String name;

  private WithMethod(Builder builder) {
    this(builder.name);
  }

  private WithMethod(String name) {
    this.name = name;
  }

  public static class Builder {

    private String name;

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public WithMethod build() {
      validate();
      return new WithMethod(this);
    }

    private void validate() {
      Validate.isTrue(!StringUtils.isBlank(name), "name may not be blank");
    }
  }

  public String getName() {
    return name;
  }

  public static WithMethod getInstanceFromField(Field field) {
    Validate.notNull(field, "field may not be null");
    return new WithMethod("with" + StringUtils.capitalize(field.getName()));
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

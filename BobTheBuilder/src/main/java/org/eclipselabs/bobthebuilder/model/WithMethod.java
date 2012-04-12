package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class WithMethod implements Comparable<WithMethod> {

  private final String name;

  private final Field field;

  private WithMethod(Builder builder) {
    this.name = builder.name;
    this.field = builder.field;
  }

  public static class Builder {

    private String name;

    private Field field;

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withField(Field field) {
      this.field = field;
      return this;
    }

    public WithMethod build() {
      validate();
      return new WithMethod(this);
    }

    private void validate() {
      Validate.isTrue(!StringUtils.isBlank(name), "name may not be blank");
      Validate.notNull(field, "field may not be null");
    }
  }

  public String getName() {
    return name;
  }

  public Field getField() {
    return field;
  }

  public static WithMethod getInstanceFromField(Field field) {
    Validate.notNull(field, "field may not be null");
    return new WithMethod.Builder()
        .withName("with" + StringUtils.capitalize(field.getName()))
        .withField(field)
        .build();
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

  @Override
  public int compareTo(WithMethod o) {
    return this.field.compareTo(o.field);
  }
}

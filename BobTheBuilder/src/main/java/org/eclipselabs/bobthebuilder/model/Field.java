package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Field implements Comparable<Field> {
  private final String name;

  private final String signature;
  
  private final int position;

  
  private Field(Builder builder) {
    this.name = builder.name;
    this.signature = builder.signature;
    this.position = builder.position;
  }

  public static class Builder {

    private String name;

    private String signature;

    private int position;

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withSignature(String signature) {
      this.signature = signature;
      return this;
    }

    public Field build() {
      validate();
      return new Field(this);
    }

    private void validate() {
      Validate.isTrue(!StringUtils.isBlank(name), "name may not be blank");
      Validate.isTrue(!StringUtils.isBlank(signature), "signature may not be blank");
      Validate.isTrue(position > 0, "position must be greater than 0");
    }

    public Builder withPosition(int offset) {
      this.position = offset;
      return this;
    }
  }

  public String getName() {
    return name;
  }

  public String getSignature() {
    return signature;
  }

  public int getPosition() {
    return position;
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
  public int compareTo(Field o) {
    final FieldPositionComparator fieldComparator = new FieldPositionComparator();
    return fieldComparator.compare(this, o);
  }

}

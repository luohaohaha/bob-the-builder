package org.eclipselabs.bobthebuilder;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class AbstractResult<T> {
  private final boolean present;

  private final T element;

  public AbstractResult(boolean present, T element) {
    Validate.isTrue(
      (present && (element != null)) || (!present && (element == null)),
      "if element is present, it may not be null. If element is not present, it should be null");
    this.present = present;
    this.element = element;
  }

  public boolean isPresent() {
    return present;
  }

  public T getElement() {
    return element;
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

package org.eclipselabs.bobthebuilder.model;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BuilderTypeSupplement {

  private final Set<Field> extraFields;

  private final Set<WithMethod> extraWithMethods;

  public BuilderTypeSupplement(Set<Field> extraFields, Set<WithMethod> extraWithMethods) {
    Validate.notNull(extraFields, "extraFields may not be null");
    Validate.notNull(extraWithMethods, "extraWithMethods may not be null");
    Validate.noNullElements(extraFields, "extraFields may not contain null elements");
    Validate.noNullElements(extraWithMethods, "extraWithMethods may not contain null elements");
    this.extraFields = extraFields;
    this.extraWithMethods = extraWithMethods;
  }

  public Set<Field> getExtraFields() {
    return Collections.unmodifiableSet(extraFields);
  }

  public Set<WithMethod> getExtraWithMethods() {
    return Collections.unmodifiableSet(extraWithMethods);
  }

  public boolean isEmptySupplement() {
    return getExtraFields().isEmpty() && getExtraWithMethods().isEmpty();
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

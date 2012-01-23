package org.eclipselabs.bobthebuilder.model;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ValidateMethodComplement {

  private final Set<FieldAssignment> fieldAssignments;

  public ValidateMethodComplement(Set<FieldAssignment> fieldAssignments) {
    Validate.notNull(fieldAssignments, "fieldAssignments may not be null");
    Validate.noNullElements(fieldAssignments, "fieldAssignments may not contain null elements");
    this.fieldAssignments = Collections.unmodifiableSet(fieldAssignments);
  }

  public Set<FieldAssignment> getFieldAssignments() {
    return fieldAssignments;
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

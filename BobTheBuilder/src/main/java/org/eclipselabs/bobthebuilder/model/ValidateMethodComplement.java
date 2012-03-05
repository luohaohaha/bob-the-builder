package org.eclipselabs.bobthebuilder.model;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ValidateMethodComplement {

  private final Set<FieldAssignment> fieldAssignments;
  
  private final boolean completeComplement;

  public ValidateMethodComplement(Set<FieldAssignment> fieldAssignments, boolean completeComplement) {
    Validate.notNull(fieldAssignments, "fieldAssignments may not be null");
    Validate.noNullElements(fieldAssignments, "fieldAssignments may not contain null elements");
    Validate.isTrue((fieldAssignments.isEmpty() && !completeComplement) || !fieldAssignments.isEmpty(),
      "if there are no missing field assigments then the complement cannot be complete");
    this.fieldAssignments = Collections.unmodifiableSet(fieldAssignments);
    this.completeComplement = completeComplement;
  }

  public Set<FieldAssignment> getFieldAssignments() {
    return fieldAssignments;
  }

  public boolean isCompleteComplement() {
    return completeComplement;
  }
  
  public boolean isEmptyComplement() {
    return getFieldAssignments().isEmpty();
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

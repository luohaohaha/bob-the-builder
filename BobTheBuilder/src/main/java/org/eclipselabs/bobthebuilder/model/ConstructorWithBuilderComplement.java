package org.eclipselabs.bobthebuilder.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ConstructorWithBuilderComplement {
  private final String name;

  private final Set<FieldAssignment> fieldAssignments;

  private ConstructorWithBuilderComplement(Builder builder) {
    this.name = builder.name;
    this.fieldAssignments = builder.fieldAssignments;
  }

  public static class Builder {

    private String name;

    private Set<FieldAssignment> fieldAssignments = new HashSet<FieldAssignment>();

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withFieldAssignments(Set<FieldAssignment> fieldAssignments) {
      this.fieldAssignments = fieldAssignments;
      return this;
    }
    
    public Builder addFieldAssignment(FieldAssignment fieldAssignment) {
      this.fieldAssignments.add(fieldAssignment);
      return this;
    }

    public ConstructorWithBuilderComplement build() {
      validate();
      return new ConstructorWithBuilderComplement(this);
    }

    private void validate() {
      Validate.isTrue(!StringUtils.isBlank(name), "name may not be blank");
      Validate.notNull(fieldAssignments, "fieldAssignment may not be null");
      Validate.noNullElements(fieldAssignments, "fieldAssignment may not contain null elements");
    }
  }
  
  
  public String getName() {
    return name;
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

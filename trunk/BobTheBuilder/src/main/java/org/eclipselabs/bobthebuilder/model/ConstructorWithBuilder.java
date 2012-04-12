package org.eclipselabs.bobthebuilder.model;

import java.util.Set;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ConstructorWithBuilder {
  private final String name;

  private final Set<FieldAssignment> fieldAssignment;

  private final String source;
  
  private ConstructorWithBuilder(Builder builder) {
    this.fieldAssignment = builder.fieldAssignment;
    this.name = builder.name;
    this.source = builder.source;
  }

  public static class Builder {

    public String source;

    private Set<FieldAssignment> fieldAssignment = new HashSet<FieldAssignment>();

    private String name;

    public Builder withFieldAssignment(Set<FieldAssignment> fieldAssignment) {
      Validate.notNull(fieldAssignment, "fieldAssignment may not be null");
      Validate.noNullElements(fieldAssignment, "fieldAssignment may not contain null elements");
      this.fieldAssignment.addAll(fieldAssignment);
      return this;
    }

    public Builder withName(String name) {
      this.name = name;
      return this;
    }

    public Builder withSource(String source) {
      this.source = source;
      return this;
    }
    
    public ConstructorWithBuilder build() {
      validate();
      return new ConstructorWithBuilder(this);
    }

    private void validate() {
      Validate.notNull(fieldAssignment, "fieldAssignment may not be null");
      Validate.isTrue(!StringUtils.isBlank(name), "name may not be blank");
    }
  }

  public String getName() {
    return name;
  }

  public Set<FieldAssignment> getFieldAssignment() {
    return fieldAssignment;
  }

  public String getSource() {
    return source;
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

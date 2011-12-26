package org.eclipselabs.bobthebuilder.model;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipselabs.bobthebuilder.ValidationFramework;

public class ValidateMethod {

  // This may be empty
  private final Set<Field> validatedFields;

  private final String source;

  // This may be null
  private ValidationFramework validationFramework;

  private ValidateMethod(Builder builder) {
    this.source = builder.source;
    this.validationFramework = builder.validationFramework;
    this.validatedFields = builder.validatedFields;
  }

  public static class Builder {

    private Set<Field> validatedFields;

    private ValidationFramework validationFramework;

    private String source;

    public Builder withValidatedFields(Set<Field> validatedFields) {
      this.validatedFields = validatedFields;
      return this;
    }

    public Builder withValidationFramework(ValidationFramework validationFramework) {
      this.validationFramework = validationFramework;
      return this;
    }

    public ValidateMethod build() {
      validate();
      return new ValidateMethod(this);
    }

    private void validate() {
      Validate.notNull(validatedFields, "missingFields may not be null");
      Validate.notNull(validationFramework, "validationFramework may not be null");
      Validate.isTrue(!StringUtils.isBlank(source), "source may not be blank");
    }

    public Builder withSource(String source) {
      this.source = source;
      return this;
    }

  }

  public Set<Field> getValidatedFields() {
    return validatedFields;
  }

  public ValidationFramework getValidationFramework() {
    return validationFramework;
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

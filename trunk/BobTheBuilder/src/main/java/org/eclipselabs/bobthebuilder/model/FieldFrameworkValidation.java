package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipselabs.bobthebuilder.ValidationFramework;

public class FieldFrameworkValidation {

  private final ValidationFramework validationFramework;
  
  private final FieldValidationType fieldValidationType;

  public FieldFrameworkValidation(ValidationFramework validationFramework,
      FieldValidationType fieldValidationType) {
    Validate.notNull(validationFramework, "validationFramework may not be null");
    Validate.notNull(fieldValidationType, "fieldValidationType may not be null");
    this.validationFramework = validationFramework;
    this.fieldValidationType = fieldValidationType;
  }

  public static final FieldFrameworkValidation GUAVA_NOT_NULL = 
    new FieldFrameworkValidation(ValidationFramework.GOOGLE_GUAVA, FieldValidationType.NOT_NULL);

  public static final FieldFrameworkValidation COMMONS2_NOT_NULL = 
    new FieldFrameworkValidation(ValidationFramework.COMMONS_LANG2, FieldValidationType.NOT_NULL);

  public static final FieldFrameworkValidation GUAVA_ARGUMENT = 
    new FieldFrameworkValidation(ValidationFramework.GOOGLE_GUAVA, FieldValidationType.ARGUMENT);
  
  public static final FieldFrameworkValidation COMMONS2_ARGUMENT = 
    new FieldFrameworkValidation(ValidationFramework.COMMONS_LANG2, FieldValidationType.ARGUMENT);
  
  public ValidationFramework getValidationFramework() {
    return validationFramework;
  }

  public FieldValidationType getFieldValidationType() {
    return fieldValidationType;
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

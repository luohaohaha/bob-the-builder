package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldFrameworkValidation;

public class ValidatedField {
  
  private final Field field;
  
  private final Set<FieldFrameworkValidation> fieldValidations = new HashSet<FieldFrameworkValidation>();

  public ValidatedField(Field field, Set<FieldFrameworkValidation> fieldValidations) {
    Validate.notNull(field, "field may not be null");
    Validate.notNull(fieldValidations, "fieldValidations may not be null");
    Validate.noNullElements(fieldValidations);
    this.field = field;
    this.fieldValidations.addAll(fieldValidations);
  }

  public Field getField() {
    return field;
  }

  public Set<FieldFrameworkValidation> getFieldValidations() {
    return Collections.unmodifiableSet(fieldValidations);
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

package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.eclipselabs.bobthebuilder.model.ValidateMethodInBuilder;

public class ValidateFieldsMethodMapper {

  private final FieldPredicate.FieldAssignment predicate;

  private final FieldBasedContentInMethodMapper fieldBaseContentInMethodMapper;

  @Inject
  public ValidateFieldsMethodMapper(
      @ValidateMethodInBuilder FieldPredicate.FieldAssignment fieldAssignmentInConstructorMapper,
      FieldBasedContentInMethodMapper fieldBaseContentInMethodMapper) {
    this.predicate = fieldAssignmentInConstructorMapper;
    this.fieldBaseContentInMethodMapper = fieldBaseContentInMethodMapper;
  }

  public Set<FieldAssignment> map(IMethod validateMethodInBuilder, Set<Field> fields) throws JavaModelException {
    Validate.notNull(validateMethodInBuilder, "validateMethodInBuilder may not be null");
    Validate.notNull(fields, "fields may not be null");
    Validate.noNullElements(fields, "fields may not contain null elements");
    return fieldBaseContentInMethodMapper.map(validateMethodInBuilder, fields, predicate);
  }
}

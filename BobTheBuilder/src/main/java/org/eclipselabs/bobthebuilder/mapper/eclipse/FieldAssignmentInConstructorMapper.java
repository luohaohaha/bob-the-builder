package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.ConstructorInMainType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;

public class FieldAssignmentInConstructorMapper {

  private final FieldPredicate predicate;

  private final FieldBasedContentInMethodMapper fieldBaseContentInMethodMapper;

  @Inject
  public FieldAssignmentInConstructorMapper(
      @ConstructorInMainType FieldPredicate fieldAssignmentInConstructorMapper,
      FieldBasedContentInMethodMapper fieldBaseContentInMethodMapper) {
    this.predicate = fieldAssignmentInConstructorMapper;
    this.fieldBaseContentInMethodMapper = fieldBaseContentInMethodMapper;
  }

  public Set<FieldAssignment> map(IMethod constructorWithBuilder, Set<Field> fields) throws JavaModelException {
    Validate.notNull(constructorWithBuilder, "constructorWithBuilder may not be null");
    Validate.notNull(fields, "fields may not be null");
    Validate.noNullElements(fields, "fields may not contain null elements");
    return fieldBaseContentInMethodMapper.map(constructorWithBuilder, fields, predicate);
  }

}

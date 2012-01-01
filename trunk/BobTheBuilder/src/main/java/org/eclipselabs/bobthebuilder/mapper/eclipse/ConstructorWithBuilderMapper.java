package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilder;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;

public class ConstructorWithBuilderMapper {
  private final MethodPredicate.ConstructorWithBuilder methodPredicate;

  private final FieldAssignmentInConstructorMapper fieldAssignmentInConstructorMapper;

  @Inject
  ConstructorWithBuilderMapper(MethodPredicate.ConstructorWithBuilder predicate,
      FieldAssignmentInConstructorMapper fieldAssignmentInConstructorMapper) {
    this.methodPredicate = predicate;
    this.fieldAssignmentInConstructorMapper = fieldAssignmentInConstructorMapper;
  }

  public ConstructorWithBuilder map(IType type, Set<Field> fields) throws JavaModelException {
    Validate.notNull(type, "type may not be null");
    Validate.notNull(fields, "fields may not be null");
    Validate.noNullElements(fields, "fields may not contain null elements");
    IMethod constructorWithBuilder = null;
    for (IMethod each : type.getMethods()) {
      if (methodPredicate.match(each)) {
        constructorWithBuilder = each;
        break;
      }
    }
    if (constructorWithBuilder == null) {
      return null;
    }
    ConstructorWithBuilder.Builder builder = new ConstructorWithBuilder.Builder();
    builder.withName(constructorWithBuilder.getElementName())
        .withSource(constructorWithBuilder.getSource())
        .withIsConstructor();
    Set<FieldAssignment> fieldAssignments =
        fieldAssignmentInConstructorMapper.map(constructorWithBuilder, fields);
    builder.withFieldAssignment(fieldAssignments);
    return builder.build();
  }
}

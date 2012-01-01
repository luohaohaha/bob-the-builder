package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;

public class FieldAssignmentInConstructorMapper {

  private final FieldPredicate.FieldAssignment predicate;

  @Inject
  public FieldAssignmentInConstructorMapper(
      FieldPredicate.FieldAssignment fieldAssignmentInConstructorMapper) {
    this.predicate = fieldAssignmentInConstructorMapper;
  }

  public Set<FieldAssignment> map(IMethod constructorWithBuilder, Set<Field> fields) throws JavaModelException {
    Validate.notNull(constructorWithBuilder, "constructorWithBuilder may not be null");
    Validate.notNull(fields, "fields may not be null");
    Validate.noNullElements(fields, "fields may not contain null elements");
    Set<FieldAssignment> result = new HashSet<FieldAssignment>();
    for (Field each : fields) {
      String fieldName = each.getName();
      boolean found =
          predicate.match(fieldName, constructorWithBuilder.getSource(), each.getSignature());
      if (found) {
        result.add(new FieldAssignment(fieldName));
      }
    }
    return Collections.unmodifiableSet(result);
  }

}

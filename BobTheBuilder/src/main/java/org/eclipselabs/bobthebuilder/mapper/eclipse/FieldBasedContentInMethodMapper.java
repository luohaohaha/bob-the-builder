package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;

public class FieldBasedContentInMethodMapper {

  public Set<FieldAssignment> map(IMethod method, Set<Field> fields, FieldPredicate fieldPredicate) throws JavaModelException {
    Validate.notNull(method, "method may not be null");
    Validate.notNull(fields, "fields may not be null");
    Validate.noNullElements(fields, "fields may not contain null elements");
    Validate.notNull(fieldPredicate, "fieldPredicate may not be null");
    Set<FieldAssignment> result = new HashSet<FieldAssignment>();
    for (Field each : fields) {
      String fieldName = each.getName();
      boolean found =
          fieldPredicate.match(fieldName, method.getSource(), each.getSignature());
      if (found) {
        result.add(new FieldAssignment(fieldName));
      }
    }
    return Collections.unmodifiableSet(result);
  }
}

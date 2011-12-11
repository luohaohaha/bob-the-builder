package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

public class MethodContentAnalyzer {

  public Set<IField> analyze(Set<IField> typeFields, MethodResult analyzedMethodResult,
    FieldPredicate fieldPredicate) throws JavaModelException {
    Validate.notNull(typeFields, "type fields set may not be null");
    Validate.isTrue(!typeFields.isEmpty(), "fields may not be empty");
    Validate.noNullElements(typeFields, "type fields set may not contain nulls");
    Validate.notNull(analyzedMethodResult, "analyzed method result may not be null");
    Validate.notNull(fieldPredicate, "fieldPredicate may not be null");
    Set<IField> fields = Collections.unmodifiableSet(typeFields);
    if (!analyzedMethodResult.isPresent()) {
      return fields;
    }
    if (analyzedMethodResult.getElement().getSource() == null) {
      return fields;
    }
    Set<IField> result = new HashSet<IField>();
    for (IField each : fields) {
      String fieldName = each.getElementName();
      boolean found = fieldPredicate.match(
        fieldName, analyzedMethodResult.getElement().getSource(), each.getTypeSignature());
      if (!found) {
        result.add(each);
      }
    }
    return Collections.unmodifiableSet(result);
  }

}

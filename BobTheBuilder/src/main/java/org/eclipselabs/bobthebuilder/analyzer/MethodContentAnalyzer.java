package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

public class MethodContentAnalyzer {

  private final Set<IField> typeFields;

  private final AnalyzerResult.ForMethod analyzedMethodResult;

  private final FieldPredicate fieldPredicate;

  public MethodContentAnalyzer(
      Set<IField> typeFields, AnalyzerResult.ForMethod analyzedMethodResult, FieldPredicate fieldPredicate) {
    Validate.notNull(typeFields, "type fields set may not be null");
    Validate.isTrue(!typeFields.isEmpty(), "fields may not be empty");
    Validate.noNullElements(typeFields, "type fields set may not contain nulls");
    Validate.notNull(analyzedMethodResult, "analyzed method result may not be null");
    Validate.notNull(fieldPredicate, "fieldPredicate may not be null");
    this.typeFields = Collections.unmodifiableSet(typeFields);
    this.analyzedMethodResult = analyzedMethodResult;
    this.fieldPredicate = fieldPredicate;
  }

  public Set<IField> analyze() throws JavaModelException {
    if (!analyzedMethodResult.isPresent()) {
      return typeFields;
    }
    if (analyzedMethodResult.getElement().getSource() == null) {
      return typeFields;
    }
    Set<IField> result = new HashSet<IField>();
    for (IField each : typeFields) {
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

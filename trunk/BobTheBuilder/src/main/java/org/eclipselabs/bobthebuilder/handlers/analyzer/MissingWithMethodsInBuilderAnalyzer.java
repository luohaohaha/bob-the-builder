package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class MissingWithMethodsInBuilderAnalyzer {
  private final Set<IField> builderFields = new HashSet<IField>();

  private final Set<IField> missingFieldsInBuilder = new HashSet<IField>();

  private final AnalyzerResult.Type builderTypeAnalyzerResult;

  private final Set<IField> extraFieldsInBuilder = new HashSet<IField>();

  MissingWithMethodsInBuilderAnalyzer(
      Set<IField> builderFields,
      Set<IField> missingFieldsInBuilder,
      AnalyzerResult.Type builderTypeAnalyzerResult,
      Set<IField> extraFieldsInBuilder) {
    Validate.notNull(builderFields, "builder fields may not be null");
    Validate.noNullElements(builderFields, "builder fields contains a null");
    Validate.notNull(missingFieldsInBuilder, "missing fields in builder may not be null");
    Validate.noNullElements(missingFieldsInBuilder, "missing fields in builder contains a null");
    Validate.notNull(builderTypeAnalyzerResult, "builder type analyzer result not be null");
    Validate.notNull(extraFieldsInBuilder, "extra fields in builder may not be null");
    Validate.noNullElements(extraFieldsInBuilder, "extra fields in builder contains a null");
    this.builderFields.addAll(builderFields);
    this.missingFieldsInBuilder.addAll(missingFieldsInBuilder);
    this.builderTypeAnalyzerResult = builderTypeAnalyzerResult;
    this.extraFieldsInBuilder.addAll(extraFieldsInBuilder);
  }

  public Set<IField> analyze() throws JavaModelException {
    Set<IField> result = new HashSet<IField>();
    result.addAll(missingFieldsInBuilder);
    if (!builderTypeAnalyzerResult.isPresent()) {
      return Collections.unmodifiableSet(result);
    }
    Iterator<IField> iterator = builderFields.iterator();
    while (iterator.hasNext()) {
      IField each = iterator.next();
      IMethod[] methods = builderTypeAnalyzerResult.getElement().getMethods();
      for (IMethod eachBuilderMethod : methods) {
        if (eachBuilderMethod.getElementName().equals(
          "with" + StringUtils.capitalize(each.getElementName())) &&
          eachBuilderMethod.getParameterTypes()[0].equals(each.getTypeSignature())) {
          iterator.remove();
        }
      }
    }
    Iterator<IField> iteratorToRemoveExtraFields = builderFields.iterator();
    while (iteratorToRemoveExtraFields.hasNext()) {
      IField each = iteratorToRemoveExtraFields.next();
      if (extraFieldsInBuilder.contains(each)) {
        iteratorToRemoveExtraFields.remove();
      }
    }
    result.addAll(builderFields);
    return Collections.unmodifiableSet(result);
  }
}

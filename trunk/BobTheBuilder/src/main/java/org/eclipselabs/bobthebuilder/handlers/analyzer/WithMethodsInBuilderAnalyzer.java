package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;

public class WithMethodsInBuilderAnalyzer {
  private final Set<IField> fields = new HashSet<IField>();

  private final AnalyzerResult.ForType builderTypeAnalyzerResult;

  WithMethodsInBuilderAnalyzer(
      Set<IField> builderFields,
      Set<IField> missingFieldsInBuilder,
      AnalyzerResult.ForType builderTypeAnalyzerResult,
      Set<IField> extraFieldsInBuilder) {
    validate(builderFields, missingFieldsInBuilder, builderTypeAnalyzerResult, extraFieldsInBuilder);
    this.builderTypeAnalyzerResult = builderTypeAnalyzerResult;
    this.fields.addAll(builderFields);
    this.fields.addAll(missingFieldsInBuilder);
    this.fields.removeAll(extraFieldsInBuilder);
  }

  private void validate(Set<IField> builderFields, Set<IField> missingFieldsInBuilder,
    AnalyzerResult.ForType builderTypeAnalyzerResult, Set<IField> extraFieldsInBuilder) {
    Validate.notNull(builderFields, "builder fields may not be null");
    Validate.noNullElements(builderFields, "builder fields contains a null");
    Validate.notNull(missingFieldsInBuilder, "missing fields in builder may not be null");
    Validate.noNullElements(missingFieldsInBuilder, "missing fields in builder contains a null");
    Validate.notNull(builderTypeAnalyzerResult, "builder type analyzer result not be null");
    Validate.notNull(extraFieldsInBuilder, "extra fields in builder may not be null");
    Validate.noNullElements(extraFieldsInBuilder, "extra fields in builder contains a null");
    Validate
        .isTrue(
          (!builderTypeAnalyzerResult.isPresent() && extraFieldsInBuilder.isEmpty()
            && builderFields.isEmpty()) || builderTypeAnalyzerResult.isPresent(),
          "If the builder class is missing, " +
            "then the builder fields and the extra builder fields should be empty");
    Validate.isTrue(
      (builderFields.isEmpty() && extraFieldsInBuilder.isEmpty()) || !builderFields.isEmpty(),
      "If there are no fields in the builder, then there should not be any extra fields");
    Validate.isTrue(builderFields.containsAll(extraFieldsInBuilder),
      "All extra builder fields should be found among the builder fields");
    validateEmptyIntersection(builderFields, missingFieldsInBuilder,
      "builder fields and missing fields in builder");
    validateEmptyIntersection(extraFieldsInBuilder, missingFieldsInBuilder,
      "extra builder fields and missing fields in builder");
  }

  private void validateEmptyIntersection(Set<IField> leftSet,
    Set<IField> rightSet, String description) {
    Set<IField> intersectionMissingAndBuilderFields = new HashSet<IField>();
    intersectionMissingAndBuilderFields.addAll(leftSet);
    intersectionMissingAndBuilderFields.retainAll(rightSet);
    Validate.isTrue(intersectionMissingAndBuilderFields.isEmpty(),
      "There should not be overlap between " + description);
  }

  public Set<IField> analyze() throws JavaModelException {
    Set<IField> result = new HashSet<IField>();
    for (IField each : fields) {
      // use ioc instead
      ForMethod analyzed =
          new WithMethodInBuilderAnalyzer(builderTypeAnalyzerResult, each).analyze();
      if (!analyzed.isPresent()) {
        result.add(each);
      }
    }
    return Collections.unmodifiableSet(result);
  }
}

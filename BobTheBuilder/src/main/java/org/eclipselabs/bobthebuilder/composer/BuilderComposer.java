package org.eclipselabs.bobthebuilder.composer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;
import org.eclipselabs.bobthebuilder.model.ValidateMethodInBuilder;

public class BuilderComposer {
  
  private static final String WITH = "with";
  private final FieldPredicate fieldPredicate;
  
  @Inject
  public BuilderComposer(@ValidateMethodInBuilder FieldPredicate fieldPredicate) {
    this.fieldPredicate = fieldPredicate;
  }

  public String composeSkeleton() {
    return StringUtils.join(new String[] {
          "public static class Builder{",
          "}" }, "\n");
  }

  public String composeFieldDeclaration(Field field) {
    Validate.notNull(field, "field may not be null");
    return "private " + field.getSignature() + " " + field.getName() + ";";
  }

  public String composeWithMethod(Field field) {
    Validate.notNull(field, "field may not be null");
    return StringUtils.join(
        new String[] {
            composeWithMethodFirstLine(field),
            "  this." + field.getName() + " = " + field.getName() + ";",
            "  return this;",
            "}" }, "\n");
  }

  public String composeWithMethodFirstLine(Field field) {
    return composeWithMethodSignature(
      WITH + StringUtils.capitalize(field.getName()), field.getSignature(), field.getName());
  }

  public String composeValidateMethodFromScratch(
    Set<Field> missingFields, ValidationFramework validationFramework) {
    Validate.notNull(validationFramework, "validationFramework may not be null");
    Validate.notNull(missingFields, "missingFields may not be null");
    Validate.noNullElements(missingFields, "missingFields may not contain null elements");
    ArrayList<String> listOfLines = new ArrayList<String>();
    listOfLines.add("private void validate() {");
    for (Field each : missingFields) {
      listOfLines.add("  " + validationFramework.composeFieldValidation(each));
    }
    listOfLines.add("}");
    return StringUtils.join(listOfLines, "\n");
  }

  //TODO this method looks an awful lot like ConstructorComposer#composeFromExisting
  public String composeValidateMethodFromExisting(
    ComposerRequest request, ValidateMethod originalValidateMethod) {
    Validate.notNull(request, "request may not be null");
    Validate.notNull(originalValidateMethod, "originalValidateMethod may not be null");
    int length = originalValidateMethod.getSource().length();
    List<String> sourceLines = new ArrayList<String>();
    String originalSource = originalValidateMethod.getSource().substring(0, length - 1);
    ArrayList<String> originalLines = new ArrayList<String>();
    originalLines.addAll(Arrays.asList(StringUtils.split(originalSource, '\n')));
    for (Field each : request.getExtraFieldsInBuilder()) {
      ListIterator<String> iterator = originalLines.listIterator();
      while(iterator.hasNext()) {
        String eachOriginalLine = iterator.next();
        if (fieldPredicate.match(each.getName(), eachOriginalLine, each.getSignature())) {
          iterator.remove();
        }
      }
    }
    sourceLines.addAll(originalLines);
    for (Field each : request.getMissingFieldValidationsInBuild()) {
      sourceLines.add("  " + request.getValidationFramework().composeFieldValidation(each));
    }
    sourceLines.add("}");
    String newLines = StringUtils.join(sourceLines, "\n");
    return newLines;
  }

  public String composeBuilderMethod(MainType type, boolean createValidateMethod) {
    Validate.notNull(type, "type may not be null");
    String signatureLine = "public " + type.getName() + " build() {";
    String constructorInvocationLine = "  return new " + type.getName() + "(this);";
    String validateMethodInvocation = createValidateMethod
          ? "  validate();"
          : "";
    String closeMethodLine = "}";
    List<String> sourceLines = new ArrayList<String>();
    sourceLines.add(signatureLine);
    if (!StringUtils.isBlank(validateMethodInvocation)) {
      sourceLines.add(validateMethodInvocation);
    }
    sourceLines.add(constructorInvocationLine);
    sourceLines.add(closeMethodLine);
    return StringUtils.join(sourceLines, "\n");
  }
  
  private String composeWithMethodSignature(
    String methodName, String fieldSignature, String fieldName) {
    Validate.notNull(fieldName, "fieldName may not be null");
    Validate.notNull(fieldSignature, "fieldSignature may not be null");
    Validate.notNull(methodName, "methodName may not be null");
    return "public Builder " + methodName + "(" + fieldSignature + " " + fieldName + ") {";
  }

}

package org.eclipselabs.bobthebuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;

public class Composer {

  public void compose(ComposerRequest request,
      DialogContent dialogRequest, CompilationUnitAnalyzer.Analyzed analyzed) throws JavaModelException {
    ICompilationUnit compilationUnit = analyzed.getCompilationUnit();
    IType type = analyzed.getType();
    if (request.isCreateConstructorWithBuilder()) {
      String constructorWithBuilderBuilder = composeConstructorWithBuilder(
          request, dialogRequest, type, analyzed);
      type.createMethod(constructorWithBuilderBuilder.toString(), analyzed.getBuilderType(),
          true, null);
    }
    compilationUnit.commitWorkingCopy(true, null);
    if (analyzed.getConstructorWithBuilder() != null &&
          (!request.getMissingAssignmentsInConstructor().isEmpty() ||
              !request.getExtraFieldsInBuilder().isEmpty())) {
      IMethod originalConstructorWithBuilder = analyzed.getConstructorWithBuilder();
      Validate.notNull(originalConstructorWithBuilder,
          "originalConstructorWithBuilder may not be null");
      int length = originalConstructorWithBuilder.getSourceRange().getLength();
      List<String> sourceLines = new ArrayList<String>();
      String originalSource = originalConstructorWithBuilder.getSource().substring(0, length - 1);
      for (IField each : request.getExtraFieldsInBuilder()) {
        originalSource = originalSource.replaceAll(
            FieldPredicate.FieldAssignment.createFieldAssignmentRegex(each.getElementName()), "");
      }
      sourceLines.add(originalSource);
      originalConstructorWithBuilder.delete(true, null);
      for (IField each : request.getMissingAssignmentsInConstructor()) {
        composeAssignmentsInConstructorWithBuilder(sourceLines, each);
      }
      sourceLines.add("}");
      type.createMethod(StringUtils.join(sourceLines, "\n"), null, true, null);
    }
    IType builder;
    if (analyzed.isMissingBuilder()) {
      type.createType(composeBuilder(), null, true, null);
      IType[] types = type.getTypes();
      Validate.notEmpty(types, "types may not be empty");
      builder = types[0];
    }
    else {
      builder = analyzed.getBuilderType();
    }
    for (IField each : request.getMissingFieldsInBuilder()) {
      builder.createField(composeFieldInBuilder(each), null, true, null);
    }
    compilationUnit.commitWorkingCopy(true, null);
    for (IField each : request.getExtraFieldsInBuilder()) {
      for (IField eachBuilderField : builder.getFields()) {
        if (eachBuilderField.getElementName().equals(each.getElementName())) {
          eachBuilderField.delete(true, null);
        }
      }
      for (IMethod eachBuilderMethod : builder.getMethods()) {
        if (eachBuilderMethod.getElementName().equals(
            "with" + StringUtils.capitalize(each.getElementName()))) {
          eachBuilderMethod.delete(true, null);
        }
      }
    }
    for (IField each : request.getMissingWithMethodsInBuilder()) {
      builder.createMethod(
          composeWithMethodInBuilder(each), null, true, null);
    }
    if (request.isCreateBuildMethodInBuilder()) {
      builder.createMethod(
          composeBuilderMethod(type, request.isCreateValidateMethodInBuilder()), null, true, null);
    }
    if (request.isCreateValidateMethodInBuilder()) {
      builder.createMethod(
          composeValidateMethodInBuilder(
            request.getMissingFieldValidationsInBuild(),
            request.getValidationFramework()),
          null, true, null);
    }
    else if (!request.getMissingFieldValidationsInBuild().isEmpty()) {
      IMethod originalValidateMethod = analyzed.getValidateMethodInBuilder();
      int length = originalValidateMethod.getSourceRange().getLength();
      List<String> sourceLines = new ArrayList<String>();
      String originalSource = originalValidateMethod.getSource().substring(0, length - 1);
      for (IField each : request.getExtraFieldsInBuilder()) {
        originalSource = originalSource.replaceAll(
            FieldPredicate.FieldAssignment.createFieldAssignmentRegex(each.getElementName()), "");
      }
      sourceLines.add(originalSource);
      originalValidateMethod.delete(true, null);
      for (IField each : request.getMissingFieldValidationsInBuild()) {
        sourceLines.add(request.getValidationFramework().composeFieldValidation(each));
      }
      sourceLines.add("}");
      builder.createMethod(StringUtils.join(sourceLines, "\n"), null, true, null);
    }
    if (request.isCreateValidateMethodInBuilder()
          || !request.getMissingFieldValidationsInBuild().isEmpty()) {
      compilationUnit.createImport(
          request.getValidationFramework().getFullClassName(), null, null);
    }
    compilationUnit.commitWorkingCopy(true, null);
  }

  private String composeValidateMethodInBuilder(
      Set<IField> missingFieldValidationsInBuild,
      ValidationFramework validationFramework) throws JavaModelException {
    ArrayList<String> listOfLines = new ArrayList<String>();
    listOfLines.add("private void validate() {");
    for (IField each : missingFieldValidationsInBuild) {
      listOfLines.add(validationFramework.composeFieldValidation(each));
    }
    listOfLines.add("}");
    return StringUtils.join(listOfLines, "\n");
  }

  private String composeBuilderMethod(IType type, boolean createValidateMethod) {
    String signatureLine = "public " + type.getElementName() + " build() {";
    String constructorInvocationLine = "  return new " + type.getElementName() + "(this);";
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

  private String composeConstructorWithBuilder(
        ComposerRequest request,
        DialogContent dialogRequest,
        IType type,
        CompilationUnitAnalyzer.Analyzed analyzed) {
    Set<IField> fieldsToAddInBuilder = new HashSet<IField>();
    fieldsToAddInBuilder.addAll(analyzed.getBuilderFields());
    fieldsToAddInBuilder.addAll(request.getMissingFieldsInBuilder());
    fieldsToAddInBuilder.removeAll(request.getExtraFieldsInBuilder());
    List<String> sourceLines = new ArrayList<String>();
    sourceLines.add("private " + type.getElementName() + "(Builder builder) {");
    for (IField each : fieldsToAddInBuilder) {
      composeAssignmentsInConstructorWithBuilder(sourceLines, each);
    }
    sourceLines.add("}");
    return StringUtils.join(sourceLines.toArray(), "\n");
  }

  private void composeAssignmentsInConstructorWithBuilder(
      List<String> sourceLines, IField each) {
    sourceLines
          .add("  " + composeSingleAssignment(each));
  }

  static String composeSingleAssignment(IField field) {
    return "this." + field.getElementName() + " = builder." + field.getElementName() + ";";
  }

  private String composeBuilder() {
    return StringUtils.join(new String[] {
          "public static class Builder{",
          "}" }, "\n");
  }

  static String composeFieldInBuilder(IField each) throws JavaModelException {
    return "private " + Signature.toString(each.getTypeSignature()) + " "
        + each.getElementName() + ";";
  }

  private String composeWithMethodInBuilder(IField each) throws JavaModelException {
    return StringUtils.join(
        new String[] {
            composeWithMethodSignature(each),
            "  this." + each.getElementName() + " = " + each.getElementName() + ";",
            "  return this;",
            "}" }, "\n");
  }

  static String composeWithMethodSignature(IField field) throws JavaModelException {
    return "public Builder with" + StringUtils.capitalize(field.getElementName()) + "("
        + Signature.toString(field.getTypeSignature()) + " " + field.getElementName()
        + ") {";
  }

  static String composeValidation(IField field, ValidationFramework validationFramework) {
    switch (validationFramework) {
      case COMMONS_LANG2:
      case COMMONS_LANG3:
        return ValidationFramework.COMMONS_LANG2.getCheckNotNull();
    }
    return null;
  }
}

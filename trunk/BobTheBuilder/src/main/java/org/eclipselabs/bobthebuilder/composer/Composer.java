package org.eclipselabs.bobthebuilder.composer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipselabs.bobthebuilder.DialogContent;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FlattenedICompilationUnit;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilder;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class Composer {

  private final ConstructorComposer constructorComposer;
  
  @Inject
  public Composer(ConstructorComposer constructorComposer) {
    this.constructorComposer = constructorComposer;
  }
  
  public void compose(ComposerRequest request,
      DialogContent dialogRequest, 
      FlattenedICompilationUnit flattenedICompilationUnit, JavaClassFile javaClassFile) throws JavaModelException {
    ICompilationUnit compilationUnit = flattenedICompilationUnit.getCompilationUnit();
    IType type = flattenedICompilationUnit.getMainType();
    if (request.isCreateConstructorWithBuilder()) {
      String constructorWithBuilderBuilder = 
        constructorComposer.composeFromScratch(request, type.getElementName());
      type.createMethod(
        constructorWithBuilderBuilder, flattenedICompilationUnit.getBuilderType(), true, null);
    }
    compilationUnit.commitWorkingCopy(true, null);
    if (flattenedICompilationUnit.getConstructorWithBuilder() != null &&
          (!request.getMissingAssignmentsInConstructor().isEmpty() ||
              !request.getExtraFieldsInBuilder().isEmpty())) {
      String sourceLines = 
        constructorComposer.composeFromExisting(
          request, javaClassFile.getMainType().getConstructorWithBuilder());
      IMethod originalConstructorWithBuilder = 
        flattenedICompilationUnit.getConstructorWithBuilder();
      originalConstructorWithBuilder.delete(true, null);
      type.createMethod(sourceLines, null, true, null);
    }
    IType builder;
    if (flattenedICompilationUnit.getBuilderType() ==  null) {
      type.createType(composeBuilder(), null, true, null);
      IType[] types = type.getTypes();
      Validate.notEmpty(types, "types may not be empty");
      builder = types[0];
    }
    else {
      builder = flattenedICompilationUnit.getBuilderType();
    }
    for (Field each : request.getMissingFieldsInBuilder()) {
      builder.createField(composeFieldInBuilder(each), null, true, null);
    }
    compilationUnit.commitWorkingCopy(true, null);
    for (Field each : request.getExtraFieldsInBuilder()) {
      for (IField eachBuilderField : builder.getFields()) {
        if (eachBuilderField.getElementName().equals(each.getName())) {
          eachBuilderField.delete(true, null);
        }
      }
      for (IMethod eachBuilderMethod : builder.getMethods()) {
        if (eachBuilderMethod.getElementName().equals(
            "with" + StringUtils.capitalize(each.getName()))) {
          eachBuilderMethod.delete(true, null);
        }
      }
    }
    for (Field each : request.getMissingWithMethodsInBuilder()) {
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
      IMethod originalValidateMethod = flattenedICompilationUnit.getValidateMethod();
      int length = originalValidateMethod.getSourceRange().getLength();
      List<String> sourceLines = new ArrayList<String>();
      String originalSource = originalValidateMethod.getSource().substring(0, length - 1);
      for (Field each : request.getExtraFieldsInBuilder()) {
        originalSource = originalSource.replaceAll(
            FieldPredicate.FieldAssignment.createFieldAssignmentRegex(each.getName()), "");
      }
      sourceLines.add(originalSource);
      originalValidateMethod.delete(true, null);
      for (Field each : request.getMissingFieldValidationsInBuild()) {
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
      Set<Field> missingFieldValidationsInBuild,
      ValidationFramework validationFramework) throws JavaModelException {
    ArrayList<String> listOfLines = new ArrayList<String>();
    listOfLines.add("private void validate() {");
    for (Field each : missingFieldValidationsInBuild) {
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

  public static String composeSingleAssignment(Field field) {
    return "this." + field.getName() + " = builder." + field.getName() + ";";
  }

  private String composeBuilder() {
    return StringUtils.join(new String[] {
          "public static class Builder{",
          "}" }, "\n");
  }

  public static String composeFieldInBuilder(IField field) throws JavaModelException {
    return composeFieldInBuilder(field.getTypeSignature(), field.getElementName());
  }

  public static String composeFieldInBuilder(Field field) throws JavaModelException {
    return composeFieldInBuilder(field.getSignature(), field.getName());
  }

  private static String composeFieldInBuilder(String signature, String name) throws JavaModelException {
    return "private " + Signature.toString(signature) + " " + name + ";";
  }

  private String composeWithMethodInBuilder(Field each) throws JavaModelException {
    return StringUtils.join(
        new String[] {
            composeWithMethodSignature(each),
            "  this." + each.getName() + " = " + each.getName() + ";",
            "  return this;",
            "}" }, "\n");
  }

  public static String composeWithMethodSignature(IField field) throws JavaModelException {
    return composeWithMethodSignature(field.getElementName(), field.getTypeSignature());
  }

  public static String composeWithMethodSignature(Field field) throws JavaModelException {
    return composeWithMethodSignature(field.getName(), field.getSignature());
  }

  private static String composeWithMethodSignature(String name, String signature) throws JavaModelException {
    return composeWithMethodSignature( "with"+ StringUtils.capitalize(name), signature, name);
  }

  public static String composeWithMethodSignature(WithMethod withMethod) throws JavaModelException {
    return composeWithMethodSignature(
      withMethod.getName(), withMethod.getField().getSignature(), withMethod.getField().getName());
  }

  private static String composeWithMethodSignature(String methodName, String fieldSignature, String fieldName) throws JavaModelException {
    return "public Builder " + methodName + "("
    + Signature.toString(fieldSignature) + " " + fieldName
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

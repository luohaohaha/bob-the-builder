package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipselabs.bobthebuilder.ValidationFramework;

public class Analyzed {

  private IType mainType;

  private Set<ValidationFramework> validationFrameworks;

  private final ICompilationUnit compilationUnit;

  private final Set<IField> fields;

  private final Set<IField> builderFields;

  private final IType type;

  private final Set<IField> missingFieldsInBuilder;

  private final Set<IField> extraFieldsInBuilder;

  private final boolean missingBuilder;

  private final Set<IField> missingWithMethodsForFields;

  private final IType builderType;

  private final boolean missingConstructorWithBuilder;

  private final Set<IField> missingFieldsInConstructorWithBuilder;

  private final IMethod constructorWithBuilder;

  private final boolean missingBuildMethodInBuilder;

  private final boolean missingValidateMethodInBuilder;

  private final Set<IField> missingFieldValidationsInBuilder;

  private final Collection<ValidationFramework> possibleValidationFrameworks;

  private final IMethod validateMethodInBuilder;

  Analyzed(
      ICompilationUnit compilationUnit,
      Set<IField> fields,
      Set<IField> builderFields,
      IType type,
      Set<IField> missingFieldsInBuilder,
      Set<IField> extraFieldsInBuilder,
      boolean missingBuilder,
      Set<IField> missingWithMethodsForFields,
      IType builderType,
      boolean missingConstructorWithBuilder,
      Set<IField> missingFieldsInConstructorWithBuilder,
      IMethod constructorWithBuilder,
      boolean missingBuildMethodInBuilder,
      boolean missingValidateMethodInBuilder,
      Set<IField> missingFieldsInBuilderValidation,
      IMethod validateMethodInBuilder,
      Collection<ValidationFramework> possibleValidationFrameworks) {
    // TODO analyzed whether all this validation is redundant.
    // move it to the analyze method
    Validate.notNull(compilationUnit, "compilation unit may not be null");
    this.compilationUnit = compilationUnit;
    Validate.notNull(fields, "fields may not be null");
    this.fields = fields;
    Validate.notNull(builderFields, "builderFields may not be null");
    this.builderFields = builderFields;
    Validate.notNull(type, "type may not be null");
    this.type = type;
    Validate.notNull(missingFieldsInBuilder, "missingFieldsInBuilder may not be null");
    this.missingFieldsInBuilder = missingFieldsInBuilder;
    Validate.notNull(extraFieldsInBuilder, "extraFieldsInBuilder may not be null");
    this.extraFieldsInBuilder = extraFieldsInBuilder;
    this.missingBuilder = missingBuilder;
    Validate.notNull(missingWithMethodsForFields, "missingWithMethodsForFields may not be null");
    this.missingWithMethodsForFields = missingWithMethodsForFields;
    this.builderType = builderType;
    this.missingConstructorWithBuilder = missingConstructorWithBuilder;
    Validate.notNull(
      missingFieldsInConstructorWithBuilder,
      "missingFieldsInConstructorWithBuilder may not be null");
    this.missingFieldsInConstructorWithBuilder = missingFieldsInConstructorWithBuilder;
    this.constructorWithBuilder = constructorWithBuilder;
    this.missingBuildMethodInBuilder = missingBuildMethodInBuilder;
    this.missingValidateMethodInBuilder = missingValidateMethodInBuilder;
    Validate.notNull(
      missingFieldsInBuilderValidation, "missingFieldsInBuilderValidation may not be null");
    this.missingFieldValidationsInBuilder = missingFieldsInBuilderValidation;
    this.validateMethodInBuilder = validateMethodInBuilder;
    Validate.notNull(
      possibleValidationFrameworks, "possibleValidationFrameworks may not be null");
    Validate.notEmpty(
      possibleValidationFrameworks, "possibleValidationFrameworks may not be empty");
    this.possibleValidationFrameworks = possibleValidationFrameworks;
  }

  public ICompilationUnit getCompilationUnit() {
    return compilationUnit;
  }

  public Set<IField> getFields() {
    return Collections.unmodifiableSet(fields);
  }

  public Set<IField> getBuilderFields() {
    return Collections.unmodifiableSet(builderFields);
  }

  public IType getType() {
    return type;
  }

  public Set<IField> getMissingFieldsInBuilder() {
    return Collections.unmodifiableSet(missingFieldsInBuilder);
  }

  public Set<IField> getExtraFieldsInBuilder() {
    return Collections.unmodifiableSet(extraFieldsInBuilder);
  }

  public boolean isMissingBuilder() {
    return missingBuilder;
  }

  public Set<IField> getMissingWithMethodsForFields() {
    return Collections.unmodifiableSet(missingWithMethodsForFields);
  }

  public boolean isMissingConstructorWithBuilder() {
    return missingConstructorWithBuilder;
  }

  public IMethod getConstructorWithBuilder() {
    return constructorWithBuilder;
  }

  public Set<IField> getMissingFieldsInConstructorWithBuilder() {
    return Collections.unmodifiableSet(missingFieldsInConstructorWithBuilder);
  }

  public IType getBuilderType() {
    return builderType;
  }

  public boolean isMissingBuildMethodInBuilder() {
    return missingBuildMethodInBuilder;
  }

  public boolean isMissingValidateMethodInBuilder() {
    return missingValidateMethodInBuilder;
  }

  public Set<IField> getMissingFieldValidationsInBuilder() {
    return Collections.unmodifiableSet(missingFieldValidationsInBuilder);
  }

  public Collection<ValidationFramework> getPossibleValidationFrameworks() {
    return Collections.unmodifiableCollection(possibleValidationFrameworks);
  }

  public IMethod getValidateMethodInBuilder() {
    return validateMethodInBuilder;
  }

  public boolean isThereAnythingToDo() {
    if (!isMissingBuilder() &&
        getMissingFieldsInBuilder().isEmpty() &&
        getExtraFieldsInBuilder().isEmpty() &&
        getMissingWithMethodsForFields().isEmpty() &&
        !isMissingConstructorWithBuilder() &&
        getMissingFieldsInConstructorWithBuilder().isEmpty() &&
        !isMissingValidateMethodInBuilder() &&
        getMissingFieldValidationsInBuilder().isEmpty()) {
      return false;
    }
    else {
      return true;
    }

  }

}

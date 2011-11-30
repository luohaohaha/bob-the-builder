package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForMethod;

public class CompilationUnitAnalyzer {
  public static final String BUILDER_CLASS_NAME = "Builder";

  private ICompilationUnit compilationUnit;

  public static class Analyzed {

    // TODO change from missing to isPresent. Some of this has been already
    // happening in the micro-analyzers
    // TODO audit whether all of this fields are really needed.
    // GRoup fields in classes of coherent

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

    private Analyzed(
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

  public CompilationUnitAnalyzer(ICompilationUnit compilationUnit) {
    Validate.notNull(compilationUnit, "Compilation Unit cannot be null");
    this.compilationUnit = compilationUnit;
  }

  public CompilationUnitAnalyzer.Analyzed analyze() throws Exception {
    Set<IField> fields = new HashSet<IField>();
    Set<IField> copyOfFields = new HashSet<IField>();
    Set<IField> builderFields = new HashSet<IField>();
    Set<IField> copyOfBuilderFields = new HashSet<IField>();
    Set<IField> anotherCopyOfBuilderFields = new HashSet<IField>();
    Set<IField> missingFieldsInBuilder = null;
    Set<IField> extraFieldsInBuilder = new HashSet<IField>();
    Set<IField> missingWithMethodsForFields = new HashSet<IField>();
    Set<IField> missingFieldsInConstructorWithBuilder = new HashSet<IField>();
    Set<IField> missingFieldValidationsInBuilder = new HashSet<IField>();
    IMethod validationMethod = null;
    IMethod constructorWithBuilder = null;
    IType type = null;
    IType builderType = null;
    boolean missingConstructorWithBuilder = false;
    boolean missingBuilder = true;
    boolean missingBuildMethodInBuilder = true;
    boolean missingValidateMethodInBuilder = true;
    Collection<ValidationFramework> validationFrameworks = null;
    try {
      type = new TypeAnalyzer(compilationUnit).analyze();
      fields = new MainTypeFieldAnalyzer(type).analyze();
      // TODO if we have our own representation of the IField, we can do fancier set operations.
      copyOfFields.addAll(fields);
      AnalyzerResult.ForType builderAnalyzerResult = new BuilderTypeAnalyzer(type).analyze();
      builderType = builderAnalyzerResult.getElement();
      missingBuilder = !builderAnalyzerResult.isPresent();
      builderFields = new BuilderTypeFieldAnalyzer(builderAnalyzerResult).analyze();
      copyOfBuilderFields.addAll(builderFields);
      anotherCopyOfBuilderFields.addAll(builderFields);
      missingFieldsInBuilder =
          new DifferenceBetweenFieldSetsAnalyzer(copyOfFields, builderFields).analyze();
      // flip-flop collections so that the subtraction of sets works
      extraFieldsInBuilder = new DifferenceBetweenFieldSetsAnalyzer(copyOfBuilderFields, fields)
          .analyze();
      missingWithMethodsForFields = new WithMethodsInBuilderAnalyzer(
          anotherCopyOfBuilderFields,
          missingFieldsInBuilder,
          builderAnalyzerResult,
          extraFieldsInBuilder).analyze();
      ForMethod constructorWithBuilderResult =
          new ConstructorWithBuilderAnalyzer(builderAnalyzerResult, type).analyze();
      missingConstructorWithBuilder = !constructorWithBuilderResult.isPresent();
      constructorWithBuilder = constructorWithBuilderResult.getElement();
      missingFieldsInConstructorWithBuilder =
          new ConstructorWithBuilderInMainTypeAnalyzer(
              fields, constructorWithBuilderResult).analyze();
      missingBuildMethodInBuilder =
          !new BuildInBuilderAnalyzer(builderAnalyzerResult).analyze().isPresent();
      ForMethod analyzedValidateInBuilder =
          new MethodAnalyzer(builderAnalyzerResult, new MethodPredicate.ValidateInBuilder())
              .analyze();
      missingValidateMethodInBuilder = !analyzedValidateInBuilder.isPresent();
      validationMethod = analyzedValidateInBuilder.getElement();
      missingFieldValidationsInBuilder =
          new MethodContentAnalyzer(
              fields, analyzedValidateInBuilder, new FieldPredicate.FieldValidation()).analyze();
      validationFrameworks =
          new ValidationFrameworkAnalyzer(analyzedValidateInBuilder, compilationUnit).analyze();
    }
    catch (JavaModelException e) {
      new IllegalStateException("Something went really wrong: " + e.getMessage());
    }
    return new Analyzed(
        compilationUnit,
        Collections.unmodifiableSet(fields),
        Collections.unmodifiableSet(builderFields),
        type,
        Collections.unmodifiableSet(missingFieldsInBuilder),
        Collections.unmodifiableSet(extraFieldsInBuilder),
        missingBuilder,
        Collections.unmodifiableSet(missingWithMethodsForFields),
        builderType,
        missingConstructorWithBuilder,
        Collections.unmodifiableSet(missingFieldsInConstructorWithBuilder),
        constructorWithBuilder,
        missingBuildMethodInBuilder,
        missingValidateMethodInBuilder,
        Collections.unmodifiableSet(missingFieldValidationsInBuilder),
        validationMethod,
        Collections.unmodifiableCollection(validationFrameworks));
  }

}

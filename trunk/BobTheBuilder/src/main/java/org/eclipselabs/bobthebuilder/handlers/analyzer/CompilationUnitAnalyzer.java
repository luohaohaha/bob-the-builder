package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.ValidationFramework;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.Method;

public class CompilationUnitAnalyzer {
  public static final String BUILDER_CLASS_NAME = "Builder";

  private ICompilationUnit compilationUnit;

  public static class Analyzed {

    public static final String BUILD_METHOD_NAME = "build";

    public static final String VALIDATE_METHOD_NAME = "validate";

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

    public final static String CONSTRUCTOR_WITH_BUILDER_SIGNATURE = "(QBuilder;)V";

    private final Set<IField> missingFieldsInConstructorWithBuilder;

    private final IMethod constructorWithBuilder;

    private final boolean missingBuildMethodInBuilder;

    private final boolean missingValidateMethodInBuilder;

    private final Set<IField> missingFieldValidationsInBuilder;

    private final Collection<ValidationFramework> possibleValidationFrameworks;

    private final IMethod validateMethodInBuilder;

    private final Collection<ValidationFramework> existingValidationFrameworkImports;

    public Analyzed(
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
        Collection<ValidationFramework> possibleValidationFrameworks,
        Collection<ValidationFramework> existingValidationFrameworkImports) {
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
      Validate.notNull(
        existingValidationFrameworkImports, "existingValidationFrameworkImports may not be null");
      this.existingValidationFrameworkImports = existingValidationFrameworkImports;
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

    public Collection<ValidationFramework> getExistingValidationFrameworkImports() {
      return Collections.unmodifiableCollection(existingValidationFrameworkImports);
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
    Collection<ValidationFramework> existingValidationFrameworkImports = null;
    try {
      type = new TypeAnalyzer(compilationUnit).analyze();
      fields = new MainTypeFieldAnalyzer(type).analyze();
      copyOfFields.addAll(fields);
      AnalyzerResult.Type builderAnalyzerResult = new BuilderTypeAnalyzer(type).analyze();
      builderType = builderAnalyzerResult.getElement();
      missingBuilder = !builderAnalyzerResult.isPresent();
      builderFields = new BuilderTypeFieldAnalyzer(builderAnalyzerResult).analyze();
      copyOfBuilderFields.addAll(builderFields);
      anotherCopyOfBuilderFields.addAll(builderFields);
      missingFieldsInBuilder =
          new MissingFieldsInBuilderAnalyzer(copyOfFields, builderFields).analyze();
      extraFieldsInBuilder = new ExtraFieldsInBuilderAnalyzer(copyOfBuilderFields, fields)
          .analyze();
      missingWithMethodsForFields = new MissingWithMethodsInBuilderAnalyzer(
          anotherCopyOfBuilderFields,
          missingFieldsInBuilder,
          builderAnalyzerResult,
          extraFieldsInBuilder).analyze();
      Method constructorWithBuilderResult =
          new ConstructorWithBuilderInMainTypeAnalyzer(builderAnalyzerResult, type).analyze();
      missingConstructorWithBuilder = !constructorWithBuilderResult.isPresent();
      constructorWithBuilder = constructorWithBuilderResult.getElement();
      missingFieldsInConstructorWithBuilder =
          new MissingInstructionsInMethodAnalyzer.ConstructorWithBuilderInMainType(
              fields, constructorWithBuilderResult).analyze();
      missingBuildMethodInBuilder = 
        !new MissingMethodAnalyzer.BuildInBuilder(builderAnalyzerResult).analyze().isPresent();
      missingValidateMethodInBuilder =
        !new MissingMethodAnalyzer.ValidateInBuilder(builderAnalyzerResult).analyze().isPresent();
      validationMethod = analyzeMissingValidateMethodInBuilder(missingBuilder, builderType)
          .getValidationMethod();
      missingFieldValidationsInBuilder =
          analyzeMissingFieldValidationsInBuilder(
            missingValidateMethodInBuilder, validationMethod, fields);
      validationFrameworks =
          analyzeValidationFramework(missingValidateMethodInBuilder, validationMethod,
            compilationUnit);
      existingValidationFrameworkImports =
          analyzeExistingValidationFrameworkImports(compilationUnit);

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
        Collections.unmodifiableCollection(validationFrameworks),
        Collections.unmodifiableCollection(existingValidationFrameworkImports));
  }

  private Collection<ValidationFramework> analyzeExistingValidationFrameworkImports(
    ICompilationUnit compilationUnit) throws JavaModelException {
    Collection<ValidationFramework> existingImports = new ArrayList<ValidationFramework>();
    IImportDeclaration[] imports = compilationUnit.getImports();
    for (IImportDeclaration each : imports) {
      for (ValidationFramework eachFramework : ValidationFramework.values()) {
        if (each.getElementName().contains(eachFramework.getFullClassName())) {
          existingImports.add(eachFramework);
        }
      }
    }
    return existingImports;
  }

  private Collection<ValidationFramework> analyzeValidationFramework(
    boolean missingValidateMethodInBuilder,
    IMethod validationMethod, ICompilationUnit compilationUnit) throws JavaModelException {
    if (missingValidateMethodInBuilder) {
      return Arrays.asList(ValidationFramework.values());
    }

    String methodSource = validationMethod.getSource();
    if (methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.GOOGLE_GUAVA.getCheckNotNull())) {
      return Arrays.asList(ValidationFramework.GOOGLE_GUAVA);
    }
    if (methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckArgument()) ||
        methodSource.contains(ValidationFramework.COMMONS_LANG2.getCheckNotNull())) {
      for (IImportDeclaration each : compilationUnit.getImports()) {
        if (each.getElementName().equals(ValidationFramework.COMMONS_LANG2.fullClassName)) {
          return Arrays.asList(ValidationFramework.COMMONS_LANG2);
        }
        else if (each.getElementName().equals(ValidationFramework.COMMONS_LANG3.fullClassName)) {
          return Arrays.asList(ValidationFramework.COMMONS_LANG3);
        }
        else {
          // Fall-back strategy
          return Arrays.asList(ValidationFramework.COMMONS_LANG2);
        }
      }
    }
    return Arrays.asList(ValidationFramework.values());
  }

  private Set<IField> analyzeMissingFieldValidationsInBuilder(
      boolean missingValidateMethodInBuilder, IMethod validateMethod, Set<IField> builderFields) throws JavaModelException {
    return analyzeMissingFieldsInMethod(
      builderFields, validateMethod, missingValidateMethodInBuilder,
      new FieldPredicate.FieldValidation());
  }

  private BuilderValidationMethodData analyzeMissingValidateMethodInBuilder(boolean missingBuilder,
    IType builderType) throws JavaModelException {
    if (missingBuilder) {
      return new BuilderValidationMethodData(true, null);
    }
    for (IMethod each : builderType.getMethods()) {
      if (each.getElementName().equals(Analyzed.VALIDATE_METHOD_NAME) &&
          each.getParameterTypes().length == 0) {
        return new BuilderValidationMethodData(false, each);
      }
    }
    return new BuilderValidationMethodData(true, null);
  }

  private Set<IField> analyzeMissingFieldsInMethod(
    Set<IField> fields, IMethod method, boolean missingMethod, FieldPredicate predicate) throws JavaModelException {
    if (missingMethod) {
      return fields;
    }
    if (method.getSource() == null) {
      return fields;
    }
    Set<IField> result = new HashSet<IField>();
    for (IField each : fields) {
      String fieldName = each.getElementName();
      boolean found = predicate.match(
        fieldName, method.getSource(), each.getTypeSignature());
      if (!found) {
        result.add(each);
      }
    }
    return result;
  }

  private static class BuilderValidationMethodData {
    private final boolean missing;

    private final IMethod validationMethod;

    BuilderValidationMethodData(boolean missing, IMethod validationMethod) {
      this.missing = missing;
      this.validationMethod = validationMethod;
    }

    public boolean isMissing() {
      return missing;
    }

    public IMethod getValidationMethod() {
      return validationMethod;
    }

  }

  boolean analyzeMissingBuildMethodInBuilder(boolean missingBuilder, IType builderType) throws JavaModelException {
    if (missingBuilder) {
      return true;
    }
    for (IMethod each : builderType.getMethods()) {
      if (each.getElementName().equals(Analyzed.BUILD_METHOD_NAME)) {
        return false;
      }
    }
    return true;
  }

}

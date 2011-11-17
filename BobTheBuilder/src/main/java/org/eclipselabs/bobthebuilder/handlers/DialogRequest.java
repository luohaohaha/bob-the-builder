package org.eclipselabs.bobthebuilder.handlers;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.FieldTextBuilder.FieldDeclarationBuilder;
import org.eclipselabs.bobthebuilder.handlers.analyzer.CompilationUnitAnalyzer;

public class DialogRequest {

  private final ICompilationUnit compilationUnit;

  private final Set<IField> fields;

  private final Set<IField> builderFields;

  private final IType type;

  private final IType builderType;

  private final Set<IField> missingFieldsInBuilder;

  private final Set<IField> extraFieldsInBuilder;

  private final boolean missingBuilder;

  private final Set<IField> missingWithMethodsForFields;

  private final boolean missingConstructorWithBuilder;

  private final boolean thereAnythingToDo;

  private final IMethod constructorWithBuilder;

  private final Set<IField> missingFieldsInConstructorWithBuilder;

  private final boolean missingBuildMethodInBuilder;

  private final Set<IField> missingFieldValidationsInBuilder;

  private final boolean missingValidateMethodInBuilder;

  private final IMethod validateMethodInBuilder;
  
  private final Collection<ValidationFramework> possibleValidationFrameworks;

  private final BobTheBuilderTreeNode tree;

  private Collection<ValidationFramework> existingValidationFrameworkImports;

  public DialogRequest(CompilationUnitAnalyzer.Analyzed analyzed) throws JavaModelException {
    Validate.notNull(analyzed, "Analyzed may not be null");
    this.builderFields = analyzed.getBuilderFields();
    this.compilationUnit = analyzed.getCompilationUnit();
    this.fields = analyzed.getFields();
    this.type = analyzed.getType();
    this.builderType = analyzed.getBuilderType();
    this.missingFieldsInBuilder = analyzed.getMissingFieldsInBuilder();
    this.extraFieldsInBuilder = analyzed.getExtraFieldsInBuilder();
    this.missingWithMethodsForFields = analyzed.getMissingWithMethodsForFields();
    this.missingBuilder = analyzed.isMissingBuilder();
    this.missingConstructorWithBuilder = analyzed.isMissingConstructorWithBuilder();
    this.constructorWithBuilder = analyzed.getConstructorWithBuilder();
    this.missingFieldsInConstructorWithBuilder = analyzed
        .getMissingFieldsInConstructorWithBuilder();
    this.missingBuildMethodInBuilder = analyzed.isMissingBuildMethodInBuilder();
    this.missingValidateMethodInBuilder = analyzed.isMissingValidateMethodInBuilder();
    this.missingFieldValidationsInBuilder = analyzed.getMissingFieldValidationsInBuilder();
    this.validateMethodInBuilder = analyzed.getValidateMethodInBuilder();
    this.possibleValidationFrameworks = analyzed.getPossibleValidationFrameworks();
    this.existingValidationFrameworkImports = analyzed.getExistingValidationFrameworkImports();
    this.thereAnythingToDo = analyzed.isThereAnythingToDo();
    this.tree = createTree();
  }

  private BobTheBuilderTreeNode createTree() throws JavaModelException {
    BobTheBuilderTreeNode tree = new BobTheBuilderTreeNode.Builder().build();
    tree.addChild(
        new FeatureTreeNode.Builder()
            .withData(Feature.MISSING_BUILDER)
            .withParent(tree)
            .withText(
              this.missingBuilder
                  ? "A builder will be created"
                  : "The existing builder will be modified")
            .build());
    tree.addChild(
        convertToTree(
          Feature.MISSING_FIELDS,
          "Select missing fields to add to the builder class",
          missingFieldsInBuilder,
          new FieldDeclarationBuilder()));
    tree.addChild(
        convertToTree(
          Feature.EXTRA_FIELDS,
          "Select existing extra fields to remove from the Builder",
          extraFieldsInBuilder,
          new FieldDeclarationBuilder()));
    tree.addChild(
        convertToTree(
          Feature.MISSING_WITHS,
          "Select with-methods to add to the Builder",
          missingWithMethodsForFields,
          new FieldTextBuilder.WithMethodBuilder()));
    tree.addChild(
        new FeatureTreeNode.Builder()
            .withData(Feature.MISSING_CONSTRUCTOR)
            .withParent(tree)
            .withText(missingConstructorWithBuilder
                ? "A private constructor will be created"
                : "A private constructor with Builder already exists")
            .build());
    tree.addChild(
        convertToTree(
          Feature.MISSING_ASSIGNMENTS,
          "Select assignments to add to the private constructor",
          missingFieldsInConstructorWithBuilder,
          new FieldTextBuilder.FieldAssignmentBuilder()));
    tree.addChild(new FeatureTreeNode.Builder()
        .withData(Feature.MISSING_BUILD)
        .withParent(tree)
        .withText(missingBuildMethodInBuilder
            ? "A build() method will be created in the Builder"
            : "The build() method already exists in the Builder")
        .build());
    tree.addChild(new FeatureTreeNode.Builder()
        .withData(Feature.MISSING_VALIDATE)
        .withParent(tree)
        .withText(missingValidateMethodInBuilder
            ? "A validate() method will be created in the Builder"
            : "The validate() method already exists in the Builder")
          .build());
    tree.addChild(
      convertToTree(
        Feature.MISSING_VALIDATIONS,
        "Select the validation to add to the validate method in the Builder",
        missingFieldValidationsInBuilder,
        new FieldTextBuilder.ValidationBuilder(possibleValidationFrameworks.iterator().next())));
    return tree;
  }

  private FeatureTreeNode convertToTree(
      Feature activateFeature,
      String featureText,
      Set<IField> missingFieldsInBuilder,
      FieldTextBuilder fieldTextBuilder) throws JavaModelException {
    FeatureTreeNode parent = new FeatureTreeNode.Builder()
        .withData(activateFeature)
        .withText(featureText)
        .withParent(tree)
        .build();
    for (IField each : missingFieldsInBuilder) {
      FieldTreeNode child = new FieldTreeNode.Builder()
          .withData(each)
          .withParent(parent)
          .withText(fieldTextBuilder.createMessage(each))
          .build();
      parent.addChild(child);
    }
    return parent;
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

  public IType getBuilderType() {
    return builderType;
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

  public boolean isThereAnythingToDo() {
    return thereAnythingToDo;
  }

  public boolean isMissingBuildMethodInBuilder() {
    return missingBuildMethodInBuilder;
  }

  public Set<IField> getMissingFieldValidationsInBuilder() {
    return Collections.unmodifiableSet(missingFieldValidationsInBuilder);
  }

  public IMethod getValidateMethodInBuilder() {
    return validateMethodInBuilder;
  }

  public boolean isMissingValidateMethodInBuilder() {
    return missingValidateMethodInBuilder;
  }

  public Collection<ValidationFramework> getPossibleValidationFrameworks() {
    return Collections.unmodifiableCollection(possibleValidationFrameworks);
  }

  public Collection<ValidationFramework> getExistingValidationFrameworkImports() {
    return Collections.unmodifiableCollection(existingValidationFrameworkImports);
  }

  public void setExistingValidationFrameworkImports(
    Collection<ValidationFramework> existingValidationFrameworkImports) {
    this.existingValidationFrameworkImports = existingValidationFrameworkImports;
  }

  public BobTheBuilderTreeNode getTree() {
    return tree;
  }

}

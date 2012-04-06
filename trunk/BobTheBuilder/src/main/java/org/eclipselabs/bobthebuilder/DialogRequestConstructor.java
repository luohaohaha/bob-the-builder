package org.eclipselabs.bobthebuilder;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.FieldTextBuilder.FieldAssignmentBuilder;
import org.eclipselabs.bobthebuilder.FieldTextBuilder.FieldDeclarationBuilder;
import org.eclipselabs.bobthebuilder.FieldTextBuilder.ValidationBuilder;
import org.eclipselabs.bobthebuilder.FieldTextBuilder.WithMethodBuilder;
import org.eclipselabs.bobthebuilder.complement.BuildMethodComplement;
import org.eclipselabs.bobthebuilder.model.BuilderTypeComplement;
import org.eclipselabs.bobthebuilder.model.BuilderTypeSupplement;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.eclipselabs.bobthebuilder.model.MainTypeComplement;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class DialogRequestConstructor {

  private final FieldDeclarationBuilder fieldDeclarationBuilder;

  private final FieldTextBuilder.WithMethodBuilder withMethodBuilder;

  private final FieldTextBuilder.FieldAssignmentBuilder fieldAssignmentBuilder;

  @Inject
  public DialogRequestConstructor(
      FieldDeclarationBuilder fieldDeclarationBuilder,
      FieldTextBuilder.WithMethodBuilder withMethodBuilder,
      FieldTextBuilder.FieldAssignmentBuilder fieldAssignmentBuilder) {
    this.fieldDeclarationBuilder = fieldDeclarationBuilder;
    this.withMethodBuilder = withMethodBuilder;
    this.fieldAssignmentBuilder = fieldAssignmentBuilder;
  }

  public DialogContent work(
    MainTypeComplement mainTypeComplement,
    BuilderTypeSupplement builderTypeSupplement) throws JavaModelException {
    Validate.notNull(mainTypeComplement, "mainTypeComplement may not be null");
    Validate.notNull(builderTypeSupplement, "builderTypeSupplement may not be null");
    BobTheBuilderTreeNode tree = new BobTheBuilderTreeNode.Builder().build();
    BuilderTypeComplement builderTypeComplement = mainTypeComplement.getBuilderTypeComplement();
    tree.addChild(
          new FeatureTreeNode.Builder()
              .withData(Feature.MISSING_BUILDER)
              .withParent(tree)
              .withText(
                builderTypeComplement.isCompleteComplement()
                    ? "A builder will be created"
                    : "The existing builder will be modified")
              .build());
    Set<Field> builderFieldsComplement = builderTypeComplement.getBuilderFieldsComplement();
    tree.addChild(
          convertToTree(
            Feature.MISSING_FIELDS,
            builderFieldsComplement.isEmpty()
                ?
                "No missing fields in builder"
                : "Fields to add to the builder...",
            builderFieldsComplement,
            fieldDeclarationBuilder,
            tree));
    Set<Field> extraFields = builderTypeSupplement.getExtraFields();
    tree.addChild(
          convertToTree(
            Feature.EXTRA_FIELDS,
            extraFields.isEmpty()
                ?
                "No extra fields in the builder"
                : "Extra fields to remove from the builder...",
            extraFields,
            fieldDeclarationBuilder,
            tree));
    Set<WithMethod> withMethodsComplement = builderTypeComplement.getWithMethodsComplement();
    tree.addChild(
          convertToTree(
            Feature.MISSING_WITHS,
            withMethodsComplement.isEmpty()
                ?
                "No with-methods missing in the builder"
                : "With-methods to add to the builder...",
            withMethodsComplement,
            withMethodBuilder,
            tree));
    boolean constructorCompleteComplement = mainTypeComplement
        .getConstructorWithBuilderComplement()
        .isCompleteComplement();
    tree.addChild(
          new FeatureTreeNode.Builder()
              .withData(constructorCompleteComplement
                  ? Feature.MISSING_CONSTRUCTOR
                  : Feature.NONE)
              .withParent(tree)
              .withText(
                constructorCompleteComplement
                    ? "A private constructor will be created"
                    : "A private constructor with the builder already exists")
              .build());
    Set<FieldAssignment> fieldAssignments = 
      mainTypeComplement.getConstructorWithBuilderComplement().getFieldAssignments();
    tree.addChild(
          convertToTree(
            Feature.MISSING_ASSIGNMENTS,
            fieldAssignments.isEmpty()
                ?
                "No assignments to add to the private constructor"
                : "Assignments to add to the private constructor...",
            fieldAssignments,
            fieldAssignmentBuilder,
            tree));
    BuildMethodComplement buildMethodComplement = builderTypeComplement.getBuildMethodComplement();
    boolean buildMethodcompleteComplement =
        buildMethodComplement.isCompleteComplement();
    tree.addChild(new FeatureTreeNode.Builder()
          .withData(buildMethodcompleteComplement
              ? Feature.MISSING_BUILD
              : Feature.NONE)
          .withParent(tree)
          .withText(buildMethodcompleteComplement
              ? "A build() method will be created in the builder"
              : "The build() method already exists in the builder")
          .build());
    boolean validateMethodComplement = buildMethodComplement.isValidateMethodComplement();
    tree.addChild(new FeatureTreeNode.Builder()
          .withData(validateMethodComplement
              ? Feature.MISSING_VALIDATE
              : Feature.NONE)
          .withParent(tree)
          .withText(validateMethodComplement
              ? "A validate() method will be created in the builder"
              : "The validate() method already exists in the builder")
            .build());
    Set<FieldAssignment> fieldsToValidate = 
      builderTypeComplement.getValidateMethodComplement().getFieldAssignments();
    tree.addChild(
          convertToTree(
            Feature.MISSING_VALIDATIONS,
            fieldsToValidate.isEmpty()
                ?
                "No fields to validate"
                : "Fields to validate...",
            fieldsToValidate,
              new FieldTextBuilder.ValidationBuilder(ValidationFramework.GOOGLE_GUAVA),
            tree));
    return new DialogContent(tree);
  }

  private FeatureTreeNode convertToTree(
    Feature feature,
    String featureText,
    Set<WithMethod> withMethodComplement,
    WithMethodBuilder withMethodBuilder,
    BobTheBuilderTreeNode tree) throws JavaModelException {
    Set<Field> fields = new HashSet<Field>();
    for (WithMethod each : withMethodComplement) {
      fields.add(each.getField());
    }
    return convertToTree(feature, featureText, fields, withMethodBuilder, tree);
  }

  private FeatureTreeNode convertToTree(
    Feature feature,
    String featureText,
    Set<FieldAssignment> fieldAssignmentComplement,
    ValidationBuilder validationBuilder,
    BobTheBuilderTreeNode tree) throws JavaModelException {
    Set<Field> fields = new HashSet<Field>();
    for (FieldAssignment each : fieldAssignmentComplement) {
      fields.add(each.getField());
    }
    return convertToTree(feature, featureText, fields, validationBuilder, tree);
  }

  private FeatureTreeNode convertToTree(
    Feature feature,
    String featureText,
    Set<FieldAssignment> fieldAssignmentComplement,
    FieldAssignmentBuilder fieldTextBuilder,
    BobTheBuilderTreeNode tree) throws JavaModelException {
    Set<Field> fields = new HashSet<Field>();
    for (FieldAssignment each : fieldAssignmentComplement) {
      fields.add(each.getField());
    }
    return convertToTree(feature, featureText, fields, fieldTextBuilder, tree);
  }

  private FeatureTreeNode convertToTree(
    Feature feature,
    String featureText,
    Set<Field> fields,
    FieldTextBuilder fieldTextBuilder,
    BobTheBuilderTreeNode tree) throws JavaModelException {
    FeatureTreeNode parent = new FeatureTreeNode.Builder()
        .withData(feature)
        .withText(featureText)
        .withParent(tree)
        .build();
    for (Field each : fields) {
      FieldTreeNode child = new FieldTreeNode.Builder()
          .withData(each)
          .withParent(parent)
          .withText(each.getName())
          .build();
      parent.addChild(child);
    }
    return parent;
  }

}

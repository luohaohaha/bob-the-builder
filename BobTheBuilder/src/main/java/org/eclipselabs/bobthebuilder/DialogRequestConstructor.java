package org.eclipselabs.bobthebuilder;

import java.util.HashSet;
import java.util.Set;

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
    tree.addChild(
          convertToTree(
            Feature.MISSING_FIELDS,
            "Select missing fields to add to the builder class",
            builderTypeComplement.getBuilderFieldsComplement(),
            new FieldDeclarationBuilder(),
            tree));
    tree.addChild(
          convertToTree(
            Feature.EXTRA_FIELDS,
            "Select existing extra fields to remove from the Builder",
            builderTypeSupplement.getExtraFields(),
            new FieldDeclarationBuilder(),
            tree));
    tree.addChild(
          convertToTree(
            Feature.MISSING_WITHS,
            "Select with-methods to add to the Builder",
            builderTypeComplement.getWithMethodsComplement(),
            new FieldTextBuilder.WithMethodBuilder(),
            tree));
    boolean constructorCompleteComplement = mainTypeComplement
        .getConstructorWithBuilderComplement()
        .isCompleteComplement();
    tree.addChild(
          new FeatureTreeNode.Builder()
              .withData(constructorCompleteComplement
                  ? Feature.NONE
                  : Feature.MISSING_CONSTRUCTOR)
              .withParent(tree)
              .withText(
                constructorCompleteComplement
                    ? "A private constructor will be created"
                    : "A private constructor with Builder already exists")
              .build());
    tree.addChild(
          convertToTree(
            Feature.MISSING_ASSIGNMENTS,
            "Select assignments to add to the private constructor",
            mainTypeComplement.getConstructorWithBuilderComplement().getFieldAssignments(),
            new FieldTextBuilder.FieldAssignmentBuilder(),
            tree));
    BuildMethodComplement buildMethodComplement = builderTypeComplement.getBuildMethodComplement();
    boolean buildMethodcompleteComplement =
        buildMethodComplement.isCompleteComplement();
    tree.addChild(new FeatureTreeNode.Builder()
          .withData(buildMethodcompleteComplement
              ? Feature.NONE
              : Feature.MISSING_BUILD)
          .withParent(tree)
          .withText(buildMethodcompleteComplement
              ? "The build() method already exists in the Builder"
              : "A build() method will be created in the Builder")
          .build());
    boolean validateMethodComplement = buildMethodComplement.isValidateMethodComplement();
    tree.addChild(new FeatureTreeNode.Builder()
          .withData(validateMethodComplement
              ? Feature.MISSING_VALIDATE
              : Feature.NONE)
          .withParent(tree)
          .withText(validateMethodComplement
              ? "A validate() method will be created in the Builder"
              : "The validate() method already exists in the Builder")
            .build());
    tree.addChild(
          convertToTree(
            Feature.MISSING_VALIDATIONS,
            "Select the validation to add to the validate method in the Builder",
            builderTypeComplement.getValidateMethodComplement().getFieldAssignments(),
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

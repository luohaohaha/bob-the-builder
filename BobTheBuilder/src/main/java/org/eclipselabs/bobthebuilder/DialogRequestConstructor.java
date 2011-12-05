package org.eclipselabs.bobthebuilder;

import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.FieldTextBuilder.FieldDeclarationBuilder;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer.Analyzed;

public class DialogRequestConstructor {

  public DialogContent work(Analyzed analyzed) throws JavaModelException {
    Validate.notNull(analyzed, "Analyzed may not be null");
    BobTheBuilderTreeNode tree = new BobTheBuilderTreeNode.Builder().build();
    tree.addChild(
          new FeatureTreeNode.Builder()
              .withData(Feature.MISSING_BUILDER)
              .withParent(tree)
              .withText(
                analyzed.isMissingBuilder()
                    ? "A builder will be created"
                    : "The existing builder will be modified")
              .build());
    tree.addChild(
          convertToTree(
            Feature.MISSING_FIELDS,
            "Select missing fields to add to the builder class",
            analyzed.getMissingFieldsInBuilder(),
            new FieldDeclarationBuilder(),
            tree));
    tree.addChild(
          convertToTree(
            Feature.EXTRA_FIELDS,
            "Select existing extra fields to remove from the Builder",
            analyzed.getExtraFieldsInBuilder(),
            new FieldDeclarationBuilder(),
            tree));
    tree.addChild(
          convertToTree(
            Feature.MISSING_WITHS,
            "Select with-methods to add to the Builder",
            analyzed.getMissingWithMethodsForFields(),
            new FieldTextBuilder.WithMethodBuilder(),
            tree));
    tree.addChild(
          new FeatureTreeNode.Builder()
              .withData(analyzed.isMissingConstructorWithBuilder()
                  ? Feature.MISSING_CONSTRUCTOR
                  : Feature.NONE)
              .withParent(tree)
              .withText(analyzed.isMissingConstructorWithBuilder()
                  ? "A private constructor will be created"
                  : "A private constructor with Builder already exists")
              .build());
    tree.addChild(
          convertToTree(
            Feature.MISSING_ASSIGNMENTS,
            "Select assignments to add to the private constructor",
            analyzed.getMissingFieldsInConstructorWithBuilder(),
            new FieldTextBuilder.FieldAssignmentBuilder(),
            tree));
    tree.addChild(new FeatureTreeNode.Builder()
          .withData(analyzed.isMissingBuildMethodInBuilder()
              ? Feature.MISSING_BUILD
              : Feature.NONE)
          .withParent(tree)
          .withText(analyzed.isMissingBuildMethodInBuilder()
              ? "A build() method will be created in the Builder"
              : "The build() method already exists in the Builder")
          .build());
    tree.addChild(new FeatureTreeNode.Builder()
          .withData(analyzed.isMissingValidateMethodInBuilder()
              ? Feature.MISSING_VALIDATE
              : Feature.NONE)
          .withParent(tree)
          .withText(analyzed.isMissingValidateMethodInBuilder()
              ? "A validate() method will be created in the Builder"
              : "The validate() method already exists in the Builder")
            .build());
    tree.addChild(
          convertToTree(
            Feature.MISSING_VALIDATIONS,
            "Select the validation to add to the validate method in the Builder",
            analyzed.getMissingFieldValidationsInBuilder(),
            new FieldTextBuilder.ValidationBuilder(
                analyzed.getPossibleValidationFrameworks().iterator().next()),
              tree));
    return new DialogContent(tree);
  }

  private FeatureTreeNode convertToTree(
    Feature activateFeature,
    String featureText,
    Set<IField> missingFieldsInBuilder,
    FieldTextBuilder fieldTextBuilder,
    BobTheBuilderTreeNode tree) throws JavaModelException {
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

}

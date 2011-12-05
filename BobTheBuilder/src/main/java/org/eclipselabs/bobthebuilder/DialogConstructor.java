package org.eclipselabs.bobthebuilder;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;

public class DialogConstructor {

  private static final int NUMBER_OF_COLUMNS = 3;

  public DialogConstructor() {}

  @SuppressWarnings("deprecation")
  public ComposerRequest show(
      final DialogContent dialogRequest, 
      final CompilationUnitAnalyzer.Analyzed analyzed, 
      Shell shell) {
    Validate.notNull(dialogRequest, "dialogRequest may not be null");
    final ComposerRequest.Builder composerRequestBuilder = new ComposerRequest.Builder();
    BobTheBuilderDialog bobTheBuilderDialog = new BobTheBuilderDialog(shell) {

      @Override
      protected void show() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = NUMBER_OF_COLUMNS;

        getShell().setLayout(gridLayout);

        String title =
            "Select Actions To Perform On "
              + analyzed.getCompilationUnit().getResource().getName();
        Label titleLabel = new Label(getShell(), SWT.BORDER);
        titleLabel.setText(title);
        titleLabel.setLayoutData(createTopSectionGridData());

        int CHECKED_TREE = SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.Expand;

        final CheckboxTreeViewer featuresTreeViewer = new CheckboxTreeViewer(getShell(),
            CHECKED_TREE);
        featuresTreeViewer.setLabelProvider(new FieldTreeLabelProvider());
        featuresTreeViewer.setContentProvider(new FieldTreeContentProvider());
        int twoLevelsExpansion = 2;
        featuresTreeViewer.setAutoExpandLevel(twoLevelsExpansion);
        featuresTreeViewer.setInput(dialogRequest.getTree());
        featuresTreeViewer.getTree().setLayoutData(createTopSectionGridData());
        featuresTreeViewer.setAllChecked(true);

        final Group validationsGroup = new Group(getShell(), SWT.SHADOW_IN);
        validationsGroup.setText("Select validation framework");
        validationsGroup.setLayout(new RowLayout());
        validationsGroup.setLayoutData(createTopSectionGridData());
        boolean checked = true;
        for (ValidationFramework each : analyzed.getPossibleValidationFrameworks()) {
          Button validationFrameworkButton = new Button(validationsGroup, SWT.RADIO);
          validationFrameworkButton.setSelection(checked);
          checked = false;
          validationFrameworkButton.setText(each.getReadableName());
          validationFrameworkButton.setData(each);
        }

        addCancelButton();

        addResetButton(featuresTreeViewer);

        GridData executeGridData = createBottomSectionGridData();
        Button executeButton = new Button(getShell(), SWT.PUSH);
        executeButton.setLayoutData(executeGridData);
        executeButton.setImage(getBobsImage());
        executeButton.setText("Bob, build!");
        executeButton.setFocus();
        executeButton.addListener(SWT.Selection, new Listener() {

          @Override
          public void handleEvent(Event arg0) {
            Object[] checkedElements = featuresTreeViewer.getCheckedElements();
            for (Object each : checkedElements) {
              if (each instanceof FieldTreeNode) {
                FieldTreeNode eachFieldNode = (FieldTreeNode) each;
                switch ((Feature) eachFieldNode.getParent().getData()) {
                  case MISSING_FIELDS:
                    composerRequestBuilder.addMissingFieldInBuilder((IField) eachFieldNode
                        .getData());
                    break;
                  case EXTRA_FIELDS:
                    composerRequestBuilder.addExtraFieldInBuilder((IField) eachFieldNode.getData());
                    break;
                  case MISSING_WITHS:
                    composerRequestBuilder.addMissingWithMethodInBuilder(
                        (IField) eachFieldNode.getData());
                    break;
                  case MISSING_ASSIGNMENTS:
                    composerRequestBuilder.addMissingAssignmentInConstructor(
                        (IField) eachFieldNode.getData());
                    break;
                  case MISSING_VALIDATIONS:
                    composerRequestBuilder.addMissingValidationInBuild((IField) eachFieldNode
                        .getData());
                }
              }
              else if (each instanceof FeatureTreeNode) {
                FeatureTreeNode eachFeatureNode = (FeatureTreeNode) each;
                switch ((Feature) eachFeatureNode.getData()) {
                  case MISSING_CONSTRUCTOR:
                    composerRequestBuilder.withConstructorWithBuilder();
                    break;
                  case MISSING_BUILD:
                    composerRequestBuilder.withBuildMethodInBuilder();
                    break;
                  case MISSING_VALIDATE:
                    composerRequestBuilder.withValidateMethodInBuilder();
                    break;
                }
              }
              else {
                throw new IllegalStateException("This tree node is not recognized.");
              }
            }
            for (Control control : validationsGroup.getChildren()) {
              if (((Button) control).getSelection()) {
                composerRequestBuilder.withValidationFramework((ValidationFramework) control
                    .getData());
              }
            }
            getShell().dispose();
          }
        });
        display();
        waitAndSee();
      }

      private void addResetButton(final CheckboxTreeViewer featuresTreeViewer) {
        GridData resetGridData = createBottomSectionGridData();

        Button resetButton = new Button(getShell(), SWT.PUSH);
        resetButton.setText("Reset");
        resetButton.setLayoutData(resetGridData);
        resetButton.addListener(SWT.Selection, new Listener() {

          @Override
          public void handleEvent(Event event) {
            featuresTreeViewer.setAllChecked(true);
            getShell().layout();
          }
        });

      }

      private GridData createTopSectionGridData() {
        GridData builderGridData = new GridData();
        builderGridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        builderGridData.horizontalSpan = NUMBER_OF_COLUMNS;
        return builderGridData;
      }

      private void addCancelButton() {
        GridData cancelGridData = createBottomSectionGridData();

        Button cancelButton = new Button(getShell(), SWT.PUSH);
        cancelButton.setText("Cancel");
        cancelButton.setLayoutData(cancelGridData);
        cancelButton.addListener(SWT.Selection, new Listener() {

          @Override
          public void handleEvent(Event event) {
            getShell().dispose();
          }
        });
      }

      private GridData createBottomSectionGridData() {
        GridData cancelGridData = new GridData();
        cancelGridData.horizontalSpan = 1;
        cancelGridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        return cancelGridData;
      }

    };
    bobTheBuilderDialog.show();
    return composerRequestBuilder.build();
  }
}

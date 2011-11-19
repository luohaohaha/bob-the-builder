package org.eclipselabs.bobthebuilder.handlers;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipselabs.bobthebuilder.Activator;

public class TreeBasedBuilderDialog extends Dialog {

  private static final String BOB_THE_BUILDER = "Bob the builder";

  private DialogRequest request;

  private Shell shell;

  private static final int NUMBER_OF_COLUMNS = 3;

  public TreeBasedBuilderDialog(DialogRequest request) {
    super(new Shell(), SWT.APPLICATION_MODAL | SWT.DIALOG_TRIM | SWT.CENTER);
    if (request == null) {
      throw new NullPointerException("Request cannot be null");
    }
    this.request = request;
    this.shell = getParent().getShell();
  }

  @SuppressWarnings("deprecation")
  public void show() {

    String iconFilePathname = "icons/btb.png";
    shell.setText("Bob The Builder Plugin");

    Image bobsImage = null;
    ImageDescriptor imageDescriptor = 
      Activator.getImageDescriptor(iconFilePathname);
    if (imageDescriptor != null) {
      bobsImage = new Image(shell.getDisplay(), imageDescriptor.getImageData());
    }
    shell.setImage(bobsImage);

    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = NUMBER_OF_COLUMNS;

    shell.setLayout(gridLayout);

    if (!request.isThereAnythingToDo()) {
      Label nothingToDoLabel = new Label(shell, SWT.BORDER);
      nothingToDoLabel.setLayoutData(createTopSectionGridData());
      nothingToDoLabel.setText("*** Nothing for Bob The Builder To Do ***");
      addCancelButton();
      display();
      return;
    }
    String title =
        "Select Actions To Perform On " + request.getCompilationUnit().getResource().getName();
    Label titleLabel = new Label(shell, SWT.BORDER);
    titleLabel.setText(title);
    titleLabel.setLayoutData(createTopSectionGridData());

    int CHECKED_TREE = SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.Expand;

    final CheckboxTreeViewer featuresTreeViewer = new CheckboxTreeViewer(shell, CHECKED_TREE);
    featuresTreeViewer.setLabelProvider(new FieldTreeLabelProvider());
    featuresTreeViewer.setContentProvider(new FieldTreeContentProvider());
    int twoLevelsExpansion = 2;
    featuresTreeViewer.setAutoExpandLevel(twoLevelsExpansion);
    featuresTreeViewer.setInput(request.getTree());
    featuresTreeViewer.getTree().setLayoutData(createTopSectionGridData());
    featuresTreeViewer.setAllChecked(true);

    final Group validationsGroup = new Group(shell, SWT.SHADOW_IN);
    validationsGroup.setText("Select validation framework");
    validationsGroup.setLayout(new RowLayout());
    validationsGroup.setLayoutData(createTopSectionGridData());
    boolean checked = true;
    for (ValidationFramework each : request.getPossibleValidationFrameworks()) {
      Button validationFrameworkButton = new Button(validationsGroup, SWT.RADIO);
      validationFrameworkButton.setSelection(checked);
      checked = false;
      validationFrameworkButton.setText(each.getReadableName());
      validationFrameworkButton.setData(each);
    }

    addCancelButton();

    addResetButton(featuresTreeViewer);

    GridData executeGridData = createBottomSectionGridDate();
    Button executeButton = new Button(shell, SWT.PUSH);
    executeButton.setLayoutData(executeGridData);
    executeButton.setImage(bobsImage);
    executeButton.setText("Bob, build!");
    executeButton.setFocus();
    executeButton.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event arg0) {
        ComposerRequest.Builder composerRequestBuilder = new ComposerRequest.Builder();
        Object[] checkedElements = featuresTreeViewer.getCheckedElements();
        for (Object each : checkedElements) {
          if (each instanceof FieldTreeNode) {
            FieldTreeNode eachFieldNode = (FieldTreeNode) each;
            switch ((Feature) eachFieldNode.getParent().getData()) {
              case MISSING_FIELDS:
                composerRequestBuilder.addMissingFieldInBuilder((IField) eachFieldNode.getData());
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
                composerRequestBuilder.addMissingValidationInBuild((IField) eachFieldNode.getData());
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
          if (((Button)control).getSelection()) {
            composerRequestBuilder.withValidationFramework((ValidationFramework) control.getData());
          }
        }
        try {
          ComposerRequest composerRequest = composerRequestBuilder.build();
          new Composer.Impl().compose(composerRequest, request);
        }
        catch (JavaModelException e) {
          bark("JavaModelException when composing. " + e.getMessage());
        }
        catch (Exception e) {
          bark("Exception when composing. " + e.getMessage());
        }
        finally {
          shell.dispose();
        }
      }
    });
    display();
  }

  private void addResetButton(final CheckboxTreeViewer featuresTreeViewer) {
    GridData resetGridData = createBottomSectionGridDate();

    Button resetButton = new Button(shell, SWT.PUSH);
    resetButton.setText("Reset");
    resetButton.setLayoutData(resetGridData);
    resetButton.addListener(SWT.Selection, new Listener() {

      @SuppressWarnings("deprecation")
      @Override
      public void handleEvent(Event event) {
        featuresTreeViewer.setAllChecked(true);
        shell.layout();
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
    GridData cancelGridData = createBottomSectionGridDate();

    Button cancelButton = new Button(shell, SWT.PUSH);
    cancelButton.setText("Cancel");
    cancelButton.setLayoutData(cancelGridData);
    cancelButton.addListener(SWT.Selection, new Listener() {

      @Override
      public void handleEvent(Event event) {
        shell.dispose();
      }
    });
  }

  private GridData createBottomSectionGridDate() {
    GridData cancelGridData = new GridData();
    cancelGridData.horizontalSpan = 1;
    cancelGridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
    return cancelGridData;
  }

  private void display() {
    shell.setSize(200, 400);
    shell.pack();
    shell.open();
  }

  private void bark(String message) {
    MessageDialog.openInformation(shell, BOB_THE_BUILDER, message);
  }
}

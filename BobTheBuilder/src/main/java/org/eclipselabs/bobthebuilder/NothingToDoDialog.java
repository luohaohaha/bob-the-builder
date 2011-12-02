package org.eclipselabs.bobthebuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class NothingToDoDialog {

  public NothingToDoDialog() {}

  public void show(Shell parentShell) {
    BobTheBuilderDialog bobTheBuilderDialog = new BobTheBuilderDialog(parentShell) {

      @Override
      protected void show() {
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;

        getShell().setLayout(gridLayout);
        Label nothingToDoLabel = new Label(getShell(), SWT.BORDER);
        nothingToDoLabel.setLayoutData(createBottomSectionGridDate());
        nothingToDoLabel.setText("*** Nothing for Bob The Builder To Do ***");
        addButton();
        display();

      }

      private void addButton() {
        GridData buttomGridData = createBottomSectionGridDate();

        Button cancelButton = new Button(getShell(), SWT.PUSH);
        cancelButton.setText("Ok");
        cancelButton.setLayoutData(buttomGridData);
        cancelButton.addListener(SWT.Selection, new Listener() {

          @Override
          public void handleEvent(Event event) {
            getShell().dispose();
          }
        });
      }

      private GridData createBottomSectionGridDate() {
        GridData cancelGridData = new GridData();
        cancelGridData.horizontalSpan = 1;
        cancelGridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
        return cancelGridData;
      }

    };
    bobTheBuilderDialog.show();
  }

}

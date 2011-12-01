package org.eclipselabs.bobthebuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class NothingToDoDialogImpl extends BobTheBuilderDialog implements NothingToDoDialog {

  public NothingToDoDialogImpl() {
    super();
  }

  /* (non-Javadoc)
   * @see org.eclipselabs.bobthebuilder.NothingToDoDialog#show()
   */
  @Override
  public void show() {
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 1;

    shell.setLayout(gridLayout);
    Label nothingToDoLabel = new Label(shell, SWT.BORDER);
    nothingToDoLabel.setLayoutData(createBottomSectionGridDate());
    nothingToDoLabel.setText("*** Nothing for Bob The Builder To Do ***");
    addButton();
    display();
  }

  private void addButton() {
    GridData buttomGridData = createBottomSectionGridDate();

    Button cancelButton = new Button(shell, SWT.PUSH);
    cancelButton.setText("Ok");
    cancelButton.setLayoutData(buttomGridData);
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
}

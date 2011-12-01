package org.eclipselabs.bobthebuilder;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public abstract class BobTheBuilderDialog extends Dialog {

  protected final Shell shell;

  protected final WindowCoordinates windowCoordinates;

  protected Image bobsImage = null;

  private final Display display;

  public BobTheBuilderDialog() {
    super(new Shell(SWT.APPLICATION_MODAL | SWT.ON_TOP));
    Shell parentShell = getParent().getShell();
    shell = new Shell(parentShell, SWT.APPLICATION_MODAL | SWT.ON_TOP);
    windowCoordinates = centerWindow();
    shell.addListener(SWT.Traverse, createListenerThatClosesDialog());
    shell.setLocation(windowCoordinates.getX(), windowCoordinates.getY());
    String iconFilePathname = "icons/btb.png";
    shell.setText("Bob The Builder Plugin");
    ImageDescriptor imageDescriptor =
        Activator.getImageDescriptor(iconFilePathname);
    if (imageDescriptor != null) {
      bobsImage = new Image(shell.getDisplay(), imageDescriptor.getImageData());
    }
    shell.setImage(bobsImage);
    display = parentShell.getDisplay();
  }

  protected void waitAndSee() {
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
  }
  private WindowCoordinates centerWindow() {
    Monitor primary = shell.getDisplay().getPrimaryMonitor();
    Rectangle bounds = primary.getBounds();
    Rectangle rect = shell.getBounds();

    int x = bounds.x + (bounds.width - rect.width) / 2;
    int y = bounds.y + (bounds.height - rect.height) / 2;
    WindowCoordinates windowCoordinates = new WindowCoordinates(x, y);
    return windowCoordinates;
  }

  private Listener createListenerThatClosesDialog() {
    return new Listener() {
      public void handleEvent(Event event) {
        switch (event.detail) {
          case SWT.TRAVERSE_ESCAPE:
            shell.close();
            event.detail = SWT.TRAVERSE_NONE;
            event.doit = false;
            break;
        }
      }
    };
  }

  protected void display() {
    shell.setSize(200, 400);
    shell.pack();
    shell.open();
  }

  static class WindowCoordinates {
    private final int x;

    private final int y;

    WindowCoordinates(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }

  }
}

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

  private final Shell shell;

  private final WindowCoordinates windowCoordinates;

  private final Image bobsImage;

  private final Display display;

  protected abstract void show();
  
  public BobTheBuilderDialog(Shell parentShell) {
    super(parentShell);
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
    else {
      bobsImage = null;
    }
    shell.setImage(bobsImage);
    display = parentShell.getDisplay();
  }

  protected void display() {
    shell.pack();
    shell.open();
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

  public Shell getShell() {
    return shell;
  }

  public Image getBobsImage() {
    return bobsImage;
  }
  
  
}

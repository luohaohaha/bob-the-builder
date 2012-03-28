package org.eclipselabs.bobthebuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipselabs.bobthebuilder.complement.ComplementModule;
import org.eclipselabs.bobthebuilder.mapper.eclipse.MapperModule;
import org.eclipselabs.bobthebuilder.supplement.SupplementModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BobTheBuilderWorker extends AbstractHandler {

  private static final String BOB_THE_BUILDER = "BobTheBuilder";

  private Shell shell;

  private SubContractor subContractor;

  public BobTheBuilderWorker() {
    Injector injector = Guice.createInjector(
      new WorkerModule(), 
      new MapperModule(), 
      new ComplementModule(), 
      new SupplementModule());
    subContractor = injector.getInstance(SubContractor.class);
    Validate.notNull(subContractor, "BobTheBuilderSubContractor may not null");
  }

  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
    IJavaElement javaElement = JavaUI.getEditorInputJavaElement(activeEditor.getEditorInput());
    ICompilationUnit compilationUnit;
    if (javaElement instanceof ICompilationUnit) {
      compilationUnit = (ICompilationUnit) javaElement;
    }
    else {
      bark("Expecting an ICompilationUnit, but got: " + javaElement.getClass());
      return null;
    }
    shell = window.getShell();

    try {
      subContractor.work(shell, compilationUnit);
    }
    catch (Exception e) {
      bark("Could not create the request to send to the dialog" + e.getClass().getName() + " "
        + e.getMessage() + " " + StringUtils.left(ExceptionUtils.getFullStackTrace(e), 100));
    }
    return null;
  }

  private void bark(String message) {
    MessageDialog.openInformation(shell, BOB_THE_BUILDER, message);
  }
}

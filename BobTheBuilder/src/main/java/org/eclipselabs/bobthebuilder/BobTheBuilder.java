package org.eclipselabs.bobthebuilder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;


/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BobTheBuilder extends AbstractHandler {
  
  private static final String BOB_THE_BUILDER = "BobTheBuilder";
  private Shell shell;
  private ICompilationUnit compilationUnit;
  /**
   * The constructor.
   */
  public BobTheBuilder() {}

  /**
   * the command has been executed, so extract extract the needed information from the application
   * context.
   */
  public Object execute(ExecutionEvent event) throws ExecutionException {
    IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    IEditorPart activeEditor = HandlerUtil.getActiveEditor(event);
    IJavaElement javaElement = JavaUI.getEditorInputJavaElement(activeEditor.getEditorInput());

    if (javaElement instanceof ICompilationUnit) {
      compilationUnit = (ICompilationUnit) javaElement;
    }
    else {
      bark("Expecting an ICompilationUnit, but got: " + javaElement.getClass());
    }
    shell = window.getShell();
    DialogRequest request = null;
    try {
      CompilationUnitAnalyzer.Analyzed analyzed = 
        new CompilationUnitAnalyzer(compilationUnit).analyze();
      request = new DialogRequest(analyzed);
    }
    catch (Exception e) {
      bark("Could not create the request to send to the dialog" + e.getClass().getName() + " "
        + e.getMessage() + " " + StringUtils.left(ExceptionUtils.getFullStackTrace(e), 100));
    }
    new TreeBasedBuilderDialog(request).show();
    return null;
  }

  private void bark(String message) {
    MessageDialog.openInformation(shell, BOB_THE_BUILDER, message);
  }
}

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
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzerImpl;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BobTheBuilderApp extends AbstractHandler {

  private static final String BOB_THE_BUILDER = "BobTheBuilder";

  private Shell shell;

  private ICompilationUnit compilationUnit;

  private final TreeBasedBuilderDialog treeBasedBuilderDialog;

  private final CompilationUnitAnalyzer compilationUnitAnalyzer;

  private final Composer composer;

  private final NothingToDoDialog nothingToDoDialog;

  public BobTheBuilderApp() {
    Injector injector = Guice.createInjector(new AppModule());
    compilationUnitAnalyzer = injector.getInstance(CompilationUnitAnalyzerImpl.class);
    Validate.notNull(compilationUnitAnalyzer, "compilationUnitAnalyzer may not null");
    treeBasedBuilderDialog = injector.getInstance(TreeBasedBuilderDialogImpl.class);
    Validate.notNull(treeBasedBuilderDialog, "treeBasedBuilderDialog may not null");
    composer = injector.getInstance(Composer.class);
    Validate.notNull(composer, "composer may not null");
    nothingToDoDialog = injector.getInstance(NothingToDoDialogImpl.class);
    Validate.notNull(nothingToDoDialog, "nothingToDoDialog may not null");
  }

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
    DialogRequest dialogRequest = null;
    try {
      CompilationUnitAnalyzerImpl.Analyzed analyzed =
          compilationUnitAnalyzer.analyze(compilationUnit);
      dialogRequest = new DialogRequest(analyzed);
    }
    catch (Exception e) {
      bark("Could not create the request to send to the dialog" + e.getClass().getName() + " "
        + e.getMessage() + " " + StringUtils.left(ExceptionUtils.getFullStackTrace(e), 100));
    }
    if (!dialogRequest.isThereAnythingToDo()) {
      nothingToDoDialog.show();
      return null;
    }
    ComposerRequest composerRequest = treeBasedBuilderDialog.show(dialogRequest);
    try {
      composer.compose(composerRequest, dialogRequest);
    }
    catch (Exception e) {
      bark("Encountered error upon composing." + e.getMessage());
    }
    return null;
  }

  private void bark(String message) {
    MessageDialog.openInformation(shell, BOB_THE_BUILDER, message);
  }
}

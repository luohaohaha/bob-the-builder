package org.eclipselabs.bobthebuilder;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;

public class BobTheBuilderSubContractor {
  private final DialogConstructor dialogConstructor;

  private final CompilationUnitAnalyzer compilationUnitAnalyzer;

  private final Composer composer;

  private final NothingToDoDialogConstructor nothingToDoDialogConstructor;

  private final DialogRequestConstructor dialogRequestConstructor;

  @Inject
  public BobTheBuilderSubContractor(
      DialogConstructor dialogConstructor,
      CompilationUnitAnalyzer compilationUnitAnalyzer,
      Composer composer,
      NothingToDoDialogConstructor nothingToDoDialogConstructor,
      DialogRequestConstructor dialogRequestConstructor) {
    this.dialogConstructor = dialogConstructor;
    this.compilationUnitAnalyzer = compilationUnitAnalyzer;
    this.composer = composer;
    this.nothingToDoDialogConstructor = nothingToDoDialogConstructor;
    this.dialogRequestConstructor = dialogRequestConstructor;
  }

  public void work(Shell shell, ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(shell, "shell may not be null");
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    CompilationUnitAnalyzer.Analyzed analyzed = compilationUnitAnalyzer.analyze(compilationUnit);
    DialogContent dialogContent = dialogRequestConstructor.work(analyzed);
    if (!analyzed.isThereAnythingToDo()) {
      nothingToDoDialogConstructor.show(shell);
      return;
    }
    ComposerRequest composerRequest = dialogConstructor.show(dialogContent, analyzed, shell);
    composer.compose(composerRequest, dialogContent, analyzed);
    return;
  }
}

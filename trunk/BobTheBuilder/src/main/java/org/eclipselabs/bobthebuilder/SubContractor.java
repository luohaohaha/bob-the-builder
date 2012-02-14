package org.eclipselabs.bobthebuilder;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.eclipselabs.bobthebuilder.analyzer.Analyzed;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;
import org.eclipselabs.bobthebuilder.complement.MainTypeComplementProvider;
import org.eclipselabs.bobthebuilder.composer.Composer;
import org.eclipselabs.bobthebuilder.composer.ComposerRequest;
import org.eclipselabs.bobthebuilder.mapper.eclipse.MainTypeMapper;

public class SubContractor {
  private final DialogConstructor dialogConstructor;

  private final CompilationUnitAnalyzer compilationUnitAnalyzer;

  private final Composer composer;

  private final NothingToDoDialogConstructor nothingToDoDialogConstructor;

  private final DialogRequestConstructor dialogRequestConstructor;

  @Inject
  public SubContractor(
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
    Analyzed analyzed = compilationUnitAnalyzer.analyze(compilationUnit);
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

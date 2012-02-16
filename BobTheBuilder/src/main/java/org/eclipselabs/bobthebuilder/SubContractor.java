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
import org.eclipselabs.bobthebuilder.mapper.eclipse.CompilationUnitFlattener;
import org.eclipselabs.bobthebuilder.mapper.eclipse.CompilationUnitMapper;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FlattenedICompilationUnit;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.MainTypeComplement;
import org.eclipselabs.bobthebuilder.supplement.BuilderTypeSupplementProvider;

public class SubContractor {
  private final DialogConstructor dialogConstructor;

  private final CompilationUnitAnalyzer compilationUnitAnalyzer;

  private final Composer composer;

  private final NothingToDoDialogConstructor nothingToDoDialogConstructor;

  private final DialogRequestConstructor dialogRequestConstructor;
  
  private final CompilationUnitMapper compilationUnitMapper;

  private final CompilationUnitFlattener compilationUnitFlattener;
  
  private final MainTypeComplementProvider mainTypeComplementProvider;
  
  private final BuilderTypeSupplementProvider builderTypeSupplementProvider;
  
  @Inject
  public SubContractor(
      DialogConstructor dialogConstructor,
      CompilationUnitAnalyzer compilationUnitAnalyzer,
      Composer composer,
      NothingToDoDialogConstructor nothingToDoDialogConstructor,
      DialogRequestConstructor dialogRequestConstructor,
      /* new stuff */
      CompilationUnitMapper compilationUnitMapper,
      CompilationUnitFlattener compilationUnitFlattener,
      MainTypeComplementProvider mainTypeComplementProvider,
      BuilderTypeSupplementProvider builderTypeSupplementProvider) {
    this.dialogConstructor = dialogConstructor;
    this.compilationUnitAnalyzer = compilationUnitAnalyzer;
    this.composer = composer;
    this.nothingToDoDialogConstructor = nothingToDoDialogConstructor;
    this.dialogRequestConstructor = dialogRequestConstructor;
    this.compilationUnitMapper = compilationUnitMapper;
    this.compilationUnitFlattener = compilationUnitFlattener;
    this.mainTypeComplementProvider = mainTypeComplementProvider;
    this.builderTypeSupplementProvider = builderTypeSupplementProvider;
  }

  public void work(Shell shell, ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(shell, "shell may not be null");
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    JavaClassFile javaClassFile = compilationUnitMapper.map(compilationUnit);
    FlattenedICompilationUnit flattenedICompilationUnit = compilationUnitFlattener.flatten(compilationUnit);
    MainType mainType = javaClassFile.getMainType();
    MainTypeComplement mainTypeComplement = mainTypeComplementProvider.complement(mainType);
    builderTypeSupplementProvider.provideSupplement(mainType, flattenedICompilationUnit.getMainType());
    
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

package org.eclipselabs.bobthebuilder;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.eclipselabs.bobthebuilder.complement.MainTypeComplementProvider;
import org.eclipselabs.bobthebuilder.composer.Composer;
import org.eclipselabs.bobthebuilder.composer.ComposerRequest;
import org.eclipselabs.bobthebuilder.mapper.eclipse.CompilationUnitFlattener;
import org.eclipselabs.bobthebuilder.mapper.eclipse.CompilationUnitMapper;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FlattenedICompilationUnit;
import org.eclipselabs.bobthebuilder.model.BuilderTypeSupplement;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.MainTypeComplement;
import org.eclipselabs.bobthebuilder.supplement.BuilderTypeSupplementProvider;

public class SubContractor {
  private final DialogConstructor dialogConstructor;

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
      Composer composer,
      NothingToDoDialogConstructor nothingToDoDialogConstructor,
      DialogRequestConstructor dialogRequestConstructor,
      CompilationUnitMapper compilationUnitMapper,
      CompilationUnitFlattener compilationUnitFlattener,
      MainTypeComplementProvider mainTypeComplementProvider,
      BuilderTypeSupplementProvider builderTypeSupplementProvider) {
    this.dialogConstructor = dialogConstructor;
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
    FlattenedICompilationUnit flattenedICompilationUnit =
        compilationUnitFlattener.flatten(compilationUnit);
    MainType mainType = javaClassFile.getMainType();
    MainTypeComplement mainTypeComplement = mainTypeComplementProvider.complement(mainType);
    BuilderTypeSupplement builderTypeSupplement = builderTypeSupplementProvider.provideSupplement(
      mainType,
      flattenedICompilationUnit.getMainType());

    DialogContent dialogContent =
        dialogRequestConstructor.work(mainTypeComplement, builderTypeSupplement);

    if (mainTypeComplement.isEmptyComplement() && builderTypeSupplement.isEmptySupplement()) {
      nothingToDoDialogConstructor.show(shell);
      return;
    }
    ComposerRequest composerRequest =
        dialogConstructor.show(dialogContent, flattenedICompilationUnit, shell);
    composer.compose(composerRequest, dialogContent, flattenedICompilationUnit, javaClassFile);
    return;
  }
}

package org.eclipselabs.bobthebuilder;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link SubContractor}
 */
public class BobTheBuilderSubContractorTest {
  @Mock
  private DialogConstructor dialogConstructor;

  @Mock
  private Composer composer;

  @Mock
  private NothingToDoDialogConstructor nothingToDoDialog;

  private SubContractor subContractor;

  @Mock
  private DialogRequestConstructor dialogRequestConstructor;

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private Shell shell;

  @Mock
  DialogContent dialogContent;

  @Mock
  private ComposerRequest composerRequest;

  @Mock
  private CompilationUnitMapper compilationUnitMapper;

  @Mock
  private CompilationUnitFlattener compilationUnitFlattener;

  @Mock
  private MainTypeComplementProvider mainTypeComplementProvider;

  @Mock
  private BuilderTypeSupplementProvider builderTypeSupplementProvider;

  @Mock
  private JavaClassFile javaClassFile;

  @Mock
  private FlattenedICompilationUnit flattenedICompilationUnit;

  @Mock
  private IType mainType;

  @Mock
  private MainTypeComplement mainTypeComplement;

  @Mock
  private BuilderTypeSupplement builderTypeSupplement;

  @Mock
  private MainType mappedMainType;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    subContractor = new SubContractor(
        dialogConstructor,
        composer,
        nothingToDoDialog,
        dialogRequestConstructor,
        compilationUnitMapper,
        compilationUnitFlattener,
        mainTypeComplementProvider,
        builderTypeSupplementProvider);
    Mockito.when(compilationUnitMapper.map(compilationUnit)).thenReturn(javaClassFile);
    Mockito.when(compilationUnitFlattener.flatten(compilationUnit))
        .thenReturn(flattenedICompilationUnit);
    Mockito.when(flattenedICompilationUnit.getMainType()).thenReturn(mainType);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWorkNullShell() throws JavaModelException {
    subContractor.work(null, compilationUnit);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWorkNullCompilationUnit() throws JavaModelException {
    subContractor.work(shell, null);
  }

  @Test(expected = RuntimeException.class)
  public void testFlattenerThrowsException() throws Exception {
    Mockito.when(compilationUnitFlattener.flatten(compilationUnit))
        .thenThrow(new RuntimeException("BUGGER"));
    subContractor.work(shell, compilationUnit);
  }

  @Test(expected = Exception.class)
  public void testDialogConstructorThrowsException() throws Exception {
    Mockito.when(compilationUnitFlattener.flatten(compilationUnit)).thenReturn(
      flattenedICompilationUnit);
    Mockito.when(dialogRequestConstructor.work(mainTypeComplement, builderTypeSupplement))
        .thenThrow(new RuntimeException("BUGGER"));
    subContractor.work(shell, compilationUnit);
  }

  @Test
  public void testNothingToDo() throws Exception {
    Mockito.when(compilationUnitMapper.map(compilationUnit)).thenReturn(javaClassFile);
    Mockito.when(compilationUnitFlattener.flatten(compilationUnit)).thenReturn(
      flattenedICompilationUnit);
    Mockito.when(dialogRequestConstructor.work(mainTypeComplement, builderTypeSupplement))
        .thenReturn(dialogContent);
    Mockito.when(javaClassFile.getMainType()).thenReturn(mappedMainType);
    Mockito.when(mainTypeComplementProvider.complement(mappedMainType)).thenReturn(
      mainTypeComplement);
    Mockito.when(builderTypeSupplementProvider.provideSupplement(mappedMainType, mainType))
        .thenReturn(builderTypeSupplement);
    Mockito.when(mainTypeComplement.isEmptyComplement()).thenReturn(true);
    Mockito.when(builderTypeSupplement.isEmptySupplement()).thenReturn(true);
    subContractor.work(shell, compilationUnit);
    Mockito.verify(nothingToDoDialog).show(shell);
    Mockito.verifyZeroInteractions(dialogConstructor);
    Mockito.verifyZeroInteractions(composer);
  }

  @Test
  public void testSuccess() throws JavaModelException {
    Mockito.when(compilationUnitMapper.map(compilationUnit)).thenReturn(javaClassFile);
    Mockito.when(compilationUnitFlattener.flatten(compilationUnit)).thenReturn(
      flattenedICompilationUnit);
    Mockito.when(dialogRequestConstructor.work(mainTypeComplement, builderTypeSupplement))
        .thenReturn(dialogContent);
    Mockito.when(javaClassFile.getMainType()).thenReturn(mappedMainType);
    Mockito.when(mainTypeComplementProvider.complement(mappedMainType)).thenReturn(
      mainTypeComplement);
    Mockito.when(builderTypeSupplementProvider.provideSupplement(mappedMainType, mainType))
        .thenReturn(builderTypeSupplement);
    Mockito.when(mainTypeComplement.isEmptyComplement()).thenReturn(false);
    Mockito.when(dialogConstructor.show(dialogContent, flattenedICompilationUnit, shell))
        .thenReturn(composerRequest);
    subContractor.work(shell, compilationUnit);
    Mockito.verify(composer).compose(composerRequest, dialogContent, flattenedICompilationUnit);
  }
}

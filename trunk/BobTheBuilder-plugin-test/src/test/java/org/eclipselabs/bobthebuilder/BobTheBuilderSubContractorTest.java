package org.eclipselabs.bobthebuilder;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
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
  private CompilationUnitAnalyzer compilationUnitAnalyzer;

  @Mock
  private Composer composer;

  @Mock
  private NothingToDoDialogConstructor nothingToDoDialog;

  @Mock
  private Analyzed analyzed;

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
  
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    subContractor = new SubContractor(
        dialogConstructor,
        compilationUnitAnalyzer,
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
  public void testAnalyzerThrowsException() throws Exception {
    Mockito.when(compilationUnitAnalyzer.analyze(compilationUnit))
        .thenThrow(new RuntimeException("BUGGER"));
    subContractor.work(shell, compilationUnit);
  }

  @Test(expected = Exception.class)
  public void testDialogConstructorThrowsException() throws Exception {
    Mockito.when(compilationUnitAnalyzer.analyze(compilationUnit)).thenReturn(analyzed);
    Mockito.when(dialogRequestConstructor.work(analyzed))
        .thenThrow(new RuntimeException("BUGGER"));
    subContractor.work(shell, compilationUnit);
  }

  @Test
  public void testNothingToDo() throws Exception {
    Mockito.when(compilationUnitAnalyzer.analyze(compilationUnit)).thenReturn(analyzed);
    Mockito.when(dialogRequestConstructor.work(analyzed)).thenReturn(dialogContent);
    Mockito.when(analyzed.isThereAnythingToDo()).thenReturn(false);
    subContractor.work(shell, compilationUnit);
    Mockito.verify(nothingToDoDialog).show(shell);
    Mockito.verifyZeroInteractions(dialogConstructor);
    Mockito.verifyZeroInteractions(composer);
  }

  @Test
  public void testSuccess() throws JavaModelException {
    Mockito.when(compilationUnitAnalyzer.analyze(compilationUnit)).thenReturn(analyzed);
    Mockito.when(dialogRequestConstructor.work(analyzed)).thenReturn(dialogContent);
    Mockito.when(analyzed.isThereAnythingToDo()).thenReturn(true);
    Mockito.when(dialogConstructor.show(dialogContent, analyzed, shell)).thenReturn(composerRequest);
    subContractor.work(shell, compilationUnit);
    Mockito.verify(composer).compose(composerRequest, dialogContent, analyzed);
  }
}

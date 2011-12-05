package org.eclipselabs.bobthebuilder;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.Shell;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * To test {@link BobTheBuilderSubContractor}
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
  private CompilationUnitAnalyzer.Analyzed analyzed;

  private BobTheBuilderSubContractor subContractor;

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

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    subContractor = new BobTheBuilderSubContractor(
        dialogConstructor,
        compilationUnitAnalyzer,
        composer,
        nothingToDoDialog,
        dialogRequestConstructor);
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

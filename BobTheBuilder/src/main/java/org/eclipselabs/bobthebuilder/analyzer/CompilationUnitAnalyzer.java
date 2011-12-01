package org.eclipselabs.bobthebuilder.analyzer;

import org.eclipse.jdt.core.ICompilationUnit;

public interface CompilationUnitAnalyzer {

  CompilationUnitAnalyzerImpl.Analyzed analyze(final ICompilationUnit compilationUnit) throws Exception;

}

package org.eclipselabs.bobthebuilder;

import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;
import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzerImpl;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(CompilationUnitAnalyzer.class).to(CompilationUnitAnalyzerImpl.class);
    bind(TreeBasedBuilderDialog.class).to(TreeBasedBuilderDialogImpl.class);
    bind(Composer.class).to(Composer.Impl.class);
    bind(NothingToDoDialog.class).to(NothingToDoDialogImpl.class);
  }

}

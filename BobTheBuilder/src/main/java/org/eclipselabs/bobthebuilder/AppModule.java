package org.eclipselabs.bobthebuilder;

import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzerImpl;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Composer.class);
    bind(CompilationUnitAnalyzerImpl.class);
    bind(TreeBasedBuilderDialogImpl.class);
    bind(NothingToDoDialogImpl.class);
  }

}

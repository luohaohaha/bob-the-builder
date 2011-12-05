package org.eclipselabs.bobthebuilder;

import org.eclipselabs.bobthebuilder.analyzer.CompilationUnitAnalyzer;

import com.google.inject.AbstractModule;

public class AppModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Composer.class);
    bind(CompilationUnitAnalyzer.class);
    bind(DialogConstructor.class);
    bind(NothingToDoDialogConstructor.class);
    bind(DialogRequestConstructor.class);
    bind(SubContractor.class);
  }

}

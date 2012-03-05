package org.eclipselabs.bobthebuilder;

import org.eclipselabs.bobthebuilder.composer.Composer;

import com.google.inject.AbstractModule;

public class WorkerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Composer.class);
    bind(DialogConstructor.class);
    bind(NothingToDoDialogConstructor.class);
    bind(DialogRequestConstructor.class);
    bind(SubContractor.class);
  }

}

package org.eclipselabs.bobthebuilder.composer;

import com.google.inject.AbstractModule;

public class ComposerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ConstructorComposer.class);
  }

}

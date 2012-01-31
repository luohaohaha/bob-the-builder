package org.eclipselabs.bobthebuilder.supplement;

import com.google.inject.AbstractModule;

public class SupplementModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(BuilderFieldsSupplementProvider.class);
  }

}

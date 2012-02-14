package org.eclipselabs.bobthebuilder.supplement;

import org.eclipselabs.bobthebuilder.mapper.eclipse.WithMethodsSupplementProvider;

import com.google.inject.AbstractModule;

public class SupplementModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(BuilderFieldsSupplementProvider.class);
    bind(BuilderTypeSupplementProvider.class);
    bind(WithMethodsSupplementProvider.class);
  }

}

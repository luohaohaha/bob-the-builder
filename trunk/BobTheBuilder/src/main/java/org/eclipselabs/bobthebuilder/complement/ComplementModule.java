package org.eclipselabs.bobthebuilder.complement;

import com.google.inject.AbstractModule;

public class ComplementModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ComplementProvider.class);
    bind(ConstructorWithBuilderComplementProvider.class);
    bind(MainTypeComplementProvider.class);
    bind(BuilderTypeComplementProvider.class);
    bind(ConstructorWithBuilderComplementProvider.class);
    bind(BuilderFieldsComplementProvider.class);
    bind(WithMethodsComplementProvider.class);
    bind(BuildMethodComplementProvider.class);
    bind(ValidateMethodComplementProvider.class);
  }
}

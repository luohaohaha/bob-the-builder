package org.eclipselabs.bobthebuilder.complement;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilderComplement;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.MainTypeComplement;

public class MainTypeComplementProvider {

  private final ConstructorWithBuilderComplementProvider constructorWithBuilderComplementProvider;

  @Inject
  public MainTypeComplementProvider(
      ConstructorWithBuilderComplementProvider constructorWithBuilderComplementProvider) {
    this.constructorWithBuilderComplementProvider = constructorWithBuilderComplementProvider;
  }

  public MainTypeComplement complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    ConstructorWithBuilderComplement constructorWithBuilderComplement =
        constructorWithBuilderComplementProvider.complement(
          mainType);
    return new MainTypeComplement.Builder()
        .withConstructorWithBuilderComplement(constructorWithBuilderComplement)
        .build();
  }
}

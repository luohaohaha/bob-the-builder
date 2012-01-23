package org.eclipselabs.bobthebuilder.complement;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderTypeComplement;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilderComplement;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.MainTypeComplement;

public class MainTypeComplementProvider {

  private final ConstructorWithBuilderComplementProvider constructorWithBuilderComplementProvider;

  private final BuilderTypeComplementProvider builderTypeComplementProvider;

  @Inject
  public MainTypeComplementProvider(
      ConstructorWithBuilderComplementProvider constructorWithBuilderComplementProvider,
      BuilderTypeComplementProvider builderTypeComplementProvider) {
    this.constructorWithBuilderComplementProvider = constructorWithBuilderComplementProvider;
    this.builderTypeComplementProvider = builderTypeComplementProvider;
  }

  public MainTypeComplement complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    ConstructorWithBuilderComplement constructorWithBuilderComplement =
        constructorWithBuilderComplementProvider.complement(
          mainType);
    BuilderTypeComplement builderTypeComplement = builderTypeComplementProvider.complement(mainType); 
    return new MainTypeComplement.Builder()
        .withConstructorWithBuilderComplement(constructorWithBuilderComplement)
        .withBuilderTypeComplement(builderTypeComplement)
        .build();
  }
}

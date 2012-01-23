package org.eclipselabs.bobthebuilder.complement;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderTypeComplement;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.JavaClassFileComplement;
import org.eclipselabs.bobthebuilder.model.MainTypeComplement;

public class ComplementProvider {

  private final MainTypeComplementProvider mainTypeComplementProvider;

  private final BuilderTypeComplementProvider builderTypeComplementProvider;

  @Inject
  public ComplementProvider(MainTypeComplementProvider mainTypeComplementProvider,
      BuilderTypeComplementProvider builderTypeComplementProvider) {
    this.mainTypeComplementProvider = mainTypeComplementProvider;
    this.builderTypeComplementProvider = builderTypeComplementProvider;
  }

  public JavaClassFileComplement complement(JavaClassFile javaClassFile) {
    Validate.notNull(javaClassFile, "javaClassFile may not be null");
    MainTypeComplement mainTypeComplement =
        mainTypeComplementProvider.complement(javaClassFile.getMainType());
    BuilderTypeComplement builderTypeComplement =
        builderTypeComplementProvider.complement(
          javaClassFile.getMainType());
    JavaClassFileComplement javaClassFileComplement = new JavaClassFileComplement.Builder()
        .withMainTypeComplement(mainTypeComplement)
        .withBuilderTypeComplement(builderTypeComplement)
        .build();
    return javaClassFileComplement;
  }

}

package org.eclipselabs.bobthebuilder.complement;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuildMethod;
import org.eclipselabs.bobthebuilder.model.BuilderTypeComplement;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.Imports;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class BuilderTypeComplementProvider {

  private final BuilderFieldsComplementProvider builderFieldsComplementProvider;

  private final WithMethodsComplementProvider withMethodsComplementProvider;

  private final BuildMethodComplementProvider buildMethodComplementProvider;

  private final ValidateMethodComplementProvider validateMethodComplementProvider;

  @Inject
  public BuilderTypeComplementProvider(
      BuilderFieldsComplementProvider builderFieldsComplementProvider,
      WithMethodsComplementProvider withersComplementProvider,
      BuildMethodComplementProvider buildMethodComplementProvider,
      ValidateMethodComplementProvider validateMethodComplementProvider) {
    this.builderFieldsComplementProvider = builderFieldsComplementProvider;
    this.withMethodsComplementProvider = withersComplementProvider;
    this.buildMethodComplementProvider = buildMethodComplementProvider;
    this.validateMethodComplementProvider = validateMethodComplementProvider;
  }

  public BuilderTypeComplement complement(MainType mainType, Imports imports) {
    Validate.notNull(imports, "imports may not be null");
    Validate.notNull(mainType, "mainType may not be null");
    Set<Field> builderFieldsComplement =
        builderFieldsComplementProvider.complement(mainType);
    BuilderTypeComplement.Builder builderTypeComplementBuilder = new BuilderTypeComplement.Builder();
    builderTypeComplementBuilder.withBuilderFieldsComplement(builderFieldsComplement);
    Set<WithMethod> withMethodsComplement = withMethodsComplementProvider.complement(mainType);
    builderTypeComplementBuilder.withWithMethodsComplement(withMethodsComplement);
    BuildMethod buildMethodComplement = buildMethodComplementProvider.complement(mainType.getBuilderType());
    builderTypeComplementBuilder.withBuildMethodComplement(buildMethodComplement);
    ValidateMethod validateMethodComplement = validateMethodComplementProvider.complement(mainType);
    builderTypeComplementBuilder.withValidateMethodComplement(validateMethodComplement);
    return builderTypeComplementBuilder.build();
  }

}

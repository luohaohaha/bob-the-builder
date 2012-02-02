package org.eclipselabs.bobthebuilder.complement;

import java.util.Set;

import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class WithMethodsComplementProvider extends ElementsComplementProvider<WithMethod> {

  public Set<WithMethod> complement(MainType mainType) {
    return super.complement(mainType);
  }

  @Override
  protected Set<WithMethod> getExistingElements(BuilderType builderType) {
    return builderType.getWithMethods();
  }

  @Override
  protected WithMethod transform(Field field) {
    return WithMethod.getInstanceFromField(field);
  }

}

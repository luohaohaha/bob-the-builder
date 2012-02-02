package org.eclipselabs.bobthebuilder.complement;

import java.util.Set;

import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;

public class BuilderFieldsComplementProvider extends ElementsComplementProvider<Field> {

  public Set<Field> complement(MainType mainType) {
    return super.complement(mainType);
  }

  @Override
  protected Set<Field> getExistingElements(BuilderType builderType) {
    return builderType.getBuilderFields();
  }

  @Override
  protected Field transform(Field field) {
    return field;
  }

}

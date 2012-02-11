package org.eclipselabs.bobthebuilder.supplement;

import java.util.Collection;

import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;

class MappedFieldSupplementor extends FieldSupplementor<MainType, BuilderType, Field> {

  @Override
  protected Collection<Field> getMainTypeFields(MainType mainType) {
    return mainType.getFields();
  }

  @Override
  protected Collection<Field> getBuilderFields(BuilderType builderType) {
    return builderType.getBuilderFields();
  }

  @Override
  protected BuilderType getBuilderType(MainType mainType) {
    return mainType.getBuilderType();
  }

}
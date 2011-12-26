package org.eclipselabs.bobthebuilder.mapper.eclipse;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.MainType;

public class MainTypeMapper {

  private final BuilderTypeMapper builderTypeMapper;

  @Inject
  public MainTypeMapper(BuilderTypeMapper builderTypeMapper) {
    this.builderTypeMapper = builderTypeMapper;
  }

  public MainType map(IType type) throws JavaModelException {
    Validate.notNull(type, "type may not be null");
    if (!type.isClass()) {
      throw new IllegalStateException("The main type has to be a class."
        + type.getElementName());
    }
    if (type.isBinary()) {
      throw new IllegalStateException("Binary types are not supported." + type.getElementName());
    }
    MainType.Builder builder = new MainType.Builder();
    builder.withName(type.getElementName());
    BuilderType builderType = builderTypeMapper.map(type);
    builder.withBuilderType(builderType);
    return builder.build();
  }

}

package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilder;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.Imports;
import org.eclipselabs.bobthebuilder.model.MainType;

public class MainTypeMapper {

  private final BuilderTypeMapper builderTypeMapper;

  private final FieldMapper fieldMapper;

  private final ConstructorWithBuilderMapper constructorWithBuilderMapper;

  @Inject
  public MainTypeMapper(BuilderTypeMapper builderTypeMapper, FieldMapper fieldMapper,
      ConstructorWithBuilderMapper constructorWithBuilderMapper) {
    this.builderTypeMapper = builderTypeMapper;
    this.fieldMapper = fieldMapper;
    this.constructorWithBuilderMapper = constructorWithBuilderMapper;
  }

  public MainType map(IType type, Imports imports) throws JavaModelException {
    Validate.notNull(type, "type may not be null");
    Validate.notNull(imports, "imports may not be null");
    if (!type.isClass()) {
      throw new IllegalStateException("The main type has to be a class."
        + type.getElementName());
    }
    if (type.isBinary()) {
      throw new IllegalStateException("Binary types are not supported." + type.getElementName());
    }
    MainType.Builder builder = new MainType.Builder();
    builder.withName(type.getElementName());
    // TODO add check for getters and pojomatics
    Set<Field> fields = fieldMapper.map(type);
    builder.withFields(fields);
    BuilderType builderType = builderTypeMapper.map(type, imports, fields);
    builder.withBuilderType(builderType);
    ConstructorWithBuilder constructorWithBuilder = constructorWithBuilderMapper.map(type, fields);
    builder.withConstructorWithBuilder(constructorWithBuilder);
    return builder.build();
  }

}

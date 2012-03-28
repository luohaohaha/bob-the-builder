package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.BuildMethod;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.Imports;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class BuilderTypeMapper {

  private final FieldMapper builderFieldsMapper;

  private final BuildMethodMapper buildMethodMapper;

  private final WithMethodsMapper withMethodsMapper;

  private final ValidateMethodMapper validateMethodMapper;

  static final String BUILDER_CLASS_NAME = "Builder";
  
  @Inject
  public BuilderTypeMapper(FieldMapper builderFieldsMapper,
      BuildMethodMapper buildMethodMapper, WithMethodsMapper withMethodsMapper,
      ValidateMethodMapper validateMethodMapper) {
    this.builderFieldsMapper = builderFieldsMapper;
    this.buildMethodMapper = buildMethodMapper;
    this.withMethodsMapper = withMethodsMapper;
    this.validateMethodMapper = validateMethodMapper;
  }

  public BuilderType map(IType type, Imports imports, Set<Field> fields) throws JavaModelException {
    Validate.notNull(type, "type may not be null");
    Validate.notNull(imports, "imports may not be null");
    IType builderType = findBuilderType(type);
    if (builderType == null) {
      return null;
    }
    BuilderType.Builder builder = new BuilderType.Builder();
    Set<Field> builderFields = builderFieldsMapper.map(builderType);
    builder.withBuilderFields(builderFields);
    BuildMethod buildMethod = buildMethodMapper.map(builderType);
    builder.withBuildMethod(buildMethod);
    Set<WithMethod> withMethods = withMethodsMapper.map(builderType);
    builder.withWithMethods(withMethods);
    ValidateMethod validateMethod = validateMethodMapper.map(builderType, imports, fields);
    builder.withValidateMethod(validateMethod);
    return builder.build();

  }

  public IType findBuilderType(IType type) throws JavaModelException {
    Validate.notNull(type, "type may not be null");
    IType builderType = null;
    for (IType each : type.getTypes()) {
      if (each.getElementName().equals(BUILDER_CLASS_NAME)) {
        builderType = each;
        continue;
      }
    }
    return builderType;
  }

}

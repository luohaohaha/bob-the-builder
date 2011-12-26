package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.BuilderTypeAnalyzer;
import org.eclipselabs.bobthebuilder.model.BuildMethod;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class BuilderTypeMapper {

  private final BuilderFieldsMapper builderFieldsMapper;

  private final BuildMethodMapper buildMethodMapper;

  private final WithMethodsMapper withMethodsMapper;

  private ValidateMethodMapper validateMethodMapper;

  @Inject
  public BuilderTypeMapper(BuilderFieldsMapper builderFieldsMapper,
      BuildMethodMapper buildMethodMapper, WithMethodsMapper withMethodsMapper) {
    this.builderFieldsMapper = builderFieldsMapper;
    this.buildMethodMapper = buildMethodMapper;
    this.withMethodsMapper = withMethodsMapper;
  }

  public BuilderType map(IType type) throws JavaModelException {
    Validate.notNull(type, "type may not be null");
    IType builderType = null;
    for (IType each : type.getTypes()) {
      if (each.getElementName().equals(BuilderTypeAnalyzer.BUILDER_CLASS_NAME)) {
        builderType = each;
        continue;
      }
    }
    if (builderType == null) {
      return null;
    }
    BuilderType.Builder builder = new BuilderType.Builder();
    Set<Field> builderFields = builderFieldsMapper.map(builderType);
    builder.withBuilderFields(builderFields);
    BuildMethod buildMethod = buildMethodMapper.map(builderType);
    builder.withBuildMethod(buildMethod);
    Set<WithMethod> WithMethods = withMethodsMapper.map(builderType);
    builder.withWithMethods(WithMethods);
    ValidateMethod validateMethod = validateMethodMapper.map(builderType);
    return builder.build();

  }

}

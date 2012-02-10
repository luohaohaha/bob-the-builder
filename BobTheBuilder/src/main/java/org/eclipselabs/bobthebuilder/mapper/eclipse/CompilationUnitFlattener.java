package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.supplement.BuilderFieldsSupplementProvider;

public class CompilationUnitFlattener {

  private final MainTypeSelector mainTypeSelector;

  private final ConstructorWithBuilderMapper constructorWithBuilderMapper;

  private final BuilderTypeMapper builderTypeMapper;

  private final ValidateMethodMapper validateMethodMapper;

  private final BuildMethodMapper buildMethodMapper;

  private final BuilderFieldsSupplementProvider builderFieldsSupplementProvider;

  private final WithMethodsMapper withMethodsMapper;

  @Inject
  public CompilationUnitFlattener(MainTypeSelector mainTypeSelector,
      ConstructorWithBuilderMapper constructorWithBuilderMapper,
      BuilderTypeMapper builderTypeMapper,
      ValidateMethodMapper validateMethodMapper,
      BuildMethodMapper buildMethodMapper,
      BuilderFieldsSupplementProvider builderFieldsSupplementProvider,
      WithMethodsMapper withMethodsMapper) {
    this.mainTypeSelector = mainTypeSelector;
    this.constructorWithBuilderMapper = constructorWithBuilderMapper;
    this.builderTypeMapper = builderTypeMapper;
    this.validateMethodMapper = validateMethodMapper;
    this.buildMethodMapper = buildMethodMapper;
    this.builderFieldsSupplementProvider = builderFieldsSupplementProvider;
    this.withMethodsMapper = withMethodsMapper;
  }

  public FlattenedICompilationUnit flatten(ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    FlattenedICompilationUnit.Builder result = new FlattenedICompilationUnit.Builder();
    result.withCompilationUnit(compilationUnit);
    IType type = mainTypeSelector.map(compilationUnit);
    result.withMainType(type);
    IMethod constructorWithBuilder = constructorWithBuilderMapper.findConstructorWithBuilder(type);
    result.withConstructorWithBuilder(constructorWithBuilder);
    IType builderType = builderTypeMapper.findBuilderType(type);
    if (builderType == null) {
      return result.build();
    }
    result.withBuilderType(builderType);
    IMethod validateMethod = validateMethodMapper.findValidateMethod(builderType);
    result.withValidateMethod(validateMethod);
    IMethod buildMethod = buildMethodMapper.findBuildMethod(builderType);
    result.withBuildMethod(buildMethod);
    Collection<IField> extraBuilderFields = builderFieldsSupplementProvider.supplement(type);
    Set<IField> extraBuilderFieldsInSet = new HashSet<IField>();
    extraBuilderFieldsInSet.addAll(extraBuilderFields);
    if (extraBuilderFieldsInSet.isEmpty()) {
      return result.build(); //If there are no extra fields there should not be extra withMethods
    }
    result.withExtraFields(extraBuilderFieldsInSet);
    Set<IMethod> extraWithMethods = withMethodsMapper.findExtraWithMethods(builderType);
    Set<IMethod> extraWithMethodsInSet = new HashSet<IMethod>();
    extraWithMethodsInSet.addAll(extraWithMethods);
    result.withExtraWithMethods(extraWithMethodsInSet);
    return result.build();
  }

}

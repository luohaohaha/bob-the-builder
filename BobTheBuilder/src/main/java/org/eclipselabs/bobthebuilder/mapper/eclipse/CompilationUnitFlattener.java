package org.eclipselabs.bobthebuilder.mapper.eclipse;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class CompilationUnitFlattener {

  private final MainTypeSelector mainTypeSelector;

  private final ConstructorWithBuilderMapper constructorWithBuilderMapper;

  private final BuilderTypeMapper builderTypeMapper;

  private final ValidateMethodMapper validateMethodMapper;

  private final BuildMethodMapper buildMethodMapper;
  
  @Inject
  public CompilationUnitFlattener(MainTypeSelector mainTypeSelector,
      ConstructorWithBuilderMapper constructorWithBuilderMapper,
      BuilderTypeMapper builderTypeMapper, ValidateMethodMapper validateMethodMapper,
      BuildMethodMapper buildMethodMapper) {
    this.mainTypeSelector = mainTypeSelector;
    this.constructorWithBuilderMapper = constructorWithBuilderMapper;
    this.builderTypeMapper = builderTypeMapper;
    this.validateMethodMapper = validateMethodMapper;
    this.buildMethodMapper = buildMethodMapper;
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
    return result.build();
  }
}

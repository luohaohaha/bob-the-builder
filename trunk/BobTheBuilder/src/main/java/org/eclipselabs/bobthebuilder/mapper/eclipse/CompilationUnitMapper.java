package org.eclipselabs.bobthebuilder.mapper.eclipse;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.MainType;

public class CompilationUnitMapper {
  
  private final MainTypeMapper mainTypeMapper;
  private final MainTypeSelector mainTypeSelector;
  
  @Inject
  public CompilationUnitMapper(MainTypeMapper mainTypeMapper, MainTypeSelector mainTypeSelector) {
    this.mainTypeMapper = mainTypeMapper;
    this.mainTypeSelector = mainTypeSelector;
    
  }
  
  public JavaClassFile map(ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    JavaClassFile.Builder javaClassFileBuilder = new JavaClassFile.Builder();
    IType type = mainTypeSelector.map(compilationUnit);
    MainType mainType = mainTypeMapper.map(type);
    javaClassFileBuilder.withMainType(mainType);
    
    return javaClassFileBuilder.build();
  }
  
}

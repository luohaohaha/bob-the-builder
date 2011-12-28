package org.eclipselabs.bobthebuilder.mapper.eclipse;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.ImportStatement;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.MainType;
import java.util.Set;

public class CompilationUnitMapper {

  private final MainTypeMapper mainTypeMapper;

  private final MainTypeSelector mainTypeSelector;

  private final ImportStatementMapper importStatementMapper;

  @Inject
  public CompilationUnitMapper(MainTypeMapper mainTypeMapper, MainTypeSelector mainTypeSelector,
      ImportStatementMapper importStatementMapper) {
    this.mainTypeMapper = mainTypeMapper;
    this.mainTypeSelector = mainTypeSelector;
    this.importStatementMapper = importStatementMapper;
  }

  public JavaClassFile map(ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    JavaClassFile.Builder javaClassFileBuilder = new JavaClassFile.Builder();
    IType type = mainTypeSelector.map(compilationUnit);
    MainType mainType = mainTypeMapper.map(type, compilationUnit);
    javaClassFileBuilder.withMainType(mainType).withName(type.getElementName());
    Set<ImportStatement> imports = importStatementMapper.map(compilationUnit);
    javaClassFileBuilder.withImports(imports);
    return javaClassFileBuilder.build();
  }

}

package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.ImportStatement;
import org.eclipselabs.bobthebuilder.model.Imports;

public class ImportStatementMapper {

  public Imports map(ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    Set<ImportStatement> imports = new HashSet<ImportStatement>();
    for (IImportDeclaration each : compilationUnit.getImports()) {
      imports.add(new ImportStatement(each.getElementName()));
    }
    return new Imports(imports);
  }

}

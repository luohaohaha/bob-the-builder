package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.CompilationUnitAnalyzer.Analyzed;

public class ConstructorWithBuilderInMainTypeAnalyzer {

  private final AnalyzerResult.Type builderTypeAnalyzerResult;
  private final IType mainType;

  ConstructorWithBuilderInMainTypeAnalyzer(
      AnalyzerResult.Type builderTypeAnalyzerResult, IType mainType) {
    Validate.notNull(builderTypeAnalyzerResult, "builder type analyzer result may not be null");
    this.builderTypeAnalyzerResult = builderTypeAnalyzerResult;
    Validate.notNull(mainType, "main type may not be null");
    this.mainType = mainType;
  }
  
  AnalyzerResult.Method analyze() throws JavaModelException {
  if (!builderTypeAnalyzerResult.isPresent()) {
    return new AnalyzerResult.Method(false, null);
  }
  for (IMethod each : mainType.getMethods()) {
    if (each.isConstructor()) {
      if (each.getSignature().equals(Analyzed.CONSTRUCTOR_WITH_BUILDER_SIGNATURE)) {
        return new AnalyzerResult.Method(true, each);
      }
    }
  }
  return new AnalyzerResult.Method(false, null);
}
  
}

package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.IMethod;
import org.eclipselabs.bobthebuilder.handlers.analyzer.CompilationUnitAnalyzer.Analyzed;

public interface MethodPredicate {
  boolean match(IMethod method);

  public static class ValidateInBuilder implements MethodPredicate {

    @Override
    public boolean match(IMethod method) {
      return method.getElementName().equals(Analyzed.VALIDATE_METHOD_NAME) &&
        method.getParameterTypes().length == 0;
    }

  }
  
  public static class BuildInBuilder implements MethodPredicate {

    @Override
    public boolean match(IMethod method) {
      return method.getElementName().equals(Analyzed.BUILD_METHOD_NAME);
    }
    
  }
}

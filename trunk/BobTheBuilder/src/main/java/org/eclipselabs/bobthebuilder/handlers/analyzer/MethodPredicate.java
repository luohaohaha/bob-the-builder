package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.CompilationUnitAnalyzer.Analyzed;

public interface MethodPredicate {
  boolean match(IMethod method) throws JavaModelException;

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

  public static class ConstructorWithBuilder implements MethodPredicate {
    public final static String CONSTRUCTOR_WITH_BUILDER_SIGNATURE = "(QBuilder;)V";

    @Override
    public boolean match(IMethod method) throws JavaModelException {
      return method.isConstructor() &&
        method.getSignature().equals(CONSTRUCTOR_WITH_BUILDER_SIGNATURE);
    }
  }
}

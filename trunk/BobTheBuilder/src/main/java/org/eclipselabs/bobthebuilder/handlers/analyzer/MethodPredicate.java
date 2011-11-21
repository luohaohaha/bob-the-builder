package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public interface MethodPredicate {
  boolean match(IMethod method) throws JavaModelException;

  public static class ValidateInBuilder implements MethodPredicate {

    static final String VALIDATE_METHOD_NAME = "validate";
    
    @Override
    public boolean match(IMethod method) {
      return method.getElementName().equals(VALIDATE_METHOD_NAME) &&
        method.getParameterTypes().length == 0;
    }

  }

  public static class BuildInBuilder implements MethodPredicate {

    static final String BUILD_METHOD_NAME = "build";
    
    @Override
    public boolean match(IMethod method) {
      return method.getElementName().equals(BUILD_METHOD_NAME);
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

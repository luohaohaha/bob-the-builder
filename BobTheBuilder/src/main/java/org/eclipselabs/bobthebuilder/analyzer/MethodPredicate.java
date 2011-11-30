package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public interface MethodPredicate {
  boolean match(IMethod method) throws JavaModelException;

  public static class ValidateInBuilder implements MethodPredicate {

    static final String VALIDATE_METHOD_NAME = "validate";

    @Override
    public boolean match(IMethod method) {
      Validate.notNull(method, "method may not be null");
      return method.getElementName().equals(VALIDATE_METHOD_NAME) &&
        method.getParameterTypes().length == 0;
    }

  }

  public static class BuildInBuilder implements MethodPredicate {

    static final String BUILD_METHOD_NAME = "build";

    @Override
    public boolean match(IMethod method) {
      Validate.notNull(method, "method may not be null");
      return method.getElementName().equals(BUILD_METHOD_NAME);
    }

  }

  public static class ConstructorWithBuilder implements MethodPredicate {
    public final static String CONSTRUCTOR_WITH_BUILDER_SIGNATURE = "(QBuilder;)V";

    @Override
    public boolean match(IMethod method) throws JavaModelException {
      Validate.notNull(method, "method may not be null");
      return method.isConstructor() &&
        method.getSignature().equals(CONSTRUCTOR_WITH_BUILDER_SIGNATURE);
    }
  }

  public static class WithMethodInBuilder implements MethodPredicate {

    private final IField field;

    public WithMethodInBuilder(IField field) {
      Validate.notNull(field, "field may not be null");
      this.field = field;
    }

    @Override
    public boolean match(IMethod method) throws JavaModelException {
      Validate.notNull(method, "method may not be null");
      return method.getElementName()
          .equals("with" + StringUtils.capitalize(field.getElementName())) &&
        method.getParameterTypes()[0].equals(field.getTypeSignature());
    }

  }
}

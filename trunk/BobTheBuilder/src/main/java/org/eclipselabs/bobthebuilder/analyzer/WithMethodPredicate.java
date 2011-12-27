package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class WithMethodPredicate {
  public boolean match(IField field, IMethod method) throws JavaModelException {
    Validate.notNull(field, "field may not be null");
    Validate.notNull(method, "method may not be null");
    return method.getElementName()
        .equals("with" + StringUtils.capitalize(field.getElementName())) &&
      method.getParameterTypes()[0].equals(field.getTypeSignature());

  }

}

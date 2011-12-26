package org.eclipselabs.bobthebuilder.mapper.eclipse;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class ValidateMethodInvocationMapper {

  public boolean map(IMethod buildMethod) throws JavaModelException {
    Validate.notNull(buildMethod, "buildMethod may not be null");
    String source = buildMethod.getSource();
    return source.contains("validate();");
  }

}

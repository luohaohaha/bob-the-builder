package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class WithMethodsMapper {

  public Set<WithMethod> map(IType builderType) throws JavaModelException {
    Validate.notNull(builderType, "builderType may not be null");
    Set<WithMethod> withMethods = new HashSet<WithMethod>();
    for (IField eachField : builderType.getFields()) {
      MethodPredicate predicate = getPredicate(eachField);
      for (IMethod eachMethod : builderType.getMethods()) {
        if (predicate.match(eachMethod)) {
          withMethods.add(new WithMethod.Builder().withName(eachMethod.getElementName()).build());
        }
      }
    }
    return null;
  }

  private MethodPredicate getPredicate(IField field) {
    return new MethodPredicate.WithMethodInBuilder(field);
  }
}

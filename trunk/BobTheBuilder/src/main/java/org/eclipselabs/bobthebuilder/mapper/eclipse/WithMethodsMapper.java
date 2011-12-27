package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.WithMethodPredicate;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class WithMethodsMapper {

  private final WithMethodPredicate withMethodPredicate;

  @Inject
  public WithMethodsMapper(WithMethodPredicate withMethodPredicate) {
    this.withMethodPredicate = withMethodPredicate;
  }

  public Set<WithMethod> map(IType builderType) throws JavaModelException {
    Validate.notNull(builderType, "builderType may not be null");
    Set<WithMethod> withMethods = new HashSet<WithMethod>();
    for (IField eachField : builderType.getFields()) {
      for (IMethod eachMethod : builderType.getMethods()) {
        if (withMethodPredicate.match(eachField, eachMethod)) {
          withMethods.add(new WithMethod.Builder().withName(eachMethod.getElementName()).build());
        }
      }
    }
    return Collections.unmodifiableSet(withMethods);
  }

}

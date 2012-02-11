package org.eclipselabs.bobthebuilder.supplement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.JavaModelException;

abstract class FieldSupplementor<M, B, F> {
  Collection<F> findSupplement(M mainType) throws JavaModelException {
    Validate.notNull(mainType, "mainType may not be null");
    B builderType = getBuilderType(mainType);
    if (builderType == null || getBuilderFields(builderType).isEmpty()) {
      return Collections.<F> emptySet();
    }
    else {
      Collection<F> builderFields = getBuilderFields(builderType);
      Collection<F> copyOfBuilderFields = new HashSet<F>();
      copyOfBuilderFields.addAll(builderFields);
      copyOfBuilderFields.removeAll(getMainTypeFields(mainType));
      return Collections.unmodifiableCollection(copyOfBuilderFields);
    }
  }

  protected abstract Collection<F> getMainTypeFields(M mainType) throws JavaModelException;

  protected abstract Collection<F> getBuilderFields(B builderType) throws JavaModelException;

  protected abstract B getBuilderType(M mainType) throws JavaModelException;
}
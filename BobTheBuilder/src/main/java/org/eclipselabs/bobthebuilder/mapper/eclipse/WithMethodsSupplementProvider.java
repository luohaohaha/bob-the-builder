package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collection;
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
import org.eclipselabs.bobthebuilder.supplement.BuilderFieldsSupplementProvider;

public class WithMethodsSupplementProvider {

  private final BuilderTypeMapper builderTypeMapper;

  private final WithMethodPredicate withMethodPredicate;

  private final BuilderFieldsSupplementProvider builderFieldsSupplementProvider;

  @Inject
  public WithMethodsSupplementProvider(
      BuilderTypeMapper builderTypeMapper,
      WithMethodPredicate withMethodPredicate,
      BuilderFieldsSupplementProvider builderFieldsSupplementProvider) {
    this.builderTypeMapper = builderTypeMapper;
    this.withMethodPredicate = withMethodPredicate;
    this.builderFieldsSupplementProvider = builderFieldsSupplementProvider;
  }

  public Set<IMethod> findExtra(IType mainType) throws JavaModelException {
    Validate.notNull(mainType, "mainType may not be null");
    Set<IMethod> extraWithMethods = new HashSet<IMethod>();
    Collection<IField> extraBuilderFields =
        builderFieldsSupplementProvider.supplement(mainType);
    if (extraBuilderFields.isEmpty()) {
      return extraWithMethods;
    }
    IType builderType = builderTypeMapper.findBuilderType(mainType);
    for (IMethod eachMethod : builderType.getMethods()) {
      for (IField eachExtraField : extraBuilderFields) {
        if (withMethodPredicate.match(eachExtraField, eachMethod)) {
          extraWithMethods.add(eachMethod);
        }
      }
    }
    return extraWithMethods;
  }
  
  public Set<WithMethod> findMappedExtra(IType mainType) throws JavaModelException {
    Set<IMethod> extraRawWithMethods = findExtra(mainType);
    Set<WithMethod> extraWithMethods = new HashSet<WithMethod>();
    if (extraRawWithMethods.isEmpty()) {
      return Collections.unmodifiableSet(extraWithMethods);
    }
    for (IMethod eachRawMethod : extraRawWithMethods) {
      extraWithMethods.add(new WithMethod.Builder().withName(eachRawMethod.getElementName()).build());
    }
    return Collections.unmodifiableSet(extraWithMethods);
  }
}

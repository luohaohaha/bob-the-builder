package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.WithMethodPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
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

  public Map<IField, IMethod> findExtra(IType mainType) throws JavaModelException {
    Validate.notNull(mainType, "mainType may not be null");
    Map<IField, IMethod> extraWithMethods = new HashMap<IField, IMethod>();
    Collection<IField> extraBuilderFields =
        builderFieldsSupplementProvider.supplement(mainType);
    if (extraBuilderFields.isEmpty()) {
      return extraWithMethods;
    }
    IType builderType = builderTypeMapper.findBuilderType(mainType);
    for (IMethod eachMethod : builderType.getMethods()) {
      for (IField eachExtraField : extraBuilderFields) {
        if (withMethodPredicate.match(eachExtraField, eachMethod)) {
          extraWithMethods.put(eachExtraField, eachMethod);
        }
      }
    }
    return extraWithMethods;
  }

  public Set<WithMethod> findMappedExtra(IType mainType) throws JavaModelException {
    Map<IField, IMethod> extraRawWithMethods = findExtra(mainType);
    Set<WithMethod> extraWithMethods = new HashSet<WithMethod>();
    if (extraRawWithMethods.isEmpty()) {
      return Collections.unmodifiableSet(extraWithMethods);
    }
    for (Map.Entry<IField, IMethod> eachEntry : extraRawWithMethods.entrySet()) {
      IField eachField = eachEntry.getKey();
      extraWithMethods.add(
          new WithMethod.Builder()
              .withName(eachEntry.getValue().getElementName())
              .withField(
                new Field.Builder()
                    .withName(eachField.getElementName())
                    .withSignature(eachField.getTypeSignature())
                    .withPosition(eachField.getSourceRange().getOffset())
                    .build())
              .build());
    }
    return Collections.unmodifiableSet(extraWithMethods);
  }
}

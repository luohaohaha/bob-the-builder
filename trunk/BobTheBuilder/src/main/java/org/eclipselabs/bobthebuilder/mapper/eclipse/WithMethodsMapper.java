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

public class WithMethodsMapper {

  private final WithMethodPredicate withMethodPredicate;

  private final FieldMapper fieldMapper;

  private final BuilderTypeMapper builderTypeMapper;

  @Inject
  public WithMethodsMapper(
      WithMethodPredicate withMethodPredicate,
      FieldMapper fieldMapper,
      BuilderTypeMapper builderTypeMapper) {
    this.withMethodPredicate = withMethodPredicate;
    this.fieldMapper = fieldMapper;
    this.builderTypeMapper = builderTypeMapper;
  }

  public Set<WithMethod> map(IType builderType) throws JavaModelException {
    return new MappedWithMethodsCollector(fieldMapper, withMethodPredicate).collect(builderType);
  }

  public Set<IMethod> findWithMethods(IType builderType) throws JavaModelException {
    return new RawWithMethodsCollector(fieldMapper, withMethodPredicate, builderTypeMapper)
        .collect(builderType);
  }

  public Set<IMethod> findExtraWithMethods(IType builderType) throws JavaModelException {
    return new RawWithMethodsCollector(fieldMapper, withMethodPredicate, builderTypeMapper)
        .findExtra(builderType);
  }

  static class RawWithMethodsCollector extends WithMethodsCollector<IMethod> {

    private final BuilderTypeMapper builderTypeMapper;

    public RawWithMethodsCollector(FieldMapper fieldMapper,
        WithMethodPredicate withMethodPredicate, BuilderTypeMapper buildTMapper) {
      super(fieldMapper, withMethodPredicate);
      this.builderTypeMapper = buildTMapper;
    }

    public Set<IMethod> findExtra(IType builderType) throws JavaModelException {
      Validate.notNull(builderType, "builderType may not be null");
      Set<IMethod> extraWithMethods = new HashSet<IMethod>();
      Collection<IField> extraBuilderFields =
          new BuilderFieldsSupplementProvider(builderTypeMapper, super.fieldMapper)
              .supplement(builderType);
      if (extraBuilderFields.isEmpty()) {
        return extraWithMethods;
      }
      for (IMethod eachMethod : builderType.getMethods()) {
        for (IField eachExtraField : extraBuilderFields) {
          if (super.withMethodPredicate.match(eachExtraField, eachMethod)) {
            extraWithMethods.add(eachMethod);
          }
        }
      }
      return extraWithMethods;
    }

    @Override
    protected IMethod transform(IField eachField, IMethod eachMethod) {
      return eachMethod;
    }

  }

  static class MappedWithMethodsCollector extends WithMethodsCollector<WithMethod> {

    public MappedWithMethodsCollector(FieldMapper fieldMapper,
        WithMethodPredicate withMethodPredicate) {
      super(fieldMapper, withMethodPredicate);
    }

    @Override
    protected WithMethod transform(IField eachField, IMethod eachMethod) {
      return new WithMethod.Builder().withName(eachMethod.getElementName()).build();
    }

  }

  static abstract class WithMethodsCollector<T> {

    private final FieldMapper fieldMapper;

    private final WithMethodPredicate withMethodPredicate;

    public WithMethodsCollector(FieldMapper fieldMapper, WithMethodPredicate withMethodPredicate) {
      this.fieldMapper = fieldMapper;
      this.withMethodPredicate = withMethodPredicate;
    }

    Set<T> collect(IType builderType) throws JavaModelException {
      Validate.notNull(builderType, "builderType may not be null");
      Set<T> withMethods = new HashSet<T>();
      for (IField eachField : fieldMapper.findFields(builderType)) {
        for (IMethod eachMethod : builderType.getMethods()) {
          if (withMethodPredicate.match(eachField, eachMethod)) {
            withMethods.add(transform(eachField, eachMethod));
          }
        }
      }
      return Collections.unmodifiableSet(withMethods);
    }

    protected abstract T transform(IField eachField, IMethod eachMethod);
  }
}

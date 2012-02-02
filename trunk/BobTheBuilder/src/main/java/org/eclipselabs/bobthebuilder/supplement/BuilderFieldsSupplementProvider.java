package org.eclipselabs.bobthebuilder.supplement;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.mapper.eclipse.BuilderTypeMapper;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FieldMapper;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;

public class BuilderFieldsSupplementProvider {
  private final BuilderTypeMapper builderTypeMapper;

  private final FieldMapper fieldMapper;

  @Inject
  public BuilderFieldsSupplementProvider(BuilderTypeMapper builderTypeMapper,
      FieldMapper fieldMapper) {
    this.builderTypeMapper = builderTypeMapper;
    this.fieldMapper = fieldMapper;
  }

  public Collection<Field> supplement(MainType mainType) throws JavaModelException {
    return new MappedFieldSupplementor().findSupplement(mainType);
  }

  public Collection<IField> supplement(IType mainType) throws JavaModelException {
    return new RawFieldSupplementor(builderTypeMapper, fieldMapper).findSupplement(mainType);
  }

  static class MappedFieldSupplementor extends FieldSupplementor<MainType, BuilderType, Field> {

    @Override
    protected Collection<Field> getMainTypeFields(MainType mainType) {
      return mainType.getFields();
    }

    @Override
    protected Collection<Field> getBuilderFields(BuilderType builderType) {
      return builderType.getBuilderFields();
    }

    @Override
    protected BuilderType getBuilderType(MainType mainType) {
      return mainType.getBuilderType();
    }

  }

  static class RawFieldSupplementor extends FieldSupplementor<IType, IType, IField> {

    private final BuilderTypeMapper builderTypeMapper;

    private final FieldMapper fieldMapper;

    public RawFieldSupplementor(BuilderTypeMapper builderTypeMapper, FieldMapper fieldMapper) {
      this.builderTypeMapper = builderTypeMapper;
      this.fieldMapper = fieldMapper;
    }

    @Override
    protected Collection<IField> getMainTypeFields(IType mainType) throws JavaModelException {
      return fieldMapper.findFields(mainType);
    }

    @Override
    protected Collection<IField> getBuilderFields(IType builderType) throws JavaModelException {
      return fieldMapper.findFields(builderType);
    }

    @Override
    protected IType getBuilderType(IType mainType) throws JavaModelException {
      IType builderType = builderTypeMapper.findBuilderType(mainType);
      return builderType;
    }

  }

  static abstract class FieldSupplementor<M, B, F> {
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

}

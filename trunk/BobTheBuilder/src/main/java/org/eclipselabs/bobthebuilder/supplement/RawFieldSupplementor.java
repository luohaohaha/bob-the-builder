package org.eclipselabs.bobthebuilder.supplement;

import java.util.Collection;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.mapper.eclipse.BuilderTypeMapper;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FieldMapper;

class RawFieldSupplementor extends FieldSupplementor<IType, IType, IField> {

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
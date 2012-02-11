package org.eclipselabs.bobthebuilder.supplement;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.mapper.eclipse.BuilderTypeMapper;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FieldMapper;
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

}

package org.eclipselabs.bobthebuilder.supplement;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.mapper.eclipse.WithMethodsSupplementProvider;
import org.eclipselabs.bobthebuilder.model.BuilderTypeSupplement;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class BuilderTypeSupplementProvider {

  private final WithMethodsSupplementProvider withMethodsSupplementProvider;

  private final BuilderFieldsSupplementProvider builderFieldsSupplementProvider;

  @Inject
  public BuilderTypeSupplementProvider(WithMethodsSupplementProvider withMethodsSupplementProvider,
      BuilderFieldsSupplementProvider builderFieldsSupplementProvider) {
    this.withMethodsSupplementProvider = withMethodsSupplementProvider;
    this.builderFieldsSupplementProvider = builderFieldsSupplementProvider;
  }

  public BuilderTypeSupplement provideSupplement(MainType mainType, IType type) throws JavaModelException {
    Set<Field> extraFields = new HashSet<Field>();
    extraFields.addAll(builderFieldsSupplementProvider.supplement(mainType));
    Set<WithMethod> extraWithMethods = new HashSet<WithMethod>();
    extraWithMethods.addAll(withMethodsSupplementProvider.findMappedExtra(type));
    return new BuilderTypeSupplement(extraFields, extraWithMethods);
  }

}

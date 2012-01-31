package org.eclipselabs.bobthebuilder.supplement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;

public class BuilderFieldsSupplementProvider {

  public Set<Field> supplement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");

    BuilderType builderType = mainType.getBuilderType();
    if (builderType == null || builderType.getBuilderFields().isEmpty()) {
      return Collections.<Field> emptySet();
    }
    else {
      Set<Field> builderFields = builderType.getBuilderFields();
      Set<Field> copyOfBuilderFields = new HashSet<Field>();
      copyOfBuilderFields.addAll(builderFields);
      copyOfBuilderFields.removeAll(mainType.getFields());
      return Collections.unmodifiableSet(copyOfBuilderFields);
    }
  }

}

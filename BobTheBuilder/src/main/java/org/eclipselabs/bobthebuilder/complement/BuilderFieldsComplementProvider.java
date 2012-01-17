package org.eclipselabs.bobthebuilder.complement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;

public class BuilderFieldsComplementProvider {

  public Set<Field> complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    Set<Field> fields = mainType.getFields();
    
    BuilderType builderType = mainType.getBuilderType();
    if (builderType == null || builderType.getBuilderFields().isEmpty()) {
      return fields;
    }
    else {
      Set<Field> copyOfFields = new HashSet<Field>();
      copyOfFields.addAll(fields);
      copyOfFields.removeAll(builderType.getBuilderFields());
      return Collections.unmodifiableSet(copyOfFields);
    }
  }

}

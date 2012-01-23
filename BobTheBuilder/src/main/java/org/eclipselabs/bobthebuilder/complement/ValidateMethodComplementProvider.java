package org.eclipselabs.bobthebuilder.complement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.ValidateMethodComplement;

public class ValidateMethodComplementProvider {

  public ValidateMethodComplement complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    BuilderType builderType = mainType.getBuilderType();
    Set<FieldAssignment> fields = new HashSet<FieldAssignment>();
    for (Field eachField : mainType.getFields()) {
      fields.add(new FieldAssignment(eachField.getName()));
    }
    if (builderType == null || builderType.getValidateMethod() == null) {
      return new ValidateMethodComplement(Collections.unmodifiableSet(fields));
    }
    else {
      fields.removeAll(builderType.getValidateMethod().getValidatedFields());
      return new ValidateMethodComplement(Collections.unmodifiableSet(fields));
    }
  }

}

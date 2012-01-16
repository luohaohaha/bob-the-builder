package org.eclipselabs.bobthebuilder.complement;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilderComplement;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldAssignment;
import org.eclipselabs.bobthebuilder.model.MainType;

public class ConstructorWithBuilderComplementProvider {

  public ConstructorWithBuilderComplement complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    ConstructorWithBuilderComplement.Builder builder = new ConstructorWithBuilderComplement.Builder();
    builder.withName(mainType.getName());
    if (mainType.getConstructorWithBuilder() == null) {
      for (Field each : mainType.getFields()) {
        builder.addFieldAssignment(new FieldAssignment(each.getName()));
      }
    }
    else {
      for(Field eachField : mainType.getFields()) {
        boolean found = false;
        for (FieldAssignment eachAssignment : mainType.getConstructorWithBuilder().getFieldAssignment()) {
          if (eachField.getName().equals(eachAssignment.getName())) {
            found = true;
            continue;
          }
        }
        if (!found) {
          builder.addFieldAssignment(new FieldAssignment(eachField.getName()));
        }
      }
    }
    return builder.build();
  }

}

package org.eclipselabs.bobthebuilder.complement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.WithMethod;

public class WithMethodsComplementProvider {

  public Set<WithMethod> complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    Set<Field> fields = mainType.getFields();
    Set<WithMethod> allNecessaryWithMethods = new HashSet<WithMethod>();
    for (Field each : fields) {
      allNecessaryWithMethods.add(WithMethod.getInstanceFromField(each));
    }

    BuilderType builderType = mainType.getBuilderType();
    if (builderType == null || builderType.getWithMethods().isEmpty()) {
      return Collections.unmodifiableSet(allNecessaryWithMethods);
    }
    else {
      Set<WithMethod> copyOfAllWithMethods = new HashSet<WithMethod>();
      copyOfAllWithMethods.addAll(allNecessaryWithMethods);
      copyOfAllWithMethods.removeAll(builderType.getWithMethods());
      return Collections.unmodifiableSet(copyOfAllWithMethods);
    }
  }

}

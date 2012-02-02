package org.eclipselabs.bobthebuilder.complement;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;

public abstract class ElementsComplementProvider<T> {
  public Set<T> complement(MainType mainType) {
    Validate.notNull(mainType, "mainType may not be null");
    Set<Field> fields = mainType.getFields();
    Set<T> allNecessaryElements = new HashSet<T>();
    for (Field each : fields) {
      allNecessaryElements.add(transform(each));
    }
    BuilderType builderType = mainType.getBuilderType();
    if (builderType == null || getExistingElements(builderType).isEmpty()) {
      return Collections.unmodifiableSet(allNecessaryElements);
    }
    else {
      Set<T> copyOfAllNecessaryElements = new HashSet<T>();
      copyOfAllNecessaryElements.addAll(allNecessaryElements);
      copyOfAllNecessaryElements.removeAll(getExistingElements(builderType));
      return Collections.unmodifiableSet(copyOfAllNecessaryElements);
    }
  }

  protected abstract Set<T> getExistingElements(BuilderType builderType);

  protected abstract T transform(Field field);
}

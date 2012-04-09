package org.eclipselabs.bobthebuilder.model;

import java.util.Comparator;

import org.apache.commons.lang.Validate;


public class FieldPositionComparator implements Comparator<Field> {

  @Override
  public int compare(Field field1, Field field2) {
    Validate.notNull(field1, "field1 may not be null");
    Validate.notNull(field2, "field2 may not be null");
    return new Integer(field1.getPosition()).compareTo(new Integer(field2.getPosition()));
  }


}

package org.eclipselabs.bobthebuilder;

enum Feature {
  MISSING_BUILDER(1),
  MISSING_CONSTRUCTOR(2),
  NO_MISSING_CONSTRUCTOR(3),
  MISSING_ASSIGNMENTS(4),
  MISSING_BUILD(5),
  NO_MISSING_BUILD(6),
  MISSING_FIELDS(7),
  MISSING_WITHS(8),
  EXTRA_FIELDS(9),
  MISSING_VALIDATE(10),
  NO_MISSING_VALIDATE(11),
  MISSING_VALIDATIONS(12) ;

  private final int ordering;

  private Feature(int ordering) {
    this.ordering = ordering;
  }

  public Integer getOrdering() {
    return ordering;
  }
}

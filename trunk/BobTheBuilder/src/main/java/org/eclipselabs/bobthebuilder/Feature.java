package org.eclipselabs.bobthebuilder;

enum Feature {
  MISSING_BUILDER(1),
  MISSING_CONSTRUCTOR(2),
  MISSING_ASSIGNMENTS(3),
  MISSING_BUILD(4),
  MISSING_FIELDS(5),
  MISSING_WITHS(6),
  EXTRA_FIELDS(7),
  MISSING_VALIDATE(8),
  MISSING_VALIDATIONS(9),
  NONE(10);

  private final int ordering;

  private Feature(int ordering) {
    this.ordering = ordering;
  }

  public Integer getOrdering() {
    return ordering;
  }
}

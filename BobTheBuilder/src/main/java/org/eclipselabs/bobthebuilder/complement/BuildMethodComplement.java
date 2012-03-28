package org.eclipselabs.bobthebuilder.complement;

public enum BuildMethodComplement {

  NOTHING_TO_DO(false),
  NEEDS_VALIDATE_INVOCATION(true),
  ENTIRE_METHOD(true);

  private final boolean validateMethodInvocation;

  private BuildMethodComplement(boolean validateMethodInvocation) {
    this.validateMethodInvocation = validateMethodInvocation;
  }

  public boolean isValidateMethodComplement() {
    return validateMethodInvocation;
  }

  public boolean isCompleteComplement() {
    return this.equals(ENTIRE_METHOD);
  }

  public boolean isEmptyComplement() {
    return this.equals(NOTHING_TO_DO);
  }
}

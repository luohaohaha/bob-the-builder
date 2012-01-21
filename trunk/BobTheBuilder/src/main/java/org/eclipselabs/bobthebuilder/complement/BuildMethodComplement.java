package org.eclipselabs.bobthebuilder.complement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//TODO this feels like an enum
public class BuildMethodComplement {

  public static final BuildMethodComplement NOTHING_TO_DO = new BuildMethodComplement(null);

  public static final BuildMethodComplement NEEDS_VALIDATE_INVOCATION = new BuildMethodComplement(false);

  public static final BuildMethodComplement ENTIRE_METHOD = new BuildMethodComplement(true);

  private final Boolean validateMethodInvocation;

  private BuildMethodComplement(Boolean validateMethodInvocation) {
    this.validateMethodInvocation = validateMethodInvocation;
  }

  public boolean isValidateMethodInvocation() {
    return validateMethodInvocation;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

}

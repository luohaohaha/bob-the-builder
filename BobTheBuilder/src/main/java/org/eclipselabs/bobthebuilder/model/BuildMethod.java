package org.eclipselabs.bobthebuilder.model;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class BuildMethod {

  private final boolean validateMethodInvocation;

  private final String source;


  private BuildMethod(Builder builder) {
    this.source = builder.source;
    this.validateMethodInvocation = builder.validateMethodInvocation;
  }

  public static class Builder {

    private String source;

    private boolean validateMethodInvocation;

    public Builder withSource(String source) {
      this.source = source;
      return this;
    }

    public Builder withValidateMethodInvocation(boolean validateMethodInvocation) {
      this.validateMethodInvocation = validateMethodInvocation;
      return this;
    }

    public BuildMethod build() {
      validate();
      return new BuildMethod(this);
    }

    private void validate() {
      Validate.isTrue(!StringUtils.isBlank(source), "source may not be blank");
    }
  }

  public boolean getValidateMethodInvocation() {
    return validateMethodInvocation;
  }

  public String getSource() {
    return source;
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

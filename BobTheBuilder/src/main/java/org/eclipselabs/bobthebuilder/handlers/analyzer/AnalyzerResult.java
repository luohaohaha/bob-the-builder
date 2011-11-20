package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class AnalyzerResult<T> {
  private final boolean present;

  private final T element;

  private AnalyzerResult(boolean present, T element) {
    Validate.isTrue(
      (present && (element != null)) || (!present && (element == null)),
      "if element is present, it may not be null. If element is not present, it should be null");
    this.present = present;
    this.element = element;
  }

  public boolean isPresent() {
    return present;
  }

  public T getElement() {
    return element;
  }

  public static class ForType extends AnalyzerResult<IType> {

    public static AnalyzerResult.ForType NOT_PRESENT = new AnalyzerResult.ForType(false, null);

    public static ForType getPresentInstance(IType type) {
      return new ForType(true, type);
    }

    private ForType(boolean present, IType element) {
      super(present, element);
    }

  }

  public static class ForMethod extends AnalyzerResult<IMethod> {
    public static AnalyzerResult.ForMethod NOT_PRESENT = new AnalyzerResult.ForMethod(false, null);

    public static ForMethod getPresentInstance(IMethod method) {
      return new ForMethod(true, method);
    }

    private ForMethod(boolean present, IMethod element) {
      super(present, element);
    }

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

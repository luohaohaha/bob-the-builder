package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class AnalyzerResult<T> {
  private final boolean present;

  private final T element;

  AnalyzerResult(boolean present, T element) {
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

  public static class Type extends AnalyzerResult<IType>{

    Type(boolean present, IType element) {
      super(present, element);
    }
    
  }
  
  public static class Method extends AnalyzerResult<IMethod> {

    Method(boolean present, IMethod element) {
      super(present, element);
    }
    
  }
}
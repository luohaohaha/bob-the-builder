package org.eclipselabs.bobthebuilder.analyzer;

import org.eclipse.jdt.core.IMethod;
import org.eclipselabs.bobthebuilder.AbstractResult;

public class MethodResult extends AbstractResult<IMethod> {
  public static MethodResult NOT_PRESENT = new MethodResult(false, null);

  public static MethodResult getPresentInstance(IMethod method) {
    return new MethodResult(true, method);
  }

  private MethodResult(boolean present, IMethod element) {
    super(present, element);
  }

}
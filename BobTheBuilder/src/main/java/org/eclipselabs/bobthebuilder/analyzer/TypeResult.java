package org.eclipselabs.bobthebuilder.analyzer;

import org.eclipse.jdt.core.IType;
import org.eclipselabs.bobthebuilder.AbstractResult;

public class TypeResult extends AbstractResult<IType> {

  public static TypeResult NOT_PRESENT = new TypeResult(false, null);

  public static TypeResult getPresentInstance(IType type) {
    return new TypeResult(true, type);
  }

  private TypeResult(boolean present, IType element) {
    super(present, element);
  }

}
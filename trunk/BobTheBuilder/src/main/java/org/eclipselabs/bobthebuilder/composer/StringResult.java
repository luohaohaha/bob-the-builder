package org.eclipselabs.bobthebuilder.composer;

import org.eclipselabs.bobthebuilder.AbstractResult;

public class StringResult extends AbstractResult<String>{

  public StringResult(boolean present, String element) {
    super(present, element);
  }

  public static StringResult NOT_PRESENT = new StringResult(false, null);
  
  public static StringResult getPresentInstance(String composed) {
    return new StringResult(true, composed);
  }
}

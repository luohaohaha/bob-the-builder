package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Comparator;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

public class RawWithMethodComparator implements Comparator<IMethod> {

  @Override
  public int compare(IMethod o1, IMethod o2) {
    Validate.notNull(o1, "o1 may not be null");
    Validate.notNull(o2, "o2 may not be null");
    try {
      return new Integer(o1.getSourceRange().getOffset()).compareTo(o2.getSourceRange().getOffset());
    }
    catch (JavaModelException e) {
      e.printStackTrace();
      return 0;
    }
  }

}

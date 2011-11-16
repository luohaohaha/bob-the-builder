package org.eclipselabs.bobthebuilder.handlers;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class FieldAnalyzer {
  private final IType type;

  FieldAnalyzer(IType type) {
    Validate.notNull(type, "Type may not be null");
    this.type = type;
  }
  
  Set<IField> analyze() throws JavaModelException {
    Set<IField> fields = new HashSet<IField>();
    for (IField each : type.getFields()) {
      if (isFinalStatic(each)) {
        continue;
      }
      fields.add(each);
    }
    return fields;
  }
  
  private boolean isFinalStatic(IField field) throws JavaModelException {
    int flags = field.getFlags();
    if (Flags.isFinal(flags) && Flags.isStatic(flags)) {
      return true;
    }
    return false;
  }
}

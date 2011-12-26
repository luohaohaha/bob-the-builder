package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

//TODO this belongs to the mapper
public class MainTypeFieldAnalyzer {

  // TODO create a particular view of the IField instead of using eclipse's IField
  // This will help the operations with sets that are necessary in the analyzer phase
  public Set<IField> analyze(IType type) throws JavaModelException {
    Validate.notNull(type, "Type may not be null");
    Set<IField> fields = new HashSet<IField>();
    for (IField each : type.getFields()) {
      if (isFinalStatic(each)) {
        continue;
      }
      fields.add(each);
    }
    return Collections.unmodifiableSet(fields);
  }

  private boolean isFinalStatic(IField field) throws JavaModelException {
    int flags = field.getFlags();
    if (Flags.isFinal(flags) && Flags.isStatic(flags)) {
      return true;
    }
    return false;
  }
}

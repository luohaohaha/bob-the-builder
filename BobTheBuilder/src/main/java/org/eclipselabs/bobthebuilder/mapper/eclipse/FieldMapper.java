package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.Field;

public class FieldMapper {

  public Set<Field> map(IType type) throws JavaModelException {
    Validate.notNull(type, "Type may not be null");
    return new MappedFieldCollector().collect(type);
  }

  private static boolean isFinalStatic(IField field) throws JavaModelException {
    int flags = field.getFlags();
    if (Flags.isFinal(flags) && Flags.isStatic(flags)) {
      return true;
    }
    return false;
  }
  
  static abstract class FieldCollector<T> {

    protected abstract T createElement(IField each) throws JavaModelException;
    
    Set<T> collect (IType typeWithFields) throws JavaModelException {
      Validate.notNull(typeWithFields, "typeWithFields may not be null");
      Set<T> result = new HashSet<T>();
      for (IField each : typeWithFields.getFields()) {
        if (isFinalStatic(each)) {
          continue;
        }
        T element = createElement(each);
        result.add(element);
      }
      return Collections.unmodifiableSet(result);
    }
  }
  
  static class MappedFieldCollector extends FieldCollector<Field> {

    @Override
    protected Field createElement(IField each) throws JavaModelException {
      return new Field.Builder()
      .withName(each.getElementName())
      .withSignature(each.getTypeSignature())
      .build();
    }
    
  }
  static class RawFieldCollector extends FieldCollector<IField> {

    @Override
    protected IField createElement(IField each) throws JavaModelException {
      return each;
    }

    
  }
}

package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.FieldPositionComparator;

public class FieldMapper {

  public Set<Field> map(IType type) throws JavaModelException {
    return new MappedFieldCollector().collect(type);
  }

  public Set<IField> findFields(IType type) throws JavaModelException {
    return new RawFieldCollector().collect(type);
  }

  private static boolean isFinalStatic(IField field) throws JavaModelException {
    int flags = field.getFlags();
    if (Flags.isFinal(flags) && Flags.isStatic(flags)) {
      return true;
    }
    return false;
  }

  static abstract class FieldCollector<T, C extends Comparator<T>> {

    protected abstract C getComparator();
    
    protected abstract T createElement(IField each) throws JavaModelException;

    Set<T> collect(IType typeWithFields) throws JavaModelException {
      Validate.notNull(typeWithFields, "typeWithFields may not be null");
      Set<T> result = new TreeSet<T>(getComparator());
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

  static class MappedFieldCollector extends FieldCollector<Field, FieldPositionComparator> {

    @Override
    protected Field createElement(IField each) throws JavaModelException {
      return new Field.Builder()
          .withName(each.getElementName())
          .withSignature(Signature.toString(each.getTypeSignature()))
          .withPosition(each.getSourceRange().getOffset())
          .build();
    }

    @Override
    protected FieldPositionComparator getComparator() {
      return new FieldPositionComparator();
    }

  }

  static class RawFieldCollector extends FieldCollector<IField, RawFieldPositionComparator> {

    @Override
    protected IField createElement(IField each) throws JavaModelException {
      return each;
    }

    @Override
    protected RawFieldPositionComparator getComparator() {
      return new RawFieldPositionComparator();
    }

  }
}

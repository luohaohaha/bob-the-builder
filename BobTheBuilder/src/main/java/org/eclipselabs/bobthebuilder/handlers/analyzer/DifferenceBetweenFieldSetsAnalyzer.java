package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

//TODO audit whether MissingInstructionsInMethodAnalyzer can be used
public class DifferenceBetweenFieldSetsAnalyzer {

  private final Set<IField> mainTypeFields = new HashSet<IField>();

  private final Set<IField> builderFields = new HashSet<IField>();

  public DifferenceBetweenFieldSetsAnalyzer(Set<IField> mainTypeFields, Set<IField> builderFields) {
    Validate.notNull(mainTypeFields, "main type fields may not be null");
    Validate.notNull(builderFields, "builder type fields may not be null");
    this.mainTypeFields.addAll(mainTypeFields);
    this.builderFields.addAll(builderFields);
  }

  public Set<IField> analyze() throws JavaModelException {
    Iterator<IField> iterator = mainTypeFields.iterator();
    while (iterator.hasNext()) {
      IField each = iterator.next();
      for (IField eachBuilderField : builderFields) {
        if (each.getElementName().equals(eachBuilderField.getElementName()) &&
            each.getTypeSignature().equals(eachBuilderField.getTypeSignature())) {
          iterator.remove();
        }
      }
    }
    return Collections.unmodifiableSet(mainTypeFields);
  }
}

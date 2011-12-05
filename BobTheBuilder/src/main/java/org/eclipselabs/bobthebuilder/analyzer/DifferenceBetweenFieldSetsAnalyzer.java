package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

// TODO audit whether MissingInstructionsInMethodAnalyzer can be used
public class DifferenceBetweenFieldSetsAnalyzer {

  public DifferenceBetweenFieldSetsAnalyzer() {}

  public Set<IField> analyze(Set<IField> mainTypeFields, Set<IField> builderFields) throws JavaModelException {
    Validate.notNull(mainTypeFields, "main type fields may not be null");
    Validate.notNull(builderFields, "builder type fields may not be null");
    Set<IField> copyOfMainTypeFields = new HashSet<IField>();
    Set<IField> copyOfBuilderFields = new HashSet<IField>();
    copyOfMainTypeFields.addAll(mainTypeFields);
    copyOfBuilderFields.addAll(builderFields);
    Iterator<IField> iterator = copyOfMainTypeFields.iterator();
    while (iterator.hasNext()) {
      IField each = iterator.next();
      for (IField eachBuilderField : copyOfBuilderFields) {
        if (each.getElementName().equals(eachBuilderField.getElementName()) &&
            each.getTypeSignature().equals(eachBuilderField.getTypeSignature())) {
          iterator.remove();
        }
      }
    }
    return Collections.unmodifiableSet(copyOfMainTypeFields);
  }
}

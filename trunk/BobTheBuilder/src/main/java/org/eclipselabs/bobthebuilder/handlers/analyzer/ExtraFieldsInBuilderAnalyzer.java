package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

public class ExtraFieldsInBuilderAnalyzer {

  private final MissingFieldsInBuilderAnalyzer missingFieldsInBuilderAnalyzer;

  public ExtraFieldsInBuilderAnalyzer(Set<IField> builderTypeFields, Set<IField> mainTypeFields) {
    Validate.notNull(mainTypeFields, "main type fields may not be null");
    Validate.notNull(builderTypeFields, "builder type fields may not be null");
    this.missingFieldsInBuilderAnalyzer =
        // flip-flop collections so that the subtraction of sets works
        new MissingFieldsInBuilderAnalyzer(builderTypeFields, mainTypeFields);
  }

  public Set<IField> analyze() throws JavaModelException {
    return missingFieldsInBuilderAnalyzer.analyze();
  }
}

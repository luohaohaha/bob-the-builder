package org.eclipselabs.bobthebuilder.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

// TODO add tests
public class WithMethodInBuilderAnalyzer {

  private MethodPredicate getPredicate(IField field) {
    return new MethodPredicate.WithMethodInBuilder(field);
  }

  public MethodResult analyze(TypeResult analyzedTypeResult, IField field) throws JavaModelException {
    Validate.notNull(field, "field may not be null");
    Validate.notNull(analyzedTypeResult, "analyzedTypeResult may not be null");
    MethodAnalyzer methodAnalyzer = new MethodAnalyzer();
    return methodAnalyzer.analyze(analyzedTypeResult, getPredicate(field));
  }
}

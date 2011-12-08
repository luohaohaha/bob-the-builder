package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.AnalyzerResult.ForType;

public class BuilderTypeFieldAnalyzer {

  public BuilderTypeFieldAnalyzer() {}

  public Set<IField> analyze(ForType analyzerResult) throws JavaModelException {
    Validate.notNull(analyzerResult, "AnalyzerResult may not be null");
    if (analyzerResult.isPresent()) {
      return new MainTypeFieldAnalyzer().analyze(analyzerResult.getElement());
    }
    else {
      return Collections.emptySet();
    }
  }
}

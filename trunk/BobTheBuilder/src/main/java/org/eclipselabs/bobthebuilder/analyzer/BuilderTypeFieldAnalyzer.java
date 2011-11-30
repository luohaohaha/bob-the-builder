package org.eclipselabs.bobthebuilder.analyzer;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

public class BuilderTypeFieldAnalyzer {

  private final AnalyzerResult.ForType  analyzedBuilderType;
  
  public BuilderTypeFieldAnalyzer(AnalyzerResult.ForType analyzedBuilder) {
    Validate.notNull(analyzedBuilder, "analyzed builder may not be null");
    this.analyzedBuilderType = analyzedBuilder;
  }
  
  public Set<IField> analyze() throws JavaModelException {
    if (analyzedBuilderType.isPresent()) {
      return new MainTypeFieldAnalyzer(analyzedBuilderType.getElement()).analyze();
    }
    else {
      return Collections.emptySet();
    }
  }
}


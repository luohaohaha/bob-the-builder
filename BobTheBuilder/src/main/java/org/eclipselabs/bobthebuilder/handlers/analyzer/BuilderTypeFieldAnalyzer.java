package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Collections;
import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class BuilderTypeFieldAnalyzer {

  private final IType builderType;
  
  private final boolean present;
  
  BuilderTypeFieldAnalyzer(boolean present, IType builderType) {
    this.builderType = builderType;
    this.present = present;
  }
  
  public BuilderTypeFieldAnalyzer(AnalyzerResult.Type result) {
    this.builderType = result.getElement();
    this.present = result.isPresent();
  }
  
  public Set<IField> analyze() throws JavaModelException {
    if (present) {
      return new MainTypeFieldAnalyzer(builderType).analyze();
    }
    else {
      return Collections.emptySet();
    }
  }
}


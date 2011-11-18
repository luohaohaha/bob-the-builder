package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.Type;

public abstract class MissingMethodAnalyzer {

  private final AnalyzerResult.Type analyzedTypeResult;

  protected abstract MethodPredicate getPredicate();

  protected MissingMethodAnalyzer(AnalyzerResult.Type analyzedTypeResult) {
    Validate.notNull(analyzedTypeResult, "analyzedTypeResult may not be null");
    this.analyzedTypeResult = analyzedTypeResult;
  }

  public AnalyzerResult.Method analyze() throws JavaModelException {
    if (!analyzedTypeResult.isPresent()) {
      return new AnalyzerResult.Method(false, null);
    }
    for (IMethod each : analyzedTypeResult.getElement().getMethods()) {
      if (getPredicate().match(each)) {
        return new AnalyzerResult.Method(true, each);
      }
    }
    return new AnalyzerResult.Method(false, null);
  }

  public static class BuildInBuilder extends MissingMethodAnalyzer {

    public BuildInBuilder(AnalyzerResult.Type analyzedBuilderTypeResult) {
      super(analyzedBuilderTypeResult);
    }

    @Override
    protected MethodPredicate getPredicate() {
      return new MethodPredicate.BuildInBuilder();
    }

  }

  public static class ValidateInBuilder extends MissingMethodAnalyzer {

    protected ValidateInBuilder(Type analyzedTypeResult) {
      super(analyzedTypeResult);
    }

    @Override
    protected MethodPredicate getPredicate() {
      return new MethodPredicate.ValidateInBuilder();
    }

  }
}

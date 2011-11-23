package org.eclipselabs.bobthebuilder.handlers.analyzer;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForType;

public abstract class MethodAnalyzer {

  private final AnalyzerResult.ForType analyzedTypeResult;

  protected abstract MethodPredicate getPredicate();

  protected MethodAnalyzer(AnalyzerResult.ForType analyzedTypeResult) {
    Validate.notNull(analyzedTypeResult, "analyzedTypeResult may not be null");
    this.analyzedTypeResult = analyzedTypeResult;
  }

  public AnalyzerResult.ForMethod analyze() throws JavaModelException {
    if (!analyzedTypeResult.isPresent()) {
      return AnalyzerResult.ForMethod.NOT_PRESENT;
    }
    for (IMethod each : analyzedTypeResult.getElement().getMethods()) {
      // TODO use ioc to add behavior
      if (getPredicate().match(each)) {
        return AnalyzerResult.ForMethod.getPresentInstance(each);
      }
    }
    return AnalyzerResult.ForMethod.NOT_PRESENT;
  }

  public static class BuildInBuilder extends MethodAnalyzer {

    public BuildInBuilder(AnalyzerResult.ForType analyzedBuilderTypeResult) {
      super(analyzedBuilderTypeResult);
    }

    @Override
    protected MethodPredicate getPredicate() {
      return new MethodPredicate.BuildInBuilder();
    }

  }

  public static class ValidateInBuilder extends MethodAnalyzer {

    protected ValidateInBuilder(ForType analyzedTypeResult) {
      super(analyzedTypeResult);
    }

    @Override
    protected MethodPredicate getPredicate() {
      return new MethodPredicate.ValidateInBuilder();
    }

  }

  public static class WithMethodInBuilder extends MethodAnalyzer {

    private final IField field;

    protected WithMethodInBuilder(ForType analyzedTypeResult, IField field) {
      super(analyzedTypeResult);
      this.field = field;
    }

    @Override
    protected MethodPredicate getPredicate() {
      return new MethodPredicate.WithMethodInBuilder(field);
    }
    
  }
  public static class ConstructorWithBuilder extends MethodAnalyzer {

    private ForType analyzedBuilderTypeResult;

    protected ConstructorWithBuilder(ForType analyzedBuilderTypeResult, IType mainType) {
      super(AnalyzerResult.ForType.getPresentInstance(mainType));
      Validate.notNull(analyzedBuilderTypeResult, "analyzedBuilderTypeResult may not be null");
      this.analyzedBuilderTypeResult = analyzedBuilderTypeResult;
    }

    // This feels like shoehorning the parent #analyze() for this case
    @Override
    public ForMethod analyze() throws JavaModelException {
      if (!analyzedBuilderTypeResult.isPresent()) {
        return AnalyzerResult.ForMethod.NOT_PRESENT;
      }
      return super.analyze();
    }

    @Override
    protected MethodPredicate getPredicate() {
      return new MethodPredicate.ConstructorWithBuilder();
    }

  }
}

package org.eclipselabs.bobthebuilder.analyzer;

import com.google.inject.AbstractModule;

public class AnalyzerModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TypeAnalyzer.class);
    bind(BuilderTypeAnalyzer.class);
    bind(BuilderTypeFieldAnalyzer.class);
    bind(MainTypeFieldAnalyzer.class);
    bind(DifferenceBetweenFieldSetsAnalyzer.class);
    bind(WithMethodsInBuilderAnalyzer.class);
    bind(ConstructorWithBuilderAnalyzer.class);
    bind(ConstructorWithBuilderInMainTypeAnalyzer.class);
    bind(BuildInBuilderAnalyzer.class);
    bind(MethodAnalyzer.class);
    bind(MethodPredicate.ValidateInBuilder.class);
    bind(MethodContentAnalyzer.class);
    bind(FieldPredicate.FieldValidation.class);
    bind(ValidationFrameworkAnalyzer.class);
  }

}

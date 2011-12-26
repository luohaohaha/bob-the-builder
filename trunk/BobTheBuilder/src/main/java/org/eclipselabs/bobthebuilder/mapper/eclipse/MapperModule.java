package org.eclipselabs.bobthebuilder.mapper.eclipse;

import org.eclipselabs.bobthebuilder.analyzer.MainTypeFieldAnalyzer;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;

import com.google.inject.AbstractModule;

public class MapperModule extends AbstractModule{

  @Override
  protected void configure() {
    bind(MainTypeMapper.class);
    bind(MainTypeSelector.class);
    bind(BuilderTypeMapper.class);
    bind(BuilderFieldsMapper.class);
    bind(MainTypeFieldAnalyzer.class);
    bind(BuildMethodMapper.class);
    bind(ValidateMethodInvocationMapper.class);
    bind(WithMethodsMapper.class);
    bind(ValidateMethodMapper.class);
    bind(ValidateFieldsMethodMapper.class);
    bind(MethodPredicate.BuildInBuilder.class);
  }

}

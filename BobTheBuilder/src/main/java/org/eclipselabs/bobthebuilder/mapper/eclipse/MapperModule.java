package org.eclipselabs.bobthebuilder.mapper.eclipse;

import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.analyzer.WithMethodPredicate;

import com.google.inject.AbstractModule;

public class MapperModule extends AbstractModule{

  @Override
  protected void configure() {
    bind(MainTypeMapper.class);
    bind(MainTypeSelector.class);
    bind(BuilderTypeMapper.class);
    bind(FieldMapper.class);
    bind(BuildMethodMapper.class);
    bind(ValidateMethodInvocationMapper.class);
    bind(WithMethodsMapper.class);
    bind(ValidateMethodMapper.class);
    bind(ValidateFieldsMethodMapper.class);
    bind(MethodPredicate.BuildInBuilder.class);
    bind(ImportStatementMapper.class);
    bind(WithMethodPredicate.class);
    bind(WithMethodsMapper.class);
    bind(MethodPredicate.ValidateInBuilder.class);
    bind(ValidationFrameworkMapper.class);
    bind(ConstructorWithBuilderMapper.class);
    bind(MethodPredicate.ConstructorWithBuilder.class);
    bind(FieldPredicate.FieldAssignment.class);
    bind(FieldAssignmentInConstructorMapper.class);
  }

}

package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;

public class ValidateMethodMapper {

  private final ValidateFieldsMethodMapper validatedFieldsMapper;

  private final MethodPredicate.ValidateInBuilder predicate;

  private final ValidationFrameworkMapper validationFrameworkMapper;

  @Inject
  public ValidateMethodMapper(ValidateFieldsMethodMapper validatedFieldsMapper,
      MethodPredicate.ValidateInBuilder validateInBuilder,
      ValidationFrameworkMapper validationFrameworkMapper) {
    this.validatedFieldsMapper = validatedFieldsMapper;
    this.predicate = validateInBuilder;
    this.validationFrameworkMapper = validationFrameworkMapper;
  }

  public ValidateMethod map(IType builderType, ICompilationUnit compilationUnit) throws JavaModelException {
    Validate.notNull(builderType, "builderType may not be null");
    Validate.notNull(compilationUnit, "compilationUnit may not be null");
    IMethod validateMethod = null;
    for (IMethod each : builderType.getMethods()) {
      if (predicate.match(each)) {
        validateMethod = each;
        continue;
      }
    }
    if (validateMethod == null) {
      return null;
    }
    ValidateMethod.Builder builder = new ValidateMethod.Builder();
    builder.withSource(validateMethod.getSource());
    Set<Field> validatedFields = validatedFieldsMapper.map(builderType);
    builder.withValidatedFields(validatedFields);
    //TODO add the validation framework at the field level and allow for more than one framework to be used
    ValidationFramework validationFramework = 
      validationFrameworkMapper.map(validateMethod, compilationUnit);
    builder.withValidationFramework(validationFramework);
    return builder.build();
  }

}

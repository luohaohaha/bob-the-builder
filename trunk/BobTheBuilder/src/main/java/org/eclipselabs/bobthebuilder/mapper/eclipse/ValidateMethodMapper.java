package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.ValidateMethod;

public class ValidateMethodMapper {

  private final ValidateFieldsMethodMapper validatedFieldsMapper;

  private final MethodPredicate.ValidateInBuilder predicate;

  @Inject
  public ValidateMethodMapper(ValidateFieldsMethodMapper validatedFieldsMapper,
      MethodPredicate.ValidateInBuilder validateInBuilder) {
    this.validatedFieldsMapper = validatedFieldsMapper;
    this.predicate = validateInBuilder;
  }

  public ValidateMethod map(IType builderType) throws JavaModelException {
    Validate.notNull(builderType, "builderType may not be null");
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
    return builder.build();
  }

}

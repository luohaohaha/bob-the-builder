package org.eclipselabs.bobthebuilder.mapper.eclipse;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.model.BuildMethod;

public class BuildMethodMapper {

  private final ValidateMethodInvocationMapper validateMethodInvocationMapper;

  private final MethodPredicate.BuildInBuilder predicate;

  @Inject
  public BuildMethodMapper(
      ValidateMethodInvocationMapper validateMethodInvocationMapper,
      MethodPredicate.BuildInBuilder methodPredicateBuildInBuilder) {
    this.validateMethodInvocationMapper = validateMethodInvocationMapper;
    this.predicate = methodPredicateBuildInBuilder;
  }

  public BuildMethod map(IType builderType) throws JavaModelException {
    Validate.notNull(builderType, "builderType may not be null");
    IMethod buildMethod = null;
    for (IMethod each : builderType.getMethods()) {
      if (predicate.match(each)) {
        buildMethod = each;
        continue;
      }
    }
    if (buildMethod == null) {
      return null;
    }
    boolean validateMethodInvocation = validateMethodInvocationMapper.map(buildMethod);
    BuildMethod.Builder builder = new BuildMethod.Builder();
    builder.withValidateMethodInvocation(validateMethodInvocation);
    builder.withSource(buildMethod.getSource());
    return builder.build();
  }

}

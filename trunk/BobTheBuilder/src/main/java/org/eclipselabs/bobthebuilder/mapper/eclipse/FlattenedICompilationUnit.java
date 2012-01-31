package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class FlattenedICompilationUnit {
  private final IType mainType;

  private final IType builderType;

  private final IMethod constructorWithBuilder;

  private final IMethod validateMethod;

  private final IMethod buildMethod;

  private final Set<IMethod> extraWithMethods;

  private final Set<IField> extraFields;

  private final ICompilationUnit compilationUnit;

  private FlattenedICompilationUnit(Builder builder) {
    this.validateMethod = builder.validateMethod;
    this.buildMethod = builder.buildMethod;
    this.compilationUnit = builder.compilationUnit;
    this.extraWithMethods = builder.extraWithMethods;
    this.extraFields = builder.extraFields;
    this.builderType = builder.builderType;
    this.constructorWithBuilder = builder.constructorWithBuilder;
    this.mainType = builder.mainType;
  }

  public static class Builder {

    public IMethod buildMethod;

    public Set<IField> extraFields = new HashSet<IField>();

    private IMethod validateMethod;

    private ICompilationUnit compilationUnit;

    private Set<IMethod> extraWithMethods = new HashSet<IMethod>();

    private IMethod constructorWithBuilder;

    private IType builderType;

    private IType mainType;

    public Builder withBuildMethod(IMethod buildMethod) {
      this.buildMethod = buildMethod;
      return this;
    }
    
    public Builder withValidateMethod(IMethod validateMethod) {
      this.validateMethod = validateMethod;
      return this;
    }

    public Builder withCompilationUnit(ICompilationUnit compilationUnit) {
      this.compilationUnit = compilationUnit;
      return this;
    }

    public Builder withExtraWithMethods(Set<IMethod> extraWithMethods) {
      this.extraWithMethods = extraWithMethods;
      return this;
    }
    
    public Builder withExtraFields(Set<IField> extraFields) {
      this.extraFields = extraFields;
      return this;
    }

    public Builder withBuilderType(IType builderType) {
      this.builderType = builderType;
      return this;
    }

    public Builder withConstructorWithBuilder(IMethod constructorWithBuilder) {
      this.constructorWithBuilder = constructorWithBuilder;
      return this;
    }

    public Builder withMainType(IType mainType) {
      this.mainType = mainType;
      return this;
    }

    public FlattenedICompilationUnit build() {
      validate();
      return new FlattenedICompilationUnit(this);
    }

    private void validate() {
      Validate.notNull(compilationUnit, "compilationUnit may not be null");
      Validate.notNull(extraWithMethods, "extraWithMethods may not be null");
      Validate.notNull(extraFields, "extraFields may not be null");
      Validate.notNull(mainType, "mainType may not be null");
    }
  }

  public IType getMainType() {
    return mainType;
  }

  public IType getBuilderType() {
    return builderType;
  }

  public IMethod getConstructorWithBuilder() {
    return constructorWithBuilder;
  }

  public IMethod getValidateMethod() {
    return validateMethod;
  }

  public Set<IMethod> getExtraWithMethods() {
    return extraWithMethods;
  }
  
  public Set<IField> getExtraFields() {
    return extraFields;
  }

  public ICompilationUnit getCompilationUnit() {
    return compilationUnit;
  }
  
  public IMethod getBuildMethod() {
    return buildMethod;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}

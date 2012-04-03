package org.eclipselabs.bobthebuilder;

import javax.inject.Inject;

import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.composer.BuilderComposer;
import org.eclipselabs.bobthebuilder.composer.ConstructorWithBuilderComposer;
import org.eclipselabs.bobthebuilder.model.Field;

//TODO add this to guice
public interface FieldTextBuilder {
  String createMessage(Field field) throws JavaModelException;
  
  public static class FieldDeclarationBuilder implements FieldTextBuilder {

    private final BuilderComposer builderComposer;
    
    @Inject
    public FieldDeclarationBuilder(BuilderComposer builderComposer) {
      this.builderComposer = builderComposer;
    }

    @Override
    public String createMessage(Field field) throws JavaModelException {
      return builderComposer.composeFieldDeclaration(field);
    }
    
  }
  
  public static class WithMethodBuilder implements FieldTextBuilder {

    private final BuilderComposer builderComposer;
    
    @Inject
    public WithMethodBuilder(BuilderComposer builderComposer) {
      this.builderComposer = builderComposer;
    }

    @Override
    public String createMessage(Field field) throws JavaModelException {
      return builderComposer.composeWithMethodFirstLine(field) + "...}";
    }
    
  }
  
  public static class FieldAssignmentBuilder implements FieldTextBuilder {

    private final ConstructorWithBuilderComposer constructorWithBuilderComposer;
    
    @Inject
    public FieldAssignmentBuilder(ConstructorWithBuilderComposer constructorWithBuilderComposer) {
      this.constructorWithBuilderComposer = constructorWithBuilderComposer;
    }
    
    @Override
    public String createMessage(Field field) throws JavaModelException {
      return constructorWithBuilderComposer.composeSingleAssignment(field);
    }
    
  }
  
  public static class ValidationBuilder implements FieldTextBuilder {

    private ValidationFramework validationFramework;

    public ValidationBuilder(ValidationFramework validationFramework) {
      this.validationFramework = validationFramework;
    }
    
    @Override
    public String createMessage(Field field) throws JavaModelException {
      return validationFramework.composeFieldValidation(field);
    }
    
  }
}

package org.eclipselabs.bobthebuilder;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.composer.Composer;
import org.eclipselabs.bobthebuilder.composer.ConstructorWithBuilderComposer;
import org.eclipselabs.bobthebuilder.model.Field;

public interface FieldTextBuilder {
  String createMessage(Field field) throws JavaModelException;
  
  public static class FieldDeclarationBuilder implements FieldTextBuilder {

    @Override
    public String createMessage(Field field) throws JavaModelException {
      return Composer.composeFieldInBuilder(field);
    }
    
  }
  
  public static class WithMethodBuilder implements FieldTextBuilder {

    @Override
    public String createMessage(Field field) throws JavaModelException {
      return Composer.composeWithMethodSignature(field) + "...}";
    }
    
  }
  
  public static class FieldAssignmentBuilder implements FieldTextBuilder {

    @Override
    public String createMessage(Field field) throws JavaModelException {
      return new ConstructorWithBuilderComposer().composeSingleAssignment(field);
    }
    
  }
  
  public static class ValidationBuilder implements FieldTextBuilder {

    private ValidationFramework validationFramework;

    ValidationBuilder(ValidationFramework validationFramework) {
      this.validationFramework = validationFramework;
    }
    
    @Override
    public String createMessage(Field field) throws JavaModelException {
      return validationFramework.composeFieldValidation(field);
    }
    
  }
}

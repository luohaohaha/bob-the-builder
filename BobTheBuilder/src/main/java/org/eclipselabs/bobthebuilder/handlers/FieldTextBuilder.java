package org.eclipselabs.bobthebuilder.handlers;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;

public interface FieldTextBuilder {
  String createMessage(IField field) throws JavaModelException;
  
  public static class FieldDeclarationBuilder implements FieldTextBuilder {

    @Override
    public String createMessage(IField field) throws JavaModelException {
      return Composer.Impl.composeFieldInBuilder(field);
    }
    
  }
  
  public static class WithMethodBuilder implements FieldTextBuilder {

    @Override
    public String createMessage(IField field) throws JavaModelException {
      return Composer.Impl.composeWithMethodSignature(field) + "...}";
    }
    
  }
  
  public static class FieldAssignmentBuilder implements FieldTextBuilder {

    @Override
    public String createMessage(IField field) throws JavaModelException {
      return Composer.Impl.composeSingleAssignment(field);
    }
    
  }
  
  public static class ValidationBuilder implements FieldTextBuilder {

    private ValidationFramework validationFramework;

    ValidationBuilder(ValidationFramework validationFramework) {
      this.validationFramework = validationFramework;
    }
    
    @Override
    public String createMessage(IField field) throws JavaModelException {
      return validationFramework.composeFieldValidation(field);
    }
    
  }
}

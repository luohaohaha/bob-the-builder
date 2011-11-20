package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.Signature;

public interface FieldPredicate {
  boolean match(String fieldToMatch, String input, String signature);

  public static class FieldAssignment implements FieldPredicate {

    public static String createFieldAssignmentRegex(String fieldName) {
      return "this\\." + fieldName + "\\s*=\\s*\\S*" + fieldName + "\\s*;";
    }

    @Override
    public boolean match(String fieldToMatch, String input, String signature) {
      Validate.notNull(fieldToMatch, "field to match may not be null");
      Validate.notNull(input, "input may not be null");
      Pattern pattern = Pattern.compile(createFieldAssignmentRegex(fieldToMatch));
      Matcher matcher = pattern.matcher(input);
      return matcher.find();
    }

  }

  public static class FieldValidation implements FieldPredicate {

    @Override
    public boolean match(String fieldToMatch, String input, String signature) {
      if (signature.equals(Signature.SIG_BOOLEAN)) { // No need to validate booleans
        return true;
      }
      return input.contains(fieldToMatch);
    }

  }
}

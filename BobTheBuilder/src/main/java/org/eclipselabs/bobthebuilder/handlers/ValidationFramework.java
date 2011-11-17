package org.eclipselabs.bobthebuilder.handlers;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public enum ValidationFramework {
  GOOGLE_GUAVA(
      "com.google.common.base.Preconditions",
      "Preconditions.checkNotNull",
      "Preconditions.checkArgument"),
  COMMONS_LANG3(
      "org.apache.commons.lang3.Validate",
      "Validate.notNull",
      "Validate.isTrue"),
  COMMONS_LANG2(
      "org.apache.commons.lang.Validate",
      COMMONS_LANG3.checkNotNull,
      COMMONS_LANG3.checkArgument);

  private static final String checkNotNullTemplateEnding = "(%1$s, \"%1$s may not be null\");";

  private static final String checkBlankStringTemplateEnding =
      "(!StringUtils.isBlank(%1$s), \"%1$s may not be blank\");";

  private static final String checkNotDefaultTemplateEnding = "(%1$s > %2$s, \"%1$s should be set\");";

  private static final String checkNotEmptyCollectionTemplateEnding =
      "(!%1$s.isEmpty(), \"%1$s may not be empty\");";

  public String fullClassName;

  private String checkArgument;

  private String checkNotNull;

  private ValidationFramework(String fullClassName, String checkNotNull, String checkArgument) {
    this.fullClassName = fullClassName;
    this.checkArgument = checkArgument;
    this.checkNotNull = checkNotNull;
  }

  public String getFullClassName() {
    return fullClassName;
  }

  public String getCheckArgument() {
    return checkArgument;
  }

  public String getCheckNotNull() {
    return checkNotNull;
  }

  public String getReadableName() {
    return StringUtils.lowerCase(this.name()).replace('_', ' ');
  }
  
  public String composeFieldValidation(IField field) throws JavaModelException {
    Validate.notNull(field, "field may not be null");
    String signature = field.getTypeSignature();
    String fieldName = field.getElementName();
    if (signature.equals(Signature.SIG_BYTE)) {
      return String.format(checkArgument + checkNotDefaultTemplateEnding, fieldName, "0");
    }
    else if (signature.equals(Signature.SIG_CHAR)) {
      return String.format(checkArgument + checkNotDefaultTemplateEnding, fieldName, "'\u0000'");
    }
    else if (signature.equals(Signature.SIG_DOUBLE)) {
      return String.format(checkArgument + checkNotDefaultTemplateEnding, fieldName, "0L");
    }
    else if (signature.equals(Signature.SIG_FLOAT)) {
      return String.format(checkArgument + checkNotDefaultTemplateEnding, fieldName, "0.0f");
    }
    else if (signature.equals(Signature.SIG_INT)) {
      return String.format(checkArgument + checkNotDefaultTemplateEnding, fieldName, "0");
    }
    else if (signature.equals(Signature.SIG_LONG)) {
      return String.format(checkArgument + checkNotDefaultTemplateEnding, fieldName, "0L");
    }
    else if (signature.equals(Signature.SIG_SHORT)) {
      return String.format(checkArgument + checkNotDefaultTemplateEnding, fieldName, "0");
    }
    else if (signature.contains("QString") || signature.contains("Qjava.lang.String")) {
      return String.format(checkArgument + checkBlankStringTemplateEnding, fieldName);
    }
    else if (signature.contains("Map")
        || signature.contains("Set")
        || signature.contains("List")) {
      return String.format(checkNotNull + checkNotNullTemplateEnding, fieldName) + " " +
        String.format(checkArgument + checkNotEmptyCollectionTemplateEnding, fieldName);
    }
    else {
      return String.format(checkNotNull + checkNotNullTemplateEnding, fieldName);
    }
  }
}

package org.eclipselabs.bobthebuilder.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipselabs.bobthebuilder.ValidationFramework;

public class Imports {

  private final Set<ImportStatement> importStatements = new HashSet<ImportStatement>();

  public Imports(Set<ImportStatement> importStatements) {
    Validate.notNull(importStatements, "importStatements may not be null");
    Validate.noNullElements(importStatements, "importStatements may not contain null elements");
    this.importStatements.addAll(importStatements);
  }

  public Set<ImportStatement> getImportStatements() {
    return Collections.unmodifiableSet(importStatements);
  }

  public boolean isCommonsLang3() {
    for (ImportStatement each : importStatements) {
      if (each.getName().equals(ValidationFramework.COMMONS_LANG3.fullClassName)) {
        return true;
      }
    }
    return false;
  }

  public boolean isCommonsLang2() {
    for (ImportStatement each : importStatements) {
      if (each.getName().equals(ValidationFramework.COMMONS_LANG2.fullClassName)) {
        return true;
      }
    }
    return false;
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

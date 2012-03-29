package org.eclipselabs.bobthebuilder.composer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.model.Field;

public class ConstructorComposer {

  public String composeFromScratch(ComposerRequest request, String mainTypeName) {
    Validate.notNull(request, "request may not be null");
    Validate.notNull(mainTypeName, "mainTypeName may not be null");
    Set<Field> fieldsToAddInBuilder = new HashSet<Field>();
    fieldsToAddInBuilder.addAll(request.getMissingAssignmentsInConstructor());
    fieldsToAddInBuilder.removeAll(request.getExtraFieldsInBuilder());
    List<String> sourceLines = new ArrayList<String>();
    sourceLines.add("private " + mainTypeName + "(Builder builder) {");
    for (Field each : fieldsToAddInBuilder) {
      sourceLines.add(composeAssignmentsInConstructorWithBuilder(each));
    }
    sourceLines.add("}");
    return StringUtils.join(sourceLines.toArray(), "\n");
  }

  private String composeAssignmentsInConstructorWithBuilder(Field field) {
    return "  " + composeSingleAssignment(field);
  }

  static String composeSingleAssignment(Field field) {
    return "this." + field.getName() + " = builder." + field.getName() + ";";
  }
}

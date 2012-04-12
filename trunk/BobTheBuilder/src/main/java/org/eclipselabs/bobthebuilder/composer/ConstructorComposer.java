package org.eclipselabs.bobthebuilder.composer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.model.ConstructorInMainType;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilder;
import org.eclipselabs.bobthebuilder.model.Field;

public class ConstructorComposer {
  
  
  private final FieldPredicate fieldPredicate;

  @Inject
  public ConstructorComposer(@ConstructorInMainType FieldPredicate fieldPredicate) {
    this.fieldPredicate = fieldPredicate;
  }

  public String composeFromScratch(ComposerRequest request, String mainTypeName) {
    Validate.notNull(request, "request may not be null");
    Validate.notNull(mainTypeName, "mainTypeName may not be null");
    Set<Field> fieldsToAddInBuilder = new HashSet<Field>();
    fieldsToAddInBuilder.addAll(request.getMissingAssignmentsInConstructor());
    fieldsToAddInBuilder.removeAll(request.getExtraFieldsInBuilder());
    List<String> sourceLines = new ArrayList<String>();
    sourceLines.add("private " + mainTypeName + "(Builder builder) {");
    for (Field each : new TreeSet<Field>(fieldsToAddInBuilder)) {
      sourceLines.add(composeAssignmentsInConstructorWithBuilder(each));
    }
    sourceLines.add("}");
    return StringUtils.join(sourceLines.toArray(), "\n");
  }

  public String composeFromExisting(
      ComposerRequest request, ConstructorWithBuilder constructorWithBuilder) {
    Validate.notNull(request, "request may not be null");
    Validate.notNull(constructorWithBuilder, "constructorWithBuilder may not be null");
    String source = constructorWithBuilder.getSource();
    int length = source.length();
    List<String> sourceLines = new ArrayList<String>();
    String originalSource = source.substring(0, length - 1);
    ArrayList<String> originalLines = new ArrayList<String>();
    originalLines.addAll(Arrays.asList(StringUtils.split(originalSource, '\n')));
    for (Field each : request.getExtraFieldsInBuilder()) {
      ListIterator<String> iterator = originalLines.listIterator();
      while(iterator.hasNext()) {
        String eachOriginalLine = iterator.next();
        if (fieldPredicate.match(each.getName(), eachOriginalLine, each.getSignature())) {
          iterator.remove();
        }
      }
    }
    sourceLines.addAll(originalLines);
    Set<Field> orderedMissingAssignmentsInConstructor = 
      new TreeSet<Field>(request.getMissingAssignmentsInConstructor());
    for (Field each : orderedMissingAssignmentsInConstructor) {
      sourceLines .add("  " + composeSingleAssignment(each));
    }
    sourceLines.add("}");
    String newConstructorWithBuilderContent = StringUtils.join(sourceLines, "\n");
    return newConstructorWithBuilderContent;
  }

  private String composeAssignmentsInConstructorWithBuilder(Field field) {
    return "  " + composeSingleAssignment(field);
  }

  static String composeSingleAssignment(Field field) {
    return "this." + field.getName() + " = builder." + field.getName() + ";";
  }
}

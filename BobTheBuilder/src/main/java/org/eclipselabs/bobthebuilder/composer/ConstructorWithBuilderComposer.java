package org.eclipselabs.bobthebuilder.composer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;
import org.eclipselabs.bobthebuilder.mapper.eclipse.FlattenedICompilationUnit;
import org.eclipselabs.bobthebuilder.model.Field;

public class ConstructorWithBuilderComposer {

  public StringResult compose(
    ComposerRequest request, IType type, FlattenedICompilationUnit flattenedICompilationUnit) throws JavaModelException {
    if (request.isCreateConstructorWithBuilder()) {
      Set<Field> fieldsToAddInBuilder = new TreeSet<Field>();
      fieldsToAddInBuilder.addAll(request.getMissingFieldsInBuilder());
      fieldsToAddInBuilder.removeAll(request.getExtraFieldsInBuilder());//This sounds idiotic
      List<String> sourceLines = new ArrayList<String>();
      sourceLines.add("private " + type.getElementName() + "(Builder builder) {");
      for (Field each : fieldsToAddInBuilder) {
        sourceLines.add(composeAssignmentsInConstructorWithBuilder(each));
      }
      sourceLines.add("}");
      return StringResult.getPresentInstance(StringUtils.join(sourceLines.toArray(), "\n"));
    }
    else if (flattenedICompilationUnit.getConstructorWithBuilder() != null &&
        (!request.getMissingAssignmentsInConstructor().isEmpty() ||
            !request.getExtraFieldsInBuilder().isEmpty())) {
      IMethod originalConstructorWithBuilder = flattenedICompilationUnit.getConstructorWithBuilder();
      Validate.notNull(originalConstructorWithBuilder,
        "originalConstructorWithBuilder may not be null");
      int length = originalConstructorWithBuilder.getSourceRange().getLength();
      List<String> sourceLines = new ArrayList<String>();
      String originalSource = originalConstructorWithBuilder.getSource().substring(0, length - 1);
      for (Field each : request.getExtraFieldsInBuilder()) {
        originalSource = originalSource.replaceAll(
          FieldPredicate.FieldAssignment.createFieldAssignmentRegex(each.getName()), "");
      }
      sourceLines.add(originalSource);
      originalConstructorWithBuilder.delete(true, null);
      for (Field each : request.getMissingAssignmentsInConstructor()) {
        sourceLines.add(
            composeAssignmentsInConstructorWithBuilder(each));
      }
      sourceLines.add("}");
      return StringResult.getPresentInstance(StringUtils.join(sourceLines, "\n"));
    }
    return StringResult.NOT_PRESENT;
  }

  public String composeAssignmentsInConstructorWithBuilder(Field field) {
    return "  " + composeSingleAssignment(field);
  }

  public String composeSingleAssignment(Field field) {
    return "this." + field.getName() + " = builder." + field.getName() + ";";
  }
}

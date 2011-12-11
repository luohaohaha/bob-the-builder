package org.eclipselabs.bobthebuilder.composer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.Analyzed;
import org.eclipselabs.bobthebuilder.analyzer.FieldPredicate;

public class ConstructorWithBuilderComposer {

  public StringResult compose(
    ComposerRequest request, IType type, Analyzed analyzed) throws JavaModelException {
    if (request.isCreateConstructorWithBuilder()) {
      Set<IField> fieldsToAddInBuilder = new HashSet<IField>();
      fieldsToAddInBuilder.addAll(analyzed.getBuilderFields());
      fieldsToAddInBuilder.addAll(request.getMissingFieldsInBuilder());
      fieldsToAddInBuilder.removeAll(request.getExtraFieldsInBuilder());
      List<String> sourceLines = new ArrayList<String>();
      sourceLines.add("private " + type.getElementName() + "(Builder builder) {");
      for (IField each : fieldsToAddInBuilder) {
        sourceLines.add(composeAssignmentsInConstructorWithBuilder(each));
      }
      sourceLines.add("}");
      return StringResult.getPresentInstance(StringUtils.join(sourceLines.toArray(), "\n"));
    }
    else if (analyzed.getConstructorWithBuilder() != null &&
        (!request.getMissingAssignmentsInConstructor().isEmpty() ||
            !request.getExtraFieldsInBuilder().isEmpty())) {
      IMethod originalConstructorWithBuilder = analyzed.getConstructorWithBuilder();
      Validate.notNull(originalConstructorWithBuilder,
        "originalConstructorWithBuilder may not be null");
      int length = originalConstructorWithBuilder.getSourceRange().getLength();
      List<String> sourceLines = new ArrayList<String>();
      String originalSource = originalConstructorWithBuilder.getSource().substring(0, length - 1);
      for (IField each : request.getExtraFieldsInBuilder()) {
        originalSource = originalSource.replaceAll(
          FieldPredicate.FieldAssignment.createFieldAssignmentRegex(each.getElementName()), "");
      }
      sourceLines.add(originalSource);
      originalConstructorWithBuilder.delete(true, null);
      for (IField each : request.getMissingAssignmentsInConstructor()) {
        sourceLines.add(
            composeAssignmentsInConstructorWithBuilder(each));
      }
      sourceLines.add("}");
      return StringResult.getPresentInstance(StringUtils.join(sourceLines, "\n"));
    }
    return StringResult.NOT_PRESENT;
  }

  public String composeAssignmentsInConstructorWithBuilder(IField field) {
    return "  " + composeSingleAssignment(field);
  }

  public String composeSingleAssignment(IField field) {
    return "this." + field.getElementName() + " = builder." + field.getElementName() + ";";
  }
}

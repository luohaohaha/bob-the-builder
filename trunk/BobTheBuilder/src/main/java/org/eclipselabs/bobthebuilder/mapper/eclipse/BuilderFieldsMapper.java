package org.eclipselabs.bobthebuilder.mapper.eclipse;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang.Validate;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MainTypeFieldAnalyzer;
import org.eclipselabs.bobthebuilder.model.Field;

public class BuilderFieldsMapper {
  private final MainTypeFieldAnalyzer mainTypeFieldAnalyzer;

  @Inject
  public BuilderFieldsMapper(MainTypeFieldAnalyzer mainTypeFieldAnalyzer) {
    this.mainTypeFieldAnalyzer = mainTypeFieldAnalyzer;
  }

  public Set<Field> map(IType builderType) throws JavaModelException {
    Validate.notNull(builderType, "builderType may not be null");
    Set<Field> builderFields = new HashSet<Field>();
    Set<IField> iFields = mainTypeFieldAnalyzer.analyze(builderType);
    for (IField each : iFields) {
      Field field = new Field.Builder()
          .withName(each.getElementName())
          .withSignature(each.getTypeSignature())
          .build();
      builderFields.add(field);
    }
    return Collections.unmodifiableSet(builderFields);
  }

}

package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.supplement.BuilderFieldsSupplementProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class CompilationUnitFlattenerTest {

  @Mock
  private MainTypeSelector mainTypeSelector;

  @Mock
  private ConstructorWithBuilderMapper constructorWithBuilderMapper;

  @Mock
  private BuilderTypeMapper builderTypeMapper;

  @Mock
  private ValidateMethodMapper validateMethodMapper;

  @Mock
  private BuildMethodMapper buildMethodMapper;

  @Mock
  private BuilderFieldsSupplementProvider builderFieldsSupplementProvider;

  @Mock
  private WithMethodsSupplementProvider withMethodsSupplementProvider;

  private CompilationUnitFlattener compilationUnitFlattener;

  private FlattenedICompilationUnit.Builder expectedFlattenedICompilationUnitBuilder;

  @Mock
  private ICompilationUnit iCompilationUnit;

  @Mock
  private IType mainType;

  @Mock
  private IMethod constructorWithBuilder;

  @Mock
  private IType builderType;

  @Mock
  private IMethod validateMethod;

  @Mock
  private IMethod buildMethod;

  @Mock
  private IField extraField;
  
  private Collection<IField> extraBuilderFields = new HashSet<IField>();

  @Mock
  private IMethod extraWithMethod;
  
  private Map<IField, IMethod> extraWithMethods = new HashMap<IField, IMethod>();

  @Mock
  private WithMethodsMapper withMethodsMapper;
  
  private Set<IMethod> existingWithMethods;
  
  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    compilationUnitFlattener = new CompilationUnitFlattener(
        mainTypeSelector,
        constructorWithBuilderMapper,
        builderTypeMapper,
        validateMethodMapper,
        buildMethodMapper,
        builderFieldsSupplementProvider,
        withMethodsSupplementProvider,
        withMethodsMapper);
    when(mainTypeSelector.map(iCompilationUnit)).thenReturn(mainType);
    when(constructorWithBuilderMapper.findConstructorWithBuilder(mainType)).thenReturn(
      constructorWithBuilder);
    when(builderTypeMapper.findBuilderType(mainType)).thenReturn(builderType);
    when(validateMethodMapper.findValidateMethod(builderType)).thenReturn(validateMethod);
    when(buildMethodMapper.findBuildMethod(builderType)).thenReturn(buildMethod);
    extraBuilderFields.add(extraField);
    when(builderFieldsSupplementProvider.supplement(mainType)).thenReturn(extraBuilderFields);
    extraWithMethods.put(extraField, extraWithMethod);
    when(withMethodsSupplementProvider.findExtra(mainType)).thenReturn(extraWithMethods);
    existingWithMethods = Sets.newHashSet(extraWithMethod);
    when(withMethodsMapper.findWithMethods(builderType)).thenReturn(existingWithMethods);
    expectedFlattenedICompilationUnitBuilder = new FlattenedICompilationUnit.Builder()
        .withCompilationUnit(iCompilationUnit)
        .withMainType(mainType)
        .withBuilderType(builderType)
        .withConstructorWithBuilder(constructorWithBuilder)
        .withValidateMethod(validateMethod)
        .withBuildMethod(buildMethod)
        .withExtraFields(Sets.newHashSet(extraBuilderFields))
        .withExtraWithMethods(Sets.newHashSet(extraWithMethods.values()))
        .withExistingWithMethods(existingWithMethods);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() throws Exception {
    compilationUnitFlattener.flatten(null);
  }

  @Test
  public void testNullBuilder() throws JavaModelException {
    when(builderTypeMapper.findBuilderType(mainType)).thenReturn(null);
    FlattenedICompilationUnit actual = compilationUnitFlattener.flatten(iCompilationUnit);
    FlattenedICompilationUnit expected = expectedFlattenedICompilationUnitBuilder
        .withValidateMethod(null)
        .withBuildMethod(null)
        .withBuilderType(null)
        .withExtraFields(Collections.<IField>emptySet())
        .withExtraWithMethods(Collections.<IMethod>emptySet())
        .withExistingWithMethods(Collections.<IMethod>emptySet())
        .build();
    assertEquals(expected, actual);
  }

  @Test
  public void testNoExtraBuilderFields() throws JavaModelException {
    Collection<IField> emptySet = Collections.<IField> emptySet();
    when(builderFieldsSupplementProvider.supplement(mainType)).thenReturn(emptySet);
    FlattenedICompilationUnit actual = compilationUnitFlattener.flatten(iCompilationUnit);
    FlattenedICompilationUnit expected = expectedFlattenedICompilationUnitBuilder
        .withExtraFields(Sets.newHashSet(emptySet))
        .withExtraWithMethods(Collections.<IMethod>emptySet())
        .withExistingWithMethods(Collections.<IMethod>emptySet())
        .build();
    assertEquals(expected, actual);

  }
  
  @Test
  public void testFlatten() throws Exception {
    FlattenedICompilationUnit actual = compilationUnitFlattener.flatten(iCompilationUnit);
    assertEquals(expectedFlattenedICompilationUnitBuilder.build(), actual);
  }
}

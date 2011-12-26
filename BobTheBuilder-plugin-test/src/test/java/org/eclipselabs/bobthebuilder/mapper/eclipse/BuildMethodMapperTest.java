package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.eclipselabs.bobthebuilder.model.BuildMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BuildMethodMapperTest {

  @Mock
  private ValidateMethodInvocationMapper validateMethodInvocationMapper;

  @Mock
  private MethodPredicate.BuildInBuilder predicate;

  @Mock
  private IType builderType;

  @Mock
  private IMethod buildMethod;

  private BuildMethodMapper buildMethodMapper;

  private Boolean validateMethodInvocation = true;

  private String source = "build method source";

  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] { buildMethod });
    Mockito.when(predicate.match(buildMethod)).thenReturn(true);
    Mockito.when(validateMethodInvocationMapper.map(buildMethod)).thenReturn(
      validateMethodInvocation);
    Mockito.when(buildMethod.getSource()).thenReturn(source);
    buildMethodMapper = new BuildMethodMapper(validateMethodInvocationMapper, predicate);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullBuilderType() throws JavaModelException {
    buildMethodMapper.map(null);
  }

  @Test
  public void testNoBuildMethod() throws JavaModelException {
    Mockito.when(builderType.getMethods()).thenReturn(new IMethod[] {});
    BuildMethod actual = buildMethodMapper.map(builderType);
    assertNull(actual);
  }

  @Test
  public void testSuccess() throws JavaModelException {
    BuildMethod actual = buildMethodMapper.map(builderType);
    BuildMethod expected = new BuildMethod.Builder().withSource(source)
        .withValidateMethodInvocation(validateMethodInvocation).build();
    assertEquals(expected, actual);

  }
}

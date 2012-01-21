package org.eclipselabs.bobthebuilder.complement;

import static org.junit.Assert.assertEquals;

import org.eclipselabs.bobthebuilder.model.BuildMethod;
import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BuildMethodComplementProviderTest {

  @Mock
  private MainType mainType;

  @Mock
  private BuilderType builderType;

  @Mock
  private BuildMethod buildMethod;

  private BuildMethodComplementProvider buildMethodComplementProvider;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    buildMethodComplementProvider = new BuildMethodComplementProvider();
    Mockito.when(mainType.getBuilderType()).thenReturn(builderType);
    Mockito.when(builderType.getBuildMethod()).thenReturn(buildMethod);
    Mockito.when(buildMethod.getValidateMethodInvocation()).thenReturn(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() {
    buildMethodComplementProvider.complement(null);
  }

  @Test
  public void testNullBuilderType() {
    Mockito.when(mainType.getBuilderType()).thenReturn(null);
    BuildMethodComplement actual = buildMethodComplementProvider.complement(mainType);
    assertEquals(BuildMethodComplement.ENTIRE_METHOD, actual);
  }

  @Test
  public void testNullBuildMethod() {
    Mockito.when(builderType.getBuildMethod()).thenReturn(null);
    BuildMethodComplement actual = buildMethodComplementProvider.complement(mainType);
    assertEquals(BuildMethodComplement.ENTIRE_METHOD, actual);
  }

  @Test
  public void testEmptyComplement() {
    BuildMethodComplement actual = buildMethodComplementProvider.complement(mainType);
    assertEquals(BuildMethodComplement.NOTHING_TO_DO, actual);
  }

  @Test
  public void testMissingValidateInvocation() {
    Mockito.when(buildMethod.getValidateMethodInvocation()).thenReturn(false);
    BuildMethodComplement actual = buildMethodComplementProvider.complement(mainType);
    assertEquals(BuildMethodComplement.NEEDS_VALIDATE_INVOCATION, actual);
  }
}

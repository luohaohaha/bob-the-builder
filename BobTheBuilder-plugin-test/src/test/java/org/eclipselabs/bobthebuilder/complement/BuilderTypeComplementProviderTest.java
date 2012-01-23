package org.eclipselabs.bobthebuilder.complement;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.eclipselabs.bobthebuilder.model.BuilderTypeComplement;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.ValidateMethodComplement;
import org.eclipselabs.bobthebuilder.model.WithMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class BuilderTypeComplementProviderTest {

  @Mock
  private BuilderFieldsComplementProvider builderFieldsComplementProvider;

  @Mock
  private WithMethodsComplementProvider withMethodsComplementProvider;

  @Mock
  private BuildMethodComplementProvider buildMethodComplementProvider;

  @Mock
  private ValidateMethodComplementProvider validateMethodComplementProvider;

  @Mock
  private MainType mainType;

  private BuilderTypeComplementProvider builderTypeComplementProvider;

  @Mock
  private Set<Field> builderFieldsComplement;

  @Mock
  private Set<WithMethod> withMethodsComplement;

  @Mock
  private BuildMethodComplement buildMethodComplement;

  @Mock
  private ValidateMethodComplement validateMethodComplement;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(builderFieldsComplementProvider.complement(mainType)).thenReturn(builderFieldsComplement);
    Mockito.when(withMethodsComplementProvider.complement(mainType)).thenReturn(withMethodsComplement);
    Mockito.when(buildMethodComplementProvider.complement(mainType)).thenReturn(buildMethodComplement);
    Mockito.when(validateMethodComplementProvider.complement(mainType)).thenReturn(validateMethodComplement);
    builderTypeComplementProvider = new BuilderTypeComplementProvider(
      builderFieldsComplementProvider, 
      withMethodsComplementProvider, 
      buildMethodComplementProvider, 
      validateMethodComplementProvider);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() {
    builderTypeComplementProvider.complement(null);
  }
  
  @Test
  public void testComplement() {
    BuilderTypeComplement actual = builderTypeComplementProvider.complement(mainType);
    assertEquals(builderFieldsComplement, actual.getBuilderFieldsComplement());
    assertEquals(buildMethodComplement, actual.getBuildMethodComplement());
    assertEquals(validateMethodComplement, actual.getValidateMethodComplement());
    assertEquals(withMethodsComplement, actual.getWithMethodsComplement());
  }
}

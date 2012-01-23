package org.eclipselabs.bobthebuilder.complement;

import static org.junit.Assert.assertEquals;

import org.eclipselabs.bobthebuilder.model.BuilderTypeComplement;
import org.eclipselabs.bobthebuilder.model.ConstructorWithBuilderComplement;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.MainTypeComplement;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class MainTypeComplementProviderTest {

  @Mock
  private ConstructorWithBuilderComplementProvider constructorWithBuilderComplementProvider;

  @Mock
  private BuilderTypeComplementProvider builderTypeComplementProvider;

  private MainTypeComplementProvider mainTypeComplementProvider;

  @Mock
  private MainType mainType;

  @Mock
  private ConstructorWithBuilderComplement constructorWithBuilderComplement;

  @Mock
  private BuilderTypeComplement builderTypeComplement;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mainTypeComplementProvider = new MainTypeComplementProvider(
        constructorWithBuilderComplementProvider, builderTypeComplementProvider);
    Mockito.when(constructorWithBuilderComplementProvider.complement(mainType)).thenReturn(
      constructorWithBuilderComplement);
    Mockito.when(builderTypeComplementProvider.complement(mainType)).thenReturn(builderTypeComplement);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() {
    mainTypeComplementProvider.complement(null);
  }
  
  @Test
  public void testComplement() {
    MainTypeComplement actual = mainTypeComplementProvider.complement(mainType);
    assertEquals(constructorWithBuilderComplement, actual.getConstructorWithBuilderComplement());
    assertEquals(builderTypeComplement, actual.getBuilderTypeComplement());
  }
}

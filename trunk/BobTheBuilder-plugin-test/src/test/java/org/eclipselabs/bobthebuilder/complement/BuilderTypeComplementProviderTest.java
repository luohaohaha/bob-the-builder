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

import com.google.common.collect.Sets;

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

  private Field field1 = new Field.Builder().withName("field1").withSignature("signature1").build();
  
  private Set<Field> builderFieldsComplement = Sets.newHashSet(field1);

  private WithMethod withMethod1 = 
    new WithMethod.Builder().withField(field1).withName("withField1").build();

  private Set<WithMethod> withMethodsComplement = Sets.newHashSet(withMethod1);

  private BuildMethodComplement buildMethodComplement = BuildMethodComplement.ENTIRE_METHOD;

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

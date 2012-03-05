package org.eclipselabs.bobthebuilder.complement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipselabs.bobthebuilder.model.BuilderType;
import org.eclipselabs.bobthebuilder.model.Field;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.eclipselabs.bobthebuilder.model.WithMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class WithMethodsComplementProviderTest {

  private static final String NAME1 = "name1";

  private static final String WITH_NAME1 = "withName1";

  private static final String NAME2 = "name2";

  private static final String NAME3 = "name3";

  private static final String WITH_NAME3 = "withName3";

  private static final String WITH_NAME2 = "withName2";

  @Mock
  private Field field1;

  @Mock
  private Field field2;

  @Mock
  private Field field3;

  private WithMethod withMethod1;

  private Set<WithMethod> builderWithMethods;

  private Set<WithMethod> expectedWithMethods;

  @Mock
  private MainType mainType;

  @Mock
  private BuilderType builderType;

  private WithMethodsComplementProvider withMethodsComplementProvider;

  private WithMethod withMethod2;

  private WithMethod withMethod3;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(field1.getName()).thenReturn(NAME1);
    Mockito.when(field2.getName()).thenReturn(NAME2);
    Mockito.when(field3.getName()).thenReturn(NAME3);
    withMethod1 = new WithMethod.Builder().withName(WITH_NAME1).withField(field1).build();
    builderWithMethods = Sets.newHashSet(withMethod1);
    Mockito.when(mainType.getFields()).thenReturn(Sets.newHashSet(field1, field2, field3));
    Mockito.when(mainType.getBuilderType()).thenReturn(builderType);
    Mockito.when(builderType.getWithMethods()).thenReturn(builderWithMethods);
    withMethodsComplementProvider = new WithMethodsComplementProvider();
    withMethod2 = new WithMethod.Builder().withName(WITH_NAME2).withField(field2).build();
    withMethod3 = new WithMethod.Builder().withName(WITH_NAME3).withField(field3).build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullMainType() {
    withMethodsComplementProvider.complement(null);
  }

  @Test
  public void testNullBuilder() {
    Mockito.when(mainType.getBuilderType()).thenReturn(null);
    Set<WithMethod> actual = withMethodsComplementProvider.complement(mainType);
    expectedWithMethods = Sets.newHashSet(withMethod1, withMethod2, withMethod3);
    assertEquals(expectedWithMethods, actual);
  }

  @Test
  public void testEmptyWithMethodsInBuilder() {
    Mockito.when(builderType.getWithMethods()).thenReturn(Sets.<WithMethod> newHashSet());
    Set<WithMethod> actual = withMethodsComplementProvider.complement(mainType);
    expectedWithMethods = Sets.newHashSet(withMethod1, withMethod2, withMethod3);
    assertEquals(expectedWithMethods, actual);
  }

  @Test
  public void testSomeWithMethodsInBuilder() {
    Set<WithMethod> actual = withMethodsComplementProvider.complement(mainType);
    expectedWithMethods = Sets.newHashSet(withMethod2, withMethod3);
    assertEquals(expectedWithMethods, actual);
  }

  @Test
  public void testAllWithMethodsInBuilder() {
    Mockito.when(builderType.getWithMethods()).thenReturn(
      Sets.newHashSet(withMethod1, withMethod2, withMethod3));
    Set<WithMethod> actual = withMethodsComplementProvider.complement(mainType);
    assertTrue(actual.isEmpty());
  }

}

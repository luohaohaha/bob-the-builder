package org.eclipselabs.bobthebuilder.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class WithMethodTest {

  private static final String NAME = "name";
  
  private static final String WITH_NAME = "withName";

  @Mock
  private Field field;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    Mockito.when(field.getName()).thenReturn(NAME);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullField() {
    WithMethod.getInstanceFromField((Field)null);
  }

  @Test
  public void testGetInstance() {
    WithMethod actual = WithMethod.getInstanceFromField(field);
    assertEquals(WITH_NAME, actual.getName());
  }
  @Test(expected = IllegalArgumentException.class)
  public void testNullName() {
    new WithMethod.Builder().withName(null).build();
  }
  @Test
  public void testBuilder() {
    WithMethod actual = new WithMethod.Builder().withName(NAME).withField(field).build();
    assertEquals(NAME, actual.getName());
    
  }
  
}

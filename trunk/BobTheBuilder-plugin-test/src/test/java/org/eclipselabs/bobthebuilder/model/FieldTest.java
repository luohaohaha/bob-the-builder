package org.eclipselabs.bobthebuilder.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;


public class FieldTest {
  
  private Field.Builder fieldBuilder1;

  private String name1 = "name1";

  private int position1 = 1;

  private String signature1 = "signature1";

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    fieldBuilder1 = 
      new Field.Builder().withName(name1).withPosition(position1).withSignature(signature1);
  }
  
  @Test
  public void testBuilder() throws Exception {
    Field actual = fieldBuilder1.build();
    assertEquals(name1, actual.getName());
    assertEquals(position1, actual.getPosition());
    assertEquals(signature1, actual.getSignature());
  }
  
  @Test
  public void testEquals() throws Exception {
    Field field1 = fieldBuilder1.build();
    Field field2 = fieldBuilder1.withPosition(300).build();
    assertEquals(field1, field2);
  }

  @Test
  public void testHashCode() throws Exception {
    Field field1 = fieldBuilder1.build();
    Set<Field> fields = Sets.newHashSet(field1);
    Field field2 = fieldBuilder1.withPosition(300).build();
    fields.remove(field2);
    assertTrue(fields.isEmpty());
  }
}

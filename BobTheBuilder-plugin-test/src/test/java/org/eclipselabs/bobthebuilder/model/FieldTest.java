package org.eclipselabs.bobthebuilder.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
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
    fieldBuilder1 = getDefaultBuilder();
  }
  
  private Field.Builder getDefaultBuilder() {
    return new Field.Builder().withName(name1).withPosition(position1).withSignature(signature1);
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
    Field field1 = getDefaultBuilder().build();
    Field field2 = getDefaultBuilder().withPosition(300).build();
    assertEquals(field1, field2);
  }

  @Test
  public void testRemoveAll() throws Exception {
    Field field1 = getDefaultBuilder().build();
    Set<Field> first = Sets.newHashSet(field1);
    Field field3 = getDefaultBuilder().withPosition(100).build();
    Field field2 = getDefaultBuilder().withName("name2").withPosition(300).build();
    Set<Field> second = Sets.newHashSet(field3, field2);
    first.removeAll(second);
    assertEquals(0, first.size());
  }

  @Test
  public void testRemoveAllDifferentCollections() throws Exception {
    Field field1 = fieldBuilder1.withPosition(20000).build();
    Set<Field> first = Sets.newHashSet(field1);
    Set<Field> copyOfFirst = new HashSet<Field>();
    copyOfFirst.addAll(first);
    Field field2 = getDefaultBuilder().withName("name2").withPosition(300).build();
    Field field3 = getDefaultBuilder().withPosition(10).build();
    Set<Field> second = Collections.unmodifiableSet(Sets.newHashSet(field3, field2));
    copyOfFirst.removeAll(second);
    assertEquals(0, copyOfFirst.size());
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

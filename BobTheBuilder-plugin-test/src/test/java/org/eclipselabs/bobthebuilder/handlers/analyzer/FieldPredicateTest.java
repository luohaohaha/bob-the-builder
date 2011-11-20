package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertTrue;

import org.eclipse.jdt.core.Signature;
import org.junit.Before;
import org.junit.Test;

/**
 * To test {@link FieldPredicate}
 */
public class FieldPredicateTest {

  private String fieldToMatch;

  private String input;

  private String signature;

  private String field1Name = "field1";

  private String field2Name = "field1";

  @Before
  public void setUp() throws Exception {
    fieldToMatch = field1Name;
    input = String.format("this.%1$s = %1$s;", field1Name);
    signature = Signature.SIG_DOUBLE;
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFieldToMatch() {
    new FieldPredicate.FieldAssignment().match(null, input, signature);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullInput() {
    new FieldPredicate.FieldAssignment().match(fieldToMatch, null, signature);
  }

  @Test
  public void testFieldAssignmentMatches() {
    boolean actual = new FieldPredicate.FieldAssignment().match(fieldToMatch, input, signature);
    assertTrue(actual);
  }

  @Test
  public void testFieldAssignmentDoesNotMatch() {
    input = String.format("this.%1$s = %1$s;", field2Name);
    boolean actual = new FieldPredicate.FieldAssignment().match(fieldToMatch, input, signature);
    assertTrue(actual);
  }

}

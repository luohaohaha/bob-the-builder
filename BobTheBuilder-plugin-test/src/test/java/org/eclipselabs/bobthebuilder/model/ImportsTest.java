package org.eclipselabs.bobthebuilder.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipselabs.bobthebuilder.ValidationFramework;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class ImportsTest {

  private Set<ImportStatement> importStatements;

  private ImportStatement ImportStatement1 = new ImportStatement("blah.blah.blah");

  private ImportStatement commonsLang2Statement = new ImportStatement(
      ValidationFramework.COMMONS_LANG2.fullClassName);

  private ImportStatement commonsLang3Statement = new ImportStatement(
      ValidationFramework.COMMONS_LANG3.fullClassName);

  @Before
  public void setUp() throws Exception {
    importStatements = Sets.newHashSet(ImportStatement1, commonsLang2Statement,
      commonsLang3Statement);
  }

  @Test
  public void testConstructor() {
    Imports actual = new Imports(importStatements);
    assertEquals(importStatements, actual.getImportStatements());
  }

  @Test
  public void testIsCommonsLang2() {
    Imports actual = new Imports(importStatements);
    assertTrue(actual.isCommonsLang2());
  }

  @Test
  public void testIsCommonsLang3() {
    Imports actual = new Imports(importStatements);
    assertTrue(actual.isCommonsLang3());
  }
  
  @Test
  public void testNotCommonsLang() {
    importStatements = Sets.newHashSet(ImportStatement1);
    Imports actual = new Imports(importStatements);
    assertFalse(actual.isCommonsLang2());
    assertFalse(actual.isCommonsLang3());
    
  }
}

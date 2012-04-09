package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class RawFieldPositionComparatorTest {

  @Mock
  private IField field1;

  @Mock
  private IField field2;
  
  @Mock
  private ISourceRange sourceRange1;

  @Mock
  private ISourceRange sourceRange2;
  
  private RawFieldPositionComparator rawFieldComparator;
  
  
  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    rawFieldComparator = new RawFieldPositionComparator();
    when(field1.getSourceRange()).thenReturn(sourceRange1);
    when(field2.getSourceRange()).thenReturn(sourceRange2);
  }
  
  @Test
  public void testSmaller() throws Exception {
    when(sourceRange1.getOffset()).thenReturn(1);
    when(sourceRange2.getOffset()).thenReturn(2);
    
    int actual = rawFieldComparator.compare(field1, field2);
    
    assertEquals(-1, actual);
  }

  @Test
  public void testSame() throws Exception {
    when(sourceRange1.getOffset()).thenReturn(1);
    when(sourceRange2.getOffset()).thenReturn(1);
    
    int actual = rawFieldComparator.compare(field1, field2);
    
    assertEquals(0, actual);
  }

  @Test
  public void testGreater() throws Exception {
    when(sourceRange1.getOffset()).thenReturn(2);
    when(sourceRange2.getOffset()).thenReturn(1);
    
    int actual = rawFieldComparator.compare(field1, field2);
    
    assertEquals(1, actual);
  }
  
}

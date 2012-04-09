package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class RawWithMethodComparatorTest {

  @Mock
  private IMethod withMethod1;

  @Mock
  private IMethod withMethod2;
  
  @Mock
  private ISourceRange sourceRange1;

  @Mock
  private ISourceRange sourceRange2;
  
  private RawWithMethodComparator rawWithMethodComparator;
  
  
  @Before
  public void setUp() throws JavaModelException {
    MockitoAnnotations.initMocks(this);
    rawWithMethodComparator = new RawWithMethodComparator();
    when(withMethod1.getSourceRange()).thenReturn(sourceRange1);
    when(withMethod2.getSourceRange()).thenReturn(sourceRange2);
  }
  
  @Test
  public void testSmaller() throws Exception {
    when(sourceRange1.getOffset()).thenReturn(1);
    when(sourceRange2.getOffset()).thenReturn(2);
    
    int actual = rawWithMethodComparator.compare(withMethod1, withMethod2);
    
    assertEquals(-1, actual);
  }

  @Test
  public void testSame() throws Exception {
    when(sourceRange1.getOffset()).thenReturn(1);
    when(sourceRange2.getOffset()).thenReturn(1);
    
    int actual = rawWithMethodComparator.compare(withMethod1, withMethod2);
    
    assertEquals(0, actual);
  }

  @Test
  public void testGreater() throws Exception {
    when(sourceRange1.getOffset()).thenReturn(2);
    when(sourceRange2.getOffset()).thenReturn(1);
    
    int actual = rawWithMethodComparator.compare(withMethod1, withMethod2);
    
    assertEquals(1, actual);
  }
  
}

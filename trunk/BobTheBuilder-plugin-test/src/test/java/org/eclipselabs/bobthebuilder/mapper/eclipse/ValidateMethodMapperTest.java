package org.eclipselabs.bobthebuilder.mapper.eclipse;

import org.eclipselabs.bobthebuilder.analyzer.MethodPredicate;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ValidateMethodMapperTest {

  @Mock
  private ValidateFieldsMethodMapper validatedFieldsMapper;

  @Mock
  private MethodPredicate.ValidateInBuilder predicate;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

}

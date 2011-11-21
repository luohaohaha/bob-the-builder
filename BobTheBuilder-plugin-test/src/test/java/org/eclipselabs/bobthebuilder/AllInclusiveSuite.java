package org.eclipselabs.bobthebuilder;

import org.eclipselabs.bobthebuilder.handlers.analyzer.CompilationUnitAnalyzerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CompilationUnitAnalyzerTest.class, ComposerTest.class})
public class AllInclusiveSuite {

}

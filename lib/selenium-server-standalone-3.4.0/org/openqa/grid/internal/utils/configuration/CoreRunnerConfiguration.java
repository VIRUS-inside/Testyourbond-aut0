package org.openqa.grid.internal.utils.configuration;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.StringConverter;
import java.util.List;

public class CoreRunnerConfiguration
  extends StandaloneConfiguration
{
  @Parameter(names={"-htmlSuite"}, arity=4, description="Run your tests as a core runner. Parameters are browser string, base test url, test suite url, path to results file.", listConverter=StringConverter.class)
  public List<String> htmlSuite;
  
  public CoreRunnerConfiguration() {}
}

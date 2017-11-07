package org.junit.runners.parameterized;

import org.junit.runner.Runner;
import org.junit.runners.model.InitializationError;

public abstract interface ParametersRunnerFactory
{
  public abstract Runner createRunnerForTestWithParameters(TestWithParameters paramTestWithParameters)
    throws InitializationError;
}

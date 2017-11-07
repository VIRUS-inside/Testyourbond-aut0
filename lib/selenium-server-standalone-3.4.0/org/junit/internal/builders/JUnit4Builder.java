package org.junit.internal.builders;

import org.junit.runners.BlockJUnit4ClassRunner;

public class JUnit4Builder extends org.junit.runners.model.RunnerBuilder
{
  public JUnit4Builder() {}
  
  public org.junit.runner.Runner runnerForClass(Class<?> testClass) throws Throwable {
    return new BlockJUnit4ClassRunner(testClass);
  }
}

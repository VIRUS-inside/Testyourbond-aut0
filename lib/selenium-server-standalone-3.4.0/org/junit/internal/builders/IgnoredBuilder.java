package org.junit.internal.builders;

import org.junit.runner.Runner;

public class IgnoredBuilder extends org.junit.runners.model.RunnerBuilder
{
  public IgnoredBuilder() {}
  
  public Runner runnerForClass(Class<?> testClass) {
    if (testClass.getAnnotation(org.junit.Ignore.class) != null) {
      return new IgnoredClassRunner(testClass);
    }
    return null;
  }
}

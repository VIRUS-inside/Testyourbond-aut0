package org.junit.internal.builders;

import org.junit.runner.Runner;

public class NullBuilder extends org.junit.runners.model.RunnerBuilder {
  public NullBuilder() {}
  
  public Runner runnerForClass(Class<?> each) throws Throwable {
    return null;
  }
}

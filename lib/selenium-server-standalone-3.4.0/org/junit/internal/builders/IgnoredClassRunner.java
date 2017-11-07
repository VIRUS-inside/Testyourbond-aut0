package org.junit.internal.builders;

import org.junit.runner.notification.RunNotifier;

public class IgnoredClassRunner extends org.junit.runner.Runner
{
  private final Class<?> clazz;
  
  public IgnoredClassRunner(Class<?> testClass)
  {
    clazz = testClass;
  }
  
  public void run(RunNotifier notifier)
  {
    notifier.fireTestIgnored(getDescription());
  }
  
  public org.junit.runner.Description getDescription()
  {
    return org.junit.runner.Description.createSuiteDescription(clazz);
  }
}

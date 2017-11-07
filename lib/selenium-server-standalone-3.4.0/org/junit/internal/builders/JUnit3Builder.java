package org.junit.internal.builders;

import junit.framework.TestCase;

public class JUnit3Builder extends org.junit.runners.model.RunnerBuilder
{
  public JUnit3Builder() {}
  
  public org.junit.runner.Runner runnerForClass(Class<?> testClass) throws Throwable {
    if (isPre4Test(testClass)) {
      return new org.junit.internal.runners.JUnit38ClassRunner(testClass);
    }
    return null;
  }
  
  boolean isPre4Test(Class<?> testClass) {
    return TestCase.class.isAssignableFrom(testClass);
  }
}

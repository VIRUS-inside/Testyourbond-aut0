package org.junit.internal.builders;

import org.junit.runner.Runner;

public class SuiteMethodBuilder extends org.junit.runners.model.RunnerBuilder
{
  public SuiteMethodBuilder() {}
  
  public Runner runnerForClass(Class<?> each) throws Throwable {
    if (hasSuiteMethod(each)) {
      return new org.junit.internal.runners.SuiteMethod(each);
    }
    return null;
  }
  
  public boolean hasSuiteMethod(Class<?> testClass) {
    try {
      testClass.getMethod("suite", new Class[0]);
    } catch (NoSuchMethodException e) {
      return false;
    }
    return true;
  }
}

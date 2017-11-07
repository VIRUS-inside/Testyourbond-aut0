package org.junit.internal.requests;

import org.junit.internal.builders.AllDefaultPossibilitiesBuilder;
import org.junit.runner.Runner;

public class ClassRequest extends org.junit.runner.Request
{
  private final Object runnerLock = new Object();
  

  private final Class<?> fTestClass;
  
  private final boolean canUseSuiteMethod;
  
  private volatile Runner runner;
  

  public ClassRequest(Class<?> testClass, boolean canUseSuiteMethod)
  {
    fTestClass = testClass;
    this.canUseSuiteMethod = canUseSuiteMethod;
  }
  
  public ClassRequest(Class<?> testClass) {
    this(testClass, true);
  }
  
  public Runner getRunner()
  {
    if (runner == null) {
      synchronized (runnerLock) {
        if (runner == null) {
          runner = new AllDefaultPossibilitiesBuilder(canUseSuiteMethod).safeRunnerForClass(fTestClass);
        }
      }
    }
    return runner;
  }
}

package org.junit.internal.runners.statements;

import org.junit.runners.model.FrameworkMethod;

public class InvokeMethod extends org.junit.runners.model.Statement
{
  private final FrameworkMethod testMethod;
  private final Object target;
  
  public InvokeMethod(FrameworkMethod testMethod, Object target) {
    this.testMethod = testMethod;
    this.target = target;
  }
  
  public void evaluate() throws Throwable
  {
    testMethod.invokeExplosively(target, new Object[0]);
  }
}

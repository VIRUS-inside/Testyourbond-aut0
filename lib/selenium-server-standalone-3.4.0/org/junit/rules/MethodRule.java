package org.junit.rules;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public abstract interface MethodRule
{
  public abstract Statement apply(Statement paramStatement, FrameworkMethod paramFrameworkMethod, Object paramObject);
}

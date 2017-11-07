package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract interface TestRule
{
  public abstract Statement apply(Statement paramStatement, Description paramDescription);
}

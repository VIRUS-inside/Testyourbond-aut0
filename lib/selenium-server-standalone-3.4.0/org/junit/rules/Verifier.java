package org.junit.rules;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;




















public abstract class Verifier
  implements TestRule
{
  public Verifier() {}
  
  public Statement apply(final Statement base, Description description)
  {
    new Statement()
    {
      public void evaluate() throws Throwable {
        base.evaluate();
        verify();
      }
    };
  }
  
  protected void verify()
    throws Throwable
  {}
}

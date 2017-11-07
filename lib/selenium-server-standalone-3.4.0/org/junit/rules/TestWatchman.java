package org.junit.rules;

import org.junit.internal.AssumptionViolatedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;


































@Deprecated
public class TestWatchman
  implements MethodRule
{
  public TestWatchman() {}
  
  public Statement apply(final Statement base, final FrameworkMethod method, Object target)
  {
    new Statement()
    {
      public void evaluate() throws Throwable {
        starting(method);
        try {
          base.evaluate();
          succeeded(method);
        } catch (AssumptionViolatedException e) {
          throw e;
        } catch (Throwable e) {
          failed(e, method);
          throw e;
        } finally {
          finished(method);
        }
      }
    };
  }
  
  public void succeeded(FrameworkMethod method) {}
  
  public void failed(Throwable e, FrameworkMethod method) {}
  
  public void starting(FrameworkMethod method) {}
  
  public void finished(FrameworkMethod method) {}
}

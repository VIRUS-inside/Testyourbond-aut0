package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

































public abstract class TestWatcher
  implements TestRule
{
  public TestWatcher() {}
  
  public Statement apply(final Statement base, final Description description)
  {
    new Statement()
    {
      public void evaluate() throws Throwable {
        List<Throwable> errors = new ArrayList();
        
        TestWatcher.this.startingQuietly(description, errors);
        try {
          base.evaluate();
          TestWatcher.this.succeededQuietly(description, errors);
        } catch (org.junit.internal.AssumptionViolatedException e) {
          errors.add(e);
          TestWatcher.this.skippedQuietly(e, description, errors);
        } catch (Throwable e) {
          errors.add(e);
          TestWatcher.this.failedQuietly(e, description, errors);
        } finally {
          TestWatcher.this.finishedQuietly(description, errors);
        }
        
        MultipleFailureException.assertEmpty(errors);
      }
    };
  }
  
  private void succeededQuietly(Description description, List<Throwable> errors)
  {
    try {
      succeeded(description);
    } catch (Throwable e) {
      errors.add(e);
    }
  }
  
  private void failedQuietly(Throwable e, Description description, List<Throwable> errors)
  {
    try {
      failed(e, description);
    } catch (Throwable e1) {
      errors.add(e1);
    }
  }
  

  private void skippedQuietly(org.junit.internal.AssumptionViolatedException e, Description description, List<Throwable> errors)
  {
    try
    {
      if ((e instanceof org.junit.AssumptionViolatedException)) {
        skipped((org.junit.AssumptionViolatedException)e, description);
      } else {
        skipped(e, description);
      }
    } catch (Throwable e1) {
      errors.add(e1);
    }
  }
  
  private void startingQuietly(Description description, List<Throwable> errors)
  {
    try {
      starting(description);
    } catch (Throwable e) {
      errors.add(e);
    }
  }
  
  private void finishedQuietly(Description description, List<Throwable> errors)
  {
    try {
      finished(description);
    } catch (Throwable e) {
      errors.add(e);
    }
  }
  




  protected void succeeded(Description description) {}
  




  protected void failed(Throwable e, Description description) {}
  




  protected void skipped(org.junit.AssumptionViolatedException e, Description description)
  {
    org.junit.internal.AssumptionViolatedException asInternalException = e;
    skipped(asInternalException, description);
  }
  
  @Deprecated
  protected void skipped(org.junit.internal.AssumptionViolatedException e, Description description) {}
  
  protected void starting(Description description) {}
  
  protected void finished(Description description) {}
}

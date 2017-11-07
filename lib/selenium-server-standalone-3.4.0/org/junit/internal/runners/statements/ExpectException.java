package org.junit.internal.runners.statements;

import org.junit.runners.model.Statement;

public class ExpectException extends Statement
{
  private final Statement next;
  private final Class<? extends Throwable> expected;
  
  public ExpectException(Statement next, Class<? extends Throwable> expected) {
    this.next = next;
    this.expected = expected;
  }
  
  public void evaluate() throws Exception
  {
    boolean complete = false;
    try {
      next.evaluate();
      complete = true;
    } catch (org.junit.internal.AssumptionViolatedException e) {
      throw e;
    } catch (Throwable e) {
      if (!expected.isAssignableFrom(e.getClass())) {
        String message = "Unexpected exception, expected<" + expected.getName() + "> but was<" + e.getClass().getName() + ">";
        

        throw new Exception(message, e);
      }
    }
    if (complete) {
      throw new AssertionError("Expected exception: " + expected.getName());
    }
  }
}

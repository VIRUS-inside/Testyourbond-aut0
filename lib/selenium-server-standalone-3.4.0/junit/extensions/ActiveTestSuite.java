package junit.extensions;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;





public class ActiveTestSuite
  extends TestSuite
{
  private volatile int fActiveTestDeathCount;
  
  public ActiveTestSuite() {}
  
  public ActiveTestSuite(Class<? extends TestCase> theClass)
  {
    super(theClass);
  }
  
  public ActiveTestSuite(String name) {
    super(name);
  }
  
  public ActiveTestSuite(Class<? extends TestCase> theClass, String name) {
    super(theClass, name);
  }
  
  public void run(TestResult result)
  {
    fActiveTestDeathCount = 0;
    super.run(result);
    waitUntilFinished();
  }
  
  public void runTest(final Test test, final TestResult result)
  {
    Thread t = new Thread()
    {
      public void run()
      {
        try
        {
          test.run(result);
        } finally {
          runFinished();
        }
      }
    };
    t.start();
  }
  
  synchronized void waitUntilFinished() {
    while (fActiveTestDeathCount < testCount()) {
      try {
        wait();
      }
      catch (InterruptedException e) {}
    }
  }
  
  public synchronized void runFinished()
  {
    fActiveTestDeathCount += 1;
    notifyAll();
  }
}

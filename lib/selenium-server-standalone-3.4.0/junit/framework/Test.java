package junit.framework;

public abstract interface Test
{
  public abstract int countTestCases();
  
  public abstract void run(TestResult paramTestResult);
}

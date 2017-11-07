package junit.framework;

import java.util.HashMap;
import java.util.List;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;

public class JUnit4TestAdapterCache extends HashMap<Description, Test>
{
  private static final long serialVersionUID = 1L;
  
  public JUnit4TestAdapterCache() {}
  
  private static final JUnit4TestAdapterCache fInstance = new JUnit4TestAdapterCache();
  
  public static JUnit4TestAdapterCache getDefault() {
    return fInstance;
  }
  
  public Test asTest(Description description) {
    if (description.isSuite()) {
      return createTest(description);
    }
    if (!containsKey(description)) {
      put(description, createTest(description));
    }
    return (Test)get(description);
  }
  
  Test createTest(Description description)
  {
    if (description.isTest()) {
      return new JUnit4TestCaseFacade(description);
    }
    TestSuite suite = new TestSuite(description.getDisplayName());
    for (Description child : description.getChildren()) {
      suite.addTest(asTest(child));
    }
    return suite;
  }
  
  public RunNotifier getNotifier(final TestResult result, JUnit4TestAdapter adapter)
  {
    RunNotifier notifier = new RunNotifier();
    notifier.addListener(new org.junit.runner.notification.RunListener()
    {
      public void testFailure(Failure failure) throws Exception {
        result.addError(asTest(failure.getDescription()), failure.getException());
      }
      
      public void testFinished(Description description) throws Exception
      {
        result.endTest(asTest(description));
      }
      
      public void testStarted(Description description) throws Exception
      {
        result.startTest(asTest(description));
      }
    });
    return notifier;
  }
  
  public List<Test> asTestList(Description description) {
    if (description.isTest()) {
      return java.util.Arrays.asList(new Test[] { asTest(description) });
    }
    List<Test> returnThis = new java.util.ArrayList();
    for (Description child : description.getChildren()) {
      returnThis.add(asTest(child));
    }
    return returnThis;
  }
}

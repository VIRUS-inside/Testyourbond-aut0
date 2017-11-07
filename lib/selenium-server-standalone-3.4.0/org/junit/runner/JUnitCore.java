package org.junit.runner;

import java.io.PrintStream;
import junit.framework.Test;
import junit.runner.Version;
import org.junit.internal.JUnitSystem;
import org.junit.internal.RealSystem;
import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;











public class JUnitCore
{
  private final RunNotifier notifier = new RunNotifier();
  



  public JUnitCore() {}
  


  public static void main(String... args)
  {
    Result result = new JUnitCore().runMain(new RealSystem(), args);
    System.exit(result.wasSuccessful() ? 0 : 1);
  }
  







  public static Result runClasses(Class<?>... classes)
  {
    return runClasses(defaultComputer(), classes);
  }
  








  public static Result runClasses(Computer computer, Class<?>... classes)
  {
    return new JUnitCore().run(computer, classes);
  }
  



  Result runMain(JUnitSystem system, String... args)
  {
    system.out().println("JUnit version " + Version.id());
    
    JUnitCommandLineParseResult jUnitCommandLineParseResult = JUnitCommandLineParseResult.parse(args);
    
    RunListener listener = new TextListener(system);
    addListener(listener);
    
    return run(jUnitCommandLineParseResult.createRequest(defaultComputer()));
  }
  


  public String getVersion()
  {
    return Version.id();
  }
  





  public Result run(Class<?>... classes)
  {
    return run(defaultComputer(), classes);
  }
  






  public Result run(Computer computer, Class<?>... classes)
  {
    return run(Request.classes(computer, classes));
  }
  





  public Result run(Request request)
  {
    return run(request.getRunner());
  }
  





  public Result run(Test test)
  {
    return run(new JUnit38ClassRunner(test));
  }
  


  public Result run(Runner runner)
  {
    Result result = new Result();
    RunListener listener = result.createListener();
    notifier.addFirstListener(listener);
    try {
      notifier.fireTestRunStarted(runner.getDescription());
      runner.run(notifier);
      notifier.fireTestRunFinished(result);
    } finally {
      removeListener(listener);
    }
    return result;
  }
  





  public void addListener(RunListener listener)
  {
    notifier.addListener(listener);
  }
  




  public void removeListener(RunListener listener)
  {
    notifier.removeListener(listener);
  }
  
  static Computer defaultComputer() {
    return new Computer();
  }
}

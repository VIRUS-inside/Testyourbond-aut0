package org.junit.runners;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.rules.RuleMemberValidator;
import org.junit.internal.runners.statements.RunAfters;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.rules.RunRules;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.manipulation.Sortable;
import org.junit.runner.manipulation.Sorter;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerScheduler;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.junit.validator.AnnotationsValidator;
import org.junit.validator.PublicClassValidator;
import org.junit.validator.TestClassValidator;


















public abstract class ParentRunner<T>
  extends Runner
  implements Filterable, Sortable
{
  private static final List<TestClassValidator> VALIDATORS = Arrays.asList(new TestClassValidator[] { new AnnotationsValidator(), new PublicClassValidator() });
  

  private final Object childrenLock = new Object();
  
  private final TestClass testClass;
  
  private volatile Collection<T> filteredChildren = null;
  
  private volatile RunnerScheduler scheduler = new RunnerScheduler() {
    public void schedule(Runnable childStatement) {
      childStatement.run();
    }
    


    public void finished() {}
  };
  

  protected ParentRunner(Class<?> testClass)
    throws InitializationError
  {
    this.testClass = createTestClass(testClass);
    validate();
  }
  
  protected TestClass createTestClass(Class<?> testClass) {
    return new TestClass(testClass);
  }
  







  protected abstract List<T> getChildren();
  







  protected abstract Description describeChild(T paramT);
  






  protected abstract void runChild(T paramT, RunNotifier paramRunNotifier);
  






  protected void collectInitializationErrors(List<Throwable> errors)
  {
    validatePublicVoidNoArgMethods(BeforeClass.class, true, errors);
    validatePublicVoidNoArgMethods(AfterClass.class, true, errors);
    validateClassRules(errors);
    applyValidators(errors);
  }
  
  private void applyValidators(List<Throwable> errors) {
    if (getTestClass().getJavaClass() != null) {
      for (TestClassValidator each : VALIDATORS) {
        errors.addAll(each.validateTestClass(getTestClass()));
      }
    }
  }
  











  protected void validatePublicVoidNoArgMethods(Class<? extends Annotation> annotation, boolean isStatic, List<Throwable> errors)
  {
    List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
    
    for (FrameworkMethod eachTestMethod : methods) {
      eachTestMethod.validatePublicVoidNoArg(isStatic, errors);
    }
  }
  
  private void validateClassRules(List<Throwable> errors) {
    RuleMemberValidator.CLASS_RULE_VALIDATOR.validate(getTestClass(), errors);
    RuleMemberValidator.CLASS_RULE_METHOD_VALIDATOR.validate(getTestClass(), errors);
  }
  
























  protected Statement classBlock(RunNotifier notifier)
  {
    Statement statement = childrenInvoker(notifier);
    if (!areAllChildrenIgnored()) {
      statement = withBeforeClasses(statement);
      statement = withAfterClasses(statement);
      statement = withClassRules(statement);
    }
    return statement;
  }
  
  private boolean areAllChildrenIgnored() {
    for (T child : getFilteredChildren()) {
      if (!isIgnored(child)) {
        return false;
      }
    }
    return true;
  }
  




  protected Statement withBeforeClasses(Statement statement)
  {
    List<FrameworkMethod> befores = testClass.getAnnotatedMethods(BeforeClass.class);
    
    return befores.isEmpty() ? statement : new RunBefores(statement, befores, null);
  }
  







  protected Statement withAfterClasses(Statement statement)
  {
    List<FrameworkMethod> afters = testClass.getAnnotatedMethods(AfterClass.class);
    
    return afters.isEmpty() ? statement : new RunAfters(statement, afters, null);
  }
  









  private Statement withClassRules(Statement statement)
  {
    List<TestRule> classRules = classRules();
    return classRules.isEmpty() ? statement : new RunRules(statement, classRules, getDescription());
  }
  




  protected List<TestRule> classRules()
  {
    List<TestRule> result = testClass.getAnnotatedMethodValues(null, ClassRule.class, TestRule.class);
    result.addAll(testClass.getAnnotatedFieldValues(null, ClassRule.class, TestRule.class));
    return result;
  }
  




  protected Statement childrenInvoker(final RunNotifier notifier)
  {
    new Statement()
    {
      public void evaluate() {
        ParentRunner.this.runChildren(notifier);
      }
    };
  }
  






  protected boolean isIgnored(T child)
  {
    return false;
  }
  
  private void runChildren(final RunNotifier notifier) {
    RunnerScheduler currentScheduler = scheduler;
    try {
      for (final T each : getFilteredChildren()) {
        currentScheduler.schedule(new Runnable() {
          public void run() {
            runChild(each, notifier);
          }
        });
      }
    } finally {
      currentScheduler.finished();
    }
  }
  


  protected String getName()
  {
    return testClass.getName();
  }
  






  public final TestClass getTestClass()
  {
    return testClass;
  }
  



  protected final void runLeaf(Statement statement, Description description, RunNotifier notifier)
  {
    EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
    eachNotifier.fireTestStarted();
    try {
      statement.evaluate();
    } catch (AssumptionViolatedException e) {
      eachNotifier.addFailedAssumption(e);
    } catch (Throwable e) {
      eachNotifier.addFailure(e);
    } finally {
      eachNotifier.fireTestFinished();
    }
  }
  



  protected Annotation[] getRunnerAnnotations()
  {
    return testClass.getAnnotations();
  }
  




  public Description getDescription()
  {
    Description description = Description.createSuiteDescription(getName(), getRunnerAnnotations());
    
    for (T child : getFilteredChildren()) {
      description.addChild(describeChild(child));
    }
    return description;
  }
  
  public void run(RunNotifier notifier)
  {
    EachTestNotifier testNotifier = new EachTestNotifier(notifier, getDescription());
    try
    {
      Statement statement = classBlock(notifier);
      statement.evaluate();
    } catch (AssumptionViolatedException e) {
      testNotifier.addFailedAssumption(e);
    } catch (StoppedByUserException e) {
      throw e;
    } catch (Throwable e) {
      testNotifier.addFailure(e);
    }
  }
  


  public void filter(Filter filter)
    throws NoTestsRemainException
  {
    synchronized (childrenLock) {
      List<T> children = new ArrayList(getFilteredChildren());
      for (Iterator<T> iter = children.iterator(); iter.hasNext();) {
        T each = iter.next();
        if (shouldRun(filter, each)) {
          try {
            filter.apply(each);
          } catch (NoTestsRemainException e) {
            iter.remove();
          }
        } else {
          iter.remove();
        }
      }
      filteredChildren = Collections.unmodifiableCollection(children);
      if (filteredChildren.isEmpty()) {
        throw new NoTestsRemainException();
      }
    }
  }
  
  public void sort(Sorter sorter) {
    synchronized (childrenLock) {
      for (T each : getFilteredChildren()) {
        sorter.apply(each);
      }
      List<T> sortedChildren = new ArrayList(getFilteredChildren());
      Collections.sort(sortedChildren, comparator(sorter));
      filteredChildren = Collections.unmodifiableCollection(sortedChildren);
    }
  }
  


  private void validate()
    throws InitializationError
  {
    List<Throwable> errors = new ArrayList();
    collectInitializationErrors(errors);
    if (!errors.isEmpty()) {
      throw new InitializationError(errors);
    }
  }
  
  private Collection<T> getFilteredChildren() {
    if (filteredChildren == null) {
      synchronized (childrenLock) {
        if (filteredChildren == null) {
          filteredChildren = Collections.unmodifiableCollection(getChildren());
        }
      }
    }
    return filteredChildren;
  }
  
  private boolean shouldRun(Filter filter, T each) {
    return filter.shouldRun(describeChild(each));
  }
  
  private Comparator<? super T> comparator(final Sorter sorter) {
    new Comparator() {
      public int compare(T o1, T o2) {
        return sorter.compare(describeChild(o1), describeChild(o2));
      }
    };
  }
  



  public void setScheduler(RunnerScheduler scheduler)
  {
    this.scheduler = scheduler;
  }
}

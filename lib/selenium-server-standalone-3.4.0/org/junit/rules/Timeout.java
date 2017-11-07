package org.junit.rules;

import java.util.concurrent.TimeUnit;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.internal.runners.statements.FailOnTimeout.Builder;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


































public class Timeout
  implements TestRule
{
  private final long timeout;
  private final TimeUnit timeUnit;
  private final boolean lookForStuckThread;
  
  public static Builder builder()
  {
    return new Builder();
  }
  











  @Deprecated
  public Timeout(int millis)
  {
    this(millis, TimeUnit.MILLISECONDS);
  }
  








  public Timeout(long timeout, TimeUnit timeUnit)
  {
    this.timeout = timeout;
    this.timeUnit = timeUnit;
    lookForStuckThread = false;
  }
  





  protected Timeout(Builder builder)
  {
    timeout = builder.getTimeout();
    timeUnit = builder.getTimeUnit();
    lookForStuckThread = builder.getLookingForStuckThread();
  }
  





  public static Timeout millis(long millis)
  {
    return new Timeout(millis, TimeUnit.MILLISECONDS);
  }
  





  public static Timeout seconds(long seconds)
  {
    return new Timeout(seconds, TimeUnit.SECONDS);
  }
  




  protected final long getTimeout(TimeUnit unit)
  {
    return unit.convert(timeout, timeUnit);
  }
  





  protected final boolean getLookingForStuckThread()
  {
    return lookForStuckThread;
  }
  







  protected Statement createFailOnTimeoutStatement(Statement statement)
    throws Exception
  {
    return FailOnTimeout.builder().withTimeout(timeout, timeUnit).withLookingForStuckThread(lookForStuckThread).build(statement);
  }
  

  public Statement apply(Statement base, Description description)
  {
    try
    {
      return createFailOnTimeoutStatement(base);
    } catch (Exception e) {
      new Statement() {
        public void evaluate() throws Throwable {
          throw new RuntimeException("Invalid parameters for Timeout", e);
        }
      };
    }
  }
  




  public static class Builder
  {
    private boolean lookForStuckThread = false;
    private long timeout = 0L;
    private TimeUnit timeUnit = TimeUnit.SECONDS;
    








    protected Builder() {}
    







    public Builder withTimeout(long timeout, TimeUnit unit)
    {
      this.timeout = timeout;
      timeUnit = unit;
      return this;
    }
    
    protected long getTimeout() {
      return timeout;
    }
    
    protected TimeUnit getTimeUnit() {
      return timeUnit;
    }
    








    public Builder withLookingForStuckThread(boolean enable)
    {
      lookForStuckThread = enable;
      return this;
    }
    
    protected boolean getLookingForStuckThread() {
      return lookForStuckThread;
    }
    



    public Timeout build()
    {
      return new Timeout(this);
    }
  }
}

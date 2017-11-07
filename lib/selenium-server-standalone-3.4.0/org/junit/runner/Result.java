package org.junit.runner;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunListener.ThreadSafe;



public class Result
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  private static final ObjectStreamField[] serialPersistentFields = ObjectStreamClass.lookup(SerializedForm.class).getFields();
  
  private final AtomicInteger count;
  
  private final AtomicInteger ignoreCount;
  private final CopyOnWriteArrayList<Failure> failures;
  private final AtomicLong runTime;
  private final AtomicLong startTime;
  private SerializedForm serializedForm;
  
  public Result()
  {
    count = new AtomicInteger();
    ignoreCount = new AtomicInteger();
    failures = new CopyOnWriteArrayList();
    runTime = new AtomicLong();
    startTime = new AtomicLong();
  }
  
  private Result(SerializedForm serializedForm) {
    count = fCount;
    ignoreCount = fIgnoreCount;
    failures = new CopyOnWriteArrayList(fFailures);
    runTime = new AtomicLong(fRunTime);
    startTime = new AtomicLong(fStartTime);
  }
  


  public int getRunCount()
  {
    return count.get();
  }
  


  public int getFailureCount()
  {
    return failures.size();
  }
  


  public long getRunTime()
  {
    return runTime.get();
  }
  


  public List<Failure> getFailures()
  {
    return failures;
  }
  


  public int getIgnoreCount()
  {
    return ignoreCount.get();
  }
  


  public boolean wasSuccessful()
  {
    return getFailureCount() == 0;
  }
  
  private void writeObject(ObjectOutputStream s) throws IOException {
    SerializedForm serializedForm = new SerializedForm(this);
    serializedForm.serialize(s);
  }
  
  private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException
  {
    serializedForm = SerializedForm.deserialize(s);
  }
  
  private Object readResolve() {
    return new Result(serializedForm);
  }
  
  @RunListener.ThreadSafe
  private class Listener extends RunListener {
    private Listener() {}
    
    public void testRunStarted(Description description) throws Exception { startTime.set(System.currentTimeMillis()); }
    
    public void testRunFinished(Result result)
      throws Exception
    {
      long endTime = System.currentTimeMillis();
      runTime.addAndGet(endTime - startTime.get());
    }
    
    public void testFinished(Description description) throws Exception
    {
      count.getAndIncrement();
    }
    
    public void testFailure(Failure failure) throws Exception
    {
      failures.add(failure);
    }
    
    public void testIgnored(Description description) throws Exception
    {
      ignoreCount.getAndIncrement();
    }
    



    public void testAssumptionFailure(Failure failure) {}
  }
  


  public RunListener createListener()
  {
    return new Listener(null);
  }
  

  private static class SerializedForm
    implements Serializable
  {
    private static final long serialVersionUID = 1L;
    private final AtomicInteger fCount;
    private final AtomicInteger fIgnoreCount;
    private final List<Failure> fFailures;
    private final long fRunTime;
    private final long fStartTime;
    
    public SerializedForm(Result result)
    {
      fCount = count;
      fIgnoreCount = ignoreCount;
      fFailures = Collections.synchronizedList(new ArrayList(failures));
      fRunTime = runTime.longValue();
      fStartTime = startTime.longValue();
    }
    
    private SerializedForm(ObjectInputStream.GetField fields) throws IOException
    {
      fCount = ((AtomicInteger)fields.get("fCount", null));
      fIgnoreCount = ((AtomicInteger)fields.get("fIgnoreCount", null));
      fFailures = ((List)fields.get("fFailures", null));
      fRunTime = fields.get("fRunTime", 0L);
      fStartTime = fields.get("fStartTime", 0L);
    }
    
    public void serialize(ObjectOutputStream s) throws IOException {
      ObjectOutputStream.PutField fields = s.putFields();
      fields.put("fCount", fCount);
      fields.put("fIgnoreCount", fIgnoreCount);
      fields.put("fFailures", fFailures);
      fields.put("fRunTime", fRunTime);
      fields.put("fStartTime", fStartTime);
      s.writeFields();
    }
    
    public static SerializedForm deserialize(ObjectInputStream s) throws ClassNotFoundException, IOException
    {
      ObjectInputStream.GetField fields = s.readFields();
      return new SerializedForm(fields);
    }
  }
}

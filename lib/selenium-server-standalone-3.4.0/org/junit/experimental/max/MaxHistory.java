package org.junit.experimental.max;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;












public class MaxHistory
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  public static MaxHistory forFolder(File file)
  {
    if (file.exists()) {
      try {
        return readHistory(file);
      } catch (CouldNotReadCoreException e) {
        e.printStackTrace();
        file.delete();
      }
    }
    return new MaxHistory(file);
  }
  
























  private final Map<String, Long> fDurations = new HashMap();
  private final Map<String, Long> fFailureTimestamps = new HashMap();
  private final File fHistoryStore;
  
  /* Error */
  private static MaxHistory readHistory(File storedResults)
    throws CouldNotReadCoreException
  {
    // Byte code:
    //   0: new 9	java/io/FileInputStream
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 10	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   8: astore_1
    //   9: new 11	java/io/ObjectInputStream
    //   12: dup
    //   13: aload_1
    //   14: invokespecial 12	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
    //   17: astore_2
    //   18: aload_2
    //   19: invokevirtual 13	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
    //   22: checkcast 7	org/junit/experimental/max/MaxHistory
    //   25: astore_3
    //   26: aload_2
    //   27: invokevirtual 14	java/io/ObjectInputStream:close	()V
    //   30: aload_1
    //   31: invokevirtual 15	java/io/FileInputStream:close	()V
    //   34: aload_3
    //   35: areturn
    //   36: astore 4
    //   38: aload_2
    //   39: invokevirtual 14	java/io/ObjectInputStream:close	()V
    //   42: aload 4
    //   44: athrow
    //   45: astore 5
    //   47: aload_1
    //   48: invokevirtual 15	java/io/FileInputStream:close	()V
    //   51: aload 5
    //   53: athrow
    //   54: astore_1
    //   55: new 4	org/junit/experimental/max/CouldNotReadCoreException
    //   58: dup
    //   59: aload_1
    //   60: invokespecial 17	org/junit/experimental/max/CouldNotReadCoreException:<init>	(Ljava/lang/Throwable;)V
    //   63: athrow
    // Line number table:
    //   Java source line #48	-> byte code offset #0
    //   Java source line #50	-> byte code offset #9
    //   Java source line #52	-> byte code offset #18
    //   Java source line #54	-> byte code offset #26
    //   Java source line #57	-> byte code offset #30
    //   Java source line #54	-> byte code offset #36
    //   Java source line #57	-> byte code offset #45
    //   Java source line #59	-> byte code offset #54
    //   Java source line #60	-> byte code offset #55
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	64	0	storedResults	File
    //   8	40	1	file	java.io.FileInputStream
    //   54	6	1	e	Exception
    //   17	22	2	stream	java.io.ObjectInputStream
    //   36	7	4	localObject1	Object
    //   45	7	5	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   18	26	36	finally
    //   36	38	36	finally
    //   9	30	45	finally
    //   36	47	45	finally
    //   0	34	54	java/lang/Exception
    //   36	54	54	java/lang/Exception
  }
  
  private MaxHistory(File storedResults)
  {
    fHistoryStore = storedResults;
  }
  
  private void save() throws IOException {
    ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(fHistoryStore));
    
    stream.writeObject(this);
    stream.close();
  }
  
  Long getFailureTimestamp(Description key) {
    return (Long)fFailureTimestamps.get(key.toString());
  }
  
  void putTestFailureTimestamp(Description key, long end) {
    fFailureTimestamps.put(key.toString(), Long.valueOf(end));
  }
  
  boolean isNewTest(Description key) {
    return !fDurations.containsKey(key.toString());
  }
  
  Long getTestDuration(Description key) {
    return (Long)fDurations.get(key.toString());
  }
  
  void putTestDuration(Description description, long duration) {
    fDurations.put(description.toString(), Long.valueOf(duration));
  }
  
  private final class RememberingListener extends RunListener {
    private long overallStart = System.currentTimeMillis();
    
    private Map<Description, Long> starts = new HashMap();
    
    private RememberingListener() {}
    
    public void testStarted(Description description) throws Exception { starts.put(description, Long.valueOf(System.nanoTime())); }
    

    public void testFinished(Description description)
      throws Exception
    {
      long end = System.nanoTime();
      long start = ((Long)starts.get(description)).longValue();
      putTestDuration(description, end - start);
    }
    
    public void testFailure(Failure failure) throws Exception
    {
      putTestFailureTimestamp(failure.getDescription(), overallStart);
    }
    


    public void testRunFinished(Result result) throws Exception { MaxHistory.this.save(); }
  }
  
  private class TestComparator implements Comparator<Description> {
    private TestComparator() {}
    
    public int compare(Description o1, Description o2) {
      if (isNewTest(o1)) {
        return -1;
      }
      if (isNewTest(o2)) {
        return 1;
      }
      
      int result = getFailure(o2).compareTo(getFailure(o1));
      return result != 0 ? result : getTestDuration(o1).compareTo(getTestDuration(o2));
    }
    

    private Long getFailure(Description key)
    {
      Long result = getFailureTimestamp(key);
      if (result == null) {
        return Long.valueOf(0L);
      }
      return result;
    }
  }
  



  public RunListener listener()
  {
    return new RememberingListener(null);
  }
  



  public Comparator<Description> testComparator()
  {
    return new TestComparator(null);
  }
}

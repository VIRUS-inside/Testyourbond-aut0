package org.apache.commons.exec;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;























public class ShutdownHookProcessDestroyer
  implements ProcessDestroyer, Runnable
{
  private final Vector<Process> processes = new Vector();
  

  private ProcessDestroyerImpl destroyProcessThread = null;
  

  private boolean added = false;
  



  private volatile boolean running = false;
  public ShutdownHookProcessDestroyer() {}
  
  private class ProcessDestroyerImpl extends Thread {
    private boolean shouldDestroy = true;
    
    public ProcessDestroyerImpl() {
      super();
    }
    
    public void run()
    {
      if (shouldDestroy) {
        ShutdownHookProcessDestroyer.this.run();
      }
    }
    
    public void setShouldDestroy(boolean shouldDestroy) {
      this.shouldDestroy = shouldDestroy;
    }
  }
  
















  private void addShutdownHook()
  {
    if (!running) {
      destroyProcessThread = new ProcessDestroyerImpl();
      Runtime.getRuntime().addShutdownHook(destroyProcessThread);
      added = true;
    }
  }
  



  private void removeShutdownHook()
  {
    if ((added) && (!running)) {
      boolean removed = Runtime.getRuntime().removeShutdownHook(destroyProcessThread);
      
      if (!removed) {
        System.err.println("Could not remove shutdown hook");
      }
      





      destroyProcessThread.setShouldDestroy(false);
      destroyProcessThread.start();
      try
      {
        destroyProcessThread.join(20000L);
      }
      catch (InterruptedException ie) {}
      

      destroyProcessThread = null;
      added = false;
    }
  }
  





  public boolean isAddedAsShutdownHook()
  {
    return added;
  }
  








  public boolean add(Process process)
  {
    synchronized (processes)
    {
      if (processes.size() == 0) {
        addShutdownHook();
      }
      processes.addElement(process);
      return processes.contains(process);
    }
  }
  








  public boolean remove(Process process)
  {
    synchronized (processes) {
      boolean processRemoved = processes.removeElement(process);
      if ((processRemoved) && (processes.size() == 0)) {
        removeShutdownHook();
      }
      return processRemoved;
    }
  }
  




  public int size()
  {
    return processes.size();
  }
  


  public void run()
  {
    synchronized (processes) {
      running = true;
      Enumeration<Process> e = processes.elements();
      while (e.hasMoreElements()) {
        Process process = (Process)e.nextElement();
        try {
          process.destroy();
        }
        catch (Throwable t) {
          System.err.println("Unable to terminate process during process shutdown");
        }
      }
    }
  }
}

package com.google.common.base.internal;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
































public class Finalizer
  implements Runnable
{
  private static final Logger logger = Logger.getLogger(Finalizer.class.getName());
  



  private static final String FINALIZABLE_REFERENCE = "com.google.common.base.FinalizableReference";
  



  private final WeakReference<Class<?>> finalizableReferenceClassReference;
  



  private final PhantomReference<Object> frqReference;
  


  private final ReferenceQueue<Object> queue;
  



  public static void startFinalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference)
  {
    if (!finalizableReferenceClass.getName().equals("com.google.common.base.FinalizableReference")) {
      throw new IllegalArgumentException("Expected com.google.common.base.FinalizableReference.");
    }
    
    Finalizer finalizer = new Finalizer(finalizableReferenceClass, queue, frqReference);
    Thread thread = new Thread(finalizer);
    thread.setName(Finalizer.class.getName());
    thread.setDaemon(true);
    try
    {
      if (inheritableThreadLocals != null) {
        inheritableThreadLocals.set(thread, null);
      }
    } catch (Throwable t) {
      logger.log(Level.INFO, "Failed to clear thread local values inherited by reference finalizer thread.", t);
    }
    



    thread.start();
  }
  




  private static final Field inheritableThreadLocals = getInheritableThreadLocalsField();
  



  private Finalizer(Class<?> finalizableReferenceClass, ReferenceQueue<Object> queue, PhantomReference<Object> frqReference)
  {
    this.queue = queue;
    
    finalizableReferenceClassReference = new WeakReference(finalizableReferenceClass);
    


    this.frqReference = frqReference;
  }
  




  public void run()
  {
    try
    {
      while (cleanUp(queue.remove())) {}
    }
    catch (InterruptedException localInterruptedException) {}
  }
  









  private boolean cleanUp(Reference<?> reference)
  {
    Method finalizeReferentMethod = getFinalizeReferentMethod();
    if (finalizeReferentMethod == null) {
      return false;
    }
    


    do
    {
      reference.clear();
      
      if (reference == frqReference)
      {


        return false;
      }
      try
      {
        finalizeReferentMethod.invoke(reference, new Object[0]);
      } catch (Throwable t) {
        logger.log(Level.SEVERE, "Error cleaning up after reference.", t);


      }
      

    }
    while ((reference = queue.poll()) != null);
    return true;
  }
  
  @Nullable
  private Method getFinalizeReferentMethod()
  {
    Class<?> finalizableReferenceClass = (Class)finalizableReferenceClassReference.get();
    if (finalizableReferenceClass == null)
    {





      return null;
    }
    try {
      return finalizableReferenceClass.getMethod("finalizeReferent", new Class[0]);
    } catch (NoSuchMethodException e) {
      throw new AssertionError(e);
    }
  }
  
  @Nullable
  public static Field getInheritableThreadLocalsField() {
    try {
      Field inheritableThreadLocals = Thread.class.getDeclaredField("inheritableThreadLocals");
      inheritableThreadLocals.setAccessible(true);
      return inheritableThreadLocals;
    } catch (Throwable t) {
      logger.log(Level.INFO, "Couldn't access Thread.inheritableThreadLocals. Reference finalizer threads will inherit thread local values.");
    }
    

    return null;
  }
}

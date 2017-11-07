package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;










































































@Beta
@GwtIncompatible
public final class Closer
  implements Closeable
{
  private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE;
  
  @VisibleForTesting
  final Suppressor suppressor;
  

  public static Closer create()
  {
    return new Closer(SUPPRESSOR);
  }
  



  private final Deque<Closeable> stack = new ArrayDeque(4);
  private Throwable thrown;
  
  @VisibleForTesting
  Closer(Suppressor suppressor) {
    this.suppressor = ((Suppressor)Preconditions.checkNotNull(suppressor));
  }
  






  @CanIgnoreReturnValue
  public <C extends Closeable> C register(@Nullable C closeable)
  {
    if (closeable != null) {
      stack.addFirst(closeable);
    }
    
    return closeable;
  }
  











  public RuntimeException rethrow(Throwable e)
    throws IOException
  {
    Preconditions.checkNotNull(e);
    thrown = e;
    Throwables.propagateIfPossible(e, IOException.class);
    throw new RuntimeException(e);
  }
  













  public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType)
    throws IOException, Exception
  {
    Preconditions.checkNotNull(e);
    thrown = e;
    Throwables.propagateIfPossible(e, IOException.class);
    Throwables.propagateIfPossible(e, declaredType);
    throw new RuntimeException(e);
  }
  














  public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2)
    throws IOException, Exception, Exception
  {
    Preconditions.checkNotNull(e);
    thrown = e;
    Throwables.propagateIfPossible(e, IOException.class);
    Throwables.propagateIfPossible(e, declaredType1, declaredType2);
    throw new RuntimeException(e);
  }
  






  public void close()
    throws IOException
  {
    Throwable throwable = thrown;
    

    while (!stack.isEmpty()) {
      Closeable closeable = (Closeable)stack.removeFirst();
      try {
        closeable.close();
      } catch (Throwable e) {
        if (throwable == null) {
          throwable = e;
        } else {
          suppressor.suppress(closeable, throwable, e);
        }
      }
    }
    
    if ((thrown == null) && (throwable != null)) {
      Throwables.propagateIfPossible(throwable, IOException.class);
      throw new AssertionError(throwable);
    }
  }
  





  @VisibleForTesting
  static abstract interface Suppressor
  {
    public abstract void suppress(Closeable paramCloseable, Throwable paramThrowable1, Throwable paramThrowable2);
  }
  




  @VisibleForTesting
  static final class LoggingSuppressor
    implements Closer.Suppressor
  {
    static final LoggingSuppressor INSTANCE = new LoggingSuppressor();
    
    LoggingSuppressor() {}
    
    public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed) {
      Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
    }
  }
  

  @VisibleForTesting
  static final class SuppressingSuppressor
    implements Closer.Suppressor
  {
    SuppressingSuppressor() {}
    

    static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
    
    static boolean isAvailable() {
      return addSuppressed != null; }
    

    static final Method addSuppressed = getAddSuppressed();
    
    private static Method getAddSuppressed() {
      try {
        return Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
      } catch (Throwable e) {}
      return null;
    }
    


    public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed)
    {
      if (thrown == suppressed) {
        return;
      }
      try {
        addSuppressed.invoke(thrown, new Object[] { suppressed });
      }
      catch (Throwable e) {
        Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
      }
    }
  }
}

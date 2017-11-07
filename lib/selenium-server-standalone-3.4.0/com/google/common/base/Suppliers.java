package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;




























@GwtCompatible
public final class Suppliers
{
  private Suppliers() {}
  
  public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier)
  {
    Preconditions.checkNotNull(function);
    Preconditions.checkNotNull(supplier);
    return new SupplierComposition(function, supplier);
  }
  
  private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
    final Function<? super F, T> function;
    final Supplier<F> supplier;
    private static final long serialVersionUID = 0L;
    
    SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) { this.function = function;
      this.supplier = supplier;
    }
    
    public T get()
    {
      return function.apply(supplier.get());
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof SupplierComposition)) {
        SupplierComposition<?, ?> that = (SupplierComposition)obj;
        return (function.equals(function)) && (supplier.equals(supplier));
      }
      return false;
    }
    
    public int hashCode()
    {
      return Objects.hashCode(new Object[] { function, supplier });
    }
    
    public String toString()
    {
      return "Suppliers.compose(" + function + ", " + supplier + ")";
    }
  }
  













  public static <T> Supplier<T> memoize(Supplier<T> delegate)
  {
    if (((delegate instanceof NonSerializableMemoizingSupplier)) || ((delegate instanceof MemoizingSupplier)))
    {
      return delegate;
    }
    return (delegate instanceof Serializable) ? new MemoizingSupplier(delegate) : new NonSerializableMemoizingSupplier(delegate);
  }
  
  @VisibleForTesting
  static class MemoizingSupplier<T>
    implements Supplier<T>, Serializable
  {
    final Supplier<T> delegate;
    volatile transient boolean initialized;
    transient T value;
    private static final long serialVersionUID = 0L;
    
    MemoizingSupplier(Supplier<T> delegate)
    {
      this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
    }
    

    public T get()
    {
      if (!initialized) {
        synchronized (this) {
          if (!initialized) {
            T t = delegate.get();
            value = t;
            initialized = true;
            return t;
          }
        }
      }
      return value;
    }
    
    public String toString()
    {
      return "Suppliers.memoize(" + delegate + ")";
    }
  }
  

  @VisibleForTesting
  static class NonSerializableMemoizingSupplier<T>
    implements Supplier<T>
  {
    volatile Supplier<T> delegate;
    volatile boolean initialized;
    T value;
    
    NonSerializableMemoizingSupplier(Supplier<T> delegate)
    {
      this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
    }
    

    public T get()
    {
      if (!initialized) {
        synchronized (this) {
          if (!initialized) {
            T t = delegate.get();
            value = t;
            initialized = true;
            
            delegate = null;
            return t;
          }
        }
      }
      return value;
    }
    
    public String toString()
    {
      return "Suppliers.memoize(" + delegate + ")";
    }
  }
  

















  public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit)
  {
    return new ExpiringMemoizingSupplier(delegate, duration, unit);
  }
  
  @VisibleForTesting
  static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
    final Supplier<T> delegate;
    final long durationNanos;
    volatile transient T value;
    volatile transient long expirationNanos;
    private static final long serialVersionUID = 0L;
    
    ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
      this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
      durationNanos = unit.toNanos(duration);
      Preconditions.checkArgument(duration > 0L);
    }
    






    public T get()
    {
      long nanos = expirationNanos;
      long now = Platform.systemNanoTime();
      if ((nanos == 0L) || (now - nanos >= 0L)) {
        synchronized (this) {
          if (nanos == expirationNanos) {
            T t = delegate.get();
            value = t;
            nanos = now + durationNanos;
            

            expirationNanos = (nanos == 0L ? 1L : nanos);
            return t;
          }
        }
      }
      return value;
    }
    


    public String toString()
    {
      return "Suppliers.memoizeWithExpiration(" + delegate + ", " + durationNanos + ", NANOS)";
    }
  }
  




  public static <T> Supplier<T> ofInstance(@Nullable T instance)
  {
    return new SupplierOfInstance(instance);
  }
  
  private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
    final T instance;
    private static final long serialVersionUID = 0L;
    
    SupplierOfInstance(@Nullable T instance) { this.instance = instance; }
    

    public T get()
    {
      return instance;
    }
    
    public boolean equals(@Nullable Object obj)
    {
      if ((obj instanceof SupplierOfInstance)) {
        SupplierOfInstance<?> that = (SupplierOfInstance)obj;
        return Objects.equal(instance, instance);
      }
      return false;
    }
    
    public int hashCode()
    {
      return Objects.hashCode(new Object[] { instance });
    }
    
    public String toString()
    {
      return "Suppliers.ofInstance(" + instance + ")";
    }
  }
  





  public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate)
  {
    return new ThreadSafeSupplier((Supplier)Preconditions.checkNotNull(delegate));
  }
  
  private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
    final Supplier<T> delegate;
    private static final long serialVersionUID = 0L;
    
    ThreadSafeSupplier(Supplier<T> delegate) { this.delegate = delegate; }
    

    public T get()
    {
      synchronized (delegate) {
        return delegate.get();
      }
    }
    
    public String toString()
    {
      return "Suppliers.synchronizedSupplier(" + delegate + ")";
    }
  }
  










  public static <T> Function<Supplier<T>, T> supplierFunction()
  {
    SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
    return sf;
  }
  
  private static abstract interface SupplierFunction<T> extends Function<Supplier<T>, T>
  {}
  
  private static enum SupplierFunctionImpl implements Suppliers.SupplierFunction<Object> { INSTANCE;
    
    private SupplierFunctionImpl() {}
    
    public Object apply(Supplier<Object> input) {
      return input.get();
    }
    
    public String toString()
    {
      return "Suppliers.supplierFunction()";
    }
  }
}

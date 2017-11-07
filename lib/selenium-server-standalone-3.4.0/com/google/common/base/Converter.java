package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;








































































































@GwtCompatible
public abstract class Converter<A, B>
  implements Function<A, B>
{
  private final boolean handleNullAutomatically;
  @LazyInit
  private transient Converter<B, A> reverse;
  
  protected Converter()
  {
    this(true);
  }
  


  Converter(boolean handleNullAutomatically)
  {
    this.handleNullAutomatically = handleNullAutomatically;
  }
  









  protected abstract B doForward(A paramA);
  









  protected abstract A doBackward(B paramB);
  









  @Nullable
  @CanIgnoreReturnValue
  public final B convert(@Nullable A a)
  {
    return correctedDoForward(a);
  }
  
  @Nullable
  B correctedDoForward(@Nullable A a) {
    if (handleNullAutomatically)
    {
      return a == null ? null : Preconditions.checkNotNull(doForward(a));
    }
    return doForward(a);
  }
  
  @Nullable
  A correctedDoBackward(@Nullable B b)
  {
    if (handleNullAutomatically)
    {
      return b == null ? null : Preconditions.checkNotNull(doBackward(b));
    }
    return doBackward(b);
  }
  








  @CanIgnoreReturnValue
  public Iterable<B> convertAll(final Iterable<? extends A> fromIterable)
  {
    Preconditions.checkNotNull(fromIterable, "fromIterable");
    new Iterable()
    {
      public Iterator<B> iterator() {
        new Iterator() {
          private final Iterator<? extends A> fromIterator = val$fromIterable.iterator();
          
          public boolean hasNext()
          {
            return fromIterator.hasNext();
          }
          
          public B next()
          {
            return convert(fromIterator.next());
          }
          
          public void remove()
          {
            fromIterator.remove();
          }
        };
      }
    };
  }
  







  @CanIgnoreReturnValue
  public Converter<B, A> reverse()
  {
    Converter<B, A> result = reverse;
    return result == null ? (this.reverse = new ReverseConverter(this)) : result;
  }
  
  private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
    final Converter<A, B> original;
    private static final long serialVersionUID = 0L;
    
    ReverseConverter(Converter<A, B> original) {
      this.original = original;
    }
    







    protected A doForward(B b)
    {
      throw new AssertionError();
    }
    
    protected B doBackward(A a)
    {
      throw new AssertionError();
    }
    
    @Nullable
    A correctedDoForward(@Nullable B b)
    {
      return original.correctedDoBackward(b);
    }
    
    @Nullable
    B correctedDoBackward(@Nullable A a)
    {
      return original.correctedDoForward(a);
    }
    
    public Converter<A, B> reverse()
    {
      return original;
    }
    
    public boolean equals(@Nullable Object object)
    {
      if ((object instanceof ReverseConverter)) {
        ReverseConverter<?, ?> that = (ReverseConverter)object;
        return original.equals(original);
      }
      return false;
    }
    
    public int hashCode()
    {
      return original.hashCode() ^ 0xFFFFFFFF;
    }
    
    public String toString()
    {
      return original + ".reverse()";
    }
  }
  








  public final <C> Converter<A, C> andThen(Converter<B, C> secondConverter)
  {
    return doAndThen(secondConverter);
  }
  


  <C> Converter<A, C> doAndThen(Converter<B, C> secondConverter)
  {
    return new ConverterComposition(this, (Converter)Preconditions.checkNotNull(secondConverter));
  }
  
  private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
    final Converter<A, B> first;
    final Converter<B, C> second;
    private static final long serialVersionUID = 0L;
    
    ConverterComposition(Converter<A, B> first, Converter<B, C> second) {
      this.first = first;
      this.second = second;
    }
    







    protected C doForward(A a)
    {
      throw new AssertionError();
    }
    
    protected A doBackward(C c)
    {
      throw new AssertionError();
    }
    
    @Nullable
    C correctedDoForward(@Nullable A a)
    {
      return second.correctedDoForward(first.correctedDoForward(a));
    }
    
    @Nullable
    A correctedDoBackward(@Nullable C c)
    {
      return first.correctedDoBackward(second.correctedDoBackward(c));
    }
    
    public boolean equals(@Nullable Object object)
    {
      if ((object instanceof ConverterComposition)) {
        ConverterComposition<?, ?, ?> that = (ConverterComposition)object;
        return (first.equals(first)) && (second.equals(second));
      }
      return false;
    }
    
    public int hashCode()
    {
      return 31 * first.hashCode() + second.hashCode();
    }
    
    public String toString()
    {
      return first + ".andThen(" + second + ")";
    }
  }
  





  @Deprecated
  @Nullable
  @CanIgnoreReturnValue
  public final B apply(@Nullable A a)
  {
    return convert(a);
  }
  











  public boolean equals(@Nullable Object object)
  {
    return super.equals(object);
  }
  

















  public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction)
  {
    return new FunctionBasedConverter(forwardFunction, backwardFunction, null);
  }
  
  private static final class FunctionBasedConverter<A, B>
    extends Converter<A, B> implements Serializable
  {
    private final Function<? super A, ? extends B> forwardFunction;
    private final Function<? super B, ? extends A> backwardFunction;
    
    private FunctionBasedConverter(Function<? super A, ? extends B> forwardFunction, Function<? super B, ? extends A> backwardFunction)
    {
      this.forwardFunction = ((Function)Preconditions.checkNotNull(forwardFunction));
      this.backwardFunction = ((Function)Preconditions.checkNotNull(backwardFunction));
    }
    
    protected B doForward(A a)
    {
      return forwardFunction.apply(a);
    }
    
    protected A doBackward(B b)
    {
      return backwardFunction.apply(b);
    }
    
    public boolean equals(@Nullable Object object)
    {
      if ((object instanceof FunctionBasedConverter)) {
        FunctionBasedConverter<?, ?> that = (FunctionBasedConverter)object;
        return (forwardFunction.equals(forwardFunction)) && 
          (backwardFunction.equals(backwardFunction));
      }
      return false;
    }
    
    public int hashCode()
    {
      return forwardFunction.hashCode() * 31 + backwardFunction.hashCode();
    }
    
    public String toString()
    {
      return "Converter.from(" + forwardFunction + ", " + backwardFunction + ")";
    }
  }
  



  public static <T> Converter<T, T> identity()
  {
    return IdentityConverter.INSTANCE;
  }
  

  private static final class IdentityConverter<T>
    extends Converter<T, T>
    implements Serializable
  {
    static final IdentityConverter INSTANCE = new IdentityConverter();
    
    private IdentityConverter() {}
    
    protected T doForward(T t) { return t; }
    

    protected T doBackward(T t)
    {
      return t;
    }
    
    public IdentityConverter<T> reverse()
    {
      return this;
    }
    
    <S> Converter<T, S> doAndThen(Converter<T, S> otherConverter)
    {
      return (Converter)Preconditions.checkNotNull(otherConverter, "otherConverter");
    }
    


    private static final long serialVersionUID = 0L;
    

    public String toString()
    {
      return "Converter.identity()";
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
  }
}

package org.hamcrest;










public abstract class Condition<T>
{
  public static final NotMatched<Object> NOT_MATCHED = new NotMatched(null);
  


  private Condition() {}
  

  public abstract boolean matching(Matcher<T> paramMatcher, String paramString);
  
  public abstract <U> Condition<U> and(Step<? super T, U> paramStep);
  
  public final boolean matching(Matcher<T> match) { return matching(match, ""); }
  public final <U> Condition<U> then(Step<? super T, U> mapping) { return and(mapping); }
  
  public static <T> Condition<T> notMatched()
  {
    return NOT_MATCHED;
  }
  

  public static <T> Condition<T> matched(T theValue, Description mismatch) { return new Matched(theValue, mismatch, null); }
  
  public static abstract interface Step<I, O> { public abstract Condition<O> apply(I paramI, Description paramDescription); }
  
  private static final class Matched<T> extends Condition<T> { private final T theValue;
    private final Description mismatch;
    
    private Matched(T theValue, Description mismatch) { super();
      this.theValue = theValue;
      this.mismatch = mismatch;
    }
    
    public boolean matching(Matcher<T> matcher, String message)
    {
      if (matcher.matches(theValue)) {
        return true;
      }
      mismatch.appendText(message);
      matcher.describeMismatch(theValue, mismatch);
      return false;
    }
    


    public <U> Condition<U> and(Condition.Step<? super T, U> next) { return next.apply(theValue, mismatch); }
  }
  
  private static final class NotMatched<T> extends Condition<T> {
    private NotMatched() { super(); }
    public boolean matching(Matcher<T> match, String message) { return false; }
    
    public <U> Condition<U> and(Condition.Step<? super T, U> mapping) {
      return notMatched();
    }
  }
}

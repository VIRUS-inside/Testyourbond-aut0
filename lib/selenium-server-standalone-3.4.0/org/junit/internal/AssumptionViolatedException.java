package org.junit.internal;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;













public class AssumptionViolatedException
  extends RuntimeException
  implements SelfDescribing
{
  private static final long serialVersionUID = 2L;
  private final String fAssumption;
  private final boolean fValueMatcher;
  private final Object fValue;
  private final Matcher<?> fMatcher;
  
  @Deprecated
  public AssumptionViolatedException(String assumption, boolean hasValue, Object value, Matcher<?> matcher)
  {
    fAssumption = assumption;
    fValue = value;
    fMatcher = matcher;
    fValueMatcher = hasValue;
    
    if ((value instanceof Throwable)) {
      initCause((Throwable)value);
    }
  }
  





  @Deprecated
  public AssumptionViolatedException(Object value, Matcher<?> matcher)
  {
    this(null, true, value, matcher);
  }
  





  @Deprecated
  public AssumptionViolatedException(String assumption, Object value, Matcher<?> matcher)
  {
    this(assumption, true, value, matcher);
  }
  




  @Deprecated
  public AssumptionViolatedException(String assumption)
  {
    this(assumption, false, null, null);
  }
  




  @Deprecated
  public AssumptionViolatedException(String assumption, Throwable e)
  {
    this(assumption, false, null, null);
    initCause(e);
  }
  
  public String getMessage()
  {
    return StringDescription.asString(this);
  }
  
  public void describeTo(Description description) {
    if (fAssumption != null) {
      description.appendText(fAssumption);
    }
    
    if (fValueMatcher)
    {
      if (fAssumption != null) {
        description.appendText(": ");
      }
      
      description.appendText("got: ");
      description.appendValue(fValue);
      
      if (fMatcher != null) {
        description.appendText(", expected: ");
        description.appendDescriptionOf(fMatcher);
      }
    }
  }
}

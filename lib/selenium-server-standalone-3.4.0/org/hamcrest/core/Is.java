package org.hamcrest.core;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;








public class Is<T>
  extends BaseMatcher<T>
{
  private final Matcher<T> matcher;
  
  public Is(Matcher<T> matcher)
  {
    this.matcher = matcher;
  }
  
  public boolean matches(Object arg)
  {
    return matcher.matches(arg);
  }
  
  public void describeTo(Description description)
  {
    description.appendText("is ").appendDescriptionOf(matcher);
  }
  
  public void describeMismatch(Object item, Description mismatchDescription)
  {
    matcher.describeMismatch(item, mismatchDescription);
  }
  









  @Factory
  public static <T> Matcher<T> is(Matcher<T> matcher)
  {
    return new Is(matcher);
  }
  








  @Factory
  public static <T> Matcher<T> is(T value)
  {
    return is(IsEqual.equalTo(value));
  }
  









  @Factory
  @Deprecated
  public static <T> Matcher<T> is(Class<T> type)
  {
    Matcher<T> typeMatcher = IsInstanceOf.instanceOf(type);
    return is(typeMatcher);
  }
  








  @Factory
  public static <T> Matcher<T> isA(Class<T> type)
  {
    Matcher<T> typeMatcher = IsInstanceOf.instanceOf(type);
    return is(typeMatcher);
  }
}

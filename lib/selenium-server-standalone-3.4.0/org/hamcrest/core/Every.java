package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class Every<T> extends org.hamcrest.TypeSafeDiagnosingMatcher<Iterable<T>>
{
  private final Matcher<? super T> matcher;
  
  public Every(Matcher<? super T> matcher)
  {
    this.matcher = matcher;
  }
  
  public boolean matchesSafely(Iterable<T> collection, Description mismatchDescription)
  {
    for (T t : collection) {
      if (!matcher.matches(t)) {
        mismatchDescription.appendText("an item ");
        matcher.describeMismatch(t, mismatchDescription);
        return false;
      }
    }
    return true;
  }
  
  public void describeTo(Description description)
  {
    description.appendText("every item is ").appendDescriptionOf(matcher);
  }
  










  @org.hamcrest.Factory
  public static <U> Matcher<Iterable<U>> everyItem(Matcher<U> itemMatcher)
  {
    return new Every(itemMatcher);
  }
}

package org.hamcrest.core;

import org.hamcrest.Matcher;

public class CombinableMatcher<T> extends org.hamcrest.TypeSafeDiagnosingMatcher<T>
{
  private final Matcher<? super T> matcher;
  
  public CombinableMatcher(Matcher<? super T> matcher)
  {
    this.matcher = matcher;
  }
  
  protected boolean matchesSafely(T item, org.hamcrest.Description mismatch)
  {
    if (!matcher.matches(item)) {
      matcher.describeMismatch(item, mismatch);
      return false;
    }
    return true;
  }
  
  public void describeTo(org.hamcrest.Description description)
  {
    description.appendDescriptionOf(matcher);
  }
  
  public CombinableMatcher<T> and(Matcher<? super T> other) {
    return new CombinableMatcher(new AllOf(templatedListWith(other)));
  }
  
  public CombinableMatcher<T> or(Matcher<? super T> other) {
    return new CombinableMatcher(new AnyOf(templatedListWith(other)));
  }
  
  private java.util.ArrayList<Matcher<? super T>> templatedListWith(Matcher<? super T> other) {
    java.util.ArrayList<Matcher<? super T>> matchers = new java.util.ArrayList();
    matchers.add(matcher);
    matchers.add(other);
    return matchers;
  }
  





  @org.hamcrest.Factory
  public static <LHS> CombinableBothMatcher<LHS> both(Matcher<? super LHS> matcher)
  {
    return new CombinableBothMatcher(matcher);
  }
  
  public static final class CombinableBothMatcher<X> {
    private final Matcher<? super X> first;
    
    public CombinableBothMatcher(Matcher<? super X> matcher) { first = matcher; }
    
    public CombinableMatcher<X> and(Matcher<? super X> other) {
      return new CombinableMatcher(first).and(other);
    }
  }
  





  @org.hamcrest.Factory
  public static <LHS> CombinableEitherMatcher<LHS> either(Matcher<? super LHS> matcher)
  {
    return new CombinableEitherMatcher(matcher);
  }
  
  public static final class CombinableEitherMatcher<X> {
    private final Matcher<? super X> first;
    
    public CombinableEitherMatcher(Matcher<? super X> matcher) { first = matcher; }
    
    public CombinableMatcher<X> or(Matcher<? super X> other) {
      return new CombinableMatcher(first).or(other);
    }
  }
}

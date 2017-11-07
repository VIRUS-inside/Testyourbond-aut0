package org.junit.rules;

import java.util.ArrayList;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.matchers.JUnitMatchers;




class ExpectedExceptionMatcherBuilder
{
  ExpectedExceptionMatcherBuilder() {}
  
  private final List<Matcher<?>> matchers = new ArrayList();
  
  void add(Matcher<?> matcher) {
    matchers.add(matcher);
  }
  
  boolean expectsThrowable() {
    return !matchers.isEmpty();
  }
  
  Matcher<Throwable> build() {
    return JUnitMatchers.isThrowable(allOfTheMatchers());
  }
  
  private Matcher<Throwable> allOfTheMatchers() {
    if (matchers.size() == 1) {
      return cast((Matcher)matchers.get(0));
    }
    return CoreMatchers.allOf(castedMatchers());
  }
  
  private List<Matcher<? super Throwable>> castedMatchers()
  {
    return new ArrayList(matchers);
  }
  
  private Matcher<Throwable> cast(Matcher<?> singleMatcher)
  {
    return singleMatcher;
  }
}

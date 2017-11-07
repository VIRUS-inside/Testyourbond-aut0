package org.hamcrest.core;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

abstract class ShortcutCombination<T> extends org.hamcrest.BaseMatcher<T>
{
  private final Iterable<Matcher<? super T>> matchers;
  
  public ShortcutCombination(Iterable<Matcher<? super T>> matchers)
  {
    this.matchers = matchers;
  }
  

  public abstract boolean matches(Object paramObject);
  
  public abstract void describeTo(Description paramDescription);
  
  protected boolean matches(Object o, boolean shortcut)
  {
    for (Matcher<? super T> matcher : matchers) {
      if (matcher.matches(o) == shortcut) {
        return shortcut;
      }
    }
    return !shortcut;
  }
  
  public void describeTo(Description description, String operator) {
    description.appendList("(", " " + operator + " ", ")", matchers);
  }
}

package org.hamcrest.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;




public class AllOf<T>
  extends DiagnosingMatcher<T>
{
  private final Iterable<Matcher<? super T>> matchers;
  
  public AllOf(Iterable<Matcher<? super T>> matchers)
  {
    this.matchers = matchers;
  }
  
  public boolean matches(Object o, Description mismatch)
  {
    for (Matcher<? super T> matcher : matchers) {
      if (!matcher.matches(o)) {
        mismatch.appendDescriptionOf(matcher).appendText(" ");
        matcher.describeMismatch(o, mismatch);
        return false;
      }
    }
    return true;
  }
  
  public void describeTo(Description description)
  {
    description.appendList("(", " and ", ")", matchers);
  }
  





  @Factory
  public static <T> Matcher<T> allOf(Iterable<Matcher<? super T>> matchers)
  {
    return new AllOf(matchers);
  }
  





  @Factory
  public static <T> Matcher<T> allOf(Matcher<? super T>... matchers)
  {
    return allOf(Arrays.asList(matchers));
  }
  





  @Factory
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second)
  {
    List<Matcher<? super T>> matchers = new ArrayList(2);
    matchers.add(first);
    matchers.add(second);
    return allOf(matchers);
  }
  





  @Factory
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third)
  {
    List<Matcher<? super T>> matchers = new ArrayList(3);
    matchers.add(first);
    matchers.add(second);
    matchers.add(third);
    return allOf(matchers);
  }
  





  @Factory
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth)
  {
    List<Matcher<? super T>> matchers = new ArrayList(4);
    matchers.add(first);
    matchers.add(second);
    matchers.add(third);
    matchers.add(fourth);
    return allOf(matchers);
  }
  





  @Factory
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth)
  {
    List<Matcher<? super T>> matchers = new ArrayList(5);
    matchers.add(first);
    matchers.add(second);
    matchers.add(third);
    matchers.add(fourth);
    matchers.add(fifth);
    return allOf(matchers);
  }
  





  @Factory
  public static <T> Matcher<T> allOf(Matcher<? super T> first, Matcher<? super T> second, Matcher<? super T> third, Matcher<? super T> fourth, Matcher<? super T> fifth, Matcher<? super T> sixth)
  {
    List<Matcher<? super T>> matchers = new ArrayList(6);
    matchers.add(first);
    matchers.add(second);
    matchers.add(third);
    matchers.add(fourth);
    matchers.add(fifth);
    matchers.add(sixth);
    return allOf(matchers);
  }
}

package org.junit.experimental.results;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;








public class ResultMatchers
{
  public ResultMatchers() {}
  
  public static Matcher<PrintableResult> isSuccessful()
  {
    return failureCountIs(0);
  }
  


  public static Matcher<PrintableResult> failureCountIs(int count)
  {
    new TypeSafeMatcher() {
      public void describeTo(Description description) {
        description.appendText("has " + val$count + " failures");
      }
      
      public boolean matchesSafely(PrintableResult item)
      {
        return item.failureCount() == val$count;
      }
    };
  }
  


  public static Matcher<Object> hasSingleFailureContaining(String string)
  {
    new BaseMatcher() {
      public boolean matches(Object item) {
        return (item.toString().contains(val$string)) && (ResultMatchers.failureCountIs(1).matches(item));
      }
      
      public void describeTo(Description description) {
        description.appendText("has single failure containing " + val$string);
      }
    };
  }
  



  public static Matcher<PrintableResult> hasFailureContaining(String string)
  {
    new BaseMatcher() {
      public boolean matches(Object item) {
        return item.toString().contains(val$string);
      }
      
      public void describeTo(Description description) {
        description.appendText("has failure containing " + val$string);
      }
    };
  }
}

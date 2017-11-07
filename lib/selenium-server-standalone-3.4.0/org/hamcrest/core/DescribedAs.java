package org.hamcrest.core;

import java.util.regex.Pattern;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;









public class DescribedAs<T>
  extends BaseMatcher<T>
{
  private final String descriptionTemplate;
  private final org.hamcrest.Matcher<T> matcher;
  private final Object[] values;
  private static final Pattern ARG_PATTERN = Pattern.compile("%([0-9]+)");
  
  public DescribedAs(String descriptionTemplate, org.hamcrest.Matcher<T> matcher, Object[] values) {
    this.descriptionTemplate = descriptionTemplate;
    this.matcher = matcher;
    this.values = ((Object[])values.clone());
  }
  
  public boolean matches(Object o)
  {
    return matcher.matches(o);
  }
  
  public void describeTo(Description description)
  {
    java.util.regex.Matcher arg = ARG_PATTERN.matcher(descriptionTemplate);
    
    int textStart = 0;
    while (arg.find()) {
      description.appendText(descriptionTemplate.substring(textStart, arg.start()));
      description.appendValue(values[Integer.parseInt(arg.group(1))]);
      textStart = arg.end();
    }
    
    if (textStart < descriptionTemplate.length()) {
      description.appendText(descriptionTemplate.substring(textStart));
    }
  }
  
  public void describeMismatch(Object item, Description description)
  {
    matcher.describeMismatch(item, description);
  }
  













  @Factory
  public static <T> org.hamcrest.Matcher<T> describedAs(String description, org.hamcrest.Matcher<T> matcher, Object... values)
  {
    return new DescribedAs(description, matcher, values);
  }
}

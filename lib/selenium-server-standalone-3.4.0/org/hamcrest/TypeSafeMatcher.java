package org.hamcrest;

import org.hamcrest.internal.ReflectiveTypeFinder;







public abstract class TypeSafeMatcher<T>
  extends BaseMatcher<T>
{
  private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 1, 0);
  

  private final Class<?> expectedType;
  

  protected TypeSafeMatcher()
  {
    this(TYPE_FINDER);
  }
  




  protected TypeSafeMatcher(Class<?> expectedType)
  {
    this.expectedType = expectedType;
  }
  




  protected TypeSafeMatcher(ReflectiveTypeFinder typeFinder)
  {
    expectedType = typeFinder.findExpectedType(getClass());
  }
  




  protected abstract boolean matchesSafely(T paramT);
  



  protected void describeMismatchSafely(T item, Description mismatchDescription)
  {
    super.describeMismatch(item, mismatchDescription);
  }
  






  public final boolean matches(Object item)
  {
    return (item != null) && (expectedType.isInstance(item)) && (matchesSafely(item));
  }
  



  public final void describeMismatch(Object item, Description description)
  {
    if (item == null) {
      super.describeMismatch(item, description);
    } else if (!expectedType.isInstance(item)) {
      description.appendText("was a ").appendText(item.getClass().getName()).appendText(" (").appendValue(item).appendText(")");

    }
    else
    {

      describeMismatchSafely(item, description);
    }
  }
}

package org.hamcrest;

public abstract interface Matcher<T>
  extends SelfDescribing
{
  public abstract boolean matches(Object paramObject);
  
  public abstract void describeMismatch(Object paramObject, Description paramDescription);
  
  @Deprecated
  public abstract void _dont_implement_Matcher___instead_extend_BaseMatcher_();
}

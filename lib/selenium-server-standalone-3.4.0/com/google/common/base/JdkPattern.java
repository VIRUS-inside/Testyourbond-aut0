package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;













@GwtIncompatible
final class JdkPattern
  extends CommonPattern
  implements Serializable
{
  private final Pattern pattern;
  private static final long serialVersionUID = 0L;
  
  JdkPattern(Pattern pattern)
  {
    this.pattern = ((Pattern)Preconditions.checkNotNull(pattern));
  }
  
  CommonMatcher matcher(CharSequence t)
  {
    return new JdkMatcher(pattern.matcher(t));
  }
  
  String pattern()
  {
    return pattern.pattern();
  }
  
  int flags()
  {
    return pattern.flags();
  }
  
  public String toString()
  {
    return pattern.toString();
  }
  
  public int hashCode()
  {
    return pattern.hashCode();
  }
  
  public boolean equals(Object o)
  {
    if (!(o instanceof JdkPattern)) {
      return false;
    }
    return pattern.equals(pattern);
  }
  
  private static final class JdkMatcher extends CommonMatcher {
    final Matcher matcher;
    
    JdkMatcher(Matcher matcher) {
      this.matcher = ((Matcher)Preconditions.checkNotNull(matcher));
    }
    
    boolean matches()
    {
      return matcher.matches();
    }
    
    boolean find()
    {
      return matcher.find();
    }
    
    boolean find(int index)
    {
      return matcher.find(index);
    }
    
    String replaceAll(String replacement)
    {
      return matcher.replaceAll(replacement);
    }
    
    int end()
    {
      return matcher.end();
    }
    
    int start()
    {
      return matcher.start();
    }
  }
}

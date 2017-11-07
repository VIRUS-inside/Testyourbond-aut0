package org.eclipse.jetty.util;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





















public class RegexSet
  extends AbstractSet<String>
  implements Predicate<String>
{
  private final Set<String> _patterns = new HashSet();
  private final Set<String> _unmodifiable = Collections.unmodifiableSet(_patterns);
  private Pattern _pattern;
  
  public RegexSet() {}
  
  public Iterator<String> iterator() {
    return _unmodifiable.iterator();
  }
  

  public int size()
  {
    return _patterns.size();
  }
  

  public boolean add(String pattern)
  {
    boolean added = _patterns.add(pattern);
    if (added)
      updatePattern();
    return added;
  }
  

  public boolean remove(Object pattern)
  {
    boolean removed = _patterns.remove(pattern);
    
    if (removed)
      updatePattern();
    return removed;
  }
  

  public boolean isEmpty()
  {
    return _patterns.isEmpty();
  }
  

  public void clear()
  {
    _patterns.clear();
    _pattern = null;
  }
  
  private void updatePattern()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("^(");
    for (String pattern : _patterns)
    {
      if (builder.length() > 2)
        builder.append('|');
      builder.append('(');
      builder.append(pattern);
      builder.append(')');
    }
    builder.append(")$");
    _pattern = Pattern.compile(builder.toString());
  }
  

  public boolean test(String s)
  {
    return (_pattern != null) && (_pattern.matcher(s).matches());
  }
  
  public boolean matches(String s)
  {
    return (_pattern != null) && (_pattern.matcher(s).matches());
  }
}

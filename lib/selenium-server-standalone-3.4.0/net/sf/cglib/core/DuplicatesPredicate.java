package net.sf.cglib.core;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;











public class DuplicatesPredicate
  implements Predicate
{
  public DuplicatesPredicate() {}
  
  private Set unique = new HashSet();
  
  public boolean evaluate(Object arg) {
    return unique.add(MethodWrapper.create((Method)arg));
  }
}

package net.sf.cglib.core;

public abstract interface NamingPolicy
{
  public abstract String getClassName(String paramString1, String paramString2, Object paramObject, Predicate paramPredicate);
  
  public abstract boolean equals(Object paramObject);
}

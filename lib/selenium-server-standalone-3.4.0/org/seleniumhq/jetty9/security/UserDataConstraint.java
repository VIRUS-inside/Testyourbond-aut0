package org.seleniumhq.jetty9.security;






















public enum UserDataConstraint
{
  None,  Integral,  Confidential;
  
  private UserDataConstraint() {}
  
  public static UserDataConstraint get(int dataConstraint) { if ((dataConstraint < -1) || (dataConstraint > 2)) throw new IllegalArgumentException("Expected -1, 0, 1, or 2, not: " + dataConstraint);
    if (dataConstraint == -1) return None;
    return values()[dataConstraint];
  }
  
  public UserDataConstraint combine(UserDataConstraint other)
  {
    if (compareTo(other) < 0) return this;
    return other;
  }
}

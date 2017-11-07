package org.seleniumhq.jetty9.security;

import java.util.List;
import java.util.Set;

public abstract interface ConstraintAware
{
  public abstract List<ConstraintMapping> getConstraintMappings();
  
  public abstract Set<String> getRoles();
  
  public abstract void setConstraintMappings(List<ConstraintMapping> paramList, Set<String> paramSet);
  
  public abstract void addConstraintMapping(ConstraintMapping paramConstraintMapping);
  
  public abstract void addRole(String paramString);
  
  public abstract void setDenyUncoveredHttpMethods(boolean paramBoolean);
  
  public abstract boolean isDenyUncoveredHttpMethods();
  
  public abstract boolean checkPathsWithUncoveredHttpMethods();
}

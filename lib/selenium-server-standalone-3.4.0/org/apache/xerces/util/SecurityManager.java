package org.apache.xerces.util;

public final class SecurityManager
{
  private static final int DEFAULT_ENTITY_EXPANSION_LIMIT = 100000;
  private static final int DEFAULT_MAX_OCCUR_NODE_LIMIT = 3000;
  private int entityExpansionLimit = 100000;
  private int maxOccurLimit = 3000;
  
  public SecurityManager() {}
  
  public void setEntityExpansionLimit(int paramInt)
  {
    entityExpansionLimit = paramInt;
  }
  
  public int getEntityExpansionLimit()
  {
    return entityExpansionLimit;
  }
  
  public void setMaxOccurNodeLimit(int paramInt)
  {
    maxOccurLimit = paramInt;
  }
  
  public int getMaxOccurNodeLimit()
  {
    return maxOccurLimit;
  }
}

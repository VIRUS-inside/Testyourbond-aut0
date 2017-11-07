package org.apache.xerces.xni;

public abstract interface XMLResourceIdentifier
{
  public abstract void setPublicId(String paramString);
  
  public abstract String getPublicId();
  
  public abstract void setExpandedSystemId(String paramString);
  
  public abstract String getExpandedSystemId();
  
  public abstract void setLiteralSystemId(String paramString);
  
  public abstract String getLiteralSystemId();
  
  public abstract void setBaseSystemId(String paramString);
  
  public abstract String getBaseSystemId();
  
  public abstract void setNamespace(String paramString);
  
  public abstract String getNamespace();
}

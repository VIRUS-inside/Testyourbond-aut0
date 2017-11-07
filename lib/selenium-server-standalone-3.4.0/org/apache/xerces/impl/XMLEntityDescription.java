package org.apache.xerces.impl;

import org.apache.xerces.xni.XMLResourceIdentifier;

public abstract interface XMLEntityDescription
  extends XMLResourceIdentifier
{
  public abstract void setEntityName(String paramString);
  
  public abstract String getEntityName();
}

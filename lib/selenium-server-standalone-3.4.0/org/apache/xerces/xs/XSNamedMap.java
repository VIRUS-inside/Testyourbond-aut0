package org.apache.xerces.xs;

import java.util.Map;

public abstract interface XSNamedMap
  extends Map
{
  public abstract int getLength();
  
  public abstract XSObject item(int paramInt);
  
  public abstract XSObject itemByName(String paramString1, String paramString2);
}

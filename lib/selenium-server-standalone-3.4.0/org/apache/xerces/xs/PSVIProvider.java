package org.apache.xerces.xs;

public abstract interface PSVIProvider
{
  public abstract ElementPSVI getElementPSVI();
  
  public abstract AttributePSVI getAttributePSVI(int paramInt);
  
  public abstract AttributePSVI getAttributePSVIByName(String paramString1, String paramString2);
}

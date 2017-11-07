package org.apache.xerces.xinclude;

import org.apache.xerces.xni.NamespaceContext;

public class XIncludeNamespaceSupport
  extends MultipleScopeNamespaceSupport
{
  private boolean[] fValidContext = new boolean[8];
  
  public XIncludeNamespaceSupport() {}
  
  public XIncludeNamespaceSupport(NamespaceContext paramNamespaceContext)
  {
    super(paramNamespaceContext);
  }
  
  public void pushContext()
  {
    super.pushContext();
    if (fCurrentContext + 1 == fValidContext.length)
    {
      boolean[] arrayOfBoolean = new boolean[fValidContext.length * 2];
      System.arraycopy(fValidContext, 0, arrayOfBoolean, 0, fValidContext.length);
      fValidContext = arrayOfBoolean;
    }
    fValidContext[fCurrentContext] = true;
  }
  
  public void setContextInvalid()
  {
    fValidContext[fCurrentContext] = false;
  }
  
  public String getURIFromIncludeParent(String paramString)
  {
    for (int i = fCurrentContext - 1; (i > 0) && (fValidContext[i] == 0); i--) {}
    return getURI(paramString, i);
  }
}

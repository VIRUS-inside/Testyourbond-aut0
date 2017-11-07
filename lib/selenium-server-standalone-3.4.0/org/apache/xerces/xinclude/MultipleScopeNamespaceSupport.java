package org.apache.xerces.xinclude;

import java.util.Enumeration;
import org.apache.xerces.util.NamespaceSupport;
import org.apache.xerces.util.NamespaceSupport.Prefixes;
import org.apache.xerces.util.XMLSymbols;
import org.apache.xerces.xni.NamespaceContext;

public class MultipleScopeNamespaceSupport
  extends NamespaceSupport
{
  protected int[] fScope = new int[8];
  protected int fCurrentScope = 0;
  
  public MultipleScopeNamespaceSupport()
  {
    fScope[0] = 0;
  }
  
  public MultipleScopeNamespaceSupport(NamespaceContext paramNamespaceContext)
  {
    super(paramNamespaceContext);
    fScope[0] = 0;
  }
  
  public Enumeration getAllPrefixes()
  {
    int i = 0;
    if (fPrefixes.length < fNamespace.length / 2)
    {
      localObject = new String[fNamespaceSize];
      fPrefixes = ((String[])localObject);
    }
    Object localObject = null;
    int j = 1;
    for (int k = fContext[fScope[fCurrentScope]]; k <= fNamespaceSize - 2; k += 2)
    {
      localObject = fNamespace[k];
      for (int m = 0; m < i; m++) {
        if (fPrefixes[m] == localObject)
        {
          j = 0;
          break;
        }
      }
      if (j != 0) {
        fPrefixes[(i++)] = localObject;
      }
      j = 1;
    }
    return new NamespaceSupport.Prefixes(this, fPrefixes, i);
  }
  
  public int getScopeForContext(int paramInt)
  {
    for (int i = fCurrentScope; paramInt < fScope[i]; i--) {}
    return i;
  }
  
  public String getPrefix(String paramString)
  {
    return getPrefix(paramString, fNamespaceSize, fContext[fScope[fCurrentScope]]);
  }
  
  public String getURI(String paramString)
  {
    return getURI(paramString, fNamespaceSize, fContext[fScope[fCurrentScope]]);
  }
  
  public String getPrefix(String paramString, int paramInt)
  {
    return getPrefix(paramString, fContext[(paramInt + 1)], fContext[fScope[getScopeForContext(paramInt)]]);
  }
  
  public String getURI(String paramString, int paramInt)
  {
    return getURI(paramString, fContext[(paramInt + 1)], fContext[fScope[getScopeForContext(paramInt)]]);
  }
  
  public String getPrefix(String paramString, int paramInt1, int paramInt2)
  {
    if (paramString == NamespaceContext.XML_URI) {
      return XMLSymbols.PREFIX_XML;
    }
    if (paramString == NamespaceContext.XMLNS_URI) {
      return XMLSymbols.PREFIX_XMLNS;
    }
    for (int i = paramInt1; i > paramInt2; i -= 2) {
      if ((fNamespace[(i - 1)] == paramString) && (getURI(fNamespace[(i - 2)]) == paramString)) {
        return fNamespace[(i - 2)];
      }
    }
    return null;
  }
  
  public String getURI(String paramString, int paramInt1, int paramInt2)
  {
    if (paramString == XMLSymbols.PREFIX_XML) {
      return NamespaceContext.XML_URI;
    }
    if (paramString == XMLSymbols.PREFIX_XMLNS) {
      return NamespaceContext.XMLNS_URI;
    }
    for (int i = paramInt1; i > paramInt2; i -= 2) {
      if (fNamespace[(i - 2)] == paramString) {
        return fNamespace[(i - 1)];
      }
    }
    return null;
  }
  
  public void reset()
  {
    fCurrentContext = fScope[fCurrentScope];
    fNamespaceSize = fContext[fCurrentContext];
  }
  
  public void pushScope()
  {
    if (fCurrentScope + 1 == fScope.length)
    {
      int[] arrayOfInt = new int[fScope.length * 2];
      System.arraycopy(fScope, 0, arrayOfInt, 0, fScope.length);
      fScope = arrayOfInt;
    }
    pushContext();
    fScope[(++fCurrentScope)] = fCurrentContext;
  }
  
  public void popScope()
  {
    fCurrentContext = fScope[(fCurrentScope--)];
    popContext();
  }
}

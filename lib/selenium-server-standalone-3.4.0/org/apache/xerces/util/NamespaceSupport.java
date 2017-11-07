package org.apache.xerces.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import org.apache.xerces.xni.NamespaceContext;

public class NamespaceSupport
  implements NamespaceContext
{
  protected String[] fNamespace = new String[32];
  protected int fNamespaceSize;
  protected int[] fContext = new int[8];
  protected int fCurrentContext;
  protected String[] fPrefixes = new String[16];
  
  public NamespaceSupport() {}
  
  public NamespaceSupport(NamespaceContext paramNamespaceContext)
  {
    pushContext();
    Enumeration localEnumeration = paramNamespaceContext.getAllPrefixes();
    while (localEnumeration.hasMoreElements())
    {
      String str1 = (String)localEnumeration.nextElement();
      String str2 = paramNamespaceContext.getURI(str1);
      declarePrefix(str1, str2);
    }
  }
  
  public void reset()
  {
    fNamespaceSize = 0;
    fCurrentContext = 0;
    fContext[fCurrentContext] = fNamespaceSize;
    fNamespace[(fNamespaceSize++)] = XMLSymbols.PREFIX_XML;
    fNamespace[(fNamespaceSize++)] = NamespaceContext.XML_URI;
    fNamespace[(fNamespaceSize++)] = XMLSymbols.PREFIX_XMLNS;
    fNamespace[(fNamespaceSize++)] = NamespaceContext.XMLNS_URI;
    fCurrentContext += 1;
  }
  
  public void pushContext()
  {
    if (fCurrentContext + 1 == fContext.length)
    {
      int[] arrayOfInt = new int[fContext.length * 2];
      System.arraycopy(fContext, 0, arrayOfInt, 0, fContext.length);
      fContext = arrayOfInt;
    }
    fContext[(++fCurrentContext)] = fNamespaceSize;
  }
  
  public void popContext()
  {
    fNamespaceSize = fContext[(fCurrentContext--)];
  }
  
  public boolean declarePrefix(String paramString1, String paramString2)
  {
    if ((paramString1 == XMLSymbols.PREFIX_XML) || (paramString1 == XMLSymbols.PREFIX_XMLNS)) {
      return false;
    }
    for (int i = fNamespaceSize; i > fContext[fCurrentContext]; i -= 2) {
      if (fNamespace[(i - 2)] == paramString1)
      {
        fNamespace[(i - 1)] = paramString2;
        return true;
      }
    }
    if (fNamespaceSize == fNamespace.length)
    {
      String[] arrayOfString = new String[fNamespaceSize * 2];
      System.arraycopy(fNamespace, 0, arrayOfString, 0, fNamespaceSize);
      fNamespace = arrayOfString;
    }
    fNamespace[(fNamespaceSize++)] = paramString1;
    fNamespace[(fNamespaceSize++)] = paramString2;
    return true;
  }
  
  public String getURI(String paramString)
  {
    for (int i = fNamespaceSize; i > 0; i -= 2) {
      if (fNamespace[(i - 2)] == paramString) {
        return fNamespace[(i - 1)];
      }
    }
    return null;
  }
  
  public String getPrefix(String paramString)
  {
    for (int i = fNamespaceSize; i > 0; i -= 2) {
      if ((fNamespace[(i - 1)] == paramString) && (getURI(fNamespace[(i - 2)]) == paramString)) {
        return fNamespace[(i - 2)];
      }
    }
    return null;
  }
  
  public int getDeclaredPrefixCount()
  {
    return (fNamespaceSize - fContext[fCurrentContext]) / 2;
  }
  
  public String getDeclaredPrefixAt(int paramInt)
  {
    return fNamespace[(fContext[fCurrentContext] + paramInt * 2)];
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
    for (int k = 2; k < fNamespaceSize - 2; k += 2)
    {
      localObject = fNamespace[(k + 2)];
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
    return new Prefixes(fPrefixes, i);
  }
  
  public boolean containsPrefix(String paramString)
  {
    for (int i = fNamespaceSize; i > 0; i -= 2) {
      if (fNamespace[(i - 2)] == paramString) {
        return true;
      }
    }
    return false;
  }
  
  protected final class Prefixes
    implements Enumeration
  {
    private String[] prefixes;
    private int counter = 0;
    private int size = 0;
    
    public Prefixes(String[] paramArrayOfString, int paramInt)
    {
      prefixes = paramArrayOfString;
      size = paramInt;
    }
    
    public boolean hasMoreElements()
    {
      return counter < size;
    }
    
    public Object nextElement()
    {
      if (counter < size) {
        return fPrefixes[(counter++)];
      }
      throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
    }
    
    public String toString()
    {
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; i < size; i++)
      {
        localStringBuffer.append(prefixes[i]);
        localStringBuffer.append(' ');
      }
      return localStringBuffer.toString();
    }
  }
}

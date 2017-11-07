package org.apache.xerces.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

public final class JAXPNamespaceContextWrapper
  implements org.apache.xerces.xni.NamespaceContext
{
  private javax.xml.namespace.NamespaceContext fNamespaceContext;
  private SymbolTable fSymbolTable;
  private List fPrefixes;
  private final Vector fAllPrefixes = new Vector();
  private int[] fContext = new int[8];
  private int fCurrentContext;
  
  public JAXPNamespaceContextWrapper(SymbolTable paramSymbolTable)
  {
    setSymbolTable(paramSymbolTable);
  }
  
  public void setNamespaceContext(javax.xml.namespace.NamespaceContext paramNamespaceContext)
  {
    fNamespaceContext = paramNamespaceContext;
  }
  
  public javax.xml.namespace.NamespaceContext getNamespaceContext()
  {
    return fNamespaceContext;
  }
  
  public void setSymbolTable(SymbolTable paramSymbolTable)
  {
    fSymbolTable = paramSymbolTable;
  }
  
  public SymbolTable getSymbolTable()
  {
    return fSymbolTable;
  }
  
  public void setDeclaredPrefixes(List paramList)
  {
    fPrefixes = paramList;
  }
  
  public List getDeclaredPrefixes()
  {
    return fPrefixes;
  }
  
  public String getURI(String paramString)
  {
    if (fNamespaceContext != null)
    {
      String str = fNamespaceContext.getNamespaceURI(paramString);
      if ((str != null) && (!"".equals(str))) {
        return fSymbolTable != null ? fSymbolTable.addSymbol(str) : str.intern();
      }
    }
    return null;
  }
  
  public String getPrefix(String paramString)
  {
    if (fNamespaceContext != null)
    {
      if (paramString == null) {
        paramString = "";
      }
      String str = fNamespaceContext.getPrefix(paramString);
      if (str == null) {
        str = "";
      }
      return fSymbolTable != null ? fSymbolTable.addSymbol(str) : str.intern();
    }
    return null;
  }
  
  public Enumeration getAllPrefixes()
  {
    return Collections.enumeration(new TreeSet(fAllPrefixes));
  }
  
  public void pushContext()
  {
    if (fCurrentContext + 1 == fContext.length)
    {
      int[] arrayOfInt = new int[fContext.length * 2];
      System.arraycopy(fContext, 0, arrayOfInt, 0, fContext.length);
      fContext = arrayOfInt;
    }
    fContext[(++fCurrentContext)] = fAllPrefixes.size();
    if (fPrefixes != null) {
      fAllPrefixes.addAll(fPrefixes);
    }
  }
  
  public void popContext()
  {
    fAllPrefixes.setSize(fContext[(fCurrentContext--)]);
  }
  
  public boolean declarePrefix(String paramString1, String paramString2)
  {
    return true;
  }
  
  public int getDeclaredPrefixCount()
  {
    return fPrefixes != null ? fPrefixes.size() : 0;
  }
  
  public String getDeclaredPrefixAt(int paramInt)
  {
    return (String)fPrefixes.get(paramInt);
  }
  
  public void reset()
  {
    fCurrentContext = 0;
    fContext[fCurrentContext] = 0;
    fAllPrefixes.clear();
  }
}

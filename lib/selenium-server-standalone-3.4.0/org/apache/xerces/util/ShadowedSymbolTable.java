package org.apache.xerces.util;

public final class ShadowedSymbolTable
  extends SymbolTable
{
  protected SymbolTable fSymbolTable;
  
  public ShadowedSymbolTable(SymbolTable paramSymbolTable)
  {
    fSymbolTable = paramSymbolTable;
  }
  
  public String addSymbol(String paramString)
  {
    if (fSymbolTable.containsSymbol(paramString)) {
      return fSymbolTable.addSymbol(paramString);
    }
    return super.addSymbol(paramString);
  }
  
  public String addSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (fSymbolTable.containsSymbol(paramArrayOfChar, paramInt1, paramInt2)) {
      return fSymbolTable.addSymbol(paramArrayOfChar, paramInt1, paramInt2);
    }
    return super.addSymbol(paramArrayOfChar, paramInt1, paramInt2);
  }
  
  public int hash(String paramString)
  {
    return fSymbolTable.hash(paramString);
  }
  
  public int hash(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    return fSymbolTable.hash(paramArrayOfChar, paramInt1, paramInt2);
  }
}

package org.apache.xerces.util;

public final class SynchronizedSymbolTable
  extends SymbolTable
{
  protected SymbolTable fSymbolTable;
  
  public SynchronizedSymbolTable(SymbolTable paramSymbolTable)
  {
    fSymbolTable = paramSymbolTable;
  }
  
  public SynchronizedSymbolTable()
  {
    fSymbolTable = new SymbolTable();
  }
  
  public SynchronizedSymbolTable(int paramInt)
  {
    fSymbolTable = new SymbolTable(paramInt);
  }
  
  public String addSymbol(String paramString)
  {
    synchronized (fSymbolTable)
    {
      String str = fSymbolTable.addSymbol(paramString);
      return str;
    }
  }
  
  public String addSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    synchronized (fSymbolTable)
    {
      String str = fSymbolTable.addSymbol(paramArrayOfChar, paramInt1, paramInt2);
      return str;
    }
  }
  
  public boolean containsSymbol(String paramString)
  {
    synchronized (fSymbolTable)
    {
      boolean bool = fSymbolTable.containsSymbol(paramString);
      return bool;
    }
  }
  
  public boolean containsSymbol(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    synchronized (fSymbolTable)
    {
      boolean bool = fSymbolTable.containsSymbol(paramArrayOfChar, paramInt1, paramInt2);
      return bool;
    }
  }
}

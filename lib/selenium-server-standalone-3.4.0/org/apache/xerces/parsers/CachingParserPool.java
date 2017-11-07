package org.apache.xerces.parsers;

import org.apache.xerces.util.ShadowedSymbolTable;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.SynchronizedSymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

public class CachingParserPool
{
  public static final boolean DEFAULT_SHADOW_SYMBOL_TABLE = false;
  public static final boolean DEFAULT_SHADOW_GRAMMAR_POOL = false;
  protected SymbolTable fSynchronizedSymbolTable;
  protected XMLGrammarPool fSynchronizedGrammarPool;
  protected boolean fShadowSymbolTable = false;
  protected boolean fShadowGrammarPool = false;
  
  public CachingParserPool()
  {
    this(new SymbolTable(), new XMLGrammarPoolImpl());
  }
  
  public CachingParserPool(SymbolTable paramSymbolTable, XMLGrammarPool paramXMLGrammarPool)
  {
    fSynchronizedSymbolTable = new SynchronizedSymbolTable(paramSymbolTable);
    fSynchronizedGrammarPool = new SynchronizedGrammarPool(paramXMLGrammarPool);
  }
  
  public SymbolTable getSymbolTable()
  {
    return fSynchronizedSymbolTable;
  }
  
  public XMLGrammarPool getXMLGrammarPool()
  {
    return fSynchronizedGrammarPool;
  }
  
  public void setShadowSymbolTable(boolean paramBoolean)
  {
    fShadowSymbolTable = paramBoolean;
  }
  
  public DOMParser createDOMParser()
  {
    SymbolTable localSymbolTable = fShadowSymbolTable ? new ShadowedSymbolTable(fSynchronizedSymbolTable) : fSynchronizedSymbolTable;
    XMLGrammarPool localXMLGrammarPool = fShadowGrammarPool ? new ShadowedGrammarPool(fSynchronizedGrammarPool) : fSynchronizedGrammarPool;
    return new DOMParser(localSymbolTable, localXMLGrammarPool);
  }
  
  public SAXParser createSAXParser()
  {
    SymbolTable localSymbolTable = fShadowSymbolTable ? new ShadowedSymbolTable(fSynchronizedSymbolTable) : fSynchronizedSymbolTable;
    XMLGrammarPool localXMLGrammarPool = fShadowGrammarPool ? new ShadowedGrammarPool(fSynchronizedGrammarPool) : fSynchronizedGrammarPool;
    return new SAXParser(localSymbolTable, localXMLGrammarPool);
  }
  
  public static final class ShadowedGrammarPool
    extends XMLGrammarPoolImpl
  {
    private XMLGrammarPool fGrammarPool;
    
    public ShadowedGrammarPool(XMLGrammarPool paramXMLGrammarPool)
    {
      fGrammarPool = paramXMLGrammarPool;
    }
    
    public Grammar[] retrieveInitialGrammarSet(String paramString)
    {
      Grammar[] arrayOfGrammar = super.retrieveInitialGrammarSet(paramString);
      if (arrayOfGrammar != null) {
        return arrayOfGrammar;
      }
      return fGrammarPool.retrieveInitialGrammarSet(paramString);
    }
    
    public Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription)
    {
      Grammar localGrammar = super.retrieveGrammar(paramXMLGrammarDescription);
      if (localGrammar != null) {
        return localGrammar;
      }
      return fGrammarPool.retrieveGrammar(paramXMLGrammarDescription);
    }
    
    public void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar)
    {
      super.cacheGrammars(paramString, paramArrayOfGrammar);
      fGrammarPool.cacheGrammars(paramString, paramArrayOfGrammar);
    }
    
    public Grammar getGrammar(XMLGrammarDescription paramXMLGrammarDescription)
    {
      if (super.containsGrammar(paramXMLGrammarDescription)) {
        return super.getGrammar(paramXMLGrammarDescription);
      }
      return null;
    }
    
    public boolean containsGrammar(XMLGrammarDescription paramXMLGrammarDescription)
    {
      return super.containsGrammar(paramXMLGrammarDescription);
    }
  }
  
  public static final class SynchronizedGrammarPool
    implements XMLGrammarPool
  {
    private XMLGrammarPool fGrammarPool;
    
    public SynchronizedGrammarPool(XMLGrammarPool paramXMLGrammarPool)
    {
      fGrammarPool = paramXMLGrammarPool;
    }
    
    public Grammar[] retrieveInitialGrammarSet(String paramString)
    {
      synchronized (fGrammarPool)
      {
        Grammar[] arrayOfGrammar = fGrammarPool.retrieveInitialGrammarSet(paramString);
        return arrayOfGrammar;
      }
    }
    
    public Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription)
    {
      synchronized (fGrammarPool)
      {
        Grammar localGrammar = fGrammarPool.retrieveGrammar(paramXMLGrammarDescription);
        return localGrammar;
      }
    }
    
    public void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar)
    {
      synchronized (fGrammarPool)
      {
        fGrammarPool.cacheGrammars(paramString, paramArrayOfGrammar);
      }
    }
    
    public void lockPool()
    {
      synchronized (fGrammarPool)
      {
        fGrammarPool.lockPool();
      }
    }
    
    public void clear()
    {
      synchronized (fGrammarPool)
      {
        fGrammarPool.clear();
      }
    }
    
    public void unlockPool()
    {
      synchronized (fGrammarPool)
      {
        fGrammarPool.unlockPool();
      }
    }
  }
}

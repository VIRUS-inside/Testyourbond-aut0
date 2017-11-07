package org.apache.xerces.util;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

public class XMLGrammarPoolImpl
  implements XMLGrammarPool
{
  protected static final int TABLE_SIZE = 11;
  protected Entry[] fGrammars = null;
  protected boolean fPoolIsLocked;
  protected int fGrammarCount = 0;
  private static final boolean DEBUG = false;
  
  public XMLGrammarPoolImpl()
  {
    fGrammars = new Entry[11];
    fPoolIsLocked = false;
  }
  
  public XMLGrammarPoolImpl(int paramInt)
  {
    fGrammars = new Entry[paramInt];
    fPoolIsLocked = false;
  }
  
  public Grammar[] retrieveInitialGrammarSet(String paramString)
  {
    synchronized (fGrammars)
    {
      int i = fGrammars.length;
      Grammar[] arrayOfGrammar = new Grammar[fGrammarCount];
      int j = 0;
      for (int k = 0; k < i; k++) {
        for (localObject1 = fGrammars[k]; localObject1 != null; localObject1 = next) {
          if (desc.getGrammarType().equals(paramString)) {
            arrayOfGrammar[(j++)] = grammar;
          }
        }
      }
      Object localObject1 = new Grammar[j];
      System.arraycopy(arrayOfGrammar, 0, localObject1, 0, j);
      Object localObject2 = localObject1;
      return localObject2;
    }
  }
  
  public void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar)
  {
    if (!fPoolIsLocked) {
      for (int i = 0; i < paramArrayOfGrammar.length; i++) {
        putGrammar(paramArrayOfGrammar[i]);
      }
    }
  }
  
  public Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    return getGrammar(paramXMLGrammarDescription);
  }
  
  public void putGrammar(Grammar paramGrammar)
  {
    if (!fPoolIsLocked) {
      synchronized (fGrammars)
      {
        XMLGrammarDescription localXMLGrammarDescription = paramGrammar.getGrammarDescription();
        int i = hashCode(localXMLGrammarDescription);
        int j = (i & 0x7FFFFFFF) % fGrammars.length;
        for (Entry localEntry1 = fGrammars[j]; localEntry1 != null; localEntry1 = next) {
          if ((hash == i) && (equals(desc, localXMLGrammarDescription)))
          {
            grammar = paramGrammar;
            return;
          }
        }
        Entry localEntry2 = new Entry(i, localXMLGrammarDescription, paramGrammar, fGrammars[j]);
        fGrammars[j] = localEntry2;
        fGrammarCount += 1;
      }
    }
  }
  
  public Grammar getGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    synchronized (fGrammars)
    {
      int i = hashCode(paramXMLGrammarDescription);
      int j = (i & 0x7FFFFFFF) % fGrammars.length;
      for (Entry localEntry = fGrammars[j]; localEntry != null; localEntry = next) {
        if ((hash == i) && (equals(desc, paramXMLGrammarDescription)))
        {
          localGrammar = grammar;
          return localGrammar;
        }
      }
      Grammar localGrammar = null;
      return localGrammar;
    }
  }
  
  public Grammar removeGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    synchronized (fGrammars)
    {
      int i = hashCode(paramXMLGrammarDescription);
      int j = (i & 0x7FFFFFFF) % fGrammars.length;
      Entry localEntry1 = fGrammars[j];
      Entry localEntry2 = null;
      while (localEntry1 != null)
      {
        if ((hash == i) && (equals(desc, paramXMLGrammarDescription)))
        {
          if (localEntry2 != null) {
            next = next;
          } else {
            fGrammars[j] = next;
          }
          localGrammar1 = grammar;
          grammar = null;
          fGrammarCount -= 1;
          Grammar localGrammar2 = localGrammar1;
          return localGrammar2;
        }
        localEntry2 = localEntry1;
        localEntry1 = next;
      }
      Grammar localGrammar1 = null;
      return localGrammar1;
    }
  }
  
  public boolean containsGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    synchronized (fGrammars)
    {
      int i = hashCode(paramXMLGrammarDescription);
      int j = (i & 0x7FFFFFFF) % fGrammars.length;
      for (Entry localEntry = fGrammars[j]; localEntry != null; localEntry = next) {
        if ((hash == i) && (equals(desc, paramXMLGrammarDescription)))
        {
          bool = true;
          return bool;
        }
      }
      boolean bool = false;
      return bool;
    }
  }
  
  public void lockPool()
  {
    fPoolIsLocked = true;
  }
  
  public void unlockPool()
  {
    fPoolIsLocked = false;
  }
  
  public void clear()
  {
    for (int i = 0; i < fGrammars.length; i++) {
      if (fGrammars[i] != null)
      {
        fGrammars[i].clear();
        fGrammars[i] = null;
      }
    }
    fGrammarCount = 0;
  }
  
  public boolean equals(XMLGrammarDescription paramXMLGrammarDescription1, XMLGrammarDescription paramXMLGrammarDescription2)
  {
    return paramXMLGrammarDescription1.equals(paramXMLGrammarDescription2);
  }
  
  public int hashCode(XMLGrammarDescription paramXMLGrammarDescription)
  {
    return paramXMLGrammarDescription.hashCode();
  }
  
  protected static final class Entry
  {
    public int hash;
    public XMLGrammarDescription desc;
    public Grammar grammar;
    public Entry next;
    
    protected Entry(int paramInt, XMLGrammarDescription paramXMLGrammarDescription, Grammar paramGrammar, Entry paramEntry)
    {
      hash = paramInt;
      desc = paramXMLGrammarDescription;
      grammar = paramGrammar;
      next = paramEntry;
    }
    
    protected void clear()
    {
      desc = null;
      grammar = null;
      if (next != null)
      {
        next.clear();
        next = null;
      }
    }
  }
}

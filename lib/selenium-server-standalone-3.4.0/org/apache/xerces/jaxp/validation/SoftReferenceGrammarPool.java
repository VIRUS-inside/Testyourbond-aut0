package org.apache.xerces.jaxp.validation;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.grammars.XMLSchemaDescription;

final class SoftReferenceGrammarPool
  implements XMLGrammarPool
{
  protected static final int TABLE_SIZE = 11;
  protected static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
  protected Entry[] fGrammars = null;
  protected boolean fPoolIsLocked;
  protected int fGrammarCount = 0;
  protected final ReferenceQueue fReferenceQueue = new ReferenceQueue();
  
  public SoftReferenceGrammarPool()
  {
    fGrammars = new Entry[11];
    fPoolIsLocked = false;
  }
  
  public SoftReferenceGrammarPool(int paramInt)
  {
    fGrammars = new Entry[paramInt];
    fPoolIsLocked = false;
  }
  
  public Grammar[] retrieveInitialGrammarSet(String paramString)
  {
    synchronized (fGrammars)
    {
      clean();
      Grammar[] arrayOfGrammar = ZERO_LENGTH_GRAMMAR_ARRAY;
      return arrayOfGrammar;
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
        clean();
        XMLGrammarDescription localXMLGrammarDescription = paramGrammar.getGrammarDescription();
        int i = hashCode(localXMLGrammarDescription);
        int j = (i & 0x7FFFFFFF) % fGrammars.length;
        for (Entry localEntry1 = fGrammars[j]; localEntry1 != null; localEntry1 = next) {
          if ((hash == i) && (equals(desc, localXMLGrammarDescription)))
          {
            if (grammar.get() != paramGrammar) {
              grammar = new SoftGrammarReference(localEntry1, paramGrammar, fReferenceQueue);
            }
            return;
          }
        }
        Entry localEntry2 = new Entry(i, j, localXMLGrammarDescription, paramGrammar, fGrammars[j], fReferenceQueue);
        fGrammars[j] = localEntry2;
        fGrammarCount += 1;
      }
    }
  }
  
  public Grammar getGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    synchronized (fGrammars)
    {
      clean();
      int i = hashCode(paramXMLGrammarDescription);
      int j = (i & 0x7FFFFFFF) % fGrammars.length;
      for (Entry localEntry = fGrammars[j]; localEntry != null; localEntry = next)
      {
        localGrammar1 = (Grammar)grammar.get();
        if (localGrammar1 == null)
        {
          removeEntry(localEntry);
        }
        else if ((hash == i) && (equals(desc, paramXMLGrammarDescription)))
        {
          Grammar localGrammar2 = localGrammar1;
          return localGrammar2;
        }
      }
      Grammar localGrammar1 = null;
      return localGrammar1;
    }
  }
  
  public Grammar removeGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    synchronized (fGrammars)
    {
      clean();
      int i = hashCode(paramXMLGrammarDescription);
      int j = (i & 0x7FFFFFFF) % fGrammars.length;
      for (Entry localEntry = fGrammars[j]; localEntry != null; localEntry = next) {
        if ((hash == i) && (equals(desc, paramXMLGrammarDescription)))
        {
          localGrammar = removeEntry(localEntry);
          return localGrammar;
        }
      }
      Grammar localGrammar = null;
      return localGrammar;
    }
  }
  
  public boolean containsGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    synchronized (fGrammars)
    {
      clean();
      int i = hashCode(paramXMLGrammarDescription);
      int j = (i & 0x7FFFFFFF) % fGrammars.length;
      for (Entry localEntry = fGrammars[j]; localEntry != null; localEntry = next)
      {
        Grammar localGrammar = (Grammar)grammar.get();
        if (localGrammar == null)
        {
          removeEntry(localEntry);
        }
        else if ((hash == i) && (equals(desc, paramXMLGrammarDescription)))
        {
          boolean bool2 = true;
          return bool2;
        }
      }
      boolean bool1 = false;
      return bool1;
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
    if ((paramXMLGrammarDescription1 instanceof XMLSchemaDescription))
    {
      if (!(paramXMLGrammarDescription2 instanceof XMLSchemaDescription)) {
        return false;
      }
      XMLSchemaDescription localXMLSchemaDescription1 = (XMLSchemaDescription)paramXMLGrammarDescription1;
      XMLSchemaDescription localXMLSchemaDescription2 = (XMLSchemaDescription)paramXMLGrammarDescription2;
      String str1 = localXMLSchemaDescription1.getTargetNamespace();
      if (str1 != null)
      {
        if (!str1.equals(localXMLSchemaDescription2.getTargetNamespace())) {
          return false;
        }
      }
      else if (localXMLSchemaDescription2.getTargetNamespace() != null) {
        return false;
      }
      String str2 = localXMLSchemaDescription1.getExpandedSystemId();
      if (str2 != null)
      {
        if (!str2.equals(localXMLSchemaDescription2.getExpandedSystemId())) {
          return false;
        }
      }
      else if (localXMLSchemaDescription2.getExpandedSystemId() != null) {
        return false;
      }
      return true;
    }
    return paramXMLGrammarDescription1.equals(paramXMLGrammarDescription2);
  }
  
  public int hashCode(XMLGrammarDescription paramXMLGrammarDescription)
  {
    if ((paramXMLGrammarDescription instanceof XMLSchemaDescription))
    {
      XMLSchemaDescription localXMLSchemaDescription = (XMLSchemaDescription)paramXMLGrammarDescription;
      String str1 = localXMLSchemaDescription.getTargetNamespace();
      String str2 = localXMLSchemaDescription.getExpandedSystemId();
      int i = str1 != null ? str1.hashCode() : 0;
      i ^= (str2 != null ? str2.hashCode() : 0);
      return i;
    }
    return paramXMLGrammarDescription.hashCode();
  }
  
  private Grammar removeEntry(Entry paramEntry)
  {
    if (prev != null) {
      prev.next = next;
    } else {
      fGrammars[bucket] = next;
    }
    if (next != null) {
      next.prev = prev;
    }
    fGrammarCount -= 1;
    grammar.entry = null;
    return (Grammar)grammar.get();
  }
  
  private void clean()
  {
    for (Reference localReference = fReferenceQueue.poll(); localReference != null; localReference = fReferenceQueue.poll())
    {
      Entry localEntry = entry;
      if (localEntry != null) {
        removeEntry(localEntry);
      }
    }
  }
  
  static final class SoftGrammarReference
    extends SoftReference
  {
    public SoftReferenceGrammarPool.Entry entry;
    
    protected SoftGrammarReference(SoftReferenceGrammarPool.Entry paramEntry, Grammar paramGrammar, ReferenceQueue paramReferenceQueue)
    {
      super(paramReferenceQueue);
      entry = paramEntry;
    }
  }
  
  static final class Entry
  {
    public int hash;
    public int bucket;
    public Entry prev;
    public Entry next;
    public XMLGrammarDescription desc;
    public SoftReferenceGrammarPool.SoftGrammarReference grammar;
    
    protected Entry(int paramInt1, int paramInt2, XMLGrammarDescription paramXMLGrammarDescription, Grammar paramGrammar, Entry paramEntry, ReferenceQueue paramReferenceQueue)
    {
      hash = paramInt1;
      bucket = paramInt2;
      prev = null;
      next = paramEntry;
      if (paramEntry != null) {
        prev = this;
      }
      desc = paramXMLGrammarDescription;
      grammar = new SoftReferenceGrammarPool.SoftGrammarReference(this, paramGrammar, paramReferenceQueue);
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

package org.apache.xerces.jaxp.validation;

import java.lang.ref.WeakReference;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class WeakReferenceXMLSchema
  extends AbstractXMLSchema
{
  private WeakReference fGrammarPool = new WeakReference(null);
  
  public WeakReferenceXMLSchema() {}
  
  public synchronized XMLGrammarPool getGrammarPool()
  {
    Object localObject = (XMLGrammarPool)fGrammarPool.get();
    if (localObject == null)
    {
      localObject = new SoftReferenceGrammarPool();
      fGrammarPool = new WeakReference(localObject);
    }
    return localObject;
  }
  
  public boolean isFullyComposed()
  {
    return false;
  }
}

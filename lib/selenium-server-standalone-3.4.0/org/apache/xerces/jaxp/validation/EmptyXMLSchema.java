package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class EmptyXMLSchema
  extends AbstractXMLSchema
  implements XMLGrammarPool
{
  private static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
  
  public EmptyXMLSchema() {}
  
  public Grammar[] retrieveInitialGrammarSet(String paramString)
  {
    return ZERO_LENGTH_GRAMMAR_ARRAY;
  }
  
  public void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar) {}
  
  public Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    return null;
  }
  
  public void lockPool() {}
  
  public void unlockPool() {}
  
  public void clear() {}
  
  public XMLGrammarPool getGrammarPool()
  {
    return this;
  }
  
  public boolean isFullyComposed()
  {
    return true;
  }
}

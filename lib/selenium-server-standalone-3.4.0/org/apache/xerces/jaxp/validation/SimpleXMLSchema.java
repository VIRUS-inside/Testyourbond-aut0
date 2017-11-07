package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class SimpleXMLSchema
  extends AbstractXMLSchema
  implements XMLGrammarPool
{
  private static final Grammar[] ZERO_LENGTH_GRAMMAR_ARRAY = new Grammar[0];
  private final Grammar fGrammar;
  private final Grammar[] fGrammars;
  private final XMLGrammarDescription fGrammarDescription;
  
  public SimpleXMLSchema(Grammar paramGrammar)
  {
    fGrammar = paramGrammar;
    fGrammars = new Grammar[] { paramGrammar };
    fGrammarDescription = paramGrammar.getGrammarDescription();
  }
  
  public Grammar[] retrieveInitialGrammarSet(String paramString)
  {
    return "http://www.w3.org/2001/XMLSchema".equals(paramString) ? (Grammar[])fGrammars.clone() : ZERO_LENGTH_GRAMMAR_ARRAY;
  }
  
  public void cacheGrammars(String paramString, Grammar[] paramArrayOfGrammar) {}
  
  public Grammar retrieveGrammar(XMLGrammarDescription paramXMLGrammarDescription)
  {
    return fGrammarDescription.equals(paramXMLGrammarDescription) ? fGrammar : null;
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

package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.XMLGrammarPool;

final class XMLSchema
  extends AbstractXMLSchema
{
  private final XMLGrammarPool fGrammarPool;
  private final boolean fFullyComposed;
  
  public XMLSchema(XMLGrammarPool paramXMLGrammarPool)
  {
    this(paramXMLGrammarPool, true);
  }
  
  public XMLSchema(XMLGrammarPool paramXMLGrammarPool, boolean paramBoolean)
  {
    fGrammarPool = paramXMLGrammarPool;
    fFullyComposed = paramBoolean;
  }
  
  public XMLGrammarPool getGrammarPool()
  {
    return fGrammarPool;
  }
  
  public boolean isFullyComposed()
  {
    return fFullyComposed;
  }
}

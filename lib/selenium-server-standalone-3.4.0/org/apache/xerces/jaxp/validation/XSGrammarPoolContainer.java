package org.apache.xerces.jaxp.validation;

import org.apache.xerces.xni.grammars.XMLGrammarPool;

public abstract interface XSGrammarPoolContainer
{
  public abstract XMLGrammarPool getGrammarPool();
  
  public abstract boolean isFullyComposed();
  
  public abstract Boolean getFeature(String paramString);
}

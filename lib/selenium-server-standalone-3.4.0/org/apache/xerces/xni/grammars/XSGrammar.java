package org.apache.xerces.xni.grammars;

import org.apache.xerces.xs.XSModel;

public abstract interface XSGrammar
  extends Grammar
{
  public abstract XSModel toXSModel();
  
  public abstract XSModel toXSModel(XSGrammar[] paramArrayOfXSGrammar);
}

package org.apache.xerces.impl.dtd;

import org.apache.xerces.xni.parser.XMLDocumentFilter;

public abstract interface XMLDTDValidatorFilter
  extends XMLDocumentFilter
{
  public abstract boolean hasGrammar();
  
  public abstract boolean validate();
}

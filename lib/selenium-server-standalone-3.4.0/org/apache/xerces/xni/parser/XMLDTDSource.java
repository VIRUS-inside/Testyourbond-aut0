package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XMLDTDHandler;

public abstract interface XMLDTDSource
{
  public abstract void setDTDHandler(XMLDTDHandler paramXMLDTDHandler);
  
  public abstract XMLDTDHandler getDTDHandler();
}

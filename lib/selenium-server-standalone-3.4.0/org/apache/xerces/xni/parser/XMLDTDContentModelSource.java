package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XMLDTDContentModelHandler;

public abstract interface XMLDTDContentModelSource
{
  public abstract void setDTDContentModelHandler(XMLDTDContentModelHandler paramXMLDTDContentModelHandler);
  
  public abstract XMLDTDContentModelHandler getDTDContentModelHandler();
}

package org.apache.xerces.xni.parser;

import org.apache.xerces.xni.XMLDocumentHandler;

public abstract interface XMLDocumentSource
{
  public abstract void setDocumentHandler(XMLDocumentHandler paramXMLDocumentHandler);
  
  public abstract XMLDocumentHandler getDocumentHandler();
}

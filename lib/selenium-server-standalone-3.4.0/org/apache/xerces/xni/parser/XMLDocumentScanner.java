package org.apache.xerces.xni.parser;

import java.io.IOException;
import org.apache.xerces.xni.XNIException;

public abstract interface XMLDocumentScanner
  extends XMLDocumentSource
{
  public abstract void setInputSource(XMLInputSource paramXMLInputSource)
    throws IOException;
  
  public abstract boolean scanDocument(boolean paramBoolean)
    throws IOException, XNIException;
}

package org.apache.xerces.xni.parser;

import java.io.IOException;
import org.apache.xerces.xni.XNIException;

public abstract interface XMLDTDScanner
  extends XMLDTDSource, XMLDTDContentModelSource
{
  public abstract void setInputSource(XMLInputSource paramXMLInputSource)
    throws IOException;
  
  public abstract boolean scanDTDInternalSubset(boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
    throws IOException, XNIException;
  
  public abstract boolean scanDTDExternalSubset(boolean paramBoolean)
    throws IOException, XNIException;
}

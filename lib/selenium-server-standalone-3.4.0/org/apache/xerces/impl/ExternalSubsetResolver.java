package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.grammars.XMLDTDDescription;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLInputSource;

public abstract interface ExternalSubsetResolver
  extends XMLEntityResolver
{
  public abstract XMLInputSource getExternalSubset(XMLDTDDescription paramXMLDTDDescription)
    throws XNIException, IOException;
}

package org.apache.xerces.xni.parser;

import java.io.IOException;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;

public abstract interface XMLEntityResolver
{
  public abstract XMLInputSource resolveEntity(XMLResourceIdentifier paramXMLResourceIdentifier)
    throws XNIException, IOException;
}

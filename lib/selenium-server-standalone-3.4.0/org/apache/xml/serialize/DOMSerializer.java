package org.apache.xml.serialize;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;

/**
 * @deprecated
 */
public abstract interface DOMSerializer
{
  public abstract void serialize(Element paramElement)
    throws IOException;
  
  public abstract void serialize(Document paramDocument)
    throws IOException;
  
  public abstract void serialize(DocumentFragment paramDocumentFragment)
    throws IOException;
}

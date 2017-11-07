package org.apache.xml.serializer;

import java.io.IOException;
import org.w3c.dom.Node;

public abstract interface DOMSerializer
{
  public abstract void serialize(Node paramNode)
    throws IOException;
}

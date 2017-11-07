package org.apache.xerces.util;

import org.apache.xerces.xni.parser.XMLInputSource;
import org.w3c.dom.Node;

public final class DOMInputSource
  extends XMLInputSource
{
  private Node fNode;
  
  public DOMInputSource()
  {
    this(null);
  }
  
  public DOMInputSource(Node paramNode)
  {
    super(null, getSystemIdFromNode(paramNode), null);
    fNode = paramNode;
  }
  
  public DOMInputSource(Node paramNode, String paramString)
  {
    super(null, paramString, null);
    fNode = paramNode;
  }
  
  public Node getNode()
  {
    return fNode;
  }
  
  public void setNode(Node paramNode)
  {
    fNode = paramNode;
  }
  
  private static String getSystemIdFromNode(Node paramNode)
  {
    if (paramNode != null) {
      try
      {
        return paramNode.getBaseURI();
      }
      catch (NoSuchMethodError localNoSuchMethodError)
      {
        return null;
      }
      catch (Exception localException)
      {
        return null;
      }
    }
    return null;
  }
}

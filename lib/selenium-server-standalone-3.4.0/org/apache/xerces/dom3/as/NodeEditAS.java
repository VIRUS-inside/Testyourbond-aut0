package org.apache.xerces.dom3.as;

import org.w3c.dom.Node;

/**
 * @deprecated
 */
public abstract interface NodeEditAS
{
  public static final short WF_CHECK = 1;
  public static final short NS_WF_CHECK = 2;
  public static final short PARTIAL_VALIDITY_CHECK = 3;
  public static final short STRICT_VALIDITY_CHECK = 4;
  
  public abstract boolean canInsertBefore(Node paramNode1, Node paramNode2);
  
  public abstract boolean canRemoveChild(Node paramNode);
  
  public abstract boolean canReplaceChild(Node paramNode1, Node paramNode2);
  
  public abstract boolean canAppendChild(Node paramNode);
  
  public abstract boolean isNodeValid(boolean paramBoolean, short paramShort)
    throws DOMASException;
}

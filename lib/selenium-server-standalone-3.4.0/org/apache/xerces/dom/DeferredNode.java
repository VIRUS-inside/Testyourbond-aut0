package org.apache.xerces.dom;

import org.w3c.dom.Node;

public abstract interface DeferredNode
  extends Node
{
  public static final short TYPE_NODE = 20;
  
  public abstract int getNodeIndex();
}

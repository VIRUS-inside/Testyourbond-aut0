package org.apache.xml.utils;

import org.w3c.dom.Node;

public abstract interface NodeConsumer
{
  public abstract void setOriginatingNode(Node paramNode);
}

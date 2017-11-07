package org.jsoup.select;

import org.jsoup.nodes.Node;

public abstract interface NodeVisitor
{
  public abstract void head(Node paramNode, int paramInt);
  
  public abstract void tail(Node paramNode, int paramInt);
}

package org.apache.xpath.axes;

import org.apache.xpath.XPathContext;

public abstract interface SubContextList
{
  public abstract int getLastPos(XPathContext paramXPathContext);
  
  public abstract int getProximityPosition(XPathContext paramXPathContext);
}

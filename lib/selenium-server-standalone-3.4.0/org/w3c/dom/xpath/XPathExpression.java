package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract interface XPathExpression
{
  public abstract Object evaluate(Node paramNode, short paramShort, Object paramObject)
    throws XPathException, DOMException;
}

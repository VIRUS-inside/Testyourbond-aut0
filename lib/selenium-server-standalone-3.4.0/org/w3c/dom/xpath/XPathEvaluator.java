package org.w3c.dom.xpath;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;

public abstract interface XPathEvaluator
{
  public abstract XPathExpression createExpression(String paramString, XPathNSResolver paramXPathNSResolver)
    throws XPathException, DOMException;
  
  public abstract XPathNSResolver createNSResolver(Node paramNode);
  
  public abstract Object evaluate(String paramString, Node paramNode, XPathNSResolver paramXPathNSResolver, short paramShort, Object paramObject)
    throws XPathException, DOMException;
}

package org.apache.xpath;

public abstract interface XPathVisitable
{
  public abstract void callVisitors(ExpressionOwner paramExpressionOwner, XPathVisitor paramXPathVisitor);
}

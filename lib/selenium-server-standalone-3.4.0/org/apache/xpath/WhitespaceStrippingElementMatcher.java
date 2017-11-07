package org.apache.xpath;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Element;

public abstract interface WhitespaceStrippingElementMatcher
{
  public abstract boolean shouldStripWhiteSpace(XPathContext paramXPathContext, Element paramElement)
    throws TransformerException;
  
  public abstract boolean canStripWhiteSpace();
}

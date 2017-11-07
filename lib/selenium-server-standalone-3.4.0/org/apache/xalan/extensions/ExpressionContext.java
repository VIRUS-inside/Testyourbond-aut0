package org.apache.xalan.extensions;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.QName;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;

public abstract interface ExpressionContext
{
  public abstract Node getContextNode();
  
  public abstract NodeIterator getContextNodes();
  
  public abstract ErrorListener getErrorListener();
  
  public abstract double toNumber(Node paramNode);
  
  public abstract String toString(Node paramNode);
  
  public abstract XObject getVariableOrParam(QName paramQName)
    throws TransformerException;
  
  public abstract XPathContext getXPathContext()
    throws TransformerException;
}

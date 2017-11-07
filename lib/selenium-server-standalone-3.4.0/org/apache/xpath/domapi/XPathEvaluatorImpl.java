package org.apache.xpath.domapi;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.res.XPATHMessages;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathException;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;























































public final class XPathEvaluatorImpl
  implements XPathEvaluator
{
  private final Document m_doc;
  
  private static class DummyPrefixResolver
    implements PrefixResolver
  {
    DummyPrefixResolver() {}
    
    public String getNamespaceForPrefix(String prefix, Node context)
    {
      String fmsg = XPATHMessages.createXPATHMessage("ER_NULL_RESOLVER", null);
      throw new DOMException((short)14, fmsg);
    }
    





    public String getNamespaceForPrefix(String prefix)
    {
      return getNamespaceForPrefix(prefix, null);
    }
    


    public boolean handlesNullPrefixes()
    {
      return false;
    }
    


    public String getBaseIdentifier()
    {
      return null;
    }
  }
  












  public XPathEvaluatorImpl(Document doc)
  {
    m_doc = doc;
  }
  




  public XPathEvaluatorImpl()
  {
    m_doc = null;
  }
  



























  public XPathExpression createExpression(String expression, XPathNSResolver resolver)
    throws XPathException, DOMException
  {
    try
    {
      XPath xpath = new XPath(expression, null, null == resolver ? new DummyPrefixResolver() : (PrefixResolver)resolver, 0);
      


      return new XPathExpressionImpl(xpath, m_doc);

    }
    catch (TransformerException e)
    {
      if ((e instanceof XPathStylesheetDOM3Exception)) {
        throw new DOMException((short)14, e.getMessageAndLocation());
      }
      throw new XPathException((short)51, e.getMessageAndLocation());
    }
  }
  

















  public XPathNSResolver createNSResolver(Node nodeResolver)
  {
    return new XPathNSResolverImpl(nodeResolver.getNodeType() == 9 ? ((Document)nodeResolver).getDocumentElement() : nodeResolver);
  }
  

























































  public Object evaluate(String expression, Node contextNode, XPathNSResolver resolver, short type, Object result)
    throws XPathException, DOMException
  {
    XPathExpression xpathExpression = createExpression(expression, resolver);
    
    return xpathExpression.evaluate(contextNode, type, result);
  }
}

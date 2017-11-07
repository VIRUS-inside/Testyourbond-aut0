package org.apache.xpath;

import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.PrefixResolverDefault;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.NodeIterator;




























































public class CachedXPathAPI
{
  protected XPathContext xpathSupport;
  
  public CachedXPathAPI()
  {
    xpathSupport = new XPathContext(false);
  }
  













  public CachedXPathAPI(CachedXPathAPI priorXPathAPI)
  {
    xpathSupport = xpathSupport;
  }
  






  public XPathContext getXPathContext()
  {
    return xpathSupport;
  }
  












  public Node selectSingleNode(Node contextNode, String str)
    throws TransformerException
  {
    return selectSingleNode(contextNode, str, contextNode);
  }
  














  public Node selectSingleNode(Node contextNode, String str, Node namespaceNode)
    throws TransformerException
  {
    NodeIterator nl = selectNodeIterator(contextNode, str, namespaceNode);
    

    return nl.nextNode();
  }
  










  public NodeIterator selectNodeIterator(Node contextNode, String str)
    throws TransformerException
  {
    return selectNodeIterator(contextNode, str, contextNode);
  }
  














  public NodeIterator selectNodeIterator(Node contextNode, String str, Node namespaceNode)
    throws TransformerException
  {
    XObject list = eval(contextNode, str, namespaceNode);
    

    return list.nodeset();
  }
  










  public NodeList selectNodeList(Node contextNode, String str)
    throws TransformerException
  {
    return selectNodeList(contextNode, str, contextNode);
  }
  














  public NodeList selectNodeList(Node contextNode, String str, Node namespaceNode)
    throws TransformerException
  {
    XObject list = eval(contextNode, str, namespaceNode);
    

    return list.nodelist();
  }
  















  public XObject eval(Node contextNode, String str)
    throws TransformerException
  {
    return eval(contextNode, str, contextNode);
  }
  































  public XObject eval(Node contextNode, String str, Node namespaceNode)
    throws TransformerException
  {
    PrefixResolverDefault prefixResolver = new PrefixResolverDefault(namespaceNode.getNodeType() == 9 ? ((Document)namespaceNode).getDocumentElement() : namespaceNode);
    



    XPath xpath = new XPath(str, null, prefixResolver, 0, null);
    


    int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);
    
    return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
  }
  





























  public XObject eval(Node contextNode, String str, PrefixResolver prefixResolver)
    throws TransformerException
  {
    XPath xpath = new XPath(str, null, prefixResolver, 0, null);
    


    XPathContext xpathSupport = new XPathContext(false);
    

    int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);
    
    return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
  }
}

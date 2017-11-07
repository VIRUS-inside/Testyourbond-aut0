package com.gargoylesoftware.htmlunit.html.xpath;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import java.util.ArrayList;
import java.util.List;
import javax.xml.transform.TransformerException;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;






















public final class XPathUtils
{
  private static ThreadLocal<Boolean> PROCESS_XPATH_ = new ThreadLocal()
  {
    protected synchronized Boolean initialValue() {
      return Boolean.FALSE;
    }
  };
  







  private XPathUtils() {}
  







  public static <T> List<T> getByXPath(DomNode node, String xpathExpr, PrefixResolver resolver)
  {
    if (xpathExpr == null) {
      throw new NullPointerException("Null is not a valid XPath expression");
    }
    
    PROCESS_XPATH_.set(Boolean.TRUE);
    List<T> list = new ArrayList();
    try {
      XObject result = evaluateXPath(node, xpathExpr, resolver);
      
      if ((result instanceof XNodeSet)) {
        NodeList nodelist = ((XNodeSet)result).nodelist();
        for (int i = 0; i < nodelist.getLength(); i++) {
          list.add(nodelist.item(i));
        }
      }
      else if ((result instanceof XNumber)) {
        list.add(Double.valueOf(result.num()));
      }
      else if ((result instanceof XBoolean)) {
        list.add(Boolean.valueOf(result.bool()));
      }
      else if ((result instanceof XString)) {
        list.add(result.str());
      }
      else {
        throw new RuntimeException("Unproccessed " + result.getClass().getName());
      }
    } catch (Exception e) {
      e = e;
      throw new RuntimeException("Could not retrieve XPath >" + xpathExpr + "< on " + node, e);
    } finally {
      localObject = finally;
      PROCESS_XPATH_.set(Boolean.FALSE);
      throw localObject;
    }
    PROCESS_XPATH_.set(Boolean.FALSE);
    
    return list;
  }
  



  public static boolean isProcessingXPath()
  {
    return ((Boolean)PROCESS_XPATH_.get()).booleanValue();
  }
  







  private static XObject evaluateXPath(DomNode contextNode, String str, PrefixResolver prefixResolver)
    throws TransformerException
  {
    XPathContext xpathSupport = new XPathContext();
    Node xpathExpressionContext;
    Node xpathExpressionContext; if (contextNode.getNodeType() == 9) {
      xpathExpressionContext = ((Document)contextNode).getDocumentElement();
    }
    else {
      xpathExpressionContext = contextNode;
    }
    
    PrefixResolver resolver = prefixResolver;
    if (resolver == null) {
      resolver = new HtmlUnitPrefixResolver(xpathExpressionContext);
    }
    
    boolean caseSensitive = contextNode.getPage().hasCaseSensitiveTagNames();
    if (!caseSensitive) {} boolean attributeCaseSensitive = 
      contextNode.getPage().getWebClient()
      .getBrowserVersion().hasFeature(BrowserVersionFeatures.XPATH_ATTRIBUTE_CASE_SENSITIVE);
    XPathAdapter xpath = new XPathAdapter(str, null, resolver, null, caseSensitive, attributeCaseSensitive);
    int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);
    return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
  }
}

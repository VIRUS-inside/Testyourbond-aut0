package org.apache.xpath.jaxp;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;


























public class XPathExpressionImpl
  implements XPathExpression
{
  private XPathFunctionResolver functionResolver;
  private XPathVariableResolver variableResolver;
  private JAXPPrefixResolver prefixResolver;
  private XPath xpath;
  private boolean featureSecureProcessing = false;
  



  protected XPathExpressionImpl() {}
  


  protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver)
  {
    this.xpath = xpath;
    this.prefixResolver = prefixResolver;
    this.functionResolver = functionResolver;
    this.variableResolver = variableResolver;
    featureSecureProcessing = false;
  }
  



  protected XPathExpressionImpl(XPath xpath, JAXPPrefixResolver prefixResolver, XPathFunctionResolver functionResolver, XPathVariableResolver variableResolver, boolean featureSecureProcessing)
  {
    this.xpath = xpath;
    this.prefixResolver = prefixResolver;
    this.functionResolver = functionResolver;
    this.variableResolver = variableResolver;
    this.featureSecureProcessing = featureSecureProcessing;
  }
  
  public void setXPath(XPath xpath) {
    this.xpath = xpath;
  }
  
  public Object eval(Object item, QName returnType) throws TransformerException
  {
    XObject resultObject = eval(item);
    return getResultAsType(resultObject, returnType);
  }
  
  private XObject eval(Object contextItem) throws TransformerException
  {
    XPathContext xpathSupport = null;
    



    if (functionResolver != null) {
      JAXPExtensionsProvider jep = new JAXPExtensionsProvider(functionResolver, featureSecureProcessing);
      
      xpathSupport = new XPathContext(jep, false);
    } else {
      xpathSupport = new XPathContext(false);
    }
    
    xpathSupport.setVarStack(new JAXPVariableStack(variableResolver));
    XObject xobj = null;
    
    Node contextNode = (Node)contextItem;
    


    if (contextNode == null) {
      contextNode = getDummyDocument();
    }
    
    xobj = xpath.execute(xpathSupport, contextNode, prefixResolver);
    return xobj;
  }
  
































  public Object evaluate(Object item, QName returnType)
    throws XPathExpressionException
  {
    if (returnType == null)
    {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "returnType" });
      

      throw new NullPointerException(fmsg);
    }
    

    if (!isSupported(returnType)) {
      String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
      

      throw new IllegalArgumentException(fmsg);
    }
    try {
      return eval(item, returnType);

    }
    catch (NullPointerException npe)
    {
      throw new XPathExpressionException(npe);
    } catch (TransformerException te) {
      Throwable nestedException = te.getException();
      if ((nestedException instanceof XPathFunctionException)) {
        throw ((XPathFunctionException)nestedException);
      }
      

      throw new XPathExpressionException(te);
    }
  }
  

























  public String evaluate(Object item)
    throws XPathExpressionException
  {
    return (String)evaluate(item, XPathConstants.STRING);
  }
  


  static DocumentBuilderFactory dbf = null;
  static DocumentBuilder db = null;
  static Document d = null;
  

































  public Object evaluate(InputSource source, QName returnType)
    throws XPathExpressionException
  {
    if ((source == null) || (returnType == null)) {
      String fmsg = XSLMessages.createXPATHMessage("ER_SOURCE_RETURN_TYPE_CANNOT_BE_NULL", null);
      

      throw new NullPointerException(fmsg);
    }
    

    if (!isSupported(returnType)) {
      String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
      

      throw new IllegalArgumentException(fmsg);
    }
    try {
      if (dbf == null) {
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
      }
      db = dbf.newDocumentBuilder();
      Document document = db.parse(source);
      return eval(document, returnType);
    } catch (Exception e) {
      throw new XPathExpressionException(e);
    }
  }
  




















  public String evaluate(InputSource source)
    throws XPathExpressionException
  {
    return (String)evaluate(source, XPathConstants.STRING);
  }
  
  private boolean isSupported(QName returnType)
  {
    if ((returnType.equals(XPathConstants.STRING)) || (returnType.equals(XPathConstants.NUMBER)) || (returnType.equals(XPathConstants.BOOLEAN)) || (returnType.equals(XPathConstants.NODE)) || (returnType.equals(XPathConstants.NODESET)))
    {




      return true;
    }
    return false;
  }
  
  private Object getResultAsType(XObject resultObject, QName returnType)
    throws TransformerException
  {
    if (returnType.equals(XPathConstants.STRING)) {
      return resultObject.str();
    }
    
    if (returnType.equals(XPathConstants.NUMBER)) {
      return new Double(resultObject.num());
    }
    
    if (returnType.equals(XPathConstants.BOOLEAN)) {
      return resultObject.bool() ? Boolean.TRUE : Boolean.FALSE;
    }
    
    if (returnType.equals(XPathConstants.NODESET)) {
      return resultObject.nodelist();
    }
    
    if (returnType.equals(XPathConstants.NODE)) {
      NodeIterator ni = resultObject.nodeset();
      
      return ni.nextNode();
    }
    

    String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
    

    throw new IllegalArgumentException(fmsg);
  }
  
  private static Document getDummyDocument()
  {
    try {
      if (dbf == null) {
        dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        dbf.setValidating(false);
      }
      db = dbf.newDocumentBuilder();
      
      DOMImplementation dim = db.getDOMImplementation();
      d = dim.createDocument("http://java.sun.com/jaxp/xpath", "dummyroot", null);
      
      return d;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}

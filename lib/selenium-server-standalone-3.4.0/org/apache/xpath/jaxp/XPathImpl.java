package org.apache.xpath.jaxp;

import java.io.IOException;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunctionException;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;
import org.apache.xalan.res.XSLMessages;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XObject;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
































public class XPathImpl
  implements javax.xml.xpath.XPath
{
  private XPathVariableResolver variableResolver;
  private XPathFunctionResolver functionResolver;
  private XPathVariableResolver origVariableResolver;
  private XPathFunctionResolver origFunctionResolver;
  private NamespaceContext namespaceContext = null;
  

  private JAXPPrefixResolver prefixResolver;
  
  private boolean featureSecureProcessing = false;
  
  XPathImpl(XPathVariableResolver vr, XPathFunctionResolver fr) {
    origVariableResolver = (this.variableResolver = vr);
    origFunctionResolver = (this.functionResolver = fr);
  }
  
  XPathImpl(XPathVariableResolver vr, XPathFunctionResolver fr, boolean featureSecureProcessing)
  {
    origVariableResolver = (this.variableResolver = vr);
    origFunctionResolver = (this.functionResolver = fr);
    this.featureSecureProcessing = featureSecureProcessing;
  }
  




  public void setXPathVariableResolver(XPathVariableResolver resolver)
  {
    if (resolver == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPathVariableResolver" });
      

      throw new NullPointerException(fmsg);
    }
    variableResolver = resolver;
  }
  




  public XPathVariableResolver getXPathVariableResolver()
  {
    return variableResolver;
  }
  




  public void setXPathFunctionResolver(XPathFunctionResolver resolver)
  {
    if (resolver == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPathFunctionResolver" });
      

      throw new NullPointerException(fmsg);
    }
    functionResolver = resolver;
  }
  




  public XPathFunctionResolver getXPathFunctionResolver()
  {
    return functionResolver;
  }
  




  public void setNamespaceContext(NamespaceContext nsContext)
  {
    if (nsContext == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "NamespaceContext" });
      

      throw new NullPointerException(fmsg);
    }
    namespaceContext = nsContext;
    prefixResolver = new JAXPPrefixResolver(nsContext);
  }
  




  public NamespaceContext getNamespaceContext()
  {
    return namespaceContext;
  }
  
  private static Document d = null;
  









  private static DocumentBuilder getParser()
  {
    try
    {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      dbf.setValidating(false);
      return dbf.newDocumentBuilder();
    }
    catch (ParserConfigurationException e) {
      throw new Error(e.toString());
    }
  }
  

  private static Document getDummyDocument()
  {
    if (d == null) {
      DOMImplementation dim = getParser().getDOMImplementation();
      d = dim.createDocument("http://java.sun.com/jaxp/xpath", "dummyroot", null);
    }
    
    return d;
  }
  
  private XObject eval(String expression, Object contextItem)
    throws TransformerException
  {
    org.apache.xpath.XPath xpath = new org.apache.xpath.XPath(expression, null, prefixResolver, 0);
    
    XPathContext xpathSupport = null;
    



    if (functionResolver != null) {
      JAXPExtensionsProvider jep = new JAXPExtensionsProvider(functionResolver, featureSecureProcessing);
      
      xpathSupport = new XPathContext(jep, false);
    } else {
      xpathSupport = new XPathContext(false);
    }
    
    XObject xobj = null;
    
    xpathSupport.setVarStack(new JAXPVariableStack(variableResolver));
    

    if ((contextItem instanceof Node)) {
      xobj = xpath.execute(xpathSupport, (Node)contextItem, prefixResolver);
    }
    else {
      xobj = xpath.execute(xpathSupport, -1, prefixResolver);
    }
    
    return xobj;
  }
  






























  public Object evaluate(String expression, Object item, QName returnType)
    throws XPathExpressionException
  {
    if (expression == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPath expression" });
      

      throw new NullPointerException(fmsg);
    }
    if (returnType == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "returnType" });
      

      throw new NullPointerException(fmsg);
    }
    

    if (!isSupported(returnType)) {
      String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
      

      throw new IllegalArgumentException(fmsg);
    }
    
    try
    {
      XObject resultObject = eval(expression, item);
      return getResultAsType(resultObject, returnType);

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
  

























  public String evaluate(String expression, Object item)
    throws XPathExpressionException
  {
    return (String)evaluate(expression, item, XPathConstants.STRING);
  }
  
















  public XPathExpression compile(String expression)
    throws XPathExpressionException
  {
    if (expression == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPath expression" });
      

      throw new NullPointerException(fmsg);
    }
    try {
      org.apache.xpath.XPath xpath = new org.apache.xpath.XPath(expression, null, prefixResolver, 0);
      

      return new XPathExpressionImpl(xpath, prefixResolver, functionResolver, variableResolver, featureSecureProcessing);

    }
    catch (TransformerException te)
    {
      throw new XPathExpressionException(te);
    }
  }
  





























  public Object evaluate(String expression, InputSource source, QName returnType)
    throws XPathExpressionException
  {
    if (source == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "source" });
      

      throw new NullPointerException(fmsg);
    }
    if (expression == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "XPath expression" });
      

      throw new NullPointerException(fmsg);
    }
    if (returnType == null) {
      String fmsg = XSLMessages.createXPATHMessage("ER_ARG_CANNOT_BE_NULL", new Object[] { "returnType" });
      

      throw new NullPointerException(fmsg);
    }
    


    if (!isSupported(returnType)) {
      String fmsg = XSLMessages.createXPATHMessage("ER_UNSUPPORTED_RETURN_TYPE", new Object[] { returnType.toString() });
      

      throw new IllegalArgumentException(fmsg);
    }
    
    try
    {
      Document document = getParser().parse(source);
      
      XObject resultObject = eval(expression, document);
      return getResultAsType(resultObject, returnType);
    } catch (SAXException e) {
      throw new XPathExpressionException(e);
    } catch (IOException e) {
      throw new XPathExpressionException(e);
    } catch (TransformerException te) {
      Throwable nestedException = te.getException();
      if ((nestedException instanceof XPathFunctionException)) {
        throw ((XPathFunctionException)nestedException);
      }
      throw new XPathExpressionException(te);
    }
  }
  



























  public String evaluate(String expression, InputSource source)
    throws XPathExpressionException
  {
    return (String)evaluate(expression, source, XPathConstants.STRING);
  }
  














  public void reset()
  {
    variableResolver = origVariableResolver;
    functionResolver = origFunctionResolver;
    namespaceContext = null;
  }
}

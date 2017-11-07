package org.apache.xalan.extensions;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;
import org.apache.xalan.res.XSLMessages;
























public class XPathFunctionResolverImpl
  implements XPathFunctionResolver
{
  public XPathFunctionResolverImpl() {}
  
  public XPathFunction resolveFunction(QName qname, int arity)
  {
    if (qname == null) {
      throw new NullPointerException(XSLMessages.createMessage("ER_XPATH_RESOLVER_NULL_QNAME", null));
    }
    

    if (arity < 0) {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_XPATH_RESOLVER_NEGATIVE_ARITY", null));
    }
    

    String uri = qname.getNamespaceURI();
    if ((uri == null) || (uri.length() == 0)) {
      return null;
    }
    String className = null;
    String methodName = null;
    if (uri.startsWith("http://exslt.org"))
    {
      className = getEXSLTClassName(uri);
      methodName = qname.getLocalPart();
    }
    else if (!uri.equals("http://xml.apache.org/xalan/java"))
    {
      int lastSlash = className.lastIndexOf('/');
      if (-1 != lastSlash) {
        className = className.substring(lastSlash + 1);
      }
    }
    String localPart = qname.getLocalPart();
    int lastDotIndex = localPart.lastIndexOf('.');
    if (lastDotIndex > 0)
    {
      if (className != null) {
        className = className + "." + localPart.substring(0, lastDotIndex);
      } else {
        className = localPart.substring(0, lastDotIndex);
      }
      methodName = localPart.substring(lastDotIndex + 1);
    }
    else {
      methodName = localPart;
    }
    if ((null == className) || (className.trim().length() == 0) || (null == methodName) || (methodName.trim().length() == 0))
    {
      return null;
    }
    ExtensionHandler handler = null;
    try
    {
      ExtensionHandler.getClassForName(className);
      handler = new ExtensionHandlerJavaClass(uri, "javaclass", className);
    }
    catch (ClassNotFoundException e)
    {
      return null;
    }
    return new XPathFunctionImpl(handler, methodName);
  }
  




  private String getEXSLTClassName(String uri)
  {
    if (uri.equals("http://exslt.org/math"))
      return "org.apache.xalan.lib.ExsltMath";
    if (uri.equals("http://exslt.org/sets"))
      return "org.apache.xalan.lib.ExsltSets";
    if (uri.equals("http://exslt.org/strings"))
      return "org.apache.xalan.lib.ExsltStrings";
    if (uri.equals("http://exslt.org/dates-and-times"))
      return "org.apache.xalan.lib.ExsltDatetime";
    if (uri.equals("http://exslt.org/dynamic"))
      return "org.apache.xalan.lib.ExsltDynamic";
    if (uri.equals("http://exslt.org/common")) {
      return "org.apache.xalan.lib.ExsltCommon";
    }
    return null;
  }
}

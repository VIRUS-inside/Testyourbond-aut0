package org.apache.xpath.functions;

import java.util.HashMap;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.StylesheetRoot;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.QName;
import org.apache.xpath.Expression;
import org.apache.xpath.ExtensionsProvider;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XObject;






























public class FuncExtElementAvailable
  extends FunctionOneArg
{
  static final long serialVersionUID = -472533699257968546L;
  
  public FuncExtElementAvailable() {}
  
  public XObject execute(XPathContext xctxt)
    throws TransformerException
  {
    String fullName = m_arg0.execute(xctxt).str();
    int indexOfNSSep = fullName.indexOf(':');
    String methName;
    String namespace; String methName; if (indexOfNSSep < 0)
    {
      String prefix = "";
      String namespace = "http://www.w3.org/1999/XSL/Transform";
      methName = fullName;
    }
    else
    {
      String prefix = fullName.substring(0, indexOfNSSep);
      namespace = xctxt.getNamespaceContext().getNamespaceForPrefix(prefix);
      if (null == namespace)
        return XBoolean.S_FALSE;
      methName = fullName.substring(indexOfNSSep + 1);
    }
    
    if ((namespace.equals("http://www.w3.org/1999/XSL/Transform")) || (namespace.equals("http://xml.apache.org/xalan")))
    {
      try
      {

        TransformerImpl transformer = (TransformerImpl)xctxt.getOwnerObject();
        return transformer.getStylesheet().getAvailableElements().containsKey(new QName(namespace, methName)) ? XBoolean.S_TRUE : XBoolean.S_FALSE;

      }
      catch (Exception e)
      {

        return XBoolean.S_FALSE;
      }
    }
    


    ExtensionsProvider extProvider = (ExtensionsProvider)xctxt.getOwnerObject();
    return extProvider.elementAvailable(namespace, methName) ? XBoolean.S_TRUE : XBoolean.S_FALSE;
  }
}

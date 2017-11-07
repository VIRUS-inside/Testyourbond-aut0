package org.apache.xalan.extensions;

import java.util.List;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;



























public class XPathFunctionImpl
  implements XPathFunction
{
  private ExtensionHandler m_handler;
  private String m_funcName;
  
  public XPathFunctionImpl(ExtensionHandler handler, String funcName)
  {
    m_handler = handler;
    m_funcName = funcName;
  }
  



  public Object evaluate(List args)
    throws XPathFunctionException
  {
    Vector argsVec = listToVector(args);
    
    try
    {
      return m_handler.callFunction(m_funcName, argsVec, null, null);
    }
    catch (TransformerException e)
    {
      throw new XPathFunctionException(e);
    }
  }
  




  private static Vector listToVector(List args)
  {
    if (args == null)
      return null;
    if ((args instanceof Vector)) {
      return (Vector)args;
    }
    
    Vector result = new Vector();
    result.addAll(args);
    return result;
  }
}

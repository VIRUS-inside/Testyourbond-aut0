package org.apache.xalan.extensions;

import java.io.IOException;
import java.util.Vector;
import javax.xml.transform.TransformerException;
import org.apache.xalan.templates.ElemTemplateElement;
import org.apache.xalan.templates.Stylesheet;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xpath.functions.FuncExtFunction;











































public abstract class ExtensionHandler
{
  protected String m_namespaceUri;
  protected String m_scriptLang;
  
  static Class getClassForName(String className)
    throws ClassNotFoundException
  {
    if (className.equals("org.apache.xalan.xslt.extensions.Redirect")) {
      className = "org.apache.xalan.lib.Redirect";
    }
    
    return ObjectFactory.findProviderClass(className, ObjectFactory.findClassLoader(), true);
  }
  








  protected ExtensionHandler(String namespaceUri, String scriptLang)
  {
    m_namespaceUri = namespaceUri;
    m_scriptLang = scriptLang;
  }
  
  public abstract boolean isFunctionAvailable(String paramString);
  
  public abstract boolean isElementAvailable(String paramString);
  
  public abstract Object callFunction(String paramString, Vector paramVector, Object paramObject, ExpressionContext paramExpressionContext)
    throws TransformerException;
  
  public abstract Object callFunction(FuncExtFunction paramFuncExtFunction, Vector paramVector, ExpressionContext paramExpressionContext)
    throws TransformerException;
  
  public abstract void processElement(String paramString, ElemTemplateElement paramElemTemplateElement, TransformerImpl paramTransformerImpl, Stylesheet paramStylesheet, Object paramObject)
    throws TransformerException, IOException;
}

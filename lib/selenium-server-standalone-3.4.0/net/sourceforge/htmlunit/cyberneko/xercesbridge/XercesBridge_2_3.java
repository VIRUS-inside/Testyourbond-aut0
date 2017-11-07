package net.sourceforge.htmlunit.cyberneko.xercesbridge;

import org.apache.xerces.xni.NamespaceContext;




















public class XercesBridge_2_3
  extends XercesBridge_2_2
{
  public XercesBridge_2_3()
    throws InstantiationException
  {
    try
    {
      NamespaceContext.class.getMethod("declarePrefix", new Class[] { String.class, String.class });
    }
    catch (NoSuchMethodException e)
    {
      throw new InstantiationException(e.getMessage());
    }
  }
  

  public void NamespaceContext_declarePrefix(NamespaceContext namespaceContext, String ns, String avalue)
  {
    namespaceContext.declarePrefix(ns, avalue);
  }
}

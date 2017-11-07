package org.apache.xalan.extensions;

import java.util.Vector;
































public class ExtensionNamespacesManager
{
  private Vector m_extensions = new Vector();
  



  private Vector m_predefExtensions = new Vector(7);
  



  private Vector m_unregisteredExtensions = new Vector();
  





  public ExtensionNamespacesManager()
  {
    setPredefinedNamespaces();
  }
  










  public void registerExtension(String namespace)
  {
    if (namespaceIndex(namespace, m_extensions) == -1)
    {
      int predef = namespaceIndex(namespace, m_predefExtensions);
      if (predef != -1) {
        m_extensions.add(m_predefExtensions.get(predef));
      } else if (!m_unregisteredExtensions.contains(namespace)) {
        m_unregisteredExtensions.add(namespace);
      }
    }
  }
  




  public void registerExtension(ExtensionNamespaceSupport extNsSpt)
  {
    String namespace = extNsSpt.getNamespace();
    if (namespaceIndex(namespace, m_extensions) == -1)
    {
      m_extensions.add(extNsSpt);
      if (m_unregisteredExtensions.contains(namespace)) {
        m_unregisteredExtensions.remove(namespace);
      }
    }
  }
  




  public int namespaceIndex(String namespace, Vector extensions)
  {
    for (int i = 0; i < extensions.size(); i++)
    {
      if (((ExtensionNamespaceSupport)extensions.get(i)).getNamespace().equals(namespace))
        return i;
    }
    return -1;
  }
  






  public Vector getExtensions()
  {
    return m_extensions;
  }
  



  public void registerUnregisteredNamespaces()
  {
    for (int i = 0; i < m_unregisteredExtensions.size(); i++)
    {
      String ns = (String)m_unregisteredExtensions.get(i);
      ExtensionNamespaceSupport extNsSpt = defineJavaNamespace(ns);
      if (extNsSpt != null) {
        m_extensions.add(extNsSpt);
      }
    }
  }
  
















  public ExtensionNamespaceSupport defineJavaNamespace(String ns)
  {
    return defineJavaNamespace(ns, ns);
  }
  
  public ExtensionNamespaceSupport defineJavaNamespace(String ns, String classOrPackage) {
    if ((null == ns) || (ns.trim().length() == 0)) {
      return null;
    }
    


    String className = classOrPackage;
    if (className.startsWith("class:")) {
      className = className.substring(6);
    }
    int lastSlash = className.lastIndexOf('/');
    if (-1 != lastSlash) {
      className = className.substring(lastSlash + 1);
    }
    

    if ((null == className) || (className.trim().length() == 0)) {
      return null;
    }
    try
    {
      ExtensionHandler.getClassForName(className);
      return new ExtensionNamespaceSupport(ns, "org.apache.xalan.extensions.ExtensionHandlerJavaClass", new Object[] { ns, "javaclass", className });
    }
    catch (ClassNotFoundException e) {}
    



    return new ExtensionNamespaceSupport(ns, "org.apache.xalan.extensions.ExtensionHandlerJavaPackage", tmp130_125);
  }
  















  private void setPredefinedNamespaces()
  {
    String uri = "http://xml.apache.org/xalan/java";
    String handlerClassName = "org.apache.xalan.extensions.ExtensionHandlerJavaPackage";
    String lang = "javapackage";
    String lib = "";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://xml.apache.org/xslt/java";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://xsl.lotus.com/java";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://xml.apache.org/xalan";
    handlerClassName = "org.apache.xalan.extensions.ExtensionHandlerJavaClass";
    lang = "javaclass";
    lib = "org.apache.xalan.lib.Extensions";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://xml.apache.org/xslt";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    


    uri = "http://xml.apache.org/xalan/redirect";
    lib = "org.apache.xalan.lib.Redirect";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://xml.apache.org/xalan/PipeDocument";
    lib = "org.apache.xalan.lib.PipeDocument";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://xml.apache.org/xalan/sql";
    lib = "org.apache.xalan.lib.sql.XConnection";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    




    uri = "http://exslt.org/common";
    lib = "org.apache.xalan.lib.ExsltCommon";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://exslt.org/math";
    lib = "org.apache.xalan.lib.ExsltMath";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://exslt.org/sets";
    lib = "org.apache.xalan.lib.ExsltSets";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://exslt.org/dates-and-times";
    lib = "org.apache.xalan.lib.ExsltDatetime";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://exslt.org/dynamic";
    lib = "org.apache.xalan.lib.ExsltDynamic";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
    

    uri = "http://exslt.org/strings";
    lib = "org.apache.xalan.lib.ExsltStrings";
    m_predefExtensions.add(new ExtensionNamespaceSupport(uri, handlerClassName, new Object[] { uri, lang, lib }));
  }
}

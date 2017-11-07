package org.apache.xalan.templates;

import javax.xml.transform.TransformerException;
import org.apache.xalan.extensions.ExtensionNamespaceSupport;
import org.apache.xalan.extensions.ExtensionNamespacesManager;
import org.apache.xalan.res.XSLMessages;
import org.apache.xalan.transformer.TransformerImpl;
import org.apache.xml.utils.StringVector;







































public class ElemExtensionDecl
  extends ElemTemplateElement
{
  static final long serialVersionUID = -4692738885172766789L;
  private String m_prefix = null;
  


  public ElemExtensionDecl() {}
  


  public void setPrefix(String v)
  {
    m_prefix = v;
  }
  






  public String getPrefix()
  {
    return m_prefix;
  }
  


  private StringVector m_functions = new StringVector();
  






  public void setFunctions(StringVector v)
  {
    m_functions = v;
  }
  






  public StringVector getFunctions()
  {
    return m_functions;
  }
  










  public String getFunction(int i)
    throws ArrayIndexOutOfBoundsException
  {
    if (null == m_functions) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return m_functions.elementAt(i);
  }
  






  public int getFunctionCount()
  {
    return null != m_functions ? m_functions.size() : 0;
  }
  


  private StringVector m_elements = null;
  






  public void setElements(StringVector v)
  {
    m_elements = v;
  }
  






  public StringVector getElements()
  {
    return m_elements;
  }
  










  public String getElement(int i)
    throws ArrayIndexOutOfBoundsException
  {
    if (null == m_elements) {
      throw new ArrayIndexOutOfBoundsException();
    }
    return m_elements.elementAt(i);
  }
  






  public int getElementCount()
  {
    return null != m_elements ? m_elements.size() : 0;
  }
  






  public int getXSLToken()
  {
    return 85;
  }
  
  public void compose(StylesheetRoot sroot) throws TransformerException
  {
    super.compose(sroot);
    String prefix = getPrefix();
    String declNamespace = getNamespaceForPrefix(prefix);
    String lang = null;
    String srcURL = null;
    String scriptSrc = null;
    if (null == declNamespace) {
      throw new TransformerException(XSLMessages.createMessage("ER_NO_NAMESPACE_DECL", new Object[] { prefix }));
    }
    for (ElemTemplateElement child = getFirstChildElem(); child != null; 
        child = child.getNextSiblingElem())
    {
      if (86 == child.getXSLToken())
      {
        ElemExtensionScript sdecl = (ElemExtensionScript)child;
        lang = sdecl.getLang();
        srcURL = sdecl.getSrc();
        ElemTemplateElement childOfSDecl = sdecl.getFirstChildElem();
        if (null != childOfSDecl)
        {
          if (78 == childOfSDecl.getXSLToken())
          {

            ElemTextLiteral tl = (ElemTextLiteral)childOfSDecl;
            char[] chars = tl.getChars();
            scriptSrc = new String(chars);
            if (scriptSrc.trim().length() == 0)
              scriptSrc = null;
          }
        }
      }
    }
    if (null == lang)
      lang = "javaclass";
    if ((lang.equals("javaclass")) && (scriptSrc != null)) {
      throw new TransformerException(XSLMessages.createMessage("ER_ELEM_CONTENT_NOT_ALLOWED", new Object[] { scriptSrc }));
    }
    

    ExtensionNamespaceSupport extNsSpt = null;
    ExtensionNamespacesManager extNsMgr = sroot.getExtensionNamespacesManager();
    if (extNsMgr.namespaceIndex(declNamespace, extNsMgr.getExtensions()) == -1)
    {

      if (lang.equals("javaclass"))
      {
        if (null == srcURL)
        {
          extNsSpt = extNsMgr.defineJavaNamespace(declNamespace);
        }
        else if (extNsMgr.namespaceIndex(srcURL, extNsMgr.getExtensions()) == -1)
        {

          extNsSpt = extNsMgr.defineJavaNamespace(declNamespace, srcURL);
        }
      }
      else
      {
        String handler = "org.apache.xalan.extensions.ExtensionHandlerGeneral";
        Object[] args = { declNamespace, m_elements, m_functions, lang, srcURL, scriptSrc, getSystemId() };
        
        extNsSpt = new ExtensionNamespaceSupport(declNamespace, handler, args);
      }
    }
    if (extNsSpt != null) {
      extNsMgr.registerExtension(extNsSpt);
    }
  }
  
  public void runtimeInit(TransformerImpl transformer)
    throws TransformerException
  {}
}

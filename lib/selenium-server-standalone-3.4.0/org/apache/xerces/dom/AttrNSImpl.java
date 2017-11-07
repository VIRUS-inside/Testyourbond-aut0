package org.apache.xerces.dom;

import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.xni.NamespaceContext;
import org.w3c.dom.DOMException;

public class AttrNSImpl
  extends AttrImpl
{
  static final long serialVersionUID = -781906615369795414L;
  static final String xmlnsURI = "http://www.w3.org/2000/xmlns/";
  static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
  protected String namespaceURI;
  protected String localName;
  
  public AttrNSImpl() {}
  
  protected AttrNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2)
  {
    super(paramCoreDocumentImpl, paramString2);
    setName(paramString1, paramString2);
  }
  
  private void setName(String paramString1, String paramString2)
  {
    CoreDocumentImpl localCoreDocumentImpl = ownerDocument();
    namespaceURI = paramString1;
    if (paramString1 != null) {
      namespaceURI = (paramString1.length() == 0 ? null : paramString1);
    }
    int i = paramString2.indexOf(':');
    int j = paramString2.lastIndexOf(':');
    localCoreDocumentImpl.checkNamespaceWF(paramString2, i, j);
    if (i < 0)
    {
      localName = paramString2;
      if (errorChecking)
      {
        localCoreDocumentImpl.checkQName(null, localName);
        if (((paramString2.equals("xmlns")) && ((paramString1 == null) || (!paramString1.equals(NamespaceContext.XMLNS_URI)))) || ((paramString1 != null) && (paramString1.equals(NamespaceContext.XMLNS_URI)) && (!paramString2.equals("xmlns"))))
        {
          String str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
          throw new DOMException((short)14, str2);
        }
      }
    }
    else
    {
      String str1 = paramString2.substring(0, i);
      localName = paramString2.substring(j + 1);
      localCoreDocumentImpl.checkQName(str1, localName);
      localCoreDocumentImpl.checkDOMNSErr(str1, paramString1);
    }
  }
  
  public AttrNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2, String paramString3)
  {
    super(paramCoreDocumentImpl, paramString2);
    localName = paramString3;
    namespaceURI = paramString1;
  }
  
  protected AttrNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
  {
    super(paramCoreDocumentImpl, paramString);
  }
  
  void rename(String paramString1, String paramString2)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    name = paramString2;
    setName(paramString1, paramString2);
  }
  
  public String getNamespaceURI()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return namespaceURI;
  }
  
  public String getPrefix()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    int i = name.indexOf(':');
    return i < 0 ? null : name.substring(0, i);
  }
  
  public void setPrefix(String paramString)
    throws DOMException
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (ownerDocumenterrorChecking)
    {
      String str;
      if (isReadOnly())
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str);
      }
      if ((paramString != null) && (paramString.length() != 0))
      {
        if (!CoreDocumentImpl.isXMLName(paramString, ownerDocument().isXML11Version()))
        {
          str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
          throw new DOMException((short)5, str);
        }
        if ((namespaceURI == null) || (paramString.indexOf(':') >= 0))
        {
          str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
          throw new DOMException((short)14, str);
        }
        if (paramString.equals("xmlns"))
        {
          if (!namespaceURI.equals("http://www.w3.org/2000/xmlns/"))
          {
            str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
            throw new DOMException((short)14, str);
          }
        }
        else if (paramString.equals("xml"))
        {
          if (!namespaceURI.equals("http://www.w3.org/XML/1998/namespace"))
          {
            str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
            throw new DOMException((short)14, str);
          }
        }
        else if (name.equals("xmlns"))
        {
          str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
          throw new DOMException((short)14, str);
        }
      }
    }
    if ((paramString != null) && (paramString.length() != 0)) {
      name = (paramString + ":" + localName);
    } else {
      name = localName;
    }
  }
  
  public String getLocalName()
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    return localName;
  }
  
  public String getTypeName()
  {
    if (type != null)
    {
      if ((type instanceof XSSimpleTypeDecl)) {
        return ((XSSimpleTypeDecl)type).getName();
      }
      return (String)type;
    }
    return null;
  }
  
  public boolean isDerivedFrom(String paramString1, String paramString2, int paramInt)
  {
    if ((type != null) && ((type instanceof XSSimpleTypeDecl))) {
      return ((XSSimpleTypeDecl)type).isDOMDerivedFrom(paramString1, paramString2, paramInt);
    }
    return false;
  }
  
  public String getTypeNamespace()
  {
    if (type != null)
    {
      if ((type instanceof XSSimpleTypeDecl)) {
        return ((XSSimpleTypeDecl)type).getNamespace();
      }
      return "http://www.w3.org/TR/REC-xml";
    }
    return null;
  }
}

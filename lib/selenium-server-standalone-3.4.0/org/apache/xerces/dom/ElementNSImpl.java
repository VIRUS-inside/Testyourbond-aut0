package org.apache.xerces.dom;

import org.apache.xerces.impl.dv.xs.XSSimpleTypeDecl;
import org.apache.xerces.impl.xs.XSComplexTypeDecl;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xs.XSTypeDefinition;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

public class ElementNSImpl
  extends ElementImpl
{
  static final long serialVersionUID = -9142310625494392642L;
  static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
  protected String namespaceURI;
  protected String localName;
  transient XSTypeDefinition type;
  
  protected ElementNSImpl() {}
  
  protected ElementNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2)
    throws DOMException
  {
    super(paramCoreDocumentImpl, paramString2);
    setName(paramString1, paramString2);
  }
  
  private void setName(String paramString1, String paramString2)
  {
    namespaceURI = paramString1;
    if (paramString1 != null) {
      namespaceURI = (paramString1.length() == 0 ? null : paramString1);
    }
    String str2;
    if (paramString2 == null)
    {
      str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
      throw new DOMException((short)14, str2);
    }
    int i = paramString2.indexOf(':');
    int j = paramString2.lastIndexOf(':');
    ownerDocument.checkNamespaceWF(paramString2, i, j);
    if (i < 0)
    {
      localName = paramString2;
      if (ownerDocument.errorChecking)
      {
        ownerDocument.checkQName(null, localName);
        if (((paramString2.equals("xmlns")) && ((paramString1 == null) || (!paramString1.equals(NamespaceContext.XMLNS_URI)))) || ((paramString1 != null) && (paramString1.equals(NamespaceContext.XMLNS_URI)) && (!paramString2.equals("xmlns"))))
        {
          str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
          throw new DOMException((short)14, str2);
        }
      }
    }
    else
    {
      String str1 = paramString2.substring(0, i);
      localName = paramString2.substring(j + 1);
      if (ownerDocument.errorChecking)
      {
        if ((paramString1 == null) || ((str1.equals("xml")) && (!paramString1.equals(NamespaceContext.XML_URI))))
        {
          str2 = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
          throw new DOMException((short)14, str2);
        }
        ownerDocument.checkQName(str1, localName);
        ownerDocument.checkDOMNSErr(str1, paramString1);
      }
    }
  }
  
  protected ElementNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString1, String paramString2, String paramString3)
    throws DOMException
  {
    super(paramCoreDocumentImpl, paramString2);
    localName = paramString3;
    namespaceURI = paramString1;
  }
  
  protected ElementNSImpl(CoreDocumentImpl paramCoreDocumentImpl, String paramString)
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
    reconcileDefaultAttributes();
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
    if (ownerDocument.errorChecking)
    {
      String str;
      if (isReadOnly())
      {
        str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
        throw new DOMException((short)7, str);
      }
      if ((paramString != null) && (paramString.length() != 0))
      {
        if (!CoreDocumentImpl.isXMLName(paramString, ownerDocument.isXML11Version()))
        {
          str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INVALID_CHARACTER_ERR", null);
          throw new DOMException((short)5, str);
        }
        if ((namespaceURI == null) || (paramString.indexOf(':') >= 0))
        {
          str = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NAMESPACE_ERR", null);
          throw new DOMException((short)14, str);
        }
        if ((paramString.equals("xml")) && (!namespaceURI.equals("http://www.w3.org/XML/1998/namespace")))
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
  
  protected Attr getXMLBaseAttribute()
  {
    return (Attr)attributes.getNamedItemNS("http://www.w3.org/XML/1998/namespace", "base");
  }
  
  public String getTypeName()
  {
    if (type != null)
    {
      if ((type instanceof XSSimpleTypeDecl)) {
        return ((XSSimpleTypeDecl)type).getTypeName();
      }
      if ((type instanceof XSComplexTypeDecl)) {
        return ((XSComplexTypeDecl)type).getTypeName();
      }
    }
    return null;
  }
  
  public String getTypeNamespace()
  {
    if (type != null) {
      return type.getNamespace();
    }
    return null;
  }
  
  public boolean isDerivedFrom(String paramString1, String paramString2, int paramInt)
  {
    if (needsSyncData()) {
      synchronizeData();
    }
    if (type != null)
    {
      if ((type instanceof XSSimpleTypeDecl)) {
        return ((XSSimpleTypeDecl)type).isDOMDerivedFrom(paramString1, paramString2, paramInt);
      }
      if ((type instanceof XSComplexTypeDecl)) {
        return ((XSComplexTypeDecl)type).isDOMDerivedFrom(paramString1, paramString2, paramInt);
      }
    }
    return false;
  }
  
  public void setType(XSTypeDefinition paramXSTypeDefinition)
  {
    type = paramXSTypeDefinition;
  }
}

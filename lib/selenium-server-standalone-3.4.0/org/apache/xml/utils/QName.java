package org.apache.xml.utils;

import java.io.Serializable;
import java.util.Stack;
import java.util.StringTokenizer;
import org.apache.xml.res.XMLMessages;
import org.w3c.dom.Element;







































































public class QName
  implements Serializable
{
  static final long serialVersionUID = 467434581652829920L;
  protected String _localName;
  protected String _namespaceURI;
  protected String _prefix;
  public static final String S_XMLNAMESPACEURI = "http://www.w3.org/XML/1998/namespace";
  private int m_hashCode;
  
  public QName() {}
  
  public QName(String namespaceURI, String localName)
  {
    this(namespaceURI, localName, false);
  }
  












  public QName(String namespaceURI, String localName, boolean validate)
  {
    if (localName == null) {
      throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
    }
    
    if (validate)
    {
      if (!XML11Char.isXML11ValidNCName(localName))
      {
        throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
      }
    }
    

    _namespaceURI = namespaceURI;
    _localName = localName;
    m_hashCode = toString().hashCode();
  }
  









  public QName(String namespaceURI, String prefix, String localName)
  {
    this(namespaceURI, prefix, localName, false);
  }
  













  public QName(String namespaceURI, String prefix, String localName, boolean validate)
  {
    if (localName == null) {
      throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
    }
    
    if (validate)
    {
      if (!XML11Char.isXML11ValidNCName(localName))
      {
        throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
      }
      

      if ((null != prefix) && (!XML11Char.isXML11ValidNCName(prefix)))
      {
        throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_PREFIX_INVALID", null));
      }
    }
    

    _namespaceURI = namespaceURI;
    _prefix = prefix;
    _localName = localName;
    m_hashCode = toString().hashCode();
  }
  







  public QName(String localName)
  {
    this(localName, false);
  }
  











  public QName(String localName, boolean validate)
  {
    if (localName == null) {
      throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_NULL", null));
    }
    
    if (validate)
    {
      if (!XML11Char.isXML11ValidNCName(localName))
      {
        throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
      }
    }
    
    _namespaceURI = null;
    _localName = localName;
    m_hashCode = toString().hashCode();
  }
  








  public QName(String qname, Stack namespaces)
  {
    this(qname, namespaces, false);
  }
  











  public QName(String qname, Stack namespaces, boolean validate)
  {
    String namespace = null;
    String prefix = null;
    int indexOfNSSep = qname.indexOf(':');
    
    if (indexOfNSSep > 0)
    {
      prefix = qname.substring(0, indexOfNSSep);
      
      if (prefix.equals("xml"))
      {
        namespace = "http://www.w3.org/XML/1998/namespace";
      }
      else {
        if (prefix.equals("xmlns"))
        {
          return;
        }
        

        int depth = namespaces.size();
        
        for (int i = depth - 1; i >= 0; i--)
        {
          NameSpace ns = (NameSpace)namespaces.elementAt(i);
          
          while (null != ns)
          {
            if ((null != m_prefix) && (prefix.equals(m_prefix)))
            {
              namespace = m_uri;
              i = -1;
              
              break;
            }
            
            ns = m_next;
          }
        }
      }
      
      if (null == namespace)
      {
        throw new RuntimeException(XMLMessages.createXMLMessage("ER_PREFIX_MUST_RESOLVE", new Object[] { prefix }));
      }
    }
    



    _localName = (indexOfNSSep < 0 ? qname : qname.substring(indexOfNSSep + 1));
    

    if (validate)
    {
      if ((_localName == null) || (!XML11Char.isXML11ValidNCName(_localName)))
      {
        throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
      }
    }
    
    _namespaceURI = namespace;
    _prefix = prefix;
    m_hashCode = toString().hashCode();
  }
  










  public QName(String qname, Element namespaceContext, PrefixResolver resolver)
  {
    this(qname, namespaceContext, resolver, false);
  }
  













  public QName(String qname, Element namespaceContext, PrefixResolver resolver, boolean validate)
  {
    _namespaceURI = null;
    
    int indexOfNSSep = qname.indexOf(':');
    
    if (indexOfNSSep > 0)
    {
      if (null != namespaceContext)
      {
        String prefix = qname.substring(0, indexOfNSSep);
        
        _prefix = prefix;
        
        if (prefix.equals("xml"))
        {
          _namespaceURI = "http://www.w3.org/XML/1998/namespace";
        }
        else
        {
          if (prefix.equals("xmlns"))
          {
            return;
          }
          

          _namespaceURI = resolver.getNamespaceForPrefix(prefix, namespaceContext);
        }
        

        if (null == _namespaceURI)
        {
          throw new RuntimeException(XMLMessages.createXMLMessage("ER_PREFIX_MUST_RESOLVE", new Object[] { prefix }));
        }
      }
    }
    








    _localName = (indexOfNSSep < 0 ? qname : qname.substring(indexOfNSSep + 1));
    

    if (validate)
    {
      if ((_localName == null) || (!XML11Char.isXML11ValidNCName(_localName)))
      {
        throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
      }
    }
    

    m_hashCode = toString().hashCode();
  }
  









  public QName(String qname, PrefixResolver resolver)
  {
    this(qname, resolver, false);
  }
  











  public QName(String qname, PrefixResolver resolver, boolean validate)
  {
    String prefix = null;
    _namespaceURI = null;
    
    int indexOfNSSep = qname.indexOf(':');
    
    if (indexOfNSSep > 0)
    {
      prefix = qname.substring(0, indexOfNSSep);
      
      if (prefix.equals("xml"))
      {
        _namespaceURI = "http://www.w3.org/XML/1998/namespace";
      }
      else
      {
        _namespaceURI = resolver.getNamespaceForPrefix(prefix);
      }
      
      if (null == _namespaceURI)
      {
        throw new RuntimeException(XMLMessages.createXMLMessage("ER_PREFIX_MUST_RESOLVE", new Object[] { prefix }));
      }
      


      _localName = qname.substring(indexOfNSSep + 1);
    } else {
      if (indexOfNSSep == 0)
      {
        throw new RuntimeException(XMLMessages.createXMLMessage("ER_NAME_CANT_START_WITH_COLON", null));
      }
      




      _localName = qname;
    }
    
    if (validate)
    {
      if ((_localName == null) || (!XML11Char.isXML11ValidNCName(_localName)))
      {
        throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_ARG_LOCALNAME_INVALID", null));
      }
    }
    


    m_hashCode = toString().hashCode();
    _prefix = prefix;
  }
  






  public String getNamespaceURI()
  {
    return _namespaceURI;
  }
  






  public String getPrefix()
  {
    return _prefix;
  }
  





  public String getLocalName()
  {
    return _localName;
  }
  








  public String toString()
  {
    return _namespaceURI != null ? "{" + _namespaceURI + "}" + _localName : _prefix != null ? _prefix + ":" + _localName : _localName;
  }
  











  public String toNamespacedString()
  {
    return _namespaceURI != null ? "{" + _namespaceURI + "}" + _localName : _localName;
  }
  







  public String getNamespace()
  {
    return getNamespaceURI();
  }
  





  public String getLocalPart()
  {
    return getLocalName();
  }
  





  public int hashCode()
  {
    return m_hashCode;
  }
  











  public boolean equals(String ns, String localPart)
  {
    String thisnamespace = getNamespaceURI();
    
    return (getLocalName().equals(localPart)) && ((null != thisnamespace) && (null != ns) ? thisnamespace.equals(ns) : (null == thisnamespace) && (null == ns));
  }
  











  public boolean equals(Object object)
  {
    if (object == this) {
      return true;
    }
    if ((object instanceof QName)) {
      QName qname = (QName)object;
      String thisnamespace = getNamespaceURI();
      String thatnamespace = qname.getNamespaceURI();
      
      return (getLocalName().equals(qname.getLocalName())) && ((null != thisnamespace) && (null != thatnamespace) ? thisnamespace.equals(thatnamespace) : (null == thisnamespace) && (null == thatnamespace));
    }
    



    return false;
  }
  









  public static QName getQNameFromString(String name)
  {
    StringTokenizer tokenizer = new StringTokenizer(name, "{}", false);
    
    String s1 = tokenizer.nextToken();
    String s2 = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
    QName qname;
    QName qname; if (null == s2) {
      qname = new QName(null, s1);
    } else {
      qname = new QName(s1, s2);
    }
    return qname;
  }
  









  public static boolean isXMLNSDecl(String attRawName)
  {
    return (attRawName.startsWith("xmlns")) && ((attRawName.equals("xmlns")) || (attRawName.startsWith("xmlns:")));
  }
  











  public static String getPrefixFromXMLNSDecl(String attRawName)
  {
    int index = attRawName.indexOf(':');
    
    return index >= 0 ? attRawName.substring(index + 1) : "";
  }
  








  public static String getLocalPart(String qname)
  {
    int index = qname.indexOf(':');
    
    return index < 0 ? qname : qname.substring(index + 1);
  }
  








  public static String getPrefixPart(String qname)
  {
    int index = qname.indexOf(':');
    
    return index >= 0 ? qname.substring(0, index) : "";
  }
}

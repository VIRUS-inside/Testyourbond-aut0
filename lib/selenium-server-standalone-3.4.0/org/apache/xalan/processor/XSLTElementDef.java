package org.apache.xalan.processor;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.xml.utils.QName;











































public class XSLTElementDef
{
  static final int T_ELEMENT = 1;
  static final int T_PCDATA = 2;
  static final int T_ANY = 3;
  
  XSLTElementDef() {}
  
  XSLTElementDef(XSLTSchema schema, String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject)
  {
    build(namespace, name, nameAlias, elements, attributes, contentHandler, classObject);
    
    if ((null != namespace) && ((namespace.equals("http://www.w3.org/1999/XSL/Transform")) || (namespace.equals("http://xml.apache.org/xalan")) || (namespace.equals("http://xml.apache.org/xslt"))))
    {



      schema.addAvailableElement(new QName(namespace, name));
      if (null != nameAlias) {
        schema.addAvailableElement(new QName(namespace, nameAlias));
      }
    }
  }
  













  XSLTElementDef(XSLTSchema schema, String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject, boolean has_required)
  {
    m_has_required = has_required;
    build(namespace, name, nameAlias, elements, attributes, contentHandler, classObject);
    
    if ((null != namespace) && ((namespace.equals("http://www.w3.org/1999/XSL/Transform")) || (namespace.equals("http://xml.apache.org/xalan")) || (namespace.equals("http://xml.apache.org/xslt"))))
    {



      schema.addAvailableElement(new QName(namespace, name));
      if (null != nameAlias) {
        schema.addAvailableElement(new QName(namespace, nameAlias));
      }
    }
  }
  
















  XSLTElementDef(XSLTSchema schema, String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject, boolean has_required, boolean required)
  {
    this(schema, namespace, name, nameAlias, elements, attributes, contentHandler, classObject, has_required);
    

    m_required = required;
  }
  



















  XSLTElementDef(XSLTSchema schema, String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject, boolean has_required, boolean required, int order, boolean multiAllowed)
  {
    this(schema, namespace, name, nameAlias, elements, attributes, contentHandler, classObject, has_required, required);
    

    m_order = order;
    m_multiAllowed = multiAllowed;
  }
  




















  XSLTElementDef(XSLTSchema schema, String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject, boolean has_required, boolean required, boolean has_order, int order, boolean multiAllowed)
  {
    this(schema, namespace, name, nameAlias, elements, attributes, contentHandler, classObject, has_required, required);
    

    m_order = order;
    m_multiAllowed = multiAllowed;
    m_isOrdered = has_order;
  }
  

















  XSLTElementDef(XSLTSchema schema, String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject, boolean has_order, int order, boolean multiAllowed)
  {
    this(schema, namespace, name, nameAlias, elements, attributes, contentHandler, classObject, order, multiAllowed);
    


    m_isOrdered = has_order;
  }
  
















  XSLTElementDef(XSLTSchema schema, String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject, int order, boolean multiAllowed)
  {
    this(schema, namespace, name, nameAlias, elements, attributes, contentHandler, classObject);
    
    m_order = order;
    m_multiAllowed = multiAllowed;
  }
  









  XSLTElementDef(Class classObject, XSLTElementProcessor contentHandler, int type)
  {
    m_classObject = classObject;
    m_type = type;
    
    setElementProcessor(contentHandler);
  }
  














  void build(String namespace, String name, String nameAlias, XSLTElementDef[] elements, XSLTAttributeDef[] attributes, XSLTElementProcessor contentHandler, Class classObject)
  {
    m_namespace = namespace;
    m_name = name;
    m_nameAlias = nameAlias;
    m_elements = elements;
    m_attributes = attributes;
    
    setElementProcessor(contentHandler);
    
    m_classObject = classObject;
    
    if ((hasRequired()) && (m_elements != null))
    {
      int n = m_elements.length;
      for (int i = 0; i < n; i++)
      {
        XSLTElementDef def = m_elements[i];
        
        if ((def != null) && (def.getRequired()))
        {
          if (m_requiredFound == null)
            m_requiredFound = new Hashtable();
          m_requiredFound.put(def.getName(), "xsl:" + def.getName());
        }
      }
    }
  }
  










  private static boolean equalsMayBeNull(Object obj1, Object obj2)
  {
    return (obj2 == obj1) || ((null != obj1) && (null != obj2) && (obj2.equals(obj1)));
  }
  

















  private static boolean equalsMayBeNullOrZeroLen(String s1, String s2)
  {
    int len1 = s1 == null ? 0 : s1.length();
    int len2 = s2 == null ? 0 : s2.length();
    
    return len1 == 0 ? true : len1 != len2 ? false : s1.equals(s2);
  }
  








  private int m_type = 1;
  private String m_namespace;
  private String m_name;
  private String m_nameAlias;
  private XSLTElementDef[] m_elements;
  private XSLTAttributeDef[] m_attributes;
  private XSLTElementProcessor m_elementProcessor;
  private Class m_classObject;
  
  int getType() { return m_type; }
  






  void setType(int t)
  {
    m_type = t;
  }
  










  String getNamespace()
  {
    return m_namespace;
  }
  










  String getName()
  {
    return m_name;
  }
  










  String getNameAlias()
  {
    return m_nameAlias;
  }
  











  public XSLTElementDef[] getElements()
  {
    return m_elements;
  }
  





  void setElements(XSLTElementDef[] defs)
  {
    m_elements = defs;
  }
  










  private boolean QNameEquals(String uri, String localName)
  {
    return (equalsMayBeNullOrZeroLen(m_namespace, uri)) && ((equalsMayBeNullOrZeroLen(m_name, localName)) || (equalsMayBeNullOrZeroLen(m_nameAlias, localName)));
  }
  












  XSLTElementProcessor getProcessorFor(String uri, String localName)
  {
    XSLTElementProcessor elemDef = null;
    
    if (null == m_elements) {
      return null;
    }
    int n = m_elements.length;
    int order = -1;
    boolean multiAllowed = true;
    for (int i = 0; i < n; i++)
    {
      XSLTElementDef def = m_elements[i];
      



      if (m_name.equals("*"))
      {


        if (!equalsMayBeNullOrZeroLen(uri, "http://www.w3.org/1999/XSL/Transform"))
        {
          elemDef = m_elementProcessor;
          order = def.getOrder();
          multiAllowed = def.getMultiAllowed();
        }
      }
      else if (def.QNameEquals(uri, localName))
      {
        if (def.getRequired())
          setRequiredFound(def.getName(), true);
        order = def.getOrder();
        multiAllowed = def.getMultiAllowed();
        elemDef = m_elementProcessor;
        break;
      }
    }
    
    if ((elemDef != null) && (isOrdered()))
    {
      int lastOrder = getLastOrder();
      if (order > lastOrder) {
        setLastOrder(order);
      } else { if ((order == lastOrder) && (!multiAllowed))
        {
          return null;
        }
        if ((order < lastOrder) && (order > 0))
        {
          return null;
        }
      }
    }
    return elemDef;
  }
  












  XSLTElementProcessor getProcessorForUnknown(String uri, String localName)
  {
    if (null == m_elements) {
      return null;
    }
    int n = m_elements.length;
    
    for (int i = 0; i < n; i++)
    {
      XSLTElementDef def = m_elements[i];
      
      if ((m_name.equals("unknown")) && (uri.length() > 0))
      {
        return m_elementProcessor;
      }
    }
    
    return null;
  }
  










  XSLTAttributeDef[] getAttributes()
  {
    return m_attributes;
  }
  










  XSLTAttributeDef getAttributeDef(String uri, String localName)
  {
    XSLTAttributeDef defaultDef = null;
    XSLTAttributeDef[] attrDefs = getAttributes();
    int nAttrDefs = attrDefs.length;
    
    for (int k = 0; k < nAttrDefs; k++)
    {
      XSLTAttributeDef attrDef = attrDefs[k];
      String uriDef = attrDef.getNamespace();
      String nameDef = attrDef.getName();
      
      if ((nameDef.equals("*")) && ((equalsMayBeNullOrZeroLen(uri, uriDef)) || ((uriDef != null) && (uriDef.equals("*")) && (uri != null) && (uri.length() > 0))))
      {

        return attrDef;
      }
      if ((nameDef.equals("*")) && (uriDef == null))
      {



        defaultDef = attrDef;
      }
      else if ((equalsMayBeNullOrZeroLen(uri, uriDef)) && (localName.equals(nameDef)))
      {

        return attrDef;
      }
    }
    
    if (null == defaultDef)
    {
      if ((uri.length() > 0) && (!equalsMayBeNullOrZeroLen(uri, "http://www.w3.org/1999/XSL/Transform")))
      {
        return XSLTAttributeDef.m_foreignAttr;
      }
    }
    
    return defaultDef;
  }
  











  public XSLTElementProcessor getElementProcessor()
  {
    return m_elementProcessor;
  }
  







  public void setElementProcessor(XSLTElementProcessor handler)
  {
    if (handler != null)
    {
      m_elementProcessor = handler;
      
      m_elementProcessor.setElemDef(this);
    }
  }
  












  Class getClassObject()
  {
    return m_classObject;
  }
  



  private boolean m_has_required = false;
  





  boolean hasRequired()
  {
    return m_has_required;
  }
  



  private boolean m_required = false;
  

  Hashtable m_requiredFound;
  


  boolean getRequired()
  {
    return m_required;
  }
  






  void setRequiredFound(String elem, boolean found)
  {
    if (m_requiredFound.get(elem) != null) {
      m_requiredFound.remove(elem);
    }
  }
  




  boolean getRequiredFound()
  {
    if (m_requiredFound == null)
      return true;
    return m_requiredFound.isEmpty();
  }
  





  String getRequiredElem()
  {
    if (m_requiredFound == null)
      return null;
    Enumeration elems = m_requiredFound.elements();
    String s = "";
    boolean first = true;
    while (elems.hasMoreElements())
    {
      if (first) {
        first = false;
      } else
        s = s + ", ";
      s = s + (String)elems.nextElement();
    }
    return s;
  }
  
  boolean m_isOrdered = false;
  

























  boolean isOrdered()
  {
    return m_isOrdered;
  }
  



  private int m_order = -1;
  





  int getOrder()
  {
    return m_order;
  }
  




  private int m_lastOrder = -1;
  





  int getLastOrder()
  {
    return m_lastOrder;
  }
  





  void setLastOrder(int order)
  {
    m_lastOrder = order;
  }
  



  private boolean m_multiAllowed = true;
  





  boolean getMultiAllowed()
  {
    return m_multiAllowed;
  }
}

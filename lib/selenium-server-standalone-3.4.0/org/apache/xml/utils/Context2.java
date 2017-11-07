package org.apache.xml.utils;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


























































































































































































































































































































































































































































final class Context2
{
  private static final Enumeration EMPTY_ENUMERATION = new Vector().elements();
  

  Hashtable prefixTable;
  
  Hashtable uriTable;
  
  Hashtable elementNameTable;
  
  Hashtable attributeNameTable;
  
  String defaultNS = null;
  




  private Vector declarations = null;
  private boolean tablesDirty = false;
  private Context2 parent = null;
  private Context2 child = null;
  



  Context2(Context2 parent)
  {
    if (parent == null)
    {
      prefixTable = new Hashtable();
      uriTable = new Hashtable();
      elementNameTable = null;
      attributeNameTable = null;
    }
    else {
      setParent(parent);
    }
  }
  




  Context2 getChild()
  {
    return child;
  }
  




  Context2 getParent()
  {
    return parent;
  }
  







  void setParent(Context2 parent)
  {
    this.parent = parent;
    child = this;
    declarations = null;
    prefixTable = prefixTable;
    uriTable = uriTable;
    elementNameTable = elementNameTable;
    attributeNameTable = attributeNameTable;
    defaultNS = defaultNS;
    tablesDirty = false;
  }
  









  void declarePrefix(String prefix, String uri)
  {
    if (!tablesDirty) {
      copyTables();
    }
    if (declarations == null) {
      declarations = new Vector();
    }
    
    prefix = prefix.intern();
    uri = uri.intern();
    if ("".equals(prefix)) {
      if ("".equals(uri)) {
        defaultNS = null;
      } else {
        defaultNS = uri;
      }
    } else {
      prefixTable.put(prefix, uri);
      uriTable.put(uri, prefix);
    }
    declarations.addElement(prefix);
  }
  





  String[] processName(String qName, boolean isAttribute)
  {
    Hashtable table;
    




    Hashtable table;
    



    if (isAttribute) {
      if (elementNameTable == null)
        elementNameTable = new Hashtable();
      table = elementNameTable;
    } else {
      if (attributeNameTable == null)
        attributeNameTable = new Hashtable();
      table = attributeNameTable;
    }
    



    String[] name = (String[])table.get(qName);
    if (name != null) {
      return name;
    }
    


    name = new String[3];
    int index = qName.indexOf(':');
    


    if (index == -1) {
      if ((isAttribute) || (defaultNS == null)) {
        name[0] = "";
      } else {
        name[0] = defaultNS;
      }
      name[1] = qName.intern();
      name[2] = name[1];

    }
    else
    {
      String prefix = qName.substring(0, index);
      String local = qName.substring(index + 1);
      String uri;
      String uri; if ("".equals(prefix)) {
        uri = defaultNS;
      } else {
        uri = (String)prefixTable.get(prefix);
      }
      if (uri == null) {
        return null;
      }
      name[0] = uri;
      name[1] = local.intern();
      name[2] = qName.intern();
    }
    

    table.put(name[2], name);
    tablesDirty = true;
    return name;
  }
  









  String getURI(String prefix)
  {
    if ("".equals(prefix))
      return defaultNS;
    if (prefixTable == null) {
      return null;
    }
    return (String)prefixTable.get(prefix);
  }
  












  String getPrefix(String uri)
  {
    if (uriTable == null) {
      return null;
    }
    return (String)uriTable.get(uri);
  }
  








  Enumeration getDeclaredPrefixes()
  {
    if (declarations == null) {
      return EMPTY_ENUMERATION;
    }
    return declarations.elements();
  }
  











  Enumeration getPrefixes()
  {
    if (prefixTable == null) {
      return EMPTY_ENUMERATION;
    }
    return prefixTable.keys();
  }
  


















  private void copyTables()
  {
    prefixTable = ((Hashtable)prefixTable.clone());
    uriTable = ((Hashtable)uriTable.clone());
    





    if (elementNameTable != null)
      elementNameTable = new Hashtable();
    if (attributeNameTable != null)
      attributeNameTable = new Hashtable();
    tablesDirty = true;
  }
}

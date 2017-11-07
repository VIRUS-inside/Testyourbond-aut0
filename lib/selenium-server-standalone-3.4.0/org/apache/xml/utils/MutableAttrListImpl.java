package org.apache.xml.utils;

import java.io.Serializable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;








































public class MutableAttrListImpl
  extends AttributesImpl
  implements Serializable
{
  static final long serialVersionUID = 6289452013442934470L;
  
  public MutableAttrListImpl() {}
  
  public MutableAttrListImpl(Attributes atts)
  {
    super(atts);
  }
  



















  public void addAttribute(String uri, String localName, String qName, String type, String value)
  {
    if (null == uri) {
      uri = "";
    }
    

    int index = getIndex(qName);
    



    if (index >= 0) {
      setAttribute(index, uri, localName, qName, type, value);
    } else {
      super.addAttribute(uri, localName, qName, type, value);
    }
  }
  





  public void addAttributes(Attributes atts)
  {
    int nAtts = atts.getLength();
    
    for (int i = 0; i < nAtts; i++)
    {
      String uri = atts.getURI(i);
      
      if (null == uri) {
        uri = "";
      }
      String localName = atts.getLocalName(i);
      String qname = atts.getQName(i);
      int index = getIndex(uri, localName);
      
      if (index >= 0) {
        setAttribute(index, uri, localName, qname, atts.getType(i), atts.getValue(i));
      }
      else {
        addAttribute(uri, localName, qname, atts.getType(i), atts.getValue(i));
      }
    }
  }
  







  public boolean contains(String name)
  {
    return getValue(name) != null;
  }
}

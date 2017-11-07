package org.apache.xalan.xsltc.runtime;

import java.util.Vector;
import org.xml.sax.Attributes;

































public class AttributeList
  implements Attributes
{
  private static final String EMPTYSTRING = "";
  private static final String CDATASTRING = "CDATA";
  private Hashtable _attributes;
  private Vector _names;
  private Vector _qnames;
  private Vector _values;
  private Vector _uris;
  private int _length;
  
  public AttributeList()
  {
    _length = 0;
  }
  


  public AttributeList(Attributes attributes)
  {
    this();
    if (attributes != null) {
      int count = attributes.getLength();
      for (int i = 0; i < count; i++) {
        add(attributes.getQName(i), attributes.getValue(i));
      }
    }
  }
  





  private void alloc()
  {
    _attributes = new Hashtable();
    _names = new Vector();
    _values = new Vector();
    _qnames = new Vector();
    _uris = new Vector();
  }
  


  public int getLength()
  {
    return _length;
  }
  


  public String getURI(int index)
  {
    if (index < _length) {
      return (String)_uris.elementAt(index);
    }
    return null;
  }
  


  public String getLocalName(int index)
  {
    if (index < _length) {
      return (String)_names.elementAt(index);
    }
    return null;
  }
  


  public String getQName(int pos)
  {
    if (pos < _length) {
      return (String)_qnames.elementAt(pos);
    }
    return null;
  }
  


  public String getType(int index)
  {
    return "CDATA";
  }
  


  public int getIndex(String namespaceURI, String localPart)
  {
    return -1;
  }
  


  public int getIndex(String qname)
  {
    return -1;
  }
  


  public String getType(String uri, String localName)
  {
    return "CDATA";
  }
  


  public String getType(String qname)
  {
    return "CDATA";
  }
  


  public String getValue(int pos)
  {
    if (pos < _length) {
      return (String)_values.elementAt(pos);
    }
    return null;
  }
  


  public String getValue(String qname)
  {
    if (_attributes != null) {
      Integer obj = (Integer)_attributes.get(qname);
      if (obj == null) return null;
      return getValue(obj.intValue());
    }
    
    return null;
  }
  


  public String getValue(String uri, String localName)
  {
    return getValue(uri + ':' + localName);
  }
  



  public void add(String qname, String value)
  {
    if (_attributes == null) {
      alloc();
    }
    
    Integer obj = (Integer)_attributes.get(qname);
    if (obj == null) {
      _attributes.put(qname, obj = new Integer(_length++));
      _qnames.addElement(qname);
      _values.addElement(value);
      int col = qname.lastIndexOf(':');
      if (col > -1) {
        _uris.addElement(qname.substring(0, col));
        _names.addElement(qname.substring(col + 1));
      }
      else {
        _uris.addElement("");
        _names.addElement(qname);
      }
    }
    else {
      int index = obj.intValue();
      _values.set(index, value);
    }
  }
  


  public void clear()
  {
    _length = 0;
    if (_attributes != null) {
      _attributes.clear();
      _names.removeAllElements();
      _values.removeAllElements();
      _qnames.removeAllElements();
      _uris.removeAllElements();
    }
  }
}

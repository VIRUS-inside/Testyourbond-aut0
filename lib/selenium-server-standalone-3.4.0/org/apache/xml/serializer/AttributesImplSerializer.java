package org.apache.xml.serializer;

import java.util.Hashtable;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;








































public final class AttributesImplSerializer
  extends AttributesImpl
{
  private final Hashtable m_indexFromQName = new Hashtable();
  
  private final StringBuffer m_buff = new StringBuffer();
  



  private static final int MAX = 12;
  



  private static final int MAXMinus1 = 11;
  




  public AttributesImplSerializer() {}
  




  public final int getIndex(String qname)
  {
    if (super.getLength() < 12)
    {


      int index = super.getIndex(qname);
      return index;
    }
    

    Integer i = (Integer)m_indexFromQName.get(qname);
    int index; int index; if (i == null) {
      index = -1;
    } else
      index = i.intValue();
    return index;
  }
  
















  public final void addAttribute(String uri, String local, String qname, String type, String val)
  {
    int index = super.getLength();
    super.addAttribute(uri, local, qname, type, val);
    


    if (index < 11)
    {
      return;
    }
    if (index == 11)
    {
      switchOverToHash(12);

    }
    else
    {

      Integer i = new Integer(index);
      m_indexFromQName.put(qname, i);
      

      m_buff.setLength(0);
      m_buff.append('{').append(uri).append('}').append(local);
      String key = m_buff.toString();
      m_indexFromQName.put(key, i);
    }
  }
  









  private void switchOverToHash(int numAtts)
  {
    for (int index = 0; index < numAtts; index++)
    {
      String qName = super.getQName(index);
      Integer i = new Integer(index);
      m_indexFromQName.put(qName, i);
      

      String uri = super.getURI(index);
      String local = super.getLocalName(index);
      m_buff.setLength(0);
      m_buff.append('{').append(uri).append('}').append(local);
      String key = m_buff.toString();
      m_indexFromQName.put(key, i);
    }
  }
  






  public final void clear()
  {
    int len = super.getLength();
    super.clear();
    if (12 <= len)
    {


      m_indexFromQName.clear();
    }
  }
  










  public final void setAttributes(Attributes atts)
  {
    super.setAttributes(atts);
    



    int numAtts = atts.getLength();
    if (12 <= numAtts) {
      switchOverToHash(numAtts);
    }
  }
  









  public final int getIndex(String uri, String localName)
  {
    if (super.getLength() < 12)
    {


      int index = super.getIndex(uri, localName);
      return index;
    }
    


    m_buff.setLength(0);
    m_buff.append('{').append(uri).append('}').append(localName);
    String key = m_buff.toString();
    Integer i = (Integer)m_indexFromQName.get(key);
    int index; int index; if (i == null) {
      index = -1;
    } else
      index = i.intValue();
    return index;
  }
}

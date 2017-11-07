package org.apache.xml.dtm.ref;

import java.util.Hashtable;
import java.util.Vector;












































public class CustomStringPool
  extends DTMStringPool
{
  final Hashtable m_stringToInt = new Hashtable();
  


  public static final int NULL = -1;
  



  public CustomStringPool() {}
  


  public void removeAllElements()
  {
    m_intToString.removeAllElements();
    if (m_stringToInt != null) {
      m_stringToInt.clear();
    }
  }
  



  public String indexToString(int i)
    throws ArrayIndexOutOfBoundsException
  {
    return (String)m_intToString.elementAt(i);
  }
  

  public int stringToIndex(String s)
  {
    if (s == null) return -1;
    Integer iobj = (Integer)m_stringToInt.get(s);
    if (iobj == null) {
      m_intToString.addElement(s);
      iobj = new Integer(m_intToString.size());
      m_stringToInt.put(s, iobj);
    }
    return iobj.intValue();
  }
}

package org.apache.xpath.objects;

import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.FastStringBuffer;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;


























public class XStringForChars
  extends XString
{
  static final long serialVersionUID = -2235248887220850467L;
  int m_start;
  int m_length;
  protected String m_strCache = null;
  







  public XStringForChars(char[] val, int start, int length)
  {
    super(val);
    m_start = start;
    m_length = length;
    if (null == val) {
      throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_FASTSTRINGBUFFER_CANNOT_BE_NULL", null));
    }
  }
  






  private XStringForChars(String val)
  {
    super(val);
    throw new IllegalArgumentException(XSLMessages.createXPATHMessage("ER_XSTRINGFORCHARS_CANNOT_TAKE_STRING", null));
  }
  






  public FastStringBuffer fsb()
  {
    throw new RuntimeException(XSLMessages.createXPATHMessage("ER_FSB_NOT_SUPPORTED_XSTRINGFORCHARS", null));
  }
  





  public void appendToFsb(FastStringBuffer fsb)
  {
    fsb.append((char[])m_obj, m_start, m_length);
  }
  






  public boolean hasString()
  {
    return null != m_strCache;
  }
  






  public String str()
  {
    if (null == m_strCache) {
      m_strCache = new String((char[])m_obj, m_start, m_length);
    }
    return m_strCache;
  }
  







  public Object object()
  {
    return str();
  }
  











  public void dispatchCharactersEvents(ContentHandler ch)
    throws SAXException
  {
    ch.characters((char[])m_obj, m_start, m_length);
  }
  









  public void dispatchAsComment(LexicalHandler lh)
    throws SAXException
  {
    lh.comment((char[])m_obj, m_start, m_length);
  }
  






  public int length()
  {
    return m_length;
  }
  













  public char charAt(int index)
  {
    return ((char[])(char[])m_obj)[(index + m_start)];
  }
  





















  public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin)
  {
    System.arraycopy((char[])m_obj, m_start + srcBegin, dst, dstBegin, srcEnd);
  }
}

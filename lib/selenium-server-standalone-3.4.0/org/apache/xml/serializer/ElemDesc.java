package org.apache.xml.serializer;

import org.apache.xml.serializer.utils.StringToIntTable;



































public final class ElemDesc
{
  private int m_flags;
  private StringToIntTable m_attrs = null;
  


  static final int EMPTY = 2;
  


  private static final int FLOW = 4;
  


  static final int BLOCK = 8;
  


  static final int BLOCKFORM = 16;
  

  static final int BLOCKFORMFIELDSET = 32;
  

  private static final int CDATA = 64;
  

  private static final int PCDATA = 128;
  

  static final int RAW = 256;
  

  private static final int INLINE = 512;
  

  private static final int INLINEA = 1024;
  

  static final int INLINELABEL = 2048;
  

  static final int FONTSTYLE = 4096;
  

  static final int PHRASE = 8192;
  

  static final int FORMCTRL = 16384;
  

  static final int SPECIAL = 32768;
  

  static final int ASPECIAL = 65536;
  

  static final int HEADMISC = 131072;
  

  static final int HEAD = 262144;
  

  static final int LIST = 524288;
  

  static final int PREFORMATTED = 1048576;
  

  static final int WHITESPACESENSITIVE = 2097152;
  

  static final int HEADELEM = 4194304;
  

  static final int HTMLELEM = 8388608;
  

  public static final int ATTRURL = 2;
  

  public static final int ATTREMPTY = 4;
  


  ElemDesc(int flags)
  {
    m_flags = flags;
  }
  










  private boolean is(int flags)
  {
    return (m_flags & flags) != 0;
  }
  
  int getFlags() {
    return m_flags;
  }
  








  void setAttr(String name, int flags)
  {
    if (null == m_attrs) {
      m_attrs = new StringToIntTable();
    }
    m_attrs.put(name, flags);
  }
  








  public boolean isAttrFlagSet(String name, int flags)
  {
    return (m_attrs.getIgnoreCase(name) & flags) != 0;
  }
}

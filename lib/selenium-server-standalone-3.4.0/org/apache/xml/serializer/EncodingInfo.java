package org.apache.xml.serializer;




















public final class EncodingInfo
{
  private final char m_highCharInContiguousGroup;
  


















  final String name;
  


















  final String javaName;
  

















  private InEncoding m_encoding;
  


















  public boolean isInEncoding(char ch)
  {
    if (m_encoding == null) {
      m_encoding = new EncodingImpl(null);
    }
    




    return m_encoding.isInEncoding(ch);
  }
  








  public boolean isInEncoding(char high, char low)
  {
    if (m_encoding == null) {
      m_encoding = new EncodingImpl(null);
    }
    




    return m_encoding.isInEncoding(high, low);
  }
  













  public EncodingInfo(String name, String javaName, char highChar)
  {
    this.name = name;
    this.javaName = javaName;
    m_highCharInContiguousGroup = highChar;
  }
  


  private class EncodingImpl
    implements EncodingInfo.InEncoding
  {
    private final String m_encoding;
    

    private final int m_first;
    

    private final int m_explFirst;
    
    private final int m_explLast;
    
    private final int m_last;
    
    private EncodingInfo.InEncoding m_before;
    
    private EncodingInfo.InEncoding m_after;
    
    private static final int RANGE = 128;
    

    EncodingImpl(EncodingInfo.1 x1)
    {
      this();
    }
    

    public boolean isInEncoding(char ch1)
    {
      int codePoint = Encodings.toCodePoint(ch1);
      boolean ret; boolean ret; if (codePoint < m_explFirst)
      {



        if (m_before == null) {
          m_before = new EncodingImpl(EncodingInfo.this, m_encoding, m_first, m_explFirst - 1, codePoint);
        }
        



        ret = m_before.isInEncoding(ch1); } else { boolean ret;
        if (m_explLast < codePoint)
        {



          if (m_after == null) {
            m_after = new EncodingImpl(EncodingInfo.this, m_encoding, m_explLast + 1, m_last, codePoint);
          }
          



          ret = m_after.isInEncoding(ch1);
        }
        else {
          int idx = codePoint - m_explFirst;
          
          boolean ret;
          if (m_alreadyKnown[idx] != 0) {
            ret = m_isInEncoding[idx];
          }
          else
          {
            ret = EncodingInfo.inEncoding(ch1, m_encoding);
            m_alreadyKnown[idx] = true;
            m_isInEncoding[idx] = ret;
          }
        } }
      return ret;
    }
    
    public boolean isInEncoding(char high, char low)
    {
      int codePoint = Encodings.toCodePoint(high, low);
      boolean ret; boolean ret; if (codePoint < m_explFirst)
      {



        if (m_before == null) {
          m_before = new EncodingImpl(EncodingInfo.this, m_encoding, m_first, m_explFirst - 1, codePoint);
        }
        



        ret = m_before.isInEncoding(high, low); } else { boolean ret;
        if (m_explLast < codePoint)
        {



          if (m_after == null) {
            m_after = new EncodingImpl(EncodingInfo.this, m_encoding, m_explLast + 1, m_last, codePoint);
          }
          



          ret = m_after.isInEncoding(high, low);
        }
        else {
          int idx = codePoint - m_explFirst;
          
          boolean ret;
          if (m_alreadyKnown[idx] != 0) {
            ret = m_isInEncoding[idx];
          }
          else
          {
            ret = EncodingInfo.inEncoding(high, low, m_encoding);
            m_alreadyKnown[idx] = true;
            m_isInEncoding[idx] = ret;
          }
        } }
      return ret;
    }
    

















































    private final boolean[] m_alreadyKnown = new boolean[''];
    



    private final boolean[] m_isInEncoding = new boolean[''];
    

    private EncodingImpl()
    {
      this(javaName, 0, Integer.MAX_VALUE, 0);
    }
    

    private EncodingImpl(String encoding, int first, int last, int codePoint)
    {
      m_first = first;
      m_last = last;
      


      m_explFirst = codePoint;
      m_explLast = (codePoint + 127);
      
      m_encoding = encoding;
      
      if (javaName != null)
      {
        int unicode;
        if ((0 <= m_explFirst) && (m_explFirst <= 127))
        {

          if (("UTF8".equals(javaName)) || ("UTF-16".equals(javaName)) || ("ASCII".equals(javaName)) || ("US-ASCII".equals(javaName)) || ("Unicode".equals(javaName)) || ("UNICODE".equals(javaName)) || (javaName.startsWith("ISO8859")))
          {

















            for (unicode = 1; unicode < 127; unicode++) {
              int idx = unicode - m_explFirst;
              if ((0 <= idx) && (idx < 128)) {
                m_alreadyKnown[idx] = true;
                m_isInEncoding[idx] = true;
              }
            }
          }
        }
        










        if (javaName == null) {
          for (int idx = 0; idx < m_alreadyKnown.length; idx++) {
            m_alreadyKnown[idx] = true;
            m_isInEncoding[idx] = true;
          }
        }
      }
    }
  }
  






  private static boolean inEncoding(char ch, String encoding)
  {
    boolean isInEncoding;
    





    try
    {
      char[] cArray = new char[1];
      cArray[0] = ch;
      
      String s = new String(cArray);
      

      byte[] bArray = s.getBytes(encoding);
      isInEncoding = inEncoding(ch, bArray);
    }
    catch (Exception e) {
      isInEncoding = false;
      



      if (encoding == null)
        isInEncoding = true;
    }
    return isInEncoding;
  }
  







  private static boolean inEncoding(char high, char low, String encoding)
  {
    boolean isInEncoding;
    





    try
    {
      char[] cArray = new char[2];
      cArray[0] = high;
      cArray[1] = low;
      
      String s = new String(cArray);
      

      byte[] bArray = s.getBytes(encoding);
      isInEncoding = inEncoding(high, bArray);
    } catch (Exception e) {
      isInEncoding = false;
    }
    
    return isInEncoding;
  }
  





  private static boolean inEncoding(char ch, byte[] data)
  {
    boolean isInEncoding;
    



    boolean isInEncoding;
    



    if ((data == null) || (data.length == 0)) {
      isInEncoding = false;
    } else {
      boolean isInEncoding;
      if (data[0] == 0) {
        isInEncoding = false; } else { boolean isInEncoding;
        if ((data[0] == 63) && (ch != '?')) {
          isInEncoding = false;








        }
        else
        {








          isInEncoding = true; }
      }
    }
    return isInEncoding;
  }
  
























  public final char getHighChar()
  {
    return m_highCharInContiguousGroup;
  }
  
  private static abstract interface InEncoding
  {
    public abstract boolean isInEncoding(char paramChar);
    
    public abstract boolean isInEncoding(char paramChar1, char paramChar2);
  }
}

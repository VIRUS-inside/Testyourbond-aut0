package org.apache.xml.utils;





























public class StringBufferPool
{
  private static ObjectPool m_stringBufPool = new ObjectPool(FastStringBuffer.class);
  



  public StringBufferPool() {}
  


  public static synchronized FastStringBuffer get()
  {
    return (FastStringBuffer)m_stringBufPool.getInstance();
  }
  








  public static synchronized void free(FastStringBuffer sb)
  {
    sb.setLength(0);
    m_stringBufPool.freeInstance(sb);
  }
}

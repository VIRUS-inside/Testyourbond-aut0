package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;































































final class SerializerTraceWriter
  extends Writer
  implements WriterChain
{
  private final Writer m_writer;
  private final SerializerTrace m_tracer;
  private int buf_length;
  private byte[] buf;
  private int count;
  
  private void setBufferSize(int size)
  {
    buf = new byte[size + 3];
    buf_length = size;
    count = 0;
  }
  











  public SerializerTraceWriter(Writer out, SerializerTrace tracer)
  {
    m_writer = out;
    m_tracer = tracer;
    setBufferSize(1024);
  }
  









  private void flushBuffer()
    throws IOException
  {
    if (count > 0)
    {
      char[] chars = new char[count];
      for (int i = 0; i < count; i++) {
        chars[i] = ((char)buf[i]);
      }
      if (m_tracer != null) {
        m_tracer.fireGenerateEvent(12, chars, 0, chars.length);
      }
      



      count = 0;
    }
  }
  




  public void flush()
    throws IOException
  {
    if (m_writer != null) {
      m_writer.flush();
    }
    
    flushBuffer();
  }
  




  public void close()
    throws IOException
  {
    if (m_writer != null) {
      m_writer.close();
    }
    
    flushBuffer();
  }
  












  public void write(int c)
    throws IOException
  {
    if (m_writer != null) {
      m_writer.write(c);
    }
    




    if (count >= buf_length) {
      flushBuffer();
    }
    if (c < 128)
    {
      buf[(count++)] = ((byte)c);
    }
    else if (c < 2048)
    {
      buf[(count++)] = ((byte)(192 + (c >> 6)));
      buf[(count++)] = ((byte)(128 + (c & 0x3F)));
    }
    else
    {
      buf[(count++)] = ((byte)(224 + (c >> 12)));
      buf[(count++)] = ((byte)(128 + (c >> 6 & 0x3F)));
      buf[(count++)] = ((byte)(128 + (c & 0x3F)));
    }
  }
  












  public void write(char[] chars, int start, int length)
    throws IOException
  {
    if (m_writer != null) {
      m_writer.write(chars, start, length);
    }
    
    int lengthx3 = (length << 1) + length;
    
    if (lengthx3 >= buf_length)
    {





      flushBuffer();
      setBufferSize(2 * lengthx3);
    }
    

    if (lengthx3 > buf_length - count)
    {
      flushBuffer();
    }
    
    int n = length + start;
    for (int i = start; i < n; i++)
    {
      char c = chars[i];
      
      if (c < '') {
        buf[(count++)] = ((byte)c);
      } else if (c < 'ࠀ')
      {
        buf[(count++)] = ((byte)(192 + (c >> '\006')));
        buf[(count++)] = ((byte)('' + (c & 0x3F)));
      }
      else
      {
        buf[(count++)] = ((byte)(224 + (c >> '\f')));
        buf[(count++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
        buf[(count++)] = ((byte)('' + (c & 0x3F)));
      }
    }
  }
  








  public void write(String s)
    throws IOException
  {
    if (m_writer != null) {
      m_writer.write(s);
    }
    
    int length = s.length();
    




    int lengthx3 = (length << 1) + length;
    
    if (lengthx3 >= buf_length)
    {





      flushBuffer();
      setBufferSize(2 * lengthx3);
    }
    
    if (lengthx3 > buf_length - count)
    {
      flushBuffer();
    }
    
    for (int i = 0; i < length; i++)
    {
      char c = s.charAt(i);
      
      if (c < '') {
        buf[(count++)] = ((byte)c);
      } else if (c < 'ࠀ')
      {
        buf[(count++)] = ((byte)(192 + (c >> '\006')));
        buf[(count++)] = ((byte)('' + (c & 0x3F)));
      }
      else
      {
        buf[(count++)] = ((byte)(224 + (c >> '\f')));
        buf[(count++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
        buf[(count++)] = ((byte)('' + (c & 0x3F)));
      }
    }
  }
  



  public Writer getWriter()
  {
    return m_writer;
  }
  




  public OutputStream getOutputStream()
  {
    OutputStream retval = null;
    if ((m_writer instanceof WriterChain))
      retval = ((WriterChain)m_writer).getOutputStream();
    return retval;
  }
}

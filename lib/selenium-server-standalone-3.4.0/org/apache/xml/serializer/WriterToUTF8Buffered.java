package org.apache.xml.serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
































































final class WriterToUTF8Buffered
  extends Writer
  implements WriterChain
{
  private static final int BYTES_MAX = 16384;
  private static final int CHARS_MAX = 5461;
  private final OutputStream m_os;
  private final byte[] m_outputBytes;
  private final char[] m_inputChars;
  private int count;
  
  public WriterToUTF8Buffered(OutputStream out)
  {
    m_os = out;
    

    m_outputBytes = new byte['䀃'];
    


    m_inputChars = new char['ᕗ'];
    count = 0;
  }
  









































  public void write(int c)
    throws IOException
  {
    if (count >= 16384) {
      flushBuffer();
    }
    if (c < 128)
    {
      m_outputBytes[(count++)] = ((byte)c);
    }
    else if (c < 2048)
    {
      m_outputBytes[(count++)] = ((byte)(192 + (c >> 6)));
      m_outputBytes[(count++)] = ((byte)(128 + (c & 0x3F)));
    }
    else if (c < 65536)
    {
      m_outputBytes[(count++)] = ((byte)(224 + (c >> 12)));
      m_outputBytes[(count++)] = ((byte)(128 + (c >> 6 & 0x3F)));
      m_outputBytes[(count++)] = ((byte)(128 + (c & 0x3F)));
    }
    else
    {
      m_outputBytes[(count++)] = ((byte)(240 + (c >> 18)));
      m_outputBytes[(count++)] = ((byte)(128 + (c >> 12 & 0x3F)));
      m_outputBytes[(count++)] = ((byte)(128 + (c >> 6 & 0x3F)));
      m_outputBytes[(count++)] = ((byte)(128 + (c & 0x3F)));
    }
  }
  


















  public void write(char[] chars, int start, int length)
    throws IOException
  {
    int lengthx3 = 3 * length;
    
    if (lengthx3 >= 16384 - count)
    {

      flushBuffer();
      
      if (lengthx3 > 16384)
      {







        int split = length / 5461;
        int chunks;
        int chunks; if (length % 5461 > 0) {
          chunks = split + 1;
        } else
          chunks = split;
        int end_chunk = start;
        for (int chunk = 1; chunk <= chunks; chunk++)
        {
          int start_chunk = end_chunk;
          end_chunk = start + (int)(length * chunk / chunks);
          



          char c = chars[(end_chunk - 1)];
          int ic = chars[(end_chunk - 1)];
          if ((c >= 55296) && (c <= 56319))
          {




            if (end_chunk < start + length)
            {

              end_chunk++;



            }
            else
            {



              end_chunk--;
            }
          }
          

          int len_chunk = end_chunk - start_chunk;
          write(chars, start_chunk, len_chunk);
        }
        return;
      }
    }
    


    int n = length + start;
    byte[] buf_loc = m_outputBytes;
    int count_loc = count;
    char c; for (int i = start; 
        






        (i < n) && ((c = chars[i]) < ''); i++) {
      buf_loc[(count_loc++)] = ((byte)c);
    }
    for (; i < n; i++)
    {

      char c = chars[i];
      
      if (c < '') {
        buf_loc[(count_loc++)] = ((byte)c);
      } else if (c < 'ࠀ')
      {
        buf_loc[(count_loc++)] = ((byte)(192 + (c >> '\006')));
        buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));







      }
      else if ((c >= 55296) && (c <= 56319))
      {

        char high = c;
        i++;
        char low = chars[i];
        
        buf_loc[(count_loc++)] = ((byte)(0xF0 | high + '@' >> 8 & 0xF0));
        buf_loc[(count_loc++)] = ((byte)(0x80 | high + '@' >> 2 & 0x3F));
        buf_loc[(count_loc++)] = ((byte)(0x80 | (low >> '\006' & 0xF) + (high << '\004' & 0x30)));
        buf_loc[(count_loc++)] = ((byte)(0x80 | low & 0x3F));
      }
      else
      {
        buf_loc[(count_loc++)] = ((byte)(224 + (c >> '\f')));
        buf_loc[(count_loc++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
        buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));
      }
    }
    
    count = count_loc;
  }
  











  public void write(String s)
    throws IOException
  {
    int length = s.length();
    int lengthx3 = 3 * length;
    
    if (lengthx3 >= 16384 - count)
    {

      flushBuffer();
      
      if (lengthx3 > 16384)
      {




        int start = 0;
        int split = length / 5461;
        int chunks;
        int chunks; if (length % 5461 > 0) {
          chunks = split + 1;
        } else
          chunks = split;
        int end_chunk = 0;
        for (int chunk = 1; chunk <= chunks; chunk++)
        {
          int start_chunk = end_chunk;
          end_chunk = 0 + (int)(length * chunk / chunks);
          s.getChars(start_chunk, end_chunk, m_inputChars, 0);
          int len_chunk = end_chunk - start_chunk;
          



          char c = m_inputChars[(len_chunk - 1)];
          if ((c >= 55296) && (c <= 56319))
          {


            end_chunk--;
            len_chunk--;
            if (chunk != chunks) {}
          }
          







          write(m_inputChars, 0, len_chunk);
        }
        return;
      }
    }
    

    s.getChars(0, length, m_inputChars, 0);
    char[] chars = m_inputChars;
    int n = length;
    byte[] buf_loc = m_outputBytes;
    int count_loc = count;
    char c; for (int i = 0; 
        






        (i < n) && ((c = chars[i]) < ''); i++) {
      buf_loc[(count_loc++)] = ((byte)c);
    }
    for (; i < n; i++)
    {

      char c = chars[i];
      
      if (c < '') {
        buf_loc[(count_loc++)] = ((byte)c);
      } else if (c < 'ࠀ')
      {
        buf_loc[(count_loc++)] = ((byte)(192 + (c >> '\006')));
        buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));







      }
      else if ((c >= 55296) && (c <= 56319))
      {

        char high = c;
        i++;
        char low = chars[i];
        
        buf_loc[(count_loc++)] = ((byte)(0xF0 | high + '@' >> 8 & 0xF0));
        buf_loc[(count_loc++)] = ((byte)(0x80 | high + '@' >> 2 & 0x3F));
        buf_loc[(count_loc++)] = ((byte)(0x80 | (low >> '\006' & 0xF) + (high << '\004' & 0x30)));
        buf_loc[(count_loc++)] = ((byte)(0x80 | low & 0x3F));
      }
      else
      {
        buf_loc[(count_loc++)] = ((byte)(224 + (c >> '\f')));
        buf_loc[(count_loc++)] = ((byte)(128 + (c >> '\006' & 0x3F)));
        buf_loc[(count_loc++)] = ((byte)('' + (c & 0x3F)));
      }
    }
    
    count = count_loc;
  }
  






  public void flushBuffer()
    throws IOException
  {
    if (count > 0)
    {
      m_os.write(m_outputBytes, 0, count);
      
      count = 0;
    }
  }
  










  public void flush()
    throws IOException
  {
    flushBuffer();
    m_os.flush();
  }
  








  public void close()
    throws IOException
  {
    flushBuffer();
    m_os.close();
  }
  






  public OutputStream getOutputStream()
  {
    return m_os;
  }
  


  public Writer getWriter()
  {
    return null;
  }
}

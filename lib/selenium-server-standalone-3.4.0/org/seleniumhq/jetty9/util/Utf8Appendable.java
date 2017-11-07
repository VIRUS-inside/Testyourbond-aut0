package org.seleniumhq.jetty9.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;












































public abstract class Utf8Appendable
{
  protected static final Logger LOG = Log.getLogger(Utf8Appendable.class);
  public static final char REPLACEMENT = '�';
  public static final byte[] REPLACEMENT_UTF8 = { -17, -65, -67 };
  
  private static final int UTF8_ACCEPT = 0;
  private static final int UTF8_REJECT = 12;
  protected final Appendable _appendable;
  protected int _state = 0;
  
  private static final byte[] BYTE_TABLE = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 10, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 3, 3, 11, 6, 6, 6, 5, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 };
  












  private static final byte[] TRANS_TABLE = { 0, 12, 24, 36, 60, 96, 84, 12, 12, 12, 48, 72, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 0, 12, 12, 12, 12, 12, 0, 12, 0, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 24, 12, 12, 12, 12, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 36, 12, 36, 12, 12, 12, 36, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12 };
  




  private int _codep;
  





  public Utf8Appendable(Appendable appendable)
  {
    _appendable = appendable;
  }
  
  public abstract int length();
  
  protected void reset()
  {
    _state = 0;
  }
  
  private void checkCharAppend()
    throws IOException
  {
    if (_state != 0)
    {
      _appendable.append(65533);
      int state = _state;
      _state = 0;
      throw new NotUtf8Exception("char appended in state " + state);
    }
  }
  
  public void append(char c)
  {
    try
    {
      checkCharAppend();
      _appendable.append(c);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void append(String s)
  {
    try
    {
      checkCharAppend();
      _appendable.append(s);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void append(String s, int offset, int length)
  {
    try
    {
      checkCharAppend();
      _appendable.append(s, offset, offset + length);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  

  public void append(byte b)
  {
    try
    {
      appendByte(b);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void append(ByteBuffer buf)
  {
    try
    {
      while (buf.remaining() > 0)
      {
        appendByte(buf.get());
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void append(byte[] b, int offset, int length)
  {
    try
    {
      int end = offset + length;
      for (int i = offset; i < end; i++) {
        appendByte(b[i]);
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  public boolean append(byte[] b, int offset, int length, int maxChars)
  {
    try
    {
      int end = offset + length;
      for (int i = offset; i < end; i++)
      {
        if (length() > maxChars)
          return false;
        appendByte(b[i]);
      }
      return true;
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  protected void appendByte(byte b)
    throws IOException
  {
    if ((b > 0) && (_state == 0))
    {
      _appendable.append((char)(b & 0xFF));
    }
    else
    {
      int i = b & 0xFF;
      int type = BYTE_TABLE[i];
      _codep = (_state == 0 ? 255 >> type & i : i & 0x3F | _codep << 6);
      int next = TRANS_TABLE[(_state + type)];
      
      switch (next)
      {
      case 0: 
        _state = next;
        if (_codep < 55296)
        {
          _appendable.append((char)_codep);
        }
        else
        {
          for (char c : Character.toChars(_codep))
            _appendable.append(c);
        }
        break;
      
      case 12: 
        String reason = "byte " + TypeUtil.toHexString(b) + " in state " + _state / 12;
        _codep = 0;
        _state = 0;
        _appendable.append(65533);
        throw new NotUtf8Exception(reason);
      
      default: 
        _state = next;
      }
      
    }
  }
  
  public boolean isUtf8SequenceComplete()
  {
    return _state == 0;
  }
  
  public static class NotUtf8Exception
    extends IllegalArgumentException
  {
    public NotUtf8Exception(String reason)
    {
      super();
    }
  }
  
  protected void checkState()
  {
    if (!isUtf8SequenceComplete())
    {
      _codep = 0;
      _state = 0;
      try
      {
        _appendable.append(65533);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
      throw new NotUtf8Exception("incomplete UTF8 sequence");
    }
  }
  
  public String toReplacedString()
  {
    if (!isUtf8SequenceComplete())
    {
      _codep = 0;
      _state = 0;
      try
      {
        _appendable.append(65533);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
      Throwable th = new NotUtf8Exception("incomplete UTF8 sequence");
      LOG.warn(th.toString(), new Object[0]);
      LOG.debug(th);
    }
    return _appendable.toString();
  }
}

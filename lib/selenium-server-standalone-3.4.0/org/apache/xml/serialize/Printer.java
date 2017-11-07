package org.apache.xml.serialize;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @deprecated
 */
public class Printer
{
  protected final OutputFormat _format;
  protected Writer _writer;
  protected StringWriter _dtdWriter;
  protected Writer _docWriter;
  protected IOException _exception;
  private static final int BufferSize = 4096;
  private final char[] _buffer = new char['က'];
  private int _pos = 0;
  
  public Printer(Writer paramWriter, OutputFormat paramOutputFormat)
  {
    _writer = paramWriter;
    _format = paramOutputFormat;
    _exception = null;
    _dtdWriter = null;
    _docWriter = null;
    _pos = 0;
  }
  
  public IOException getException()
  {
    return _exception;
  }
  
  public void enterDTD()
    throws IOException
  {
    if (_dtdWriter == null)
    {
      flushLine(false);
      _dtdWriter = new StringWriter();
      _docWriter = _writer;
      _writer = _dtdWriter;
    }
  }
  
  public String leaveDTD()
    throws IOException
  {
    if (_writer == _dtdWriter)
    {
      flushLine(false);
      _writer = _docWriter;
      return _dtdWriter.toString();
    }
    return null;
  }
  
  public void printText(String paramString)
    throws IOException
  {
    try
    {
      int i = paramString.length();
      for (int j = 0; j < i; j++)
      {
        if (_pos == 4096)
        {
          _writer.write(_buffer);
          _pos = 0;
        }
        _buffer[_pos] = paramString.charAt(j);
        _pos += 1;
      }
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
      throw localIOException;
    }
  }
  
  public void printText(StringBuffer paramStringBuffer)
    throws IOException
  {
    try
    {
      int i = paramStringBuffer.length();
      for (int j = 0; j < i; j++)
      {
        if (_pos == 4096)
        {
          _writer.write(_buffer);
          _pos = 0;
        }
        _buffer[_pos] = paramStringBuffer.charAt(j);
        _pos += 1;
      }
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
      throw localIOException;
    }
  }
  
  public void printText(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      while (paramInt2-- > 0)
      {
        if (_pos == 4096)
        {
          _writer.write(_buffer);
          _pos = 0;
        }
        _buffer[_pos] = paramArrayOfChar[paramInt1];
        paramInt1++;
        _pos += 1;
      }
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
      throw localIOException;
    }
  }
  
  public void printText(char paramChar)
    throws IOException
  {
    try
    {
      if (_pos == 4096)
      {
        _writer.write(_buffer);
        _pos = 0;
      }
      _buffer[_pos] = paramChar;
      _pos += 1;
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
      throw localIOException;
    }
  }
  
  public void printSpace()
    throws IOException
  {
    try
    {
      if (_pos == 4096)
      {
        _writer.write(_buffer);
        _pos = 0;
      }
      _buffer[_pos] = ' ';
      _pos += 1;
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
      throw localIOException;
    }
  }
  
  public void breakLine()
    throws IOException
  {
    try
    {
      if (_pos == 4096)
      {
        _writer.write(_buffer);
        _pos = 0;
      }
      _buffer[_pos] = '\n';
      _pos += 1;
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
      throw localIOException;
    }
  }
  
  public void breakLine(boolean paramBoolean)
    throws IOException
  {
    breakLine();
  }
  
  public void flushLine(boolean paramBoolean)
    throws IOException
  {
    try
    {
      _writer.write(_buffer, 0, _pos);
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
    }
    _pos = 0;
  }
  
  public void flush()
    throws IOException
  {
    try
    {
      _writer.write(_buffer, 0, _pos);
      _writer.flush();
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
      throw localIOException;
    }
    _pos = 0;
  }
  
  public void indent() {}
  
  public void unindent() {}
  
  public int getNextIndent()
  {
    return 0;
  }
  
  public void setNextIndent(int paramInt) {}
  
  public void setThisIndent(int paramInt) {}
}

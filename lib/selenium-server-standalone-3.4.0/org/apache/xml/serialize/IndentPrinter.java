package org.apache.xml.serialize;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/**
 * @deprecated
 */
public class IndentPrinter
  extends Printer
{
  private StringBuffer _line = new StringBuffer(80);
  private StringBuffer _text = new StringBuffer(20);
  private int _spaces = 0;
  private int _thisIndent = this._nextIndent = 0;
  private int _nextIndent;
  
  public IndentPrinter(Writer paramWriter, OutputFormat paramOutputFormat)
  {
    super(paramWriter, paramOutputFormat);
  }
  
  public void enterDTD()
  {
    if (_dtdWriter == null)
    {
      _line.append(_text);
      _text = new StringBuffer(20);
      flushLine(false);
      _dtdWriter = new StringWriter();
      _docWriter = _writer;
      _writer = _dtdWriter;
    }
  }
  
  public String leaveDTD()
  {
    if (_writer == _dtdWriter)
    {
      _line.append(_text);
      _text = new StringBuffer(20);
      flushLine(false);
      _writer = _docWriter;
      return _dtdWriter.toString();
    }
    return null;
  }
  
  public void printText(String paramString)
  {
    _text.append(paramString);
  }
  
  public void printText(StringBuffer paramStringBuffer)
  {
    _text.append(paramStringBuffer.toString());
  }
  
  public void printText(char paramChar)
  {
    _text.append(paramChar);
  }
  
  public void printText(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    _text.append(paramArrayOfChar, paramInt1, paramInt2);
  }
  
  public void printSpace()
  {
    if (_text.length() > 0)
    {
      if ((_format.getLineWidth() > 0) && (_thisIndent + _line.length() + _spaces + _text.length() > _format.getLineWidth()))
      {
        flushLine(false);
        try
        {
          _writer.write(_format.getLineSeparator());
        }
        catch (IOException localIOException)
        {
          if (_exception != null) {
            break label113;
          }
        }
        _exception = localIOException;
      }
      label113:
      while (_spaces > 0)
      {
        _line.append(' ');
        _spaces -= 1;
      }
      _line.append(_text);
      _text = new StringBuffer(20);
    }
    _spaces += 1;
  }
  
  public void breakLine()
  {
    breakLine(false);
  }
  
  public void breakLine(boolean paramBoolean)
  {
    if (_text.length() > 0)
    {
      while (_spaces > 0)
      {
        _line.append(' ');
        _spaces -= 1;
      }
      _line.append(_text);
      _text = new StringBuffer(20);
    }
    flushLine(paramBoolean);
    try
    {
      _writer.write(_format.getLineSeparator());
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
    }
  }
  
  public void flushLine(boolean paramBoolean)
  {
    if (_line.length() > 0) {
      try
      {
        if ((_format.getIndenting()) && (!paramBoolean))
        {
          int i = _thisIndent;
          if ((2 * i > _format.getLineWidth()) && (_format.getLineWidth() > 0)) {}
          for (i = _format.getLineWidth() / 2; i > 0; i--) {
            _writer.write(32);
          }
        }
        _thisIndent = _nextIndent;
        _spaces = 0;
        _writer.write(_line.toString());
        _line = new StringBuffer(40);
      }
      catch (IOException localIOException)
      {
        if (_exception == null) {
          _exception = localIOException;
        }
      }
    }
  }
  
  public void flush()
  {
    if ((_line.length() > 0) || (_text.length() > 0)) {
      breakLine();
    }
    try
    {
      _writer.flush();
    }
    catch (IOException localIOException)
    {
      if (_exception == null) {
        _exception = localIOException;
      }
    }
  }
  
  public void indent()
  {
    _nextIndent += _format.getIndent();
  }
  
  public void unindent()
  {
    _nextIndent -= _format.getIndent();
    if (_nextIndent < 0) {
      _nextIndent = 0;
    }
    if (_line.length() + _spaces + _text.length() == 0) {
      _thisIndent = _nextIndent;
    }
  }
  
  public int getNextIndent()
  {
    return _nextIndent;
  }
  
  public void setNextIndent(int paramInt)
  {
    _nextIndent = paramInt;
  }
  
  public void setThisIndent(int paramInt)
  {
    _thisIndent = paramInt;
  }
}

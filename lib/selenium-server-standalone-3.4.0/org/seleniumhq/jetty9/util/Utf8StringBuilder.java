package org.seleniumhq.jetty9.util;

















public class Utf8StringBuilder
  extends Utf8Appendable
{
  final StringBuilder _buffer;
  















  public Utf8StringBuilder()
  {
    super(new StringBuilder());
    _buffer = ((StringBuilder)_appendable);
  }
  
  public Utf8StringBuilder(int capacity)
  {
    super(new StringBuilder(capacity));
    _buffer = ((StringBuilder)_appendable);
  }
  

  public int length()
  {
    return _buffer.length();
  }
  

  public void reset()
  {
    super.reset();
    _buffer.setLength(0);
  }
  
  public StringBuilder getStringBuilder()
  {
    checkState();
    return _buffer;
  }
  

  public String toString()
  {
    checkState();
    return _buffer.toString();
  }
}

package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;























@JsxClass(isJSObject=false)
public class ArrayBufferView
  extends SimpleScriptable
{
  private ArrayBuffer buffer_;
  private int byteLength_;
  private int byteOffset_;
  
  public ArrayBufferView() {}
  
  protected void constructor(ArrayBuffer buffer, int byteOffset, int length)
  {
    buffer_ = buffer;
    byteOffset_ = byteOffset;
    byteLength_ = length;
  }
  



  @JsxGetter
  public ArrayBuffer getBuffer()
  {
    return buffer_;
  }
  



  protected void setBuffer(ArrayBuffer buffer)
  {
    buffer_ = buffer;
  }
  



  @JsxGetter
  public int getByteLength()
  {
    return byteLength_;
  }
  



  protected void setByteLength(int byteLength)
  {
    byteLength_ = byteLength;
  }
  



  @JsxGetter
  public int getByteOffset()
  {
    return byteOffset_;
  }
}

package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

























@JsxClass
public class Uint8Array
  extends ArrayBufferViewBase
{
  @JsxConstant
  public static final int BYTES_PER_ELEMENT = 1;
  
  public Uint8Array() {}
  
  @JsxConstructor
  public void constructor(Object object, Object byteOffset, Object length)
  {
    super.constructor(object, byteOffset, length);
  }
  



  protected byte[] toArray(Number number)
  {
    return new byte[] { number != null ? number.byteValue() : 0 };
  }
  



  protected Object fromArray(byte[] array, int offset)
  {
    if ((offset < 0) || (offset >= array.length)) {
      return Scriptable.NOT_FOUND;
    }
    return Integer.valueOf(array[offset] & 0xFF);
  }
  



  protected int getBytesPerElement()
  {
    return 1;
  }
}

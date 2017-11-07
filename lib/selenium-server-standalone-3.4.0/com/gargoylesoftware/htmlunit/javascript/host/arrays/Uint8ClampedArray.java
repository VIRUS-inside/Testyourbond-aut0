package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
























@JsxClass
public class Uint8ClampedArray
  extends ArrayBufferViewBase
{
  @JsxConstant
  public static final int BYTES_PER_ELEMENT = 1;
  
  public Uint8ClampedArray() {}
  
  @JsxConstructor
  public void constructor(Object object, Object byteOffset, Object length)
  {
    super.constructor(object, byteOffset, length);
  }
  



  protected byte[] toArray(Number number)
  {
    if ((number == null) || (number.intValue() < 0)) {
      number = Integer.valueOf(0);
    }
    else if (number.intValue() > 255) {
      number = Integer.valueOf(255);
    }
    return new byte[] { number.byteValue() };
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

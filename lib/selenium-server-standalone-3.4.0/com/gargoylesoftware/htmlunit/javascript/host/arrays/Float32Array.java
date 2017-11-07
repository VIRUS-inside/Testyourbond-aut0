package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;


























@JsxClass
public class Float32Array
  extends ArrayBufferViewBase
{
  @JsxConstant
  public static final int BYTES_PER_ELEMENT = 4;
  
  public Float32Array() {}
  
  @JsxConstructor
  public void constructor(Object object, Object byteOffset, Object length)
  {
    super.constructor(object, byteOffset, length);
  }
  



  protected byte[] toArray(Number number)
  {
    ByteBuffer buff = ByteBuffer.allocate(4);
    buff.order(ByteOrder.LITTLE_ENDIAN);
    buff.putFloat(number != null ? number.floatValue() : NaN.0F);
    return buff.array();
  }
  



  protected Object fromArray(byte[] array, int offset)
  {
    if ((offset < 0) || (offset >= array.length)) {
      return Scriptable.NOT_FOUND;
    }
    ByteBuffer buff = ByteBuffer.wrap(array);
    buff.order(ByteOrder.LITTLE_ENDIAN);
    return Float.valueOf(buff.getFloat(offset));
  }
  



  protected int getBytesPerElement()
  {
    return 4;
  }
}

package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;


























@JsxClass
public class Float64Array
  extends ArrayBufferViewBase
{
  @JsxConstant
  public static final int BYTES_PER_ELEMENT = 8;
  
  public Float64Array() {}
  
  @JsxConstructor
  public void constructor(Object object, Object byteOffset, Object length)
  {
    super.constructor(object, byteOffset, length);
  }
  



  protected byte[] toArray(Number number)
  {
    ByteBuffer buff = ByteBuffer.allocate(8);
    buff.order(ByteOrder.LITTLE_ENDIAN);
    buff.putDouble(number != null ? number.doubleValue() : NaN.0D);
    return buff.array();
  }
  



  protected Object fromArray(byte[] array, int offset)
  {
    if ((offset < 0) || (offset >= array.length)) {
      return Scriptable.NOT_FOUND;
    }
    ByteBuffer buff = ByteBuffer.wrap(array);
    buff.order(ByteOrder.LITTLE_ENDIAN);
    return Double.valueOf(buff.getDouble(offset));
  }
  



  protected int getBytesPerElement()
  {
    return 8;
  }
}

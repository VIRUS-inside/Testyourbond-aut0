package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;


























@JsxClass
public class Uint16Array
  extends ArrayBufferViewBase
{
  @JsxConstant
  public static final int BYTES_PER_ELEMENT = 2;
  
  public Uint16Array() {}
  
  @JsxConstructor
  public void constructor(Object object, Object byteOffset, Object length)
  {
    super.constructor(object, byteOffset, length);
  }
  



  protected byte[] toArray(Number number)
  {
    ByteBuffer buff = ByteBuffer.allocate(2);
    buff.order(ByteOrder.LITTLE_ENDIAN);
    buff.putShort((short)(number != null ? number.shortValue() : 0));
    return buff.array();
  }
  



  protected Object fromArray(byte[] array, int offset)
  {
    if ((offset < 0) || (offset >= array.length)) {
      return Scriptable.NOT_FOUND;
    }
    ByteBuffer buff = ByteBuffer.wrap(array);
    buff.order(ByteOrder.LITTLE_ENDIAN);
    return Integer.valueOf(buff.getShort(offset) & 0xFFFF);
  }
  



  protected int getBytesPerElement()
  {
    return 2;
  }
}

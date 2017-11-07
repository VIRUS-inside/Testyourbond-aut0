package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

























@JsxClass
public class DataView
  extends ArrayBufferView
{
  public DataView() {}
  
  @JsxConstructor
  public void constructor(ArrayBuffer array, int byteOffset, Object length)
  {
    if (length == Undefined.instance) {
      length = Integer.valueOf(array.getByteLength());
    }
    super.constructor(array, byteOffset, ((Number)length).intValue());
  }
  




  @JsxFunction
  public byte getInt8(int byteOffset)
  {
    return getBuffer().getBytes()[(getByteOffset() + byteOffset)];
  }
  




  @JsxFunction
  public void setInt8(int byteOffset, int value)
  {
    byte[] array = getBuffer().getBytes();
    array[(getByteOffset() + byteOffset)] = ((byte)value);
  }
  





  @JsxFunction
  public short getInt16(int byteOffset, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    return buff.getShort(getByteOffset() + byteOffset);
  }
  





  @JsxFunction
  public void setInt16(int byteOffset, int value, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    buff.putShort(getByteOffset() + byteOffset, (short)value);
  }
  





  @JsxFunction
  public int getInt32(int byteOffset, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    return buff.getInt(getByteOffset() + byteOffset);
  }
  





  @JsxFunction
  public void setInt32(int byteOffset, int value, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    buff.putInt(getByteOffset() + byteOffset, value);
  }
  




  @JsxFunction
  public int getUint8(int byteOffset)
  {
    return getBuffer().getBytes()[(getByteOffset() + byteOffset)] & 0xFF;
  }
  




  @JsxFunction
  public void setUint8(int byteOffset, int value)
  {
    setInt8(byteOffset, value);
  }
  





  @JsxFunction
  public int getUint16(int byteOffset, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    return buff.getShort(getByteOffset() + byteOffset) & 0xFFFF;
  }
  





  @JsxFunction
  public void setUint16(int byteOffset, int value, boolean littleEndian)
  {
    setInt16(byteOffset, value, littleEndian);
  }
  





  @JsxFunction
  public long getUint32(int byteOffset, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    return buff.getInt(getByteOffset() + byteOffset) & 0xFFFFFFFF;
  }
  





  @JsxFunction
  public void setUint32(int byteOffset, int value, boolean littleEndian)
  {
    setInt32(byteOffset, value, littleEndian);
  }
  





  @JsxFunction
  public float getFloat32(int byteOffset, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    return buff.getFloat(getByteOffset() + byteOffset);
  }
  





  @JsxFunction
  public void setFloat32(int byteOffset, double value, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    buff.putFloat(getByteOffset() + byteOffset, (float)value);
  }
  





  @JsxFunction
  public double getFloat64(int byteOffset, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    return buff.getDouble(getByteOffset() + byteOffset);
  }
  





  @JsxFunction
  public void setFloat64(int byteOffset, double value, boolean littleEndian)
  {
    ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
    if (littleEndian) {
      buff.order(ByteOrder.LITTLE_ENDIAN);
    }
    buff.putDouble(getByteOffset() + byteOffset, value);
  }
}

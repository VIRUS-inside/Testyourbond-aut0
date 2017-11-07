package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;



























@JsxClass(isJSObject=false)
public class ArrayBufferViewBase
  extends ArrayBufferView
{
  public ArrayBufferViewBase() {}
  
  public void constructor(Object object, Object byteOffset, Object length)
  {
    if ((object instanceof Number)) {
      constructor(((Number)object).intValue());
    }
    else if ((object instanceof NativeArray)) {
      constructor((NativeArray)object);
    }
    else if ((object instanceof ArrayBufferViewBase)) {
      constructor((ArrayBufferViewBase)object);
    }
    else if ((object instanceof ArrayBuffer)) {
      ArrayBuffer array = (ArrayBuffer)object;
      
      double dbByteOffset = Context.toNumber(byteOffset);
      if (dbByteOffset != dbByteOffset) {
        dbByteOffset = 0.0D;
      }
      
      double dbLength = Context.toNumber(length);
      if (dbLength != dbLength) {
        dbLength = array.getByteLength();
      }
      super.constructor(array, (int)dbByteOffset, (int)dbLength);
    }
    else {
      Context.reportRuntimeError("Invalid type " + object.getClass().getName());
    }
  }
  
  private void constructor(int length) {
    int byteLength = length * getBytesPerElement();
    setByteLength(byteLength);
    initBuffer(byteLength);
  }
  
  private void constructor(NativeArray array) {
    int byteLength = (int)array.getLength() * getBytesPerElement();
    setByteLength(byteLength);
    initBuffer(byteLength);
    set(array, 0);
  }
  
  private void constructor(ArrayBufferViewBase array) {
    int byteLength = array.getLength() * getBytesPerElement();
    setByteLength(byteLength);
    initBuffer(byteLength);
    set(array, 0);
  }
  
  private void initBuffer(int lengthInBytes) {
    ArrayBuffer buffer = new ArrayBuffer();
    buffer.constructor(lengthInBytes);
    buffer.setPrototype(getPrototype(buffer.getClass()));
    buffer.setParentScope(getParentScope());
    setBuffer(buffer);
  }
  



  @JsxGetter
  public int getLength()
  {
    return getByteLength() / getBytesPerElement();
  }
  




  @JsxFunction
  public void set(ScriptableObject sourceArray, int offset)
  {
    int length = ((Number)ScriptableObject.getProperty(sourceArray, "length")).intValue();
    for (int i = 0; i < length; i++) {
      put(i + offset, this, sourceArray.get(Integer.valueOf(i)));
    }
  }
  



  public Object get(int index, Scriptable start)
  {
    int offset = index * getBytesPerElement() + getByteOffset();
    ArrayBuffer buffer = getBuffer();
    if (buffer == null) {
      return Scriptable.NOT_FOUND;
    }
    return fromArray(buffer.getBytes(), offset);
  }
  



  public void put(int index, Scriptable start, Object value)
  {
    getBuffer().setBytes(index * getBytesPerElement() + getByteOffset(), 
      value == null ? toArray(null) : toArray(Double.valueOf(Context.toNumber(value))));
  }
  




  protected byte[] toArray(Number number)
  {
    return null;
  }
  





  protected Object fromArray(byte[] array, int offset)
  {
    return null;
  }
  





  @JsxFunction
  public ArrayBufferView subarray(int begin, Object end)
  {
    if (end == Undefined.instance) {
      end = Integer.valueOf(getLength());
    }
    try {
      ArrayBufferView object = (ArrayBufferView)getClass().newInstance();
      object.setPrototype(getPrototype());
      object.setParentScope(getParentScope());
      object.constructor(getBuffer(), begin, ((Number)end).intValue() - begin);
      return object;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  



  protected int getBytesPerElement()
  {
    return 1;
  }
}

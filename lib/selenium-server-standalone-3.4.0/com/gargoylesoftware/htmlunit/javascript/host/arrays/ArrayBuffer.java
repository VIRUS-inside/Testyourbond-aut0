package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;























@JsxClass
public class ArrayBuffer
  extends SimpleScriptable
{
  private byte[] bytes_;
  
  public ArrayBuffer() {}
  
  @JsxConstructor
  public void constructor(int length)
  {
    bytes_ = new byte[length];
  }
  



  @JsxGetter
  public int getByteLength()
  {
    return bytes_.length;
  }
  






  @JsxFunction
  public ArrayBuffer slice(Object begin, Object end)
  {
    if ((begin == Undefined.instance) || ((begin instanceof Boolean))) {
      throw Context.reportRuntimeError("Invalid type " + begin.getClass().getName());
    }
    
    double beginNumber = Context.toNumber(begin);
    int beginInt;
    int beginInt; if (Double.isNaN(beginNumber)) {
      beginInt = 0;
    } else { int beginInt;
      if (Double.isInfinite(beginNumber)) {
        if (beginNumber > 0.0D) {
          byte[] byteArray = new byte[0];
          ArrayBuffer arrayBuffer = new ArrayBuffer();
          bytes_ = byteArray;
          return arrayBuffer;
        }
        beginInt = 0;
      }
      else {
        beginInt = (int)beginNumber;
        if (beginInt != beginNumber)
          throw Context.reportRuntimeError("Invalid type " + begin.getClass().getName());
      }
    }
    double endNumber;
    double endNumber;
    if (end == Undefined.instance) {
      endNumber = getByteLength();
    }
    else {
      endNumber = Context.toNumber(end);
    }
    
    if ((Double.isNaN(endNumber)) || (Double.isInfinite(endNumber)) || (endNumber < beginInt)) {
      endNumber = beginInt;
    }
    
    byte[] byteArray = new byte[(int)endNumber - beginInt];
    System.arraycopy(bytes_, beginInt, byteArray, 0, byteArray.length);
    ArrayBuffer arrayBuffer = new ArrayBuffer();
    bytes_ = byteArray;
    return arrayBuffer;
  }
  
  byte getByte(int index) {
    return bytes_[index];
  }
  




  public void setBytes(int index, byte[] array)
  {
    int i = array.length - 1;
    if (index + i >= bytes_.length) {}
    for (i = bytes_.length - index - 1; 
        
        i >= 0; i--) {
      bytes_[(index + i)] = array[i];
    }
  }
  


  public byte[] getBytes()
  {
    return bytes_;
  }
}

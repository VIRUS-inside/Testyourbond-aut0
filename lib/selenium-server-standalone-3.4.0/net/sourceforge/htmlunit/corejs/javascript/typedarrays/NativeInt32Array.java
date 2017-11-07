package net.sourceforge.htmlunit.corejs.javascript.typedarrays;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;












public class NativeInt32Array
  extends NativeTypedArrayView<Integer>
{
  private static final long serialVersionUID = -8963461831950499340L;
  private static final String CLASS_NAME = "Int32Array";
  private static final int BYTES_PER_ELEMENT = 4;
  
  public NativeInt32Array() {}
  
  public NativeInt32Array(NativeArrayBuffer ab, int off, int len)
  {
    super(ab, off, len, len * 4);
  }
  
  public NativeInt32Array(int len) {
    this(new NativeArrayBuffer(len * 4), 0, len);
  }
  
  public String getClassName()
  {
    return "Int32Array";
  }
  
  public static void init(Context cx, Scriptable scope, boolean sealed) {
    NativeInt32Array a = new NativeInt32Array();
    a.exportAsJSClass(4, scope, sealed);
  }
  

  protected NativeTypedArrayView construct(NativeArrayBuffer ab, int off, int len)
  {
    return new NativeInt32Array(ab, off, len);
  }
  
  public int getBytesPerElement()
  {
    return 4;
  }
  

  protected NativeTypedArrayView realThis(Scriptable thisObj, IdFunctionObject f)
  {
    if (!(thisObj instanceof NativeInt32Array)) {
      throw incompatibleCallError(f);
    }
    return (NativeInt32Array)thisObj;
  }
  
  protected Object js_get(int index)
  {
    if (checkIndex(index)) {
      return Undefined.instance;
    }
    return ByteIo.readInt32(arrayBuffer.buffer, index * 4 + offset, false);
  }
  

  protected Object js_set(int index, Object c)
  {
    if (checkIndex(index)) {
      return Undefined.instance;
    }
    int val = ScriptRuntime.toInt32(c);
    ByteIo.writeInt32(arrayBuffer.buffer, index * 4 + offset, val, false);
    
    return null;
  }
  
  public Integer get(int i)
  {
    if (checkIndex(i)) {
      throw new IndexOutOfBoundsException();
    }
    return (Integer)js_get(i);
  }
  
  public Integer set(int i, Integer aByte)
  {
    if (checkIndex(i)) {
      throw new IndexOutOfBoundsException();
    }
    return (Integer)js_set(i, aByte);
  }
}

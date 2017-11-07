package net.sourceforge.htmlunit.corejs.javascript.typedarrays;

import net.sourceforge.htmlunit.corejs.javascript.IdScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;










public abstract class NativeArrayBufferView
  extends IdScriptableObject
{
  private static final long serialVersionUID = 6884475582973958419L;
  protected final NativeArrayBuffer arrayBuffer;
  protected final int offset;
  protected final int byteLength;
  private static final int Id_buffer = 1;
  private static final int Id_byteOffset = 2;
  private static final int Id_byteLength = 3;
  private static final int MAX_INSTANCE_ID = 3;
  
  public NativeArrayBufferView()
  {
    arrayBuffer = NativeArrayBuffer.EMPTY_BUFFER;
    offset = 0;
    byteLength = 0;
  }
  
  protected NativeArrayBufferView(NativeArrayBuffer ab, int offset, int byteLength)
  {
    this.offset = offset;
    this.byteLength = byteLength;
    arrayBuffer = ab;
  }
  


  public NativeArrayBuffer getBuffer()
  {
    return arrayBuffer;
  }
  



  public int getByteOffset()
  {
    return offset;
  }
  



  public int getByteLength()
  {
    return byteLength;
  }
  
  protected static boolean isArg(Object[] args, int i) {
    return (args.length > i) && (!Undefined.instance.equals(args[i]));
  }
  


  protected int getMaxInstanceId()
  {
    return 3;
  }
  
  protected String getInstanceIdName(int id)
  {
    switch (id) {
    case 1: 
      return "buffer";
    case 2: 
      return "byteOffset";
    case 3: 
      return "byteLength";
    }
    return super.getInstanceIdName(id);
  }
  

  protected Object getInstanceIdValue(int id)
  {
    switch (id) {
    case 1: 
      return arrayBuffer;
    case 2: 
      return ScriptRuntime.wrapInt(offset);
    case 3: 
      return ScriptRuntime.wrapInt(byteLength);
    }
    return super.getInstanceIdValue(id);
  }
  






  protected int findInstanceIdInfo(String s)
  {
    int id = 0;
    String X = null;
    
    int s_length = s.length();
    if (s_length == 6) {
      X = "buffer";
      id = 1;
    } else if (s_length == 10) {
      int c = s.charAt(4);
      if (c == 76) {
        X = "byteLength";
        id = 3;
      } else if (c == 79) {
        X = "byteOffset";
        id = 2;
      }
    }
    if ((X != null) && (X != s) && (!X.equals(s))) {
      id = 0;
    }
    

    if (id == 0) {
      return super.findInstanceIdInfo(s);
    }
    return instanceIdInfo(5, id);
  }
}

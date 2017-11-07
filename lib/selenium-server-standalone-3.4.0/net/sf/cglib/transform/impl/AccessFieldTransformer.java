package net.sf.cglib.transform.impl;

import net.sf.cglib.asm..Type;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;













public class AccessFieldTransformer
  extends ClassEmitterTransformer
{
  private Callback callback;
  
  public AccessFieldTransformer(Callback callback)
  {
    this.callback = callback;
  }
  



  public void declare_field(int access, String name, .Type type, Object value)
  {
    super.declare_field(access, name, type, value);
    
    String property = TypeUtils.upperFirst(callback.getPropertyName(getClassType(), name));
    if (property != null)
    {
      CodeEmitter e = begin_method(1, new Signature("get" + property, type, Constants.TYPES_EMPTY), null);
      



      e.load_this();
      e.getfield(name);
      e.return_value();
      e.end_method();
      
      e = begin_method(1, new Signature("set" + property, .Type.VOID_TYPE, new .Type[] { type }), null);
      



      e.load_this();
      e.load_arg(0);
      e.putfield(name);
      e.return_value();
      e.end_method();
    }
  }
  
  public static abstract interface Callback
  {
    public abstract String getPropertyName(.Type paramType, String paramString);
  }
}

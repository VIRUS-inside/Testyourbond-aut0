package net.sf.cglib.transform.impl;

import java.lang.reflect.Method;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;










public class AddStaticInitTransformer
  extends ClassEmitterTransformer
{
  private MethodInfo info;
  
  public AddStaticInitTransformer(Method classInit)
  {
    info = ReflectUtils.getMethodInfo(classInit);
    if (!TypeUtils.isStatic(info.getModifiers())) {
      throw new IllegalArgumentException(classInit + " is not static");
    }
    .Type[] types = info.getSignature().getArgumentTypes();
    if ((types.length != 1) || 
      (!types[0].equals(Constants.TYPE_CLASS)) || 
      (!info.getSignature().getReturnType().equals(.Type.VOID_TYPE))) {
      throw new IllegalArgumentException(classInit + " illegal signature");
    }
  }
  
  protected void init() {
    if (!TypeUtils.isInterface(getAccess())) {
      CodeEmitter e = getStaticHook();
      EmitUtils.load_class_this(e);
      e.invoke(info);
    }
  }
}

package net.sf.cglib.proxy;

import java.util.List;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.Signature;

abstract interface CallbackGenerator
{
  public abstract void generate(ClassEmitter paramClassEmitter, Context paramContext, List paramList)
    throws Exception;
  
  public abstract void generateStatic(CodeEmitter paramCodeEmitter, Context paramContext, List paramList)
    throws Exception;
  
  public static abstract interface Context
  {
    public abstract ClassLoader getClassLoader();
    
    public abstract CodeEmitter beginMethod(ClassEmitter paramClassEmitter, MethodInfo paramMethodInfo);
    
    public abstract int getOriginalModifiers(MethodInfo paramMethodInfo);
    
    public abstract int getIndex(MethodInfo paramMethodInfo);
    
    public abstract void emitCallback(CodeEmitter paramCodeEmitter, int paramInt);
    
    public abstract Signature getImplSignature(MethodInfo paramMethodInfo);
    
    public abstract void emitInvoke(CodeEmitter paramCodeEmitter, MethodInfo paramMethodInfo);
  }
}

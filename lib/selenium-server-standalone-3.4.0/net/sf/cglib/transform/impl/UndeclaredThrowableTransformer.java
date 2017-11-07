package net.sf.cglib.transform.impl;

import java.lang.reflect.Constructor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.Block;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.transform.ClassEmitterTransformer;










public class UndeclaredThrowableTransformer
  extends ClassEmitterTransformer
{
  private .Type wrapper;
  
  public UndeclaredThrowableTransformer(Class wrapper)
  {
    this.wrapper = .Type.getType(wrapper);
    boolean found = false;
    Constructor[] cstructs = wrapper.getConstructors();
    for (int i = 0; i < cstructs.length; i++) {
      Class[] types = cstructs[i].getParameterTypes();
      if ((types.length == 1) && (types[0].equals(Throwable.class))) {
        found = true;
        break;
      }
    }
    if (!found)
      throw new IllegalArgumentException(wrapper + " does not have a single-arg constructor that takes a Throwable");
  }
  
  public CodeEmitter begin_method(int access, Signature sig, final .Type[] exceptions) {
    CodeEmitter e = super.begin_method(access, sig, exceptions);
    if ((TypeUtils.isAbstract(access)) || (sig.equals(Constants.SIG_STATIC))) {
      return e;
    }
    new CodeEmitter(e)
    {
      private Block handler;
      
      public void visitMaxs(int maxStack, int maxLocals)
      {
        handler.end();
        EmitUtils.wrap_undeclared_throwable(this, handler, exceptions, wrapper);
        super.visitMaxs(maxStack, maxLocals);
      }
    };
  }
}

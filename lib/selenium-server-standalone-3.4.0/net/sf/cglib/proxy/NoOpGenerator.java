package net.sf.cglib.proxy;

import java.util.Iterator;
import java.util.List;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.TypeUtils;










class NoOpGenerator
  implements CallbackGenerator
{
  NoOpGenerator() {}
  
  public static final NoOpGenerator INSTANCE = new NoOpGenerator();
  
  public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
    for (Iterator it = methods.iterator(); it.hasNext();) {
      MethodInfo method = (MethodInfo)it.next();
      if ((TypeUtils.isBridge(method.getModifiers())) || (
        (TypeUtils.isProtected(context.getOriginalModifiers(method))) && 
        (TypeUtils.isPublic(method.getModifiers())))) {
        CodeEmitter e = EmitUtils.begin_method(ce, method);
        e.load_this();
        e.load_args();
        context.emitInvoke(e, method);
        e.return_value();
        e.end_method();
      }
    }
  }
  
  public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {}
}

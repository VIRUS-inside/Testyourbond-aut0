package net.sf.cglib.proxy;

import java.util.Iterator;
import java.util.List;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;







class FixedValueGenerator
  implements CallbackGenerator
{
  FixedValueGenerator() {}
  
  public static final FixedValueGenerator INSTANCE = new FixedValueGenerator();
  
  private static final .Type FIXED_VALUE = TypeUtils.parseType("net.sf.cglib.proxy.FixedValue");
  
  private static final Signature LOAD_OBJECT = TypeUtils.parseSignature("Object loadObject()");
  
  public void generate(ClassEmitter ce, CallbackGenerator.Context context, List methods) {
    for (Iterator it = methods.iterator(); it.hasNext();) {
      MethodInfo method = (MethodInfo)it.next();
      CodeEmitter e = context.beginMethod(ce, method);
      context.emitCallback(e, context.getIndex(method));
      e.invoke_interface(FIXED_VALUE, LOAD_OBJECT);
      e.unbox_or_zero(e.getReturnType());
      e.return_value();
      e.end_method();
    }
  }
  
  public void generateStatic(CodeEmitter e, CallbackGenerator.Context context, List methods) {}
}

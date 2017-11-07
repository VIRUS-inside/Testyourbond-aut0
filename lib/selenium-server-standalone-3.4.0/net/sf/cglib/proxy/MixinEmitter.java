package net.sf.cglib.proxy;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.ClassInfo;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.MethodWrapper;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;









class MixinEmitter
  extends ClassEmitter
{
  private static final String FIELD_NAME = "CGLIB$DELEGATES";
  private static final Signature CSTRUCT_OBJECT_ARRAY = TypeUtils.parseConstructor("Object[]");
  
  private static final .Type MIXIN = TypeUtils.parseType("net.sf.cglib.proxy.Mixin");
  private static final Signature NEW_INSTANCE = new Signature("newInstance", MIXIN, new .Type[] { Constants.TYPE_OBJECT_ARRAY });
  
  public MixinEmitter(.ClassVisitor v, String className, Class[] classes, int[] route)
  {
    super(v);
    
    begin_class(46, 1, className, MIXIN, 
    


      TypeUtils.getTypes(getInterfaces(classes)), "<generated>");
    
    EmitUtils.null_constructor(this);
    EmitUtils.factory_method(this, NEW_INSTANCE);
    
    declare_field(2, "CGLIB$DELEGATES", Constants.TYPE_OBJECT_ARRAY, null);
    
    CodeEmitter e = begin_method(1, CSTRUCT_OBJECT_ARRAY, null);
    e.load_this();
    e.super_invoke_constructor();
    e.load_this();
    e.load_arg(0);
    e.putfield("CGLIB$DELEGATES");
    e.return_value();
    e.end_method();
    
    Set unique = new HashSet();
    for (int i = 0; i < classes.length; i++) {
      Method[] methods = getMethods(classes[i]);
      for (int j = 0; j < methods.length; j++) {
        if (unique.add(MethodWrapper.create(methods[j]))) {
          MethodInfo method = ReflectUtils.getMethodInfo(methods[j]);
          int modifiers = 1;
          if ((method.getModifiers() & 0x80) == 128) {
            modifiers |= 0x80;
          }
          e = EmitUtils.begin_method(this, method, modifiers);
          e.load_this();
          e.getfield("CGLIB$DELEGATES");
          e.aaload(route != null ? route[i] : i);
          e.checkcast(method.getClassInfo().getType());
          e.load_args();
          e.invoke(method);
          e.return_value();
          e.end_method();
        }
      }
    }
    
    end_class();
  }
  
  protected Class[] getInterfaces(Class[] classes) {
    return classes;
  }
  
  protected Method[] getMethods(Class type) {
    return type.getMethods();
  }
}

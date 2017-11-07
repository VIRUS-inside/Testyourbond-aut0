package net.sf.cglib.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..Label;
import net.sf.cglib.asm..Type;
import net.sf.cglib.core.Block;
import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.MethodInfo;
import net.sf.cglib.core.MethodInfoTransformer;
import net.sf.cglib.core.ObjectSwitchCallback;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.Transformer;
import net.sf.cglib.core.TypeUtils;
import net.sf.cglib.core.VisibilityPredicate;

class FastClassEmitter extends ClassEmitter
{
  private static final Signature CSTRUCT_CLASS = TypeUtils.parseConstructor("Class");
  
  private static final Signature METHOD_GET_INDEX = TypeUtils.parseSignature("int getIndex(String, Class[])");
  private static final Signature SIGNATURE_GET_INDEX = new Signature("getIndex", .Type.INT_TYPE, new .Type[] { net.sf.cglib.core.Constants.TYPE_SIGNATURE });
  

  private static final Signature TO_STRING = TypeUtils.parseSignature("String toString()");
  
  private static final Signature CONSTRUCTOR_GET_INDEX = TypeUtils.parseSignature("int getIndex(Class[])");
  
  private static final Signature INVOKE = TypeUtils.parseSignature("Object invoke(int, Object, Object[])");
  
  private static final Signature NEW_INSTANCE = TypeUtils.parseSignature("Object newInstance(int, Object[])");
  
  private static final Signature GET_MAX_INDEX = TypeUtils.parseSignature("int getMaxIndex()");
  
  private static final Signature GET_SIGNATURE_WITHOUT_RETURN_TYPE = TypeUtils.parseSignature("String getSignatureWithoutReturnType(String, Class[])");
  
  private static final .Type FAST_CLASS = TypeUtils.parseType("net.sf.cglib.reflect.FastClass");
  
  private static final .Type ILLEGAL_ARGUMENT_EXCEPTION = TypeUtils.parseType("IllegalArgumentException");
  
  private static final .Type INVOCATION_TARGET_EXCEPTION = TypeUtils.parseType("java.lang.reflect.InvocationTargetException");
  private static final .Type[] INVOCATION_TARGET_EXCEPTION_ARRAY = { INVOCATION_TARGET_EXCEPTION };
  private static final int TOO_MANY_METHODS = 100;
  
  public FastClassEmitter(.ClassVisitor v, String className, Class type) { super(v);
    
    .Type base = .Type.getType(type);
    begin_class(46, 1, className, FAST_CLASS, null, "<generated>");
    

    CodeEmitter e = begin_method(1, CSTRUCT_CLASS, null);
    e.load_this();
    e.load_args();
    e.super_invoke_constructor(CSTRUCT_CLASS);
    e.return_value();
    e.end_method();
    
    VisibilityPredicate vp = new VisibilityPredicate(type, false);
    List methods = ReflectUtils.addAllMethods(type, new ArrayList());
    CollectionUtils.filter(methods, vp);
    CollectionUtils.filter(methods, new net.sf.cglib.core.DuplicatesPredicate());
    List constructors = new ArrayList(java.util.Arrays.asList(type.getDeclaredConstructors()));
    CollectionUtils.filter(constructors, vp);
    

    emitIndexBySignature(methods);
    

    emitIndexByClassArray(methods);
    

    e = begin_method(1, CONSTRUCTOR_GET_INDEX, null);
    e.load_args();
    List info = CollectionUtils.transform(constructors, MethodInfoTransformer.getInstance());
    EmitUtils.constructor_switch(e, info, new GetIndexCallback(e, info));
    e.end_method();
    

    e = begin_method(1, INVOKE, INVOCATION_TARGET_EXCEPTION_ARRAY);
    e.load_arg(1);
    e.checkcast(base);
    e.load_arg(0);
    invokeSwitchHelper(e, methods, 2, base);
    e.end_method();
    

    e = begin_method(1, NEW_INSTANCE, INVOCATION_TARGET_EXCEPTION_ARRAY);
    e.new_instance(base);
    e.dup();
    e.load_arg(0);
    invokeSwitchHelper(e, constructors, 1, base);
    e.end_method();
    

    e = begin_method(1, GET_MAX_INDEX, null);
    e.push(methods.size() - 1);
    e.return_value();
    e.end_method();
    
    end_class();
  }
  
  private void emitIndexBySignature(List methods)
  {
    CodeEmitter e = begin_method(1, SIGNATURE_GET_INDEX, null);
    List signatures = CollectionUtils.transform(methods, new Transformer() {
      public Object transform(Object obj) {
        return ReflectUtils.getSignature((Method)obj).toString();
      }
    });
    e.load_arg(0);
    e.invoke_virtual(net.sf.cglib.core.Constants.TYPE_OBJECT, TO_STRING);
    signatureSwitchHelper(e, signatures);
    e.end_method();
  }
  
  private void emitIndexByClassArray(List methods)
  {
    CodeEmitter e = begin_method(1, METHOD_GET_INDEX, null);
    if (methods.size() > 100)
    {
      List signatures = CollectionUtils.transform(methods, new Transformer() {
        public Object transform(Object obj) {
          String s = ReflectUtils.getSignature((Method)obj).toString();
          return s.substring(0, s.lastIndexOf(')') + 1);
        }
      });
      e.load_args();
      e.invoke_static(FAST_CLASS, GET_SIGNATURE_WITHOUT_RETURN_TYPE);
      signatureSwitchHelper(e, signatures);
    } else {
      e.load_args();
      List info = CollectionUtils.transform(methods, MethodInfoTransformer.getInstance());
      EmitUtils.method_switch(e, info, new GetIndexCallback(e, info));
    }
    e.end_method();
  }
  
  private void signatureSwitchHelper(final CodeEmitter e, final List signatures) {
    ObjectSwitchCallback callback = new ObjectSwitchCallback()
    {
      public void processCase(Object key, .Label end) {
        e.push(signatures.indexOf(key));
        e.return_value();
      }
      
      public void processDefault() { e.push(-1);
        e.return_value();
      }
    };
    EmitUtils.string_switch(e, 
      (String[])signatures.toArray(new String[signatures.size()]), 1, callback);
  }
  

  private static void invokeSwitchHelper(final CodeEmitter e, List members, final int arg, final .Type base)
  {
    List info = CollectionUtils.transform(members, MethodInfoTransformer.getInstance());
    final .Label illegalArg = e.make_label();
    Block block = e.begin_block();
    e.process_switch(getIntRange(info.size()), new net.sf.cglib.core.ProcessSwitchCallback() {
      public void processCase(int key, .Label end) {
        MethodInfo method = (MethodInfo)val$info.get(key);
        .Type[] types = method.getSignature().getArgumentTypes();
        for (int i = 0; i < types.length; i++) {
          e.load_arg(arg);
          e.aaload(i);
          e.unbox(types[i]);
        }
        

        e.invoke(method, base);
        if (!TypeUtils.isConstructor(method)) {
          e.box(method.getSignature().getReturnType());
        }
        e.return_value();
      }
      
      public void processDefault() { e.goTo(illegalArg);
      }
    });
    block.end();
    EmitUtils.wrap_throwable(block, INVOCATION_TARGET_EXCEPTION);
    e.mark(illegalArg);
    e.throw_exception(ILLEGAL_ARGUMENT_EXCEPTION, "Cannot find matching method/constructor");
  }
  
  private static class GetIndexCallback implements ObjectSwitchCallback {
    private CodeEmitter e;
    private Map indexes = new java.util.HashMap();
    
    public GetIndexCallback(CodeEmitter e, List methods) {
      this.e = e;
      int index = 0;
      for (Iterator it = methods.iterator(); it.hasNext();) {
        indexes.put(it.next(), new Integer(index++));
      }
    }
    
    public void processCase(Object key, .Label end) {
      e.push(((Integer)indexes.get(key)).intValue());
      e.return_value();
    }
    
    public void processDefault() {
      e.push(-1);
      e.return_value();
    }
  }
  
  private static int[] getIntRange(int length) {
    int[] range = new int[length];
    for (int i = 0; i < length; i++) {
      range[i] = i;
    }
    return range;
  }
}

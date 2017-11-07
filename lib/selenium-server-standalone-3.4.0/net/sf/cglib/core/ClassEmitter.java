package net.sf.cglib.core;

import java.util.HashMap;
import java.util.Map;
import net.sf.cglib.asm..ClassVisitor;
import net.sf.cglib.asm..FieldVisitor;
import net.sf.cglib.asm..MethodVisitor;
import net.sf.cglib.asm..Type;
import net.sf.cglib.transform.ClassTransformer;




















public class ClassEmitter
  extends ClassTransformer
{
  private ClassInfo classInfo;
  private Map fieldInfo;
  private static int hookCounter;
  private .MethodVisitor rawStaticInit;
  private CodeEmitter staticInit;
  private CodeEmitter staticHook;
  private Signature staticHookSig;
  
  public ClassEmitter(.ClassVisitor cv)
  {
    setTarget(cv);
  }
  
  public ClassEmitter() {
    super(327680);
  }
  
  public void setTarget(.ClassVisitor cv) {
    this.cv = cv;
    fieldInfo = new HashMap();
    

    staticInit = (this.staticHook = null);
    staticHookSig = null;
  }
  
  private static synchronized int getNextHook() {
    return ++hookCounter;
  }
  
  public ClassInfo getClassInfo() {
    return classInfo;
  }
  
  public void begin_class(int version, final int access, String className, final .Type superType, final .Type[] interfaces, String source) {
    final .Type classType = .Type.getType("L" + className.replace('.', '/') + ";");
    classInfo = new ClassInfo() {
      public .Type getType() {
        return classType;
      }
      
      public .Type getSuperType() { return superType != null ? superType : Constants.TYPE_OBJECT; }
      
      public .Type[] getInterfaces() {
        return interfaces;
      }
      
      public int getModifiers() { return access;
      }
    };
    cv.visit(version, access, classInfo
    
      .getType().getInternalName(), null, classInfo
      
      .getSuperType().getInternalName(), 
      TypeUtils.toInternalNames(interfaces));
    if (source != null)
      cv.visitSource(source, null);
    init();
  }
  
  public CodeEmitter getStaticHook() {
    if (TypeUtils.isInterface(getAccess())) {
      throw new IllegalStateException("static hook is invalid for this class");
    }
    if (staticHook == null) {
      staticHookSig = new Signature("CGLIB$STATICHOOK" + getNextHook(), "()V");
      staticHook = begin_method(8, staticHookSig, null);
      

      if (staticInit != null) {
        staticInit.invoke_static_this(staticHookSig);
      }
    }
    return staticHook;
  }
  
  protected void init() {}
  
  public int getAccess()
  {
    return classInfo.getModifiers();
  }
  
  public .Type getClassType() {
    return classInfo.getType();
  }
  
  public .Type getSuperType() {
    return classInfo.getSuperType();
  }
  
  public void end_class() {
    if ((staticHook != null) && (staticInit == null))
    {
      begin_static();
    }
    if (staticInit != null) {
      staticHook.return_value();
      staticHook.end_method();
      rawStaticInit.visitInsn(177);
      rawStaticInit.visitMaxs(0, 0);
      staticInit = (this.staticHook = null);
      staticHookSig = null;
    }
    cv.visitEnd();
  }
  
  public CodeEmitter begin_method(int access, Signature sig, .Type[] exceptions) {
    if (classInfo == null)
      throw new IllegalStateException("classInfo is null! " + this);
    .MethodVisitor v = cv.visitMethod(access, sig
      .getName(), sig
      .getDescriptor(), null, 
      
      TypeUtils.toInternalNames(exceptions));
    if ((sig.equals(Constants.SIG_STATIC)) && (!TypeUtils.isInterface(getAccess()))) {
      rawStaticInit = v;
      .MethodVisitor wrapped = new .MethodVisitor(327680, v)
      {
        public void visitMaxs(int maxStack, int maxLocals) {}
        
        public void visitInsn(int insn) {
          if (insn != 177) {
            super.visitInsn(insn);
          }
        }
      };
      staticInit = new CodeEmitter(this, wrapped, access, sig, exceptions);
      if (staticHook == null)
      {
        getStaticHook();
      } else {
        staticInit.invoke_static_this(staticHookSig);
      }
      return staticInit; }
    if (sig.equals(staticHookSig)) {
      new CodeEmitter(this, v, access, sig, exceptions) {
        public boolean isStaticHook() {
          return true;
        }
      };
    }
    return new CodeEmitter(this, v, access, sig, exceptions);
  }
  
  public CodeEmitter begin_static()
  {
    return begin_method(8, Constants.SIG_STATIC, null);
  }
  
  public void declare_field(int access, String name, .Type type, Object value) {
    FieldInfo existing = (FieldInfo)fieldInfo.get(name);
    FieldInfo info = new FieldInfo(access, name, type, value);
    if (existing != null) {
      if (!info.equals(existing)) {
        throw new IllegalArgumentException("Field \"" + name + "\" has been declared differently");
      }
    } else {
      fieldInfo.put(name, info);
      cv.visitField(access, name, type.getDescriptor(), null, value);
    }
  }
  
  boolean isFieldDeclared(String name)
  {
    return fieldInfo.get(name) != null;
  }
  
  FieldInfo getFieldInfo(String name) {
    FieldInfo field = (FieldInfo)fieldInfo.get(name);
    if (field == null) {
      throw new IllegalArgumentException("Field " + name + " is not declared in " + getClassType().getClassName());
    }
    return field;
  }
  
  static class FieldInfo {
    int access;
    String name;
    .Type type;
    Object value;
    
    public FieldInfo(int access, String name, .Type type, Object value) {
      this.access = access;
      this.name = name;
      this.type = type;
      this.value = value;
    }
    
    public boolean equals(Object o) {
      if (o == null)
        return false;
      if (!(o instanceof FieldInfo))
        return false;
      FieldInfo other = (FieldInfo)o;
      if ((access != access) || 
        (!name.equals(name)) || 
        (!type.equals(type))) {
        return false;
      }
      if (((value == null ? 1 : 0) ^ (value == null ? 1 : 0)) != 0)
        return false;
      if ((value != null) && (!value.equals(value)))
        return false;
      return true;
    }
    
    public int hashCode() {
      return access ^ name.hashCode() ^ type.hashCode() ^ (value == null ? 0 : value.hashCode());
    }
  }
  




  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    begin_class(version, access, name
    
      .replace('/', '.'), 
      TypeUtils.fromInternalName(superName), 
      TypeUtils.fromInternalNames(interfaces), null);
  }
  
  public void visitEnd()
  {
    end_class();
  }
  



  public .FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
  {
    declare_field(access, name, .Type.getType(desc), value);
    return null;
  }
  



  public .MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    return begin_method(access, new Signature(name, desc), 
    
      TypeUtils.fromInternalNames(exceptions));
  }
}

package net.sf.cglib.core;

import java.util.Arrays;
import net.sf.cglib.asm..Attribute;
import net.sf.cglib.asm..Label;
import net.sf.cglib.asm..MethodVisitor;
import net.sf.cglib.asm..Type;
















public class CodeEmitter
  extends LocalVariablesSorter
{
  private static final Signature BOOLEAN_VALUE = TypeUtils.parseSignature("boolean booleanValue()");
  
  private static final Signature CHAR_VALUE = TypeUtils.parseSignature("char charValue()");
  
  private static final Signature LONG_VALUE = TypeUtils.parseSignature("long longValue()");
  
  private static final Signature DOUBLE_VALUE = TypeUtils.parseSignature("double doubleValue()");
  
  private static final Signature FLOAT_VALUE = TypeUtils.parseSignature("float floatValue()");
  
  private static final Signature INT_VALUE = TypeUtils.parseSignature("int intValue()");
  
  private static final Signature CSTRUCT_NULL = TypeUtils.parseConstructor("");
  
  private static final Signature CSTRUCT_STRING = TypeUtils.parseConstructor("String");
  
  public static final int ADD = 96;
  
  public static final int MUL = 104;
  public static final int XOR = 130;
  public static final int USHR = 124;
  public static final int SUB = 100;
  public static final int DIV = 108;
  public static final int NEG = 116;
  public static final int REM = 112;
  public static final int AND = 126;
  public static final int OR = 128;
  public static final int GT = 157;
  public static final int LT = 155;
  public static final int GE = 156;
  public static final int LE = 158;
  public static final int NE = 154;
  public static final int EQ = 153;
  private ClassEmitter ce;
  private State state;
  
  private static class State
    extends MethodInfo
  {
    ClassInfo classInfo;
    int access;
    Signature sig;
    .Type[] argumentTypes;
    int localOffset;
    .Type[] exceptionTypes;
    
    State(ClassInfo classInfo, int access, Signature sig, .Type[] exceptionTypes)
    {
      this.classInfo = classInfo;
      this.access = access;
      this.sig = sig;
      this.exceptionTypes = exceptionTypes;
      localOffset = (TypeUtils.isStatic(access) ? 0 : 1);
      argumentTypes = sig.getArgumentTypes();
    }
    
    public ClassInfo getClassInfo() {
      return classInfo;
    }
    
    public int getModifiers() {
      return access;
    }
    
    public Signature getSignature() {
      return sig;
    }
    
    public .Type[] getExceptionTypes() {
      return exceptionTypes;
    }
    
    public .Attribute getAttribute()
    {
      return null;
    }
  }
  
  CodeEmitter(ClassEmitter ce, .MethodVisitor mv, int access, Signature sig, .Type[] exceptionTypes) {
    super(access, sig.getDescriptor(), mv);
    this.ce = ce;
    state = new State(ce.getClassInfo(), access, sig, exceptionTypes);
  }
  
  public CodeEmitter(CodeEmitter wrap) {
    super(wrap);
    ce = ce;
    state = state;
  }
  
  public boolean isStaticHook() {
    return false;
  }
  
  public Signature getSignature() {
    return state.sig;
  }
  
  public .Type getReturnType() {
    return state.sig.getReturnType();
  }
  
  public MethodInfo getMethodInfo() {
    return state;
  }
  
  public ClassEmitter getClassEmitter() {
    return ce;
  }
  
  public void end_method() {
    visitMaxs(0, 0);
  }
  
  public Block begin_block() {
    return new Block(this);
  }
  
  public void catch_exception(Block block, .Type exception) {
    if (block.getEnd() == null) {
      throw new IllegalStateException("end of block is unset");
    }
    mv.visitTryCatchBlock(block.getStart(), block
      .getEnd(), 
      mark(), exception
      .getInternalName());
  }
  
  public void goTo(.Label label) { mv.visitJumpInsn(167, label); }
  public void ifnull(.Label label) { mv.visitJumpInsn(198, label); }
  public void ifnonnull(.Label label) { mv.visitJumpInsn(199, label); }
  
  public void if_jump(int mode, .Label label) {
    mv.visitJumpInsn(mode, label);
  }
  
  public void if_icmp(int mode, .Label label) {
    if_cmp(.Type.INT_TYPE, mode, label);
  }
  
  public void if_cmp(.Type type, int mode, .Label label) {
    int intOp = -1;
    int jumpmode = mode;
    switch (mode) {
    case 156:  jumpmode = 155; break;
    case 158:  jumpmode = 157;
    }
    switch (type.getSort()) {
    case 7: 
      mv.visitInsn(148);
      break;
    case 8: 
      mv.visitInsn(152);
      break;
    case 6: 
      mv.visitInsn(150);
      break;
    case 9: 
    case 10: 
      switch (mode) {
      case 153: 
        mv.visitJumpInsn(165, label);
        return;
      case 154: 
        mv.visitJumpInsn(166, label);
        return;
      }
      throw new IllegalArgumentException("Bad comparison for type " + type);
    default: 
      switch (mode) {
      case 153:  intOp = 159; break;
      case 154:  intOp = 160; break;
      case 156:  swap();
      case 155:  intOp = 161; break;
      case 158:  swap();
      case 157:  intOp = 163;
      }
      mv.visitJumpInsn(intOp, label);
      return;
    }
    if_jump(jumpmode, label);
  }
  
  public void pop() { mv.visitInsn(87); }
  public void pop2() { mv.visitInsn(88); }
  public void dup() { mv.visitInsn(89); }
  public void dup2() { mv.visitInsn(92); }
  public void dup_x1() { mv.visitInsn(90); }
  public void dup_x2() { mv.visitInsn(91); }
  public void dup2_x1() { mv.visitInsn(93); }
  public void dup2_x2() { mv.visitInsn(94); }
  public void swap() { mv.visitInsn(95); }
  public void aconst_null() { mv.visitInsn(1); }
  
  public void swap(.Type prev, .Type type) {
    if (type.getSize() == 1) {
      if (prev.getSize() == 1) {
        swap();
      } else {
        dup_x2();
        pop();
      }
    }
    else if (prev.getSize() == 1) {
      dup2_x1();
      pop2();
    } else {
      dup2_x2();
      pop2();
    }
  }
  

  public void monitorenter() { mv.visitInsn(194); }
  public void monitorexit() { mv.visitInsn(195); }
  
  public void math(int op, .Type type) { mv.visitInsn(type.getOpcode(op)); }
  
  public void array_load(.Type type) { mv.visitInsn(type.getOpcode(46)); }
  public void array_store(.Type type) { mv.visitInsn(type.getOpcode(79)); }
  


  public void cast_numeric(.Type from, .Type to)
  {
    if (from != to) {
      if (from == .Type.DOUBLE_TYPE) {
        if (to == .Type.FLOAT_TYPE) {
          mv.visitInsn(144);
        } else if (to == .Type.LONG_TYPE) {
          mv.visitInsn(143);
        } else {
          mv.visitInsn(142);
          cast_numeric(.Type.INT_TYPE, to);
        }
      } else if (from == .Type.FLOAT_TYPE) {
        if (to == .Type.DOUBLE_TYPE) {
          mv.visitInsn(141);
        } else if (to == .Type.LONG_TYPE) {
          mv.visitInsn(140);
        } else {
          mv.visitInsn(139);
          cast_numeric(.Type.INT_TYPE, to);
        }
      } else if (from == .Type.LONG_TYPE) {
        if (to == .Type.DOUBLE_TYPE) {
          mv.visitInsn(138);
        } else if (to == .Type.FLOAT_TYPE) {
          mv.visitInsn(137);
        } else {
          mv.visitInsn(136);
          cast_numeric(.Type.INT_TYPE, to);
        }
      }
      else if (to == .Type.BYTE_TYPE) {
        mv.visitInsn(145);
      } else if (to == .Type.CHAR_TYPE) {
        mv.visitInsn(146);
      } else if (to == .Type.DOUBLE_TYPE) {
        mv.visitInsn(135);
      } else if (to == .Type.FLOAT_TYPE) {
        mv.visitInsn(134);
      } else if (to == .Type.LONG_TYPE) {
        mv.visitInsn(133);
      } else if (to == .Type.SHORT_TYPE) {
        mv.visitInsn(147);
      }
    }
  }
  
  public void push(int i)
  {
    if (i < -1) {
      mv.visitLdcInsn(new Integer(i));
    } else if (i <= 5) {
      mv.visitInsn(TypeUtils.ICONST(i));
    } else if (i <= 127) {
      mv.visitIntInsn(16, i);
    } else if (i <= 32767) {
      mv.visitIntInsn(17, i);
    } else {
      mv.visitLdcInsn(new Integer(i));
    }
  }
  
  public void push(long value) {
    if ((value == 0L) || (value == 1L)) {
      mv.visitInsn(TypeUtils.LCONST(value));
    } else {
      mv.visitLdcInsn(new Long(value));
    }
  }
  
  public void push(float value) {
    if ((value == 0.0F) || (value == 1.0F) || (value == 2.0F)) {
      mv.visitInsn(TypeUtils.FCONST(value));
    } else
      mv.visitLdcInsn(new Float(value));
  }
  
  public void push(double value) {
    if ((value == 0.0D) || (value == 1.0D)) {
      mv.visitInsn(TypeUtils.DCONST(value));
    } else {
      mv.visitLdcInsn(new Double(value));
    }
  }
  
  public void push(String value) {
    mv.visitLdcInsn(value);
  }
  
  public void newarray() {
    newarray(Constants.TYPE_OBJECT);
  }
  
  public void newarray(.Type type) {
    if (TypeUtils.isPrimitive(type)) {
      mv.visitIntInsn(188, TypeUtils.NEWARRAY(type));
    } else {
      emit_type(189, type);
    }
  }
  
  public void arraylength() {
    mv.visitInsn(190);
  }
  
  public void load_this() {
    if (TypeUtils.isStatic(state.access)) {
      throw new IllegalStateException("no 'this' pointer within static method");
    }
    mv.visitVarInsn(25, 0);
  }
  


  public void load_args()
  {
    load_args(0, state.argumentTypes.length);
  }
  



  public void load_arg(int index)
  {
    load_local(state.argumentTypes[index], state.localOffset + 
      skipArgs(index));
  }
  
  public void load_args(int fromArg, int count)
  {
    int pos = state.localOffset + skipArgs(fromArg);
    for (int i = 0; i < count; i++) {
      .Type t = state.argumentTypes[(fromArg + i)];
      load_local(t, pos);
      pos += t.getSize();
    }
  }
  
  private int skipArgs(int numArgs) {
    int amount = 0;
    for (int i = 0; i < numArgs; i++) {
      amount += state.argumentTypes[i].getSize();
    }
    return amount;
  }
  
  private void load_local(.Type t, int pos)
  {
    mv.visitVarInsn(t.getOpcode(21), pos);
  }
  
  private void store_local(.Type t, int pos)
  {
    mv.visitVarInsn(t.getOpcode(54), pos);
  }
  
  public void iinc(Local local, int amount) {
    mv.visitIincInsn(local.getIndex(), amount);
  }
  
  public void store_local(Local local) {
    store_local(local.getType(), local.getIndex());
  }
  
  public void load_local(Local local) {
    load_local(local.getType(), local.getIndex());
  }
  
  public void return_value() {
    mv.visitInsn(state.sig.getReturnType().getOpcode(172));
  }
  
  public void getfield(String name) {
    ClassEmitter.FieldInfo info = ce.getFieldInfo(name);
    int opcode = TypeUtils.isStatic(access) ? 178 : 180;
    emit_field(opcode, ce.getClassType(), name, type);
  }
  
  public void putfield(String name) {
    ClassEmitter.FieldInfo info = ce.getFieldInfo(name);
    int opcode = TypeUtils.isStatic(access) ? 179 : 181;
    emit_field(opcode, ce.getClassType(), name, type);
  }
  
  public void super_getfield(String name, .Type type) {
    emit_field(180, ce.getSuperType(), name, type);
  }
  
  public void super_putfield(String name, .Type type) {
    emit_field(181, ce.getSuperType(), name, type);
  }
  
  public void super_getstatic(String name, .Type type) {
    emit_field(178, ce.getSuperType(), name, type);
  }
  
  public void super_putstatic(String name, .Type type) {
    emit_field(179, ce.getSuperType(), name, type);
  }
  
  public void getfield(.Type owner, String name, .Type type) {
    emit_field(180, owner, name, type);
  }
  
  public void putfield(.Type owner, String name, .Type type) {
    emit_field(181, owner, name, type);
  }
  
  public void getstatic(.Type owner, String name, .Type type) {
    emit_field(178, owner, name, type);
  }
  
  public void putstatic(.Type owner, String name, .Type type) {
    emit_field(179, owner, name, type);
  }
  
  void emit_field(int opcode, .Type ctype, String name, .Type ftype)
  {
    mv.visitFieldInsn(opcode, ctype
      .getInternalName(), name, ftype
      
      .getDescriptor());
  }
  
  public void super_invoke() {
    super_invoke(state.sig);
  }
  
  public void super_invoke(Signature sig) {
    emit_invoke(183, ce.getSuperType(), sig);
  }
  
  public void invoke_constructor(.Type type) {
    invoke_constructor(type, CSTRUCT_NULL);
  }
  
  public void super_invoke_constructor() {
    invoke_constructor(ce.getSuperType());
  }
  
  public void invoke_constructor_this() {
    invoke_constructor(ce.getClassType());
  }
  




  private void emit_invoke(int opcode, .Type type, Signature sig)
  {
    mv.visitMethodInsn(opcode, type
      .getInternalName(), sig
      .getName(), sig
      .getDescriptor(), ((!sig.getName().equals("<init>")) || (opcode == 182) || (opcode != 184)) || (opcode == 185));
  }
  
  public void invoke_interface(.Type owner, Signature sig)
  {
    emit_invoke(185, owner, sig);
  }
  
  public void invoke_virtual(.Type owner, Signature sig) {
    emit_invoke(182, owner, sig);
  }
  
  public void invoke_static(.Type owner, Signature sig) {
    emit_invoke(184, owner, sig);
  }
  
  public void invoke_virtual_this(Signature sig) {
    invoke_virtual(ce.getClassType(), sig);
  }
  
  public void invoke_static_this(Signature sig) {
    invoke_static(ce.getClassType(), sig);
  }
  
  public void invoke_constructor(.Type type, Signature sig) {
    emit_invoke(183, type, sig);
  }
  
  public void invoke_constructor_this(Signature sig) {
    invoke_constructor(ce.getClassType(), sig);
  }
  
  public void super_invoke_constructor(Signature sig) {
    invoke_constructor(ce.getSuperType(), sig);
  }
  
  public void new_instance_this() {
    new_instance(ce.getClassType());
  }
  

  public void new_instance(.Type type) { emit_type(187, type); }
  
  private void emit_type(int opcode, .Type type) {
    String desc;
    String desc;
    if (TypeUtils.isArray(type)) {
      desc = type.getDescriptor();
    } else {
      desc = type.getInternalName();
    }
    mv.visitTypeInsn(opcode, desc);
  }
  
  public void aaload(int index) {
    push(index);
    aaload();
  }
  
  public void aaload() { mv.visitInsn(50); }
  public void aastore() { mv.visitInsn(83); }
  public void athrow() { mv.visitInsn(191); }
  
  public .Label make_label() {
    return new .Label();
  }
  
  public Local make_local() {
    return make_local(Constants.TYPE_OBJECT);
  }
  
  public Local make_local(.Type type) {
    return new Local(newLocal(type.getSize()), type);
  }
  
  public void checkcast_this() {
    checkcast(ce.getClassType());
  }
  
  public void checkcast(.Type type) {
    if (!type.equals(Constants.TYPE_OBJECT)) {
      emit_type(192, type);
    }
  }
  
  public void instance_of(.Type type) {
    emit_type(193, type);
  }
  

  public void instance_of_this() { instance_of(ce.getClassType()); }
  
  public void process_switch(int[] keys, ProcessSwitchCallback callback) {
    float density;
    float density;
    if (keys.length == 0) {
      density = 0.0F;
    } else {
      density = keys.length / (keys[(keys.length - 1)] - keys[0] + 1);
    }
    process_switch(keys, callback, density >= 0.5F);
  }
  
  public void process_switch(int[] keys, ProcessSwitchCallback callback, boolean useTable) {
    if (!isSorted(keys))
      throw new IllegalArgumentException("keys to switch must be sorted ascending");
    .Label def = make_label();
    .Label end = make_label();
    try
    {
      if (keys.length > 0) {
        int len = keys.length;
        int min = keys[0];
        int max = keys[(len - 1)];
        int range = max - min + 1;
        
        if (useTable) {
          .Label[] labels = new .Label[range];
          Arrays.fill(labels, def);
          for (int i = 0; i < len; i++) {
            labels[(keys[i] - min)] = make_label();
          }
          mv.visitTableSwitchInsn(min, max, def, labels);
          for (int i = 0; i < range; i++) {
            .Label label = labels[i];
            if (label != def) {
              mark(label);
              callback.processCase(i + min, end);
            }
          }
        } else {
          .Label[] labels = new .Label[len];
          for (int i = 0; i < len; i++) {
            labels[i] = make_label();
          }
          mv.visitLookupSwitchInsn(def, keys, labels);
          for (int i = 0; i < len; i++) {
            mark(labels[i]);
            callback.processCase(keys[i], end);
          }
        }
      }
      
      mark(def);
      callback.processDefault();
      mark(end);
    }
    catch (RuntimeException e) {
      throw e;
    } catch (Error e) {
      throw e;
    } catch (Exception e) {
      throw new CodeGenerationException(e);
    }
  }
  
  private static boolean isSorted(int[] keys) {
    for (int i = 1; i < keys.length; i++) {
      if (keys[i] < keys[(i - 1)])
        return false;
    }
    return true;
  }
  
  public void mark(.Label label) {
    mv.visitLabel(label);
  }
  
  .Label mark() {
    .Label label = make_label();
    mv.visitLabel(label);
    return label;
  }
  
  public void push(boolean value) {
    push(value ? 1 : 0);
  }
  


  public void not()
  {
    push(1);
    math(130, .Type.INT_TYPE);
  }
  
  public void throw_exception(.Type type, String msg) {
    new_instance(type);
    dup();
    push(msg);
    invoke_constructor(type, CSTRUCT_STRING);
    athrow();
  }
  






  public void box(.Type type)
  {
    if (TypeUtils.isPrimitive(type)) {
      if (type == .Type.VOID_TYPE) {
        aconst_null();
      } else {
        .Type boxed = TypeUtils.getBoxedType(type);
        new_instance(boxed);
        if (type.getSize() == 2)
        {
          dup_x2();
          dup_x2();
          pop();
        }
        else {
          dup_x1();
          swap();
        }
        invoke_constructor(boxed, new Signature("<init>", .Type.VOID_TYPE, new .Type[] { type }));
      }
    }
  }
  






  public void unbox(.Type type)
  {
    .Type t = Constants.TYPE_NUMBER;
    Signature sig = null;
    switch (type.getSort()) {
    case 0: 
      return;
    case 2: 
      t = Constants.TYPE_CHARACTER;
      sig = CHAR_VALUE;
      break;
    case 1: 
      t = Constants.TYPE_BOOLEAN;
      sig = BOOLEAN_VALUE;
      break;
    case 8: 
      sig = DOUBLE_VALUE;
      break;
    case 6: 
      sig = FLOAT_VALUE;
      break;
    case 7: 
      sig = LONG_VALUE;
      break;
    case 3: 
    case 4: 
    case 5: 
      sig = INT_VALUE;
    }
    
    if (sig == null) {
      checkcast(type);
    } else {
      checkcast(t);
      invoke_virtual(t, sig);
    }
  }
  








  public void create_arg_array()
  {
    push(state.argumentTypes.length);
    newarray();
    for (int i = 0; i < state.argumentTypes.length; i++) {
      dup();
      push(i);
      load_arg(i);
      box(state.argumentTypes[i]);
      aastore();
    }
  }
  



  public void zero_or_null(.Type type)
  {
    if (TypeUtils.isPrimitive(type)) {
      switch (type.getSort()) {
      case 8: 
        push(0.0D);
        break;
      case 7: 
        push(0L);
        break;
      case 6: 
        push(0.0F);
        break;
      case 0: 
        aconst_null();
      case 1: case 2: case 3: case 4: case 5: default: 
        push(0);break;
      }
    } else {
      aconst_null();
    }
  }
  



  public void unbox_or_zero(.Type type)
  {
    if (TypeUtils.isPrimitive(type)) {
      if (type != .Type.VOID_TYPE) {
        .Label nonNull = make_label();
        .Label end = make_label();
        dup();
        ifnonnull(nonNull);
        pop();
        zero_or_null(type);
        goTo(end);
        mark(nonNull);
        unbox(type);
        mark(end);
      }
    } else {
      checkcast(type);
    }
  }
  
  public void visitMaxs(int maxStack, int maxLocals) {
    if (!TypeUtils.isAbstract(state.access)) {
      mv.visitMaxs(0, 0);
    }
  }
  
  public void invoke(MethodInfo method, .Type virtualType) {
    ClassInfo classInfo = method.getClassInfo();
    .Type type = classInfo.getType();
    Signature sig = method.getSignature();
    if (sig.getName().equals("<init>")) {
      invoke_constructor(type, sig);
    } else if (TypeUtils.isInterface(classInfo.getModifiers())) {
      invoke_interface(type, sig);
    } else if (TypeUtils.isStatic(method.getModifiers())) {
      invoke_static(type, sig);
    } else {
      invoke_virtual(virtualType, sig);
    }
  }
  
  public void invoke(MethodInfo method) {
    invoke(method, method.getClassInfo().getType());
  }
}

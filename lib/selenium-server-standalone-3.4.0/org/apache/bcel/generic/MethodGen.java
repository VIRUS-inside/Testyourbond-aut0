package org.apache.bcel.generic;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;
import org.apache.bcel.classfile.AccessFlags;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.Code;
import org.apache.bcel.classfile.CodeException;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ExceptionTable;
import org.apache.bcel.classfile.FieldOrMethod;
import org.apache.bcel.classfile.LineNumber;
import org.apache.bcel.classfile.LineNumberTable;
import org.apache.bcel.classfile.LocalVariable;
import org.apache.bcel.classfile.LocalVariableTable;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.classfile.Utility;






















































public class MethodGen
  extends FieldGenOrMethodGen
{
  private String class_name;
  private Type[] arg_types;
  private String[] arg_names;
  private int max_locals;
  private int max_stack;
  private InstructionList il;
  private boolean strip_attributes;
  private ArrayList variable_vec = new ArrayList();
  private ArrayList line_number_vec = new ArrayList();
  private ArrayList exception_vec = new ArrayList();
  private ArrayList throws_vec = new ArrayList();
  private ArrayList code_attrs_vec = new ArrayList();
  










  private ArrayList observers;
  











  public MethodGen(int access_flags, Type return_type, Type[] arg_types, String[] arg_names, String method_name, String class_name, InstructionList il, ConstantPoolGen cp)
  {
    setAccessFlags(access_flags);
    setType(return_type);
    setArgumentTypes(arg_types);
    setArgumentNames(arg_names);
    setName(method_name);
    setClassName(class_name);
    setInstructionList(il);
    setConstantPool(cp);
    
    if ((access_flags & 0x500) == 0) {
      InstructionHandle start = il.getStart();
      InstructionHandle end = il.getEnd();
      


      if ((!isStatic()) && (class_name != null)) {
        addLocalVariable("this", new ObjectType(class_name), start, end);
      }
      if (arg_types != null) {
        int size = arg_types.length;
        
        if (arg_names != null) {
          if (size != arg_names.length) {
            throw new ClassGenException("Mismatch in argument array lengths: " + size + " vs. " + arg_names.length);
          }
        } else {
          arg_names = new String[size];
          
          for (int i = 0; i < size; i++) {
            arg_names[i] = ("arg" + i);
          }
          setArgumentNames(arg_names);
        }
        
        for (int i = 0; i < size; i++) {
          addLocalVariable(arg_names[i], arg_types[i], start, end);
        }
      }
    }
  }
  





  public MethodGen(Method m, String class_name, ConstantPoolGen cp)
  {
    this(m.getAccessFlags(), Type.getReturnType(m.getSignature()), Type.getArgumentTypes(m.getSignature()), null, m.getName(), class_name, (m.getAccessFlags() & 0x500) == 0 ? new InstructionList(m.getCode().getCode()) : null, cp);
    





    Attribute[] attributes = m.getAttributes();
    for (int i = 0; i < attributes.length; i++) {
      Attribute a = attributes[i];
      
      if ((a instanceof Code)) {
        Code c = (Code)a;
        setMaxStack(c.getMaxStack());
        setMaxLocals(c.getMaxLocals());
        
        CodeException[] ces = c.getExceptionTable();
        
        if (ces != null) {
          for (int j = 0; j < ces.length; j++) {
            CodeException ce = ces[j];
            int type = ce.getCatchType();
            ObjectType c_type = null;
            
            if (type > 0) {
              String cen = m.getConstantPool().getConstantString(type, (byte)7);
              c_type = new ObjectType(cen);
            }
            
            int end_pc = ce.getEndPC();
            int length = m.getCode().getCode().length;
            
            InstructionHandle end;
            
            if (length == end_pc) {
              end = il.getEnd();
            } else {
              end = il.findHandle(end_pc);
              end = end.getPrev();
            }
            
            addExceptionHandler(il.findHandle(ce.getStartPC()), end, il.findHandle(ce.getHandlerPC()), c_type);
          }
        }
        

        Attribute[] c_attributes = c.getAttributes();
        for (int j = 0; j < c_attributes.length; j++) {
          a = c_attributes[j];
          
          if ((a instanceof LineNumberTable)) {
            LineNumber[] ln = ((LineNumberTable)a).getLineNumberTable();
            for (int k = 0; k < ln.length; k++) {
              LineNumber l = ln[k];
              addLineNumber(il.findHandle(l.getStartPC()), l.getLineNumber());
            }
          } else if ((a instanceof LocalVariableTable)) {
            LocalVariable[] lv = ((LocalVariableTable)a).getLocalVariableTable();
            for (int k = 0; k < lv.length; k++) {
              LocalVariable l = lv[k];
              InstructionHandle start = il.findHandle(l.getStartPC());
              InstructionHandle end = il.findHandle(l.getStartPC() + l.getLength());
              

              if (start == null)
                start = il.getStart();
              if (end == null) {
                end = il.getEnd();
              }
              addLocalVariable(l.getName(), Type.getType(l.getSignature()), l.getIndex(), start, end);
            }
          }
          else {
            addCodeAttribute(a);
          }
        } } else if ((a instanceof ExceptionTable)) {
        String[] names = ((ExceptionTable)a).getExceptionNames();
        for (int j = 0; j < names.length; j++)
          addException(names[j]);
      } else {
        addAttribute(a);
      }
    }
  }
  












  public LocalVariableGen addLocalVariable(String name, Type type, int slot, InstructionHandle start, InstructionHandle end)
  {
    byte t = type.getType();
    int add = type.getSize();
    
    if (slot + add > max_locals) {
      max_locals = (slot + add);
    }
    LocalVariableGen l = new LocalVariableGen(slot, name, type, start, end);
    
    int i;
    if ((i = variable_vec.indexOf(l)) >= 0) {
      variable_vec.set(i, l);
    } else
      variable_vec.add(l);
    return l;
  }
  













  public LocalVariableGen addLocalVariable(String name, Type type, InstructionHandle start, InstructionHandle end)
  {
    return addLocalVariable(name, type, max_locals, start, end);
  }
  



  public void removeLocalVariable(LocalVariableGen l)
  {
    variable_vec.remove(l);
  }
  


  public void removeLocalVariables()
  {
    variable_vec.clear();
  }
  


  private static final void sort(LocalVariableGen[] vars, int l, int r)
  {
    int i = l;int j = r;
    int m = vars[((l + r) / 2)].getIndex();
    
    do
    {
      while (vars[i].getIndex() < m) i++;
      while (m < vars[j].getIndex()) { j--;
      }
      if (i <= j) {
        LocalVariableGen h = vars[i];vars[i] = vars[j];vars[j] = h;
        i++;j--;
      }
    } while (i <= j);
    
    if (l < j) sort(vars, l, j);
    if (i < r) { sort(vars, i, r);
    }
  }
  




  public LocalVariableGen[] getLocalVariables()
  {
    int size = variable_vec.size();
    LocalVariableGen[] lg = new LocalVariableGen[size];
    variable_vec.toArray(lg);
    
    for (int i = 0; i < size; i++) {
      if (lg[i].getStart() == null) {
        lg[i].setStart(il.getStart());
      }
      if (lg[i].getEnd() == null) {
        lg[i].setEnd(il.getEnd());
      }
    }
    if (size > 1) {
      sort(lg, 0, size - 1);
    }
    return lg;
  }
  


  public LocalVariableTable getLocalVariableTable(ConstantPoolGen cp)
  {
    LocalVariableGen[] lg = getLocalVariables();
    int size = lg.length;
    LocalVariable[] lv = new LocalVariable[size];
    
    for (int i = 0; i < size; i++) {
      lv[i] = lg[i].getLocalVariable(cp);
    }
    return new LocalVariableTable(cp.addUtf8("LocalVariableTable"), 2 + lv.length * 10, lv, cp.getConstantPool());
  }
  







  public LineNumberGen addLineNumber(InstructionHandle ih, int src_line)
  {
    LineNumberGen l = new LineNumberGen(ih, src_line);
    line_number_vec.add(l);
    return l;
  }
  


  public void removeLineNumber(LineNumberGen l)
  {
    line_number_vec.remove(l);
  }
  


  public void removeLineNumbers()
  {
    line_number_vec.clear();
  }
  


  public LineNumberGen[] getLineNumbers()
  {
    LineNumberGen[] lg = new LineNumberGen[line_number_vec.size()];
    line_number_vec.toArray(lg);
    return lg;
  }
  


  public LineNumberTable getLineNumberTable(ConstantPoolGen cp)
  {
    int size = line_number_vec.size();
    LineNumber[] ln = new LineNumber[size];
    try
    {
      for (int i = 0; i < size; i++) {
        ln[i] = ((LineNumberGen)line_number_vec.get(i)).getLineNumber();
      }
    } catch (ArrayIndexOutOfBoundsException e) {}
    return new LineNumberTable(cp.addUtf8("LineNumberTable"), 2 + ln.length * 4, ln, cp.getConstantPool());
  }
  














  public CodeExceptionGen addExceptionHandler(InstructionHandle start_pc, InstructionHandle end_pc, InstructionHandle handler_pc, ObjectType catch_type)
  {
    if ((start_pc == null) || (end_pc == null) || (handler_pc == null)) {
      throw new ClassGenException("Exception handler target is null instruction");
    }
    CodeExceptionGen c = new CodeExceptionGen(start_pc, end_pc, handler_pc, catch_type);
    
    exception_vec.add(c);
    return c;
  }
  


  public void removeExceptionHandler(CodeExceptionGen c)
  {
    exception_vec.remove(c);
  }
  


  public void removeExceptionHandlers()
  {
    exception_vec.clear();
  }
  


  public CodeExceptionGen[] getExceptionHandlers()
  {
    CodeExceptionGen[] cg = new CodeExceptionGen[exception_vec.size()];
    exception_vec.toArray(cg);
    return cg;
  }
  


  private CodeException[] getCodeExceptions()
  {
    int size = exception_vec.size();
    CodeException[] c_exc = new CodeException[size];
    try
    {
      for (int i = 0; i < size; i++) {
        CodeExceptionGen c = (CodeExceptionGen)exception_vec.get(i);
        c_exc[i] = c.getCodeException(cp);
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {}
    return c_exc;
  }
  




  public void addException(String class_name)
  {
    throws_vec.add(class_name);
  }
  


  public void removeException(String c)
  {
    throws_vec.remove(c);
  }
  


  public void removeExceptions()
  {
    throws_vec.clear();
  }
  


  public String[] getExceptions()
  {
    String[] e = new String[throws_vec.size()];
    throws_vec.toArray(e);
    return e;
  }
  


  private ExceptionTable getExceptionTable(ConstantPoolGen cp)
  {
    int size = throws_vec.size();
    int[] ex = new int[size];
    try
    {
      for (int i = 0; i < size; i++) {
        ex[i] = cp.addClass((String)throws_vec.get(i));
      }
    } catch (ArrayIndexOutOfBoundsException e) {}
    return new ExceptionTable(cp.addUtf8("Exceptions"), 2 + 2 * size, ex, cp.getConstantPool());
  }
  








  public void addCodeAttribute(Attribute a)
  {
    code_attrs_vec.add(a);
  }
  
  public void removeCodeAttribute(Attribute a)
  {
    code_attrs_vec.remove(a);
  }
  

  public void removeCodeAttributes()
  {
    code_attrs_vec.clear();
  }
  


  public Attribute[] getCodeAttributes()
  {
    Attribute[] attributes = new Attribute[code_attrs_vec.size()];
    code_attrs_vec.toArray(attributes);
    return attributes;
  }
  





  public Method getMethod()
  {
    String signature = getSignature();
    int name_index = cp.addUtf8(name);
    int signature_index = cp.addUtf8(signature);
    


    byte[] byte_code = null;
    
    if (il != null) {
      byte_code = il.getByteCode();
    }
    
    LineNumberTable lnt = null;
    LocalVariableTable lvt = null;
    


    if ((variable_vec.size() > 0) && (!strip_attributes)) {
      addCodeAttribute(lvt = getLocalVariableTable(cp));
    }
    if ((line_number_vec.size() > 0) && (!strip_attributes)) {
      addCodeAttribute(lnt = getLineNumberTable(cp));
    }
    Attribute[] code_attrs = getCodeAttributes();
    


    int attrs_len = 0;
    for (int i = 0; i < code_attrs.length; i++) {
      attrs_len += code_attrs[i].getLength() + 6;
    }
    CodeException[] c_exc = getCodeExceptions();
    int exc_len = c_exc.length * 8;
    
    Code code = null;
    if ((il != null) && (!isAbstract())) {
      code = new Code(cp.addUtf8("Code"), 8 + byte_code.length + 2 + exc_len + 2 + attrs_len, max_stack, max_locals, byte_code, c_exc, code_attrs, cp.getConstantPool());
      







      addAttribute(code);
    }
    
    ExceptionTable et = null;
    
    if (throws_vec.size() > 0) {
      addAttribute(et = getExceptionTable(cp));
    }
    Method m = new Method(access_flags, name_index, signature_index, getAttributes(), cp.getConstantPool());
    


    if (lvt != null) removeCodeAttribute(lvt);
    if (lnt != null) removeCodeAttribute(lnt);
    if (code != null) removeAttribute(code);
    if (et != null) { removeAttribute(et);
    }
    return m;
  }
  




  public void removeNOPs()
  {
    if (il != null)
    {
      InstructionHandle next;
      
      for (InstructionHandle ih = il.getStart(); ih != null; ih = next) {
        next = next;
        
        if ((next != null) && ((ih.getInstruction() instanceof NOP))) { InstructionHandle[] targets;
          int i;
          try { il.delete(ih);
          } catch (TargetLostException e) {
            targets = e.getTargets();
            
            i = 0; } for (; i < targets.length; i++) {
            InstructionTargeter[] targeters = targets[i].getTargeters();
            
            for (int j = 0; j < targeters.length; j++) {
              targeters[j].updateTarget(targets[i], next);
            }
          }
        }
      }
    }
  }
  



  public void setMaxLocals(int m) { max_locals = m; }
  public int getMaxLocals() { return max_locals; }
  



  public void setMaxStack(int m) { max_stack = m; }
  public int getMaxStack() { return max_stack; }
  


  public String getClassName() { return class_name; }
  public void setClassName(String class_name) { this.class_name = class_name; }
  
  public void setReturnType(Type return_type) { setType(return_type); }
  public Type getReturnType() { return getType(); }
  
  public void setArgumentTypes(Type[] arg_types) { this.arg_types = arg_types; }
  public Type[] getArgumentTypes() { return (Type[])arg_types.clone(); }
  public void setArgumentType(int i, Type type) { arg_types[i] = type; }
  public Type getArgumentType(int i) { return arg_types[i]; }
  
  public void setArgumentNames(String[] arg_names) { this.arg_names = arg_names; }
  public String[] getArgumentNames() { return (String[])arg_names.clone(); }
  public void setArgumentName(int i, String name) { arg_names[i] = name; }
  public String getArgumentName(int i) { return arg_names[i]; }
  
  public InstructionList getInstructionList() { return il; }
  public void setInstructionList(InstructionList il) { this.il = il; }
  
  public String getSignature() {
    return Type.getMethodSignature(type, arg_types);
  }
  



  public void setMaxStack()
  {
    if (il != null) {
      max_stack = getMaxStack(cp, il, getExceptionHandlers());
    } else {
      max_stack = 0;
    }
  }
  

  public void setMaxLocals()
  {
    if (il != null) {
      int max = isStatic() ? 0 : 1;
      
      if (arg_types != null) {
        for (int i = 0; i < arg_types.length; i++)
          max += arg_types[i].getSize();
      }
      for (InstructionHandle ih = il.getStart(); ih != null; ih = ih.getNext()) {
        Instruction ins = ih.getInstruction();
        
        if (((ins instanceof LocalVariableInstruction)) || ((ins instanceof RET)) || ((ins instanceof IINC)))
        {

          int index = ((IndexedInstruction)ins).getIndex() + ((TypedInstruction)ins).getType(cp).getSize();
          

          if (index > max) {
            max = index;
          }
        }
      }
      max_locals = max;
    } else {
      max_locals = 0;
    }
  }
  


  public void stripAttributes(boolean flag) { strip_attributes = flag; }
  
  static final class BranchTarget {
    InstructionHandle target;
    int stackDepth;
    
    BranchTarget(InstructionHandle target, int stackDepth) {
      this.target = target;
      this.stackDepth = stackDepth;
    } }
  
  static final class BranchStack { BranchStack() {}
    
    Stack branchTargets = new Stack();
    Hashtable visitedTargets = new Hashtable();
    
    public void push(InstructionHandle target, int stackDepth) {
      if (visited(target)) {
        return;
      }
      branchTargets.push(visit(target, stackDepth));
    }
    
    public MethodGen.BranchTarget pop() {
      if (!branchTargets.empty()) {
        MethodGen.BranchTarget bt = (MethodGen.BranchTarget)branchTargets.pop();
        return bt;
      }
      
      return null;
    }
    
    private final MethodGen.BranchTarget visit(InstructionHandle target, int stackDepth) {
      MethodGen.BranchTarget bt = new MethodGen.BranchTarget(target, stackDepth);
      visitedTargets.put(target, bt);
      
      return bt;
    }
    
    private final boolean visited(InstructionHandle target) {
      return visitedTargets.get(target) != null;
    }
  }
  




  public static int getMaxStack(ConstantPoolGen cp, InstructionList il, CodeExceptionGen[] et)
  {
    BranchStack branchTargets = new BranchStack();
    





    for (int i = 0; i < et.length; i++) {
      InstructionHandle handler_pc = et[i].getHandlerPC();
      if (handler_pc != null) {
        branchTargets.push(handler_pc, 1);
      }
    }
    int stackDepth = 0;int maxStackDepth = 0;
    InstructionHandle ih = il.getStart();
    
    while (ih != null) {
      Instruction instruction = ih.getInstruction();
      short opcode = instruction.getOpcode();
      int delta = instruction.produceStack(cp) - instruction.consumeStack(cp);
      
      stackDepth += delta;
      if (stackDepth > maxStackDepth) {
        maxStackDepth = stackDepth;
      }
      
      if ((instruction instanceof BranchInstruction)) {
        BranchInstruction branch = (BranchInstruction)instruction;
        if ((instruction instanceof Select))
        {
          Select select = (Select)branch;
          InstructionHandle[] targets = select.getTargets();
          for (int i = 0; i < targets.length; i++) {
            branchTargets.push(targets[i], stackDepth);
          }
          ih = null;
        } else if (!(branch instanceof IfInstruction))
        {

          if ((opcode == 168) || (opcode == 201))
            branchTargets.push(ih.getNext(), stackDepth - 1);
          ih = null;
        }
        


        branchTargets.push(branch.getTarget(), stackDepth);

      }
      else if ((opcode == 191) || (opcode == 169) || ((opcode >= 172) && (opcode <= 177)))
      {
        ih = null;
      }
      
      if (ih != null) {
        ih = ih.getNext();
      }
      if (ih == null) {
        BranchTarget bt = branchTargets.pop();
        if (bt != null) {
          ih = target;
          stackDepth = stackDepth;
        }
      }
    }
    
    return maxStackDepth;
  }
  



  public void addObserver(MethodObserver o)
  {
    if (observers == null) {
      observers = new ArrayList();
    }
    observers.add(o);
  }
  

  public void removeObserver(MethodObserver o)
  {
    if (observers != null) {
      observers.remove(o);
    }
  }
  


  public void update()
  {
    if (observers != null) {
      for (Iterator e = observers.iterator(); e.hasNext();) {
        ((MethodObserver)e.next()).notify(this);
      }
    }
  }
  



  public final String toString()
  {
    String access = Utility.accessToString(access_flags);
    String signature = Type.getMethodSignature(type, arg_types);
    
    signature = Utility.methodSignatureToString(signature, name, access, true, getLocalVariableTable(cp));
    

    StringBuffer buf = new StringBuffer(signature);
    
    if (throws_vec.size() > 0) {
      for (Iterator e = throws_vec.iterator(); e.hasNext();) {
        buf.append("\n\t\tthrows " + e.next());
      }
    }
    return buf.toString();
  }
  

  public MethodGen copy(String class_name, ConstantPoolGen cp)
  {
    Method m = ((MethodGen)clone()).getMethod();
    MethodGen mg = new MethodGen(m, class_name, this.cp);
    
    if (this.cp != cp) {
      mg.setConstantPool(cp);
      mg.getInstructionList().replaceConstantPool(this.cp, cp);
    }
    
    return mg;
  }
}

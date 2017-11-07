package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.DLOAD;
import org.apache.bcel.generic.DSTORE;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;























public final class RealType
  extends NumberType
{
  protected RealType() {}
  
  public String toString()
  {
    return "real";
  }
  
  public boolean identicalTo(Type other) {
    return this == other;
  }
  
  public String toSignature() {
    return "D";
  }
  
  public org.apache.bcel.generic.Type toJCType() {
    return org.apache.bcel.generic.Type.DOUBLE;
  }
  


  public int distanceTo(Type type)
  {
    if (type == this) {
      return 0;
    }
    if (type == Type.Int) {
      return 1;
    }
    
    return Integer.MAX_VALUE;
  }
  







  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
  {
    if (type == Type.String) {
      translateTo(classGen, methodGen, (StringType)type);
    }
    else if (type == Type.Boolean) {
      translateTo(classGen, methodGen, (BooleanType)type);
    }
    else if (type == Type.Reference) {
      translateTo(classGen, methodGen, (ReferenceType)type);
    }
    else if (type == Type.Int) {
      translateTo(classGen, methodGen, (IntType)type);
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
      
      classGen.getParser().reportError(2, err);
    }
  }
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    il.append(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "realToString", "(D)Ljava/lang/String;")));
  }
  








  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
  {
    InstructionList il = methodGen.getInstructionList();
    FlowList falsel = translateToDesynthesized(classGen, methodGen, type);
    il.append(ICONST_1);
    BranchHandle truec = il.append(new GOTO(null));
    falsel.backPatch(il.append(ICONST_0));
    truec.setTarget(il.append(NOP));
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, IntType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    il.append(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "realToInt", "(D)I")));
  }
  










  public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
  {
    FlowList flowlist = new FlowList();
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    il.append(DUP2);
    LocalVariableGen local = methodGen.addLocalVariable("real_to_boolean_tmp", org.apache.bcel.generic.Type.DOUBLE, null, null);
    

    local.setStart(il.append(new DSTORE(local.getIndex())));
    

    il.append(DCONST_0);
    il.append(DCMPG);
    flowlist.add(il.append(new IFEQ(null)));
    


    il.append(new DLOAD(local.getIndex()));
    local.setEnd(il.append(new DLOAD(local.getIndex())));
    il.append(DCMPG);
    flowlist.add(il.append(new IFNE(null)));
    return flowlist;
  }
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    il.append(new NEW(cpg.addClass("java.lang.Double")));
    il.append(DUP_X2);
    il.append(DUP_X2);
    il.append(POP);
    il.append(new INVOKESPECIAL(cpg.addMethodref("java.lang.Double", "<init>", "(D)V")));
  }
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    InstructionList il = methodGen.getInstructionList();
    if (clazz == Character.TYPE) {
      il.append(D2I);
      il.append(I2C);
    }
    else if (clazz == Byte.TYPE) {
      il.append(D2I);
      il.append(I2B);
    }
    else if (clazz == Short.TYPE) {
      il.append(D2I);
      il.append(I2S);
    }
    else if (clazz == Integer.TYPE) {
      il.append(D2I);
    }
    else if (clazz == Long.TYPE) {
      il.append(D2L);
    }
    else if (clazz == Float.TYPE) {
      il.append(D2F);
    }
    else if (clazz == Double.TYPE) {
      il.append(NOP);

    }
    else if (clazz.isAssignableFrom(Double.class)) {
      translateTo(classGen, methodGen, Type.Reference);
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
      
      classGen.getParser().reportError(2, err);
    }
  }
  




  public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    InstructionList il = methodGen.getInstructionList();
    
    if ((clazz == Character.TYPE) || (clazz == Byte.TYPE) || (clazz == Short.TYPE) || (clazz == Integer.TYPE))
    {
      il.append(I2D);
    }
    else if (clazz == Long.TYPE) {
      il.append(L2D);
    }
    else if (clazz == Float.TYPE) {
      il.append(F2D);
    }
    else if (clazz == Double.TYPE) {
      il.append(NOP);
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
      
      classGen.getParser().reportError(2, err);
    }
  }
  



  public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
  {
    translateTo(classGen, methodGen, Type.Reference);
  }
  



  public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    il.append(new CHECKCAST(cpg.addClass("java.lang.Double")));
    il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.Double", "doubleValue", "()D")));
  }
  

  public Instruction ADD()
  {
    return InstructionConstants.DADD;
  }
  
  public Instruction SUB() {
    return InstructionConstants.DSUB;
  }
  
  public Instruction MUL() {
    return InstructionConstants.DMUL;
  }
  
  public Instruction DIV() {
    return InstructionConstants.DDIV;
  }
  
  public Instruction REM() {
    return InstructionConstants.DREM;
  }
  
  public Instruction NEG() {
    return InstructionConstants.DNEG;
  }
  
  public Instruction LOAD(int slot) {
    return new DLOAD(slot);
  }
  
  public Instruction STORE(int slot) {
    return new DSTORE(slot);
  }
  
  public Instruction POP() {
    return POP2;
  }
  
  public Instruction CMP(boolean less) {
    return less ? InstructionConstants.DCMPG : InstructionConstants.DCMPL;
  }
  
  public Instruction DUP() {
    return DUP2;
  }
}

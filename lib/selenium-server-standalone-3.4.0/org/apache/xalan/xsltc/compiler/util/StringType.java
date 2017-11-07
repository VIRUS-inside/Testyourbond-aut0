package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;























public class StringType
  extends Type
{
  protected StringType() {}
  
  public String toString()
  {
    return "string";
  }
  
  public boolean identicalTo(Type other) {
    return this == other;
  }
  
  public String toSignature() {
    return "Ljava/lang/String;";
  }
  
  public boolean isSimple() {
    return true;
  }
  
  public org.apache.bcel.generic.Type toJCType() {
    return org.apache.bcel.generic.Type.STRING;
  }
  







  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
  {
    if (type == Type.Boolean) {
      translateTo(classGen, methodGen, (BooleanType)type);
    }
    else if (type == Type.Real) {
      translateTo(classGen, methodGen, (RealType)type);
    }
    else if (type == Type.Reference) {
      translateTo(classGen, methodGen, (ReferenceType)type);
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
      
      classGen.getParser().reportError(2, err);
    }
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
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    il.append(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "stringToReal", "(Ljava/lang/String;)D")));
  }
  










  public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    il.append(new INVOKEVIRTUAL(cpg.addMethodref("java.lang.String", "length", "()I")));
    
    return new FlowList(il.append(new IFEQ(null)));
  }
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
  {
    methodGen.getInstructionList().append(NOP);
  }
  







  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    if (clazz.isAssignableFrom(String.class)) {
      methodGen.getInstructionList().append(NOP);
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
      
      classGen.getParser().reportError(2, err);
    }
  }
  






  public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    if (clazz.getName().equals("java.lang.String"))
    {
      il.append(DUP);
      BranchHandle ifNonNull = il.append(new IFNONNULL(null));
      il.append(POP);
      il.append(new PUSH(cpg, ""));
      ifNonNull.setTarget(il.append(NOP));
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
    methodGen.getInstructionList().append(NOP);
  }
  


  public String getClassName()
  {
    return "java.lang.String";
  }
  
  public Instruction LOAD(int slot)
  {
    return new ALOAD(slot);
  }
  
  public Instruction STORE(int slot) {
    return new ASTORE(slot);
  }
}

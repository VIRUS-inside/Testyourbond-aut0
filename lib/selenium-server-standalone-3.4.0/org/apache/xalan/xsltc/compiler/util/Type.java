package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;

























public abstract class Type
  implements Constants
{
  public static final Type Int = new IntType();
  public static final Type Real = new RealType();
  public static final Type Boolean = new BooleanType();
  public static final Type NodeSet = new NodeSetType();
  public static final Type String = new StringType();
  public static final Type ResultTree = new ResultTreeType();
  public static final Type Reference = new ReferenceType();
  public static final Type Void = new VoidType();
  public static final Type Object = new ObjectType(Object.class);
  
  public static final Type Node = new NodeType(-1);
  public static final Type Root = new NodeType(9);
  public static final Type Element = new NodeType(1);
  public static final Type Attribute = new NodeType(2);
  public static final Type Text = new NodeType(3);
  public static final Type Comment = new NodeType(8);
  public static final Type Processing_Instruction = new NodeType(7);
  

  public Type() {}
  
  public static Type newObjectType(String javaClassName)
  {
    if (javaClassName == "java.lang.Object") {
      return Object;
    }
    if (javaClassName == "java.lang.String") {
      return String;
    }
    
    return new ObjectType(javaClassName);
  }
  




  public static Type newObjectType(Class clazz)
  {
    if (clazz == Object.class) {
      return Object;
    }
    if (clazz == String.class) {
      return String;
    }
    
    return new ObjectType(clazz);
  }
  



  public abstract String toString();
  



  public abstract boolean identicalTo(Type paramType);
  



  public boolean isNumber()
  {
    return false;
  }
  



  public boolean implementedAsMethod()
  {
    return false;
  }
  



  public boolean isSimple()
  {
    return false;
  }
  


  public abstract org.apache.bcel.generic.Type toJCType();
  


  public int distanceTo(Type type)
  {
    return type == this ? 0 : Integer.MAX_VALUE;
  }
  




  public abstract String toSignature();
  




  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
  {
    ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
    
    classGen.getParser().reportError(2, err);
  }
  







  public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, Type type)
  {
    FlowList fl = null;
    if (type == Boolean) {
      fl = translateToDesynthesized(classGen, methodGen, (BooleanType)type);
    }
    else
    {
      translateTo(classGen, methodGen, type);
    }
    return fl;
  }
  






  public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
  {
    ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), type.toString());
    
    classGen.getParser().reportError(2, err);
    return null;
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getClass().toString());
    
    classGen.getParser().reportError(2, err);
  }
  





  public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", clazz.getClass().toString(), toString());
    
    classGen.getParser().reportError(2, err);
  }
  



  public void translateBox(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), "[" + toString() + "]");
    
    classGen.getParser().reportError(2, err);
  }
  



  public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", "[" + toString() + "]", toString());
    
    classGen.getParser().reportError(2, err);
  }
  


  public String getClassName()
  {
    return "";
  }
  
  public Instruction ADD() {
    return null;
  }
  
  public Instruction SUB() {
    return null;
  }
  
  public Instruction MUL() {
    return null;
  }
  
  public Instruction DIV() {
    return null;
  }
  
  public Instruction REM() {
    return null;
  }
  
  public Instruction NEG() {
    return null;
  }
  
  public Instruction LOAD(int slot) {
    return null;
  }
  
  public Instruction STORE(int slot) {
    return null;
  }
  
  public Instruction POP() {
    return POP;
  }
  
  public BranchInstruction GT(boolean tozero) {
    return null;
  }
  
  public BranchInstruction GE(boolean tozero) {
    return null;
  }
  
  public BranchInstruction LT(boolean tozero) {
    return null;
  }
  
  public BranchInstruction LE(boolean tozero) {
    return null;
  }
  
  public Instruction CMP(boolean less) {
    return null;
  }
  
  public Instruction DUP() {
    return DUP;
  }
}

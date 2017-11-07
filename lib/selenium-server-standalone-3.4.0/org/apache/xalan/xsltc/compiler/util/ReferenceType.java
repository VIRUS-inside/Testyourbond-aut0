package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;



























public final class ReferenceType
  extends Type
{
  protected ReferenceType() {}
  
  public String toString()
  {
    return "reference";
  }
  
  public boolean identicalTo(Type other) {
    return this == other;
  }
  
  public String toSignature() {
    return "Ljava/lang/Object;";
  }
  
  public org.apache.bcel.generic.Type toJCType() {
    return org.apache.bcel.generic.Type.OBJECT;
  }
  







  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
  {
    if (type == Type.String) {
      translateTo(classGen, methodGen, (StringType)type);
    }
    else if (type == Type.Real) {
      translateTo(classGen, methodGen, (RealType)type);
    }
    else if (type == Type.Boolean) {
      translateTo(classGen, methodGen, (BooleanType)type);
    }
    else if (type == Type.NodeSet) {
      translateTo(classGen, methodGen, (NodeSetType)type);
    }
    else if (type == Type.Node) {
      translateTo(classGen, methodGen, (NodeType)type);
    }
    else if (type == Type.ResultTree) {
      translateTo(classGen, methodGen, (ResultTreeType)type);
    }
    else if (type == Type.Object) {
      translateTo(classGen, methodGen, (ObjectType)type);
    }
    else if (type != Type.Reference)
    {

      ErrorMsg err = new ErrorMsg("INTERNAL_ERR", type.toString());
      classGen.getParser().reportError(2, err);
    }
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, StringType type)
  {
    int current = methodGen.getLocalIndex("current");
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    if (current < 0) {
      il.append(new PUSH(cpg, 0));
    }
    else {
      il.append(new ILOAD(current));
    }
    il.append(methodGen.loadDOM());
    int stringF = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "stringF", "(Ljava/lang/Object;ILorg/apache/xalan/xsltc/DOM;)Ljava/lang/String;");
    





    il.append(new INVOKESTATIC(stringF));
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, RealType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    il.append(methodGen.loadDOM());
    int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "numberF", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)D");
    



    il.append(new INVOKESTATIC(index));
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "booleanF", "(Ljava/lang/Object;)Z");
    


    il.append(new INVOKESTATIC(index));
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNodeSet", "(Ljava/lang/Object;)Lorg/apache/xml/dtm/DTMAxisIterator;");
    



    il.append(new INVOKESTATIC(index));
    

    index = cpg.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "reset", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
    il.append(new INVOKEINTERFACE(index, 1));
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeType type)
  {
    translateTo(classGen, methodGen, Type.NodeSet);
    Type.NodeSet.translateTo(classGen, methodGen, type);
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ResultTreeType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToResultTree", "(Ljava/lang/Object;)Lorg/apache/xalan/xsltc/DOM;");
    
    il.append(new INVOKESTATIC(index));
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type)
  {
    methodGen.getInstructionList().append(NOP);
  }
  



  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    int referenceToLong = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToLong", "(Ljava/lang/Object;)J");
    

    int referenceToDouble = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToDouble", "(Ljava/lang/Object;)D");
    

    int referenceToBoolean = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToBoolean", "(Ljava/lang/Object;)Z");
    


    if (clazz.getName().equals("java.lang.Object")) {
      il.append(NOP);
    }
    else if (clazz == Double.TYPE) {
      il.append(new INVOKESTATIC(referenceToDouble));
    }
    else if (clazz.getName().equals("java.lang.Double")) {
      il.append(new INVOKESTATIC(referenceToDouble));
      Type.Real.translateTo(classGen, methodGen, Type.Reference);
    }
    else if (clazz == Float.TYPE) {
      il.append(new INVOKESTATIC(referenceToDouble));
      il.append(D2F);
    }
    else if (clazz.getName().equals("java.lang.String")) {
      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToString", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)Ljava/lang/String;");
      




      il.append(methodGen.loadDOM());
      il.append(new INVOKESTATIC(index));
    }
    else if (clazz.getName().equals("org.w3c.dom.Node")) {
      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNode", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)Lorg/w3c/dom/Node;");
      




      il.append(methodGen.loadDOM());
      il.append(new INVOKESTATIC(index));
    }
    else if (clazz.getName().equals("org.w3c.dom.NodeList")) {
      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "referenceToNodeList", "(Ljava/lang/Object;Lorg/apache/xalan/xsltc/DOM;)Lorg/w3c/dom/NodeList;");
      




      il.append(methodGen.loadDOM());
      il.append(new INVOKESTATIC(index));
    }
    else if (clazz.getName().equals("org.apache.xalan.xsltc.DOM")) {
      translateTo(classGen, methodGen, Type.ResultTree);
    }
    else if (clazz == Long.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
    }
    else if (clazz == Integer.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
    }
    else if (clazz == Short.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
      il.append(I2S);
    }
    else if (clazz == Byte.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
      il.append(I2B);
    }
    else if (clazz == Character.TYPE) {
      il.append(new INVOKESTATIC(referenceToLong));
      il.append(L2I);
      il.append(I2C);
    }
    else if (clazz == Boolean.TYPE) {
      il.append(new INVOKESTATIC(referenceToBoolean));
    }
    else if (clazz.getName().equals("java.lang.Boolean")) {
      il.append(new INVOKESTATIC(referenceToBoolean));
      Type.Boolean.translateTo(classGen, methodGen, Type.Reference);
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
      
      classGen.getParser().reportError(2, err);
    }
  }
  




  public void translateFrom(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    if (clazz.getName().equals("java.lang.Object")) {
      methodGen.getInstructionList().append(NOP);
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), clazz.getName());
      
      classGen.getParser().reportError(2, err);
    }
  }
  








  public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
  {
    InstructionList il = methodGen.getInstructionList();
    translateTo(classGen, methodGen, type);
    return new FlowList(il.append(new IFEQ(null)));
  }
  




  public void translateBox(ClassGenerator classGen, MethodGenerator methodGen) {}
  



  public void translateUnBox(ClassGenerator classGen, MethodGenerator methodGen) {}
  



  public Instruction LOAD(int slot)
  {
    return new ALOAD(slot);
  }
  
  public Instruction STORE(int slot) {
    return new ASTORE(slot);
  }
}

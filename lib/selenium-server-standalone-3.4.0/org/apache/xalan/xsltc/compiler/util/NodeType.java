package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.FlowList;
import org.apache.xalan.xsltc.compiler.Parser;
























public final class NodeType
  extends Type
{
  private final int _type;
  
  protected NodeType()
  {
    this(-1);
  }
  
  protected NodeType(int type) {
    _type = type;
  }
  
  public int getType() {
    return _type;
  }
  
  public String toString() {
    return "node-type";
  }
  
  public boolean identicalTo(Type other) {
    return other instanceof NodeType;
  }
  
  public int hashCode() {
    return _type;
  }
  
  public String toSignature() {
    return "I";
  }
  
  public org.apache.bcel.generic.Type toJCType() {
    return org.apache.bcel.generic.Type.INT;
  }
  







  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Type type)
  {
    if (type == Type.String) {
      translateTo(classGen, methodGen, (StringType)type);
    }
    else if (type == Type.Boolean) {
      translateTo(classGen, methodGen, (BooleanType)type);
    }
    else if (type == Type.Real) {
      translateTo(classGen, methodGen, (RealType)type);
    }
    else if (type == Type.NodeSet) {
      translateTo(classGen, methodGen, (NodeSetType)type);
    }
    else if (type == Type.Reference) {
      translateTo(classGen, methodGen, (ReferenceType)type);
    }
    else if (type == Type.Object) {
      translateTo(classGen, methodGen, (ObjectType)type);
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
    int index;
    switch (_type) {
    case 1: 
    case 9: 
      il.append(methodGen.loadDOM());
      il.append(SWAP);
      index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getElementValue", "(I)Ljava/lang/String;");
      

      il.append(new INVOKEINTERFACE(index, 2));
      break;
    
    case -1: 
    case 2: 
    case 7: 
    case 8: 
      il.append(methodGen.loadDOM());
      il.append(SWAP);
      index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
      

      il.append(new INVOKEINTERFACE(index, 2));
      break;
    case 0: case 3: case 4: 
    case 5: case 6: default: 
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
    translateTo(classGen, methodGen, Type.String);
    Type.String.translateTo(classGen, methodGen, Type.Real);
  }
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, NodeSetType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.SingletonIterator")));
    il.append(DUP_X1);
    il.append(SWAP);
    int init = cpg.addMethodref("org.apache.xalan.xsltc.dom.SingletonIterator", "<init>", "(I)V");
    
    il.append(new INVOKESPECIAL(init));
  }
  





  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ObjectType type)
  {
    methodGen.getInstructionList().append(NOP);
  }
  








  public FlowList translateToDesynthesized(ClassGenerator classGen, MethodGenerator methodGen, BooleanType type)
  {
    InstructionList il = methodGen.getInstructionList();
    return new FlowList(il.append(new IFEQ(null)));
  }
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, ReferenceType type)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.runtime.Node")));
    il.append(DUP_X1);
    il.append(SWAP);
    il.append(new PUSH(cpg, _type));
    il.append(new INVOKESPECIAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.Node", "<init>", "(II)V")));
  }
  






  public void translateTo(ClassGenerator classGen, MethodGenerator methodGen, Class clazz)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    String className = clazz.getName();
    if (className.equals("java.lang.String")) {
      translateTo(classGen, methodGen, Type.String);
      return;
    }
    
    il.append(methodGen.loadDOM());
    il.append(SWAP);
    
    if ((className.equals("org.w3c.dom.Node")) || (className.equals("java.lang.Object")))
    {
      int index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNode", "(I)Lorg/w3c/dom/Node;");
      

      il.append(new INVOKEINTERFACE(index, 2));
    }
    else if (className.equals("org.w3c.dom.NodeList")) {
      int index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "makeNodeList", "(I)Lorg/w3c/dom/NodeList;");
      

      il.append(new INVOKEINTERFACE(index, 2));
    }
    else {
      ErrorMsg err = new ErrorMsg("DATA_CONVERSION_ERR", toString(), className);
      
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
    il.append(new CHECKCAST(cpg.addClass("org.apache.xalan.xsltc.runtime.Node")));
    il.append(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.Node", "node", "I")));
  }
  




  public String getClassName()
  {
    return "org.apache.xalan.xsltc.runtime.Node";
  }
  
  public Instruction LOAD(int slot) {
    return new ILOAD(slot);
  }
  
  public Instruction STORE(int slot) {
    return new ISTORE(slot);
  }
}

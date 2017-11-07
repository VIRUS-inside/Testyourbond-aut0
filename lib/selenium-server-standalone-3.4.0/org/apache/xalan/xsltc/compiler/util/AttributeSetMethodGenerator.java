package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.Type;
























public final class AttributeSetMethodGenerator
  extends MethodGenerator
{
  private static final int DOM_INDEX = 1;
  private static final int ITERATOR_INDEX = 2;
  private static final int HANDLER_INDEX = 3;
  private static final Type[] argTypes = new Type[3];
  
  private static final String[] argNames = new String[3];
  private final Instruction _aloadDom;
  
  static { argTypes[0] = Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;");
    argNames[0] = "dom";
    argTypes[1] = Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;");
    argNames[1] = "iterator";
    argTypes[2] = Util.getJCRefType(TRANSLET_OUTPUT_SIG);
    argNames[2] = "handler";
  }
  

  private final Instruction _astoreDom;
  
  private final Instruction _astoreIterator;
  private final Instruction _aloadIterator;
  private final Instruction _astoreHandler;
  private final Instruction _aloadHandler;
  public AttributeSetMethodGenerator(String methodName, ClassGen classGen)
  {
    super(2, Type.VOID, argTypes, argNames, methodName, classGen.getClassName(), new InstructionList(), classGen.getConstantPool());
    





    _aloadDom = new ALOAD(1);
    _astoreDom = new ASTORE(1);
    _astoreIterator = new ASTORE(2);
    _aloadIterator = new ALOAD(2);
    _astoreHandler = new ASTORE(3);
    _aloadHandler = new ALOAD(3);
  }
  
  public Instruction storeIterator() {
    return _astoreIterator;
  }
  
  public Instruction loadIterator() {
    return _aloadIterator;
  }
  
  public int getIteratorIndex() {
    return 2;
  }
  
  public Instruction storeHandler() {
    return _astoreHandler;
  }
  
  public Instruction loadHandler() {
    return _aloadHandler;
  }
  
  public int getLocalIndex(String name) {
    return -1;
  }
}

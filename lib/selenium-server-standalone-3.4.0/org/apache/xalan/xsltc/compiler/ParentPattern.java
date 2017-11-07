package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;























final class ParentPattern
  extends RelativePathPattern
{
  private final Pattern _left;
  private final RelativePathPattern _right;
  
  public ParentPattern(Pattern left, RelativePathPattern right)
  {
    (this._left = left).setParent(this);
    (this._right = right).setParent(this);
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    _left.setParser(parser);
    _right.setParser(parser);
  }
  
  public boolean isWildcard() {
    return false;
  }
  
  public StepPattern getKernelPattern() {
    return _right.getKernelPattern();
  }
  
  public void reduceKernelPattern() {
    _right.reduceKernelPattern();
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    _left.typeCheck(stable);
    return _right.typeCheck(stable);
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    LocalVariableGen local = methodGen.addLocalVariable2("ppt", Util.getJCRefType("I"), null);
    



    org.apache.bcel.generic.Instruction loadLocal = new ILOAD(local.getIndex());
    
    org.apache.bcel.generic.Instruction storeLocal = new ISTORE(local.getIndex());
    

    if (_right.isWildcard()) {
      il.append(methodGen.loadDOM());
      il.append(SWAP);
    }
    else if ((_right instanceof StepPattern)) {
      il.append(DUP);
      local.setStart(il.append(storeLocal));
      
      _right.translate(classGen, methodGen);
      
      il.append(methodGen.loadDOM());
      local.setEnd(il.append(loadLocal));
    }
    else {
      _right.translate(classGen, methodGen);
      
      if ((_right instanceof AncestorPattern)) {
        il.append(methodGen.loadDOM());
        il.append(SWAP);
      }
    }
    
    int getParent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
    

    il.append(new INVOKEINTERFACE(getParent, 2));
    
    SyntaxTreeNode p = getParent();
    if ((p == null) || ((p instanceof Instruction)) || ((p instanceof TopLevelElement)))
    {

      _left.translate(classGen, methodGen);
    }
    else {
      il.append(DUP);
      InstructionHandle storeInst = il.append(storeLocal);
      
      if (local.getStart() == null) {
        local.setStart(storeInst);
      }
      _left.translate(classGen, methodGen);
      
      il.append(methodGen.loadDOM());
      local.setEnd(il.append(loadLocal));
    }
    
    methodGen.removeLocalVariable(local);
    




    if ((_right instanceof AncestorPattern)) {
      AncestorPattern ancestor = (AncestorPattern)_right;
      _left.backPatchFalseList(ancestor.getLoopHandle());
    }
    
    _trueList.append(_right._trueList.append(_left._trueList));
    _falseList.append(_right._falseList.append(_left._falseList));
  }
  
  public String toString() {
    return "Parent(" + _left + ", " + _right + ')';
  }
}

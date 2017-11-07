package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

























final class ProcessingInstructionPattern
  extends StepPattern
{
  private String _name = null;
  private boolean _typeChecked = false;
  


  public ProcessingInstructionPattern(String name)
  {
    super(3, 7, null);
    _name = name;
  }
  





  public double getDefaultPriority() { return _name != null ? 0.0D : -0.5D; }
  
  public String toString() {
    if (_predicates == null) {
      return "processing-instruction(" + _name + ")";
    }
    return "processing-instruction(" + _name + ")" + _predicates;
  }
  
  public void reduceKernelPattern() {
    _typeChecked = true;
  }
  
  public boolean isWildcard() {
    return false;
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    if (hasPredicates())
    {
      int n = _predicates.size();
      for (int i = 0; i < n; i++) {
        Predicate pred = (Predicate)_predicates.elementAt(i);
        pred.typeCheck(stable);
      }
    }
    return Type.NodeSet;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    int gname = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeName", "(I)Ljava/lang/String;");
    

    int cmp = cpg.addMethodref("java.lang.String", "equals", "(Ljava/lang/Object;)Z");
    


    il.append(methodGen.loadCurrentNode());
    il.append(SWAP);
    

    il.append(methodGen.storeCurrentNode());
    

    if (!_typeChecked) {
      il.append(methodGen.loadCurrentNode());
      int getType = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
      

      il.append(methodGen.loadDOM());
      il.append(methodGen.loadCurrentNode());
      il.append(new INVOKEINTERFACE(getType, 2));
      il.append(new PUSH(cpg, 7));
      _falseList.add(il.append(new IF_ICMPEQ(null)));
    }
    

    il.append(new PUSH(cpg, _name));
    
    il.append(methodGen.loadDOM());
    il.append(methodGen.loadCurrentNode());
    il.append(new INVOKEINTERFACE(gname, 2));
    
    il.append(new INVOKEVIRTUAL(cmp));
    _falseList.add(il.append(new IFEQ(null)));
    

    if (hasPredicates()) {
      int n = _predicates.size();
      for (int i = 0; i < n; i++) {
        Predicate pred = (Predicate)_predicates.elementAt(i);
        Expression exp = pred.getExpr();
        exp.translateDesynthesized(classGen, methodGen);
        _trueList.append(_trueList);
        _falseList.append(_falseList);
      }
    }
    


    InstructionHandle restore = il.append(methodGen.storeCurrentNode());
    backPatchTrueList(restore);
    BranchHandle skipFalse = il.append(new GOTO(null));
    

    restore = il.append(methodGen.storeCurrentNode());
    backPatchFalseList(restore);
    _falseList.add(il.append(new GOTO(null)));
    

    skipFalse.setTarget(il.append(NOP));
  }
}

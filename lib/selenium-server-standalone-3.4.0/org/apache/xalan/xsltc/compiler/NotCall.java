package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
























final class NotCall
  extends FunctionCall
{
  public NotCall(QName fname, Vector arguments)
  {
    super(fname, arguments);
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    InstructionList il = methodGen.getInstructionList();
    argument().translate(classGen, methodGen);
    il.append(ICONST_1);
    il.append(IXOR);
  }
  
  public void translateDesynthesized(ClassGenerator classGen, MethodGenerator methodGen)
  {
    InstructionList il = methodGen.getInstructionList();
    Expression exp = argument();
    exp.translateDesynthesized(classGen, methodGen);
    BranchHandle gotoh = il.append(new GOTO(null));
    _trueList = _falseList;
    _falseList = _trueList;
    _falseList.add(gotoh);
  }
}

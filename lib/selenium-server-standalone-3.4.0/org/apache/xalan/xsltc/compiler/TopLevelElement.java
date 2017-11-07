package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;


























class TopLevelElement
  extends SyntaxTreeNode
{
  protected Vector _dependencies = null;
  
  TopLevelElement() {}
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError
  {
    return typeCheckContents(stable);
  }
  


  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ErrorMsg msg = new ErrorMsg("NOT_IMPLEMENTED_ERR", getClass(), this);
    
    getParser().reportError(2, msg);
  }
  




  public InstructionList compile(ClassGenerator classGen, MethodGenerator methodGen)
  {
    InstructionList save = methodGen.getInstructionList();
    InstructionList result; methodGen.setInstructionList(result = new InstructionList());
    translate(classGen, methodGen);
    methodGen.setInstructionList(save);
    return result;
  }
  
  public void display(int indent) {
    indent(indent);
    Util.println("TopLevelElement");
    displayContents(indent + 4);
  }
  



  public void addDependency(TopLevelElement other)
  {
    if (_dependencies == null) {
      _dependencies = new Vector();
    }
    if (!_dependencies.contains(other)) {
      _dependencies.addElement(other);
    }
  }
  



  public Vector getDependencies()
  {
    return _dependencies;
  }
}

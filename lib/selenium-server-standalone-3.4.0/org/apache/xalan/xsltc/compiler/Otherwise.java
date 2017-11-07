package org.apache.xalan.xsltc.compiler;

import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;





















final class Otherwise
  extends Instruction
{
  Otherwise() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("Otherwise");
    indent(indent + 4);
    displayContents(indent + 4);
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    typeCheckContents(stable);
    return Type.Void;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    Parser parser = getParser();
    ErrorMsg err = new ErrorMsg("STRAY_OTHERWISE_ERR", this);
    parser.reportError(3, err);
  }
}

package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import org.apache.xalan.xsltc.compiler.util.BooleanType;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;






















final class When
  extends Instruction
{
  private Expression _test;
  
  When() {}
  
  private boolean _ignore = false;
  
  public void display(int indent) {
    indent(indent);
    Util.println("When");
    indent(indent + 4);
    System.out.print("test ");
    Util.println(_test.toString());
    displayContents(indent + 4);
  }
  
  public Expression getTest() {
    return _test;
  }
  
  public boolean ignore() {
    return _ignore;
  }
  
  public void parseContents(Parser parser) {
    _test = parser.parseExpression(this, "test", null);
    


    Object result = _test.evaluateAtCompileTime();
    if ((result != null) && ((result instanceof Boolean))) {
      _ignore = (!((Boolean)result).booleanValue());
    }
    
    parseChildren(parser);
    

    if (_test.isDummy()) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "test");
    }
  }
  






  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    if (!(_test.typeCheck(stable) instanceof BooleanType)) {
      _test = new CastExpr(_test, Type.Boolean);
    }
    
    if (!_ignore) {
      typeCheckContents(stable);
    }
    
    return Type.Void;
  }
  



  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ErrorMsg msg = new ErrorMsg("STRAY_WHEN_ERR", this);
    getParser().reportError(3, msg);
  }
}

package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;






















final class UnaryOpExpr
  extends Expression
{
  private Expression _left;
  
  public UnaryOpExpr(Expression left)
  {
    (this._left = left).setParent(this);
  }
  



  public boolean hasPositionCall()
  {
    return _left.hasPositionCall();
  }
  


  public boolean hasLastCall()
  {
    return _left.hasLastCall();
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    _left.setParser(parser);
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    Type tleft = _left.typeCheck(stable);
    MethodType ptype = lookupPrimop(stable, "u-", new MethodType(Type.Void, tleft));
    


    if (ptype != null) {
      Type arg1 = (Type)ptype.argsType().elementAt(0);
      if (!arg1.identicalTo(tleft)) {
        _left = new CastExpr(_left, arg1);
      }
      return this._type = ptype.resultType();
    }
    
    throw new TypeCheckError(this);
  }
  
  public String toString() {
    return "u-(" + _left + ')';
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    InstructionList il = methodGen.getInstructionList();
    _left.translate(classGen, methodGen);
    il.append(_type.NEG());
  }
}

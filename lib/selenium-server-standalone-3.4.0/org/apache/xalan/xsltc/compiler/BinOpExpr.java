package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
























final class BinOpExpr
  extends Expression
{
  public static final int PLUS = 0;
  public static final int MINUS = 1;
  public static final int TIMES = 2;
  public static final int DIV = 3;
  public static final int MOD = 4;
  private static final String[] Ops = { "+", "-", "*", "/", "%" };
  
  private int _op;
  private Expression _left;
  private Expression _right;
  
  public BinOpExpr(int op, Expression left, Expression right)
  {
    _op = op;
    (this._left = left).setParent(this);
    (this._right = right).setParent(this);
  }
  



  public boolean hasPositionCall()
  {
    if (_left.hasPositionCall()) return true;
    if (_right.hasPositionCall()) return true;
    return false;
  }
  


  public boolean hasLastCall()
  {
    return (_left.hasLastCall()) || (_right.hasLastCall());
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    _left.setParser(parser);
    _right.setParser(parser);
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    Type tleft = _left.typeCheck(stable);
    Type tright = _right.typeCheck(stable);
    MethodType ptype = lookupPrimop(stable, Ops[_op], new MethodType(Type.Void, tleft, tright));
    

    if (ptype != null) {
      Type arg1 = (Type)ptype.argsType().elementAt(0);
      if (!arg1.identicalTo(tleft)) {
        _left = new CastExpr(_left, arg1);
      }
      Type arg2 = (Type)ptype.argsType().elementAt(1);
      if (!arg2.identicalTo(tright)) {
        _right = new CastExpr(_right, arg1);
      }
      return this._type = ptype.resultType();
    }
    throw new TypeCheckError(this);
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    InstructionList il = methodGen.getInstructionList();
    
    _left.translate(classGen, methodGen);
    _right.translate(classGen, methodGen);
    
    switch (_op) {
    case 0: 
      il.append(_type.ADD());
      break;
    case 1: 
      il.append(_type.SUB());
      break;
    case 2: 
      il.append(_type.MUL());
      break;
    case 3: 
      il.append(_type.DIV());
      break;
    case 4: 
      il.append(_type.REM());
      break;
    default: 
      ErrorMsg msg = new ErrorMsg("ILLEGAL_BINARY_OP_ERR", this);
      getParser().reportError(3, msg);
    }
  }
  
  public String toString() {
    return Ops[_op] + '(' + _left + ", " + _right + ')';
  }
}

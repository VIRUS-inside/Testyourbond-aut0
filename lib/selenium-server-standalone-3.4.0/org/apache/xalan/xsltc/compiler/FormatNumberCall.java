package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;


























final class FormatNumberCall
  extends FunctionCall
{
  private Expression _value;
  private Expression _format;
  private Expression _name;
  private QName _resolvedQName = null;
  
  public FormatNumberCall(QName fname, Vector arguments) {
    super(fname, arguments);
    _value = argument(0);
    _format = argument(1);
    _name = (argumentCount() == 3 ? argument(2) : null);
  }
  
  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    getStylesheet().numberFormattingUsed();
    
    Type tvalue = _value.typeCheck(stable);
    if (!(tvalue instanceof RealType)) {
      _value = new CastExpr(_value, Type.Real);
    }
    Type tformat = _format.typeCheck(stable);
    if (!(tformat instanceof StringType)) {
      _format = new CastExpr(_format, Type.String);
    }
    if (argumentCount() == 3) {
      Type tname = _name.typeCheck(stable);
      
      if ((_name instanceof LiteralExpr)) {
        LiteralExpr literal = (LiteralExpr)_name;
        _resolvedQName = getParser().getQNameIgnoreDefaultNs(literal.getValue());

      }
      else if (!(tname instanceof StringType)) {
        _name = new CastExpr(_name, Type.String);
      }
    }
    return this._type = Type.String;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    _value.translate(classGen, methodGen);
    _format.translate(classGen, methodGen);
    
    int fn3arg = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "formatNumber", "(DLjava/lang/String;Ljava/text/DecimalFormat;)Ljava/lang/String;");
    



    int get = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getDecimalFormat", "(Ljava/lang/String;)Ljava/text/DecimalFormat;");
    



    il.append(classGen.loadTranslet());
    if (_name == null) {
      il.append(new PUSH(cpg, ""));
    }
    else if (_resolvedQName != null) {
      il.append(new PUSH(cpg, _resolvedQName.toString()));
    }
    else {
      _name.translate(classGen, methodGen);
    }
    il.append(new INVOKEVIRTUAL(get));
    il.append(new INVOKESTATIC(fn3arg));
  }
}

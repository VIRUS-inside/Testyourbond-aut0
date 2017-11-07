package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

























final class UnparsedEntityUriCall
  extends FunctionCall
{
  private Expression _entity;
  
  public UnparsedEntityUriCall(QName fname, Vector arguments)
  {
    super(fname, arguments);
    _entity = argument();
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    Type entity = _entity.typeCheck(stable);
    if (!(entity instanceof StringType)) {
      _entity = new CastExpr(_entity, Type.String);
    }
    return this._type = Type.String;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    il.append(methodGen.loadDOM());
    
    _entity.translate(classGen, methodGen);
    
    il.append(new INVOKEINTERFACE(cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getUnparsedEntityURI", "(Ljava/lang/String;)Ljava/lang/String;"), 2));
  }
}

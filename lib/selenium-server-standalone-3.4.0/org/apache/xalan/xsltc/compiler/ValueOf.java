package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;























final class ValueOf
  extends Instruction
{
  private Expression _select;
  
  ValueOf() {}
  
  private boolean _escaping = true;
  private boolean _isString = false;
  
  public void display(int indent) {
    indent(indent);
    Util.println("ValueOf");
    indent(indent + 4);
    Util.println("select " + _select.toString());
  }
  
  public void parseContents(Parser parser) {
    _select = parser.parseExpression(this, "select", null);
    

    if (_select.isDummy()) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
      return;
    }
    String str = getAttribute("disable-output-escaping");
    if ((str != null) && (str.equals("yes"))) _escaping = false;
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    Type type = _select.typeCheck(stable);
    

    if ((type != null) && (!type.identicalTo(Type.Node)))
    {








      if (type.identicalTo(Type.NodeSet)) {
        _select = new CastExpr(_select, Type.Node);
      } else {
        _isString = true;
        if (!type.identicalTo(Type.String)) {
          _select = new CastExpr(_select, Type.String);
        }
        _isString = true;
      }
    }
    return Type.Void;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int setEscaping = cpg.addInterfaceMethodref(OUTPUT_HANDLER, "setEscaping", "(Z)Z");
    


    if (!_escaping) {
      il.append(methodGen.loadHandler());
      il.append(new PUSH(cpg, false));
      il.append(new INVOKEINTERFACE(setEscaping, 2));
    }
    





    if (_isString) {
      int characters = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "characters", CHARACTERSW_SIG);
      


      il.append(classGen.loadTranslet());
      _select.translate(classGen, methodGen);
      il.append(methodGen.loadHandler());
      il.append(new INVOKEVIRTUAL(characters));
    } else {
      int characters = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "characters", CHARACTERS_SIG);
      


      il.append(methodGen.loadDOM());
      _select.translate(classGen, methodGen);
      il.append(methodGen.loadHandler());
      il.append(new INVOKEINTERFACE(characters, 3));
    }
    

    if (!_escaping) {
      il.append(methodGen.loadHandler());
      il.append(SWAP);
      il.append(new INVOKEINTERFACE(setEscaping, 2));
      il.append(POP);
    }
  }
}

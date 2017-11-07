package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;






















final class CopyOf
  extends Instruction
{
  private Expression _select;
  
  CopyOf() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("CopyOf");
    indent(indent + 4);
    Util.println("select " + _select.toString());
  }
  
  public void parseContents(Parser parser) {
    _select = parser.parseExpression(this, "select", null);
    
    if (_select.isDummy()) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
      return;
    }
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    Type tselect = _select.typeCheck(stable);
    if ((!(tselect instanceof NodeType)) && (!(tselect instanceof NodeSetType)) && (!(tselect instanceof ReferenceType)) && (!(tselect instanceof ResultTreeType)))
    {





      _select = new CastExpr(_select, Type.String);
    }
    return Type.Void;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    Type tselect = _select.getType();
    
    String CPY1_SIG = "(Lorg/apache/xml/dtm/DTMAxisIterator;" + TRANSLET_OUTPUT_SIG + ")V";
    int cpy1 = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "copy", CPY1_SIG);
    
    String CPY2_SIG = "(I" + TRANSLET_OUTPUT_SIG + ")V";
    int cpy2 = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "copy", CPY2_SIG);
    
    String getDoc_SIG = "()I";
    int getDoc = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getDocument", "()I");
    

    if ((tselect instanceof NodeSetType)) {
      il.append(methodGen.loadDOM());
      

      _select.translate(classGen, methodGen);
      _select.startIterator(classGen, methodGen);
      

      il.append(methodGen.loadHandler());
      il.append(new INVOKEINTERFACE(cpy1, 3));
    }
    else if ((tselect instanceof NodeType)) {
      il.append(methodGen.loadDOM());
      _select.translate(classGen, methodGen);
      il.append(methodGen.loadHandler());
      il.append(new INVOKEINTERFACE(cpy2, 3));
    }
    else if ((tselect instanceof ResultTreeType)) {
      _select.translate(classGen, methodGen);
      
      il.append(DUP);
      il.append(new INVOKEINTERFACE(getDoc, 1));
      il.append(methodGen.loadHandler());
      il.append(new INVOKEINTERFACE(cpy2, 3));
    }
    else if ((tselect instanceof ReferenceType)) {
      _select.translate(classGen, methodGen);
      il.append(methodGen.loadHandler());
      il.append(methodGen.loadCurrentNode());
      il.append(methodGen.loadDOM());
      int copy = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "copy", "(Ljava/lang/Object;" + TRANSLET_OUTPUT_SIG + "I" + "Lorg/apache/xalan/xsltc/DOM;" + ")V");
      





      il.append(new INVOKESTATIC(copy));
    }
    else {
      il.append(classGen.loadTranslet());
      _select.translate(classGen, methodGen);
      il.append(methodGen.loadHandler());
      il.append(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "characters", CHARACTERSW_SIG)));
    }
  }
}

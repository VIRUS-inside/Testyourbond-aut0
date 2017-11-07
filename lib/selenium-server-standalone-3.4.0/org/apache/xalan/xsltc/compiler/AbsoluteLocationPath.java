package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;























final class AbsoluteLocationPath
  extends Expression
{
  private Expression _path;
  
  public AbsoluteLocationPath()
  {
    _path = null;
  }
  
  public AbsoluteLocationPath(Expression path) {
    _path = path;
    if (path != null) {
      _path.setParent(this);
    }
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    if (_path != null) {
      _path.setParser(parser);
    }
  }
  
  public Expression getPath() {
    return _path;
  }
  
  public String toString() {
    return "AbsoluteLocationPath(" + (_path != null ? _path.toString() : "null") + ')';
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError
  {
    if (_path != null) {
      Type ptype = _path.typeCheck(stable);
      if ((ptype instanceof NodeType)) {
        _path = new CastExpr(_path, Type.NodeSet);
      }
    }
    return this._type = Type.NodeSet;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    if (_path != null) {
      int initAI = cpg.addMethodref("org.apache.xalan.xsltc.dom.AbsoluteIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;)V");
      














      _path.translate(classGen, methodGen);
      LocalVariableGen relPathIterator = methodGen.addLocalVariable("abs_location_path_tmp", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
      


      relPathIterator.setStart(il.append(new ASTORE(relPathIterator.getIndex())));
      


      il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.AbsoluteIterator")));
      il.append(DUP);
      relPathIterator.setEnd(il.append(new ALOAD(relPathIterator.getIndex())));
      


      il.append(new INVOKESPECIAL(initAI));
    }
    else {
      int gitr = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
      

      il.append(methodGen.loadDOM());
      il.append(new INVOKEINTERFACE(gitr, 1));
    }
  }
}

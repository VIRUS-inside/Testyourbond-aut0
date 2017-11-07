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






















final class FilteredAbsoluteLocationPath
  extends Expression
{
  private Expression _path;
  
  public FilteredAbsoluteLocationPath()
  {
    _path = null;
  }
  
  public FilteredAbsoluteLocationPath(Expression path) {
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
    return "FilteredAbsoluteLocationPath(" + (_path != null ? _path.toString() : "null") + ')';
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
      int initDFI = cpg.addMethodref("org.apache.xalan.xsltc.dom.DupFilterIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;)V");
      














      LocalVariableGen pathTemp = methodGen.addLocalVariable("filtered_absolute_location_path_tmp", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
      


      _path.translate(classGen, methodGen);
      pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
      

      il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.DupFilterIterator")));
      il.append(DUP);
      pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
      

      il.append(new INVOKESPECIAL(initDFI));
    }
    else {
      int git = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getIterator", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
      

      il.append(methodGen.loadDOM());
      il.append(new INVOKEINTERFACE(git, 1));
    }
  }
}

package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

























final class FilterParentPath
  extends Expression
{
  private Expression _filterExpr;
  private Expression _path;
  private boolean _hasDescendantAxis = false;
  
  public FilterParentPath(Expression filterExpr, Expression path) {
    (this._path = path).setParent(this);
    (this._filterExpr = filterExpr).setParent(this);
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    _filterExpr.setParser(parser);
    _path.setParser(parser);
  }
  
  public String toString() {
    return "FilterParentPath(" + _filterExpr + ", " + _path + ')';
  }
  
  public void setDescendantAxis() {
    _hasDescendantAxis = true;
  }
  



  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    Type ftype = _filterExpr.typeCheck(stable);
    if (!(ftype instanceof NodeSetType)) {
      if ((ftype instanceof ReferenceType)) {
        _filterExpr = new CastExpr(_filterExpr, Type.NodeSet);





      }
      else if ((ftype instanceof NodeType)) {
        _filterExpr = new CastExpr(_filterExpr, Type.NodeSet);
      }
      else {
        throw new TypeCheckError(this);
      }
    }
    

    Type ptype = _path.typeCheck(stable);
    if (!(ptype instanceof NodeSetType)) {
      _path = new CastExpr(_path, Type.NodeSet);
    }
    
    return this._type = Type.NodeSet;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    int initSI = cpg.addMethodref("org.apache.xalan.xsltc.dom.StepIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
    















    _filterExpr.translate(classGen, methodGen);
    LocalVariableGen filterTemp = methodGen.addLocalVariable("filter_parent_path_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    


    filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
    
    _path.translate(classGen, methodGen);
    LocalVariableGen pathTemp = methodGen.addLocalVariable("filter_parent_path_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    


    pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
    
    il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.StepIterator")));
    il.append(DUP);
    filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
    pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
    

    il.append(new INVOKESPECIAL(initSI));
    

    if (_hasDescendantAxis) {
      int incl = cpg.addMethodref("org.apache.xml.dtm.ref.DTMAxisIteratorBase", "includeSelf", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
      

      il.append(new INVOKEVIRTUAL(incl));
    }
    
    SyntaxTreeNode parent = getParent();
    
    boolean parentAlreadyOrdered = ((parent instanceof RelativeLocationPath)) || ((parent instanceof FilterParentPath)) || ((parent instanceof KeyCall)) || ((parent instanceof CurrentCall)) || ((parent instanceof DocumentCall));
    





    if (!parentAlreadyOrdered) {
      int order = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
      

      il.append(methodGen.loadDOM());
      il.append(SWAP);
      il.append(methodGen.loadContextNode());
      il.append(new INVOKEINTERFACE(order, 3));
    }
  }
}

package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

































class FilterExpr
  extends Expression
{
  private Expression _primary;
  private final Vector _predicates;
  
  public FilterExpr(Expression primary, Vector predicates)
  {
    _primary = primary;
    _predicates = predicates;
    primary.setParent(this);
  }
  
  protected Expression getExpr() {
    if ((_primary instanceof CastExpr)) {
      return ((CastExpr)_primary).getExpr();
    }
    return _primary;
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    _primary.setParser(parser);
    if (_predicates != null) {
      int n = _predicates.size();
      for (int i = 0; i < n; i++) {
        Expression exp = (Expression)_predicates.elementAt(i);
        exp.setParser(parser);
        exp.setParent(this);
      }
    }
  }
  
  public String toString() {
    return "filter-expr(" + _primary + ", " + _predicates + ")";
  }
  





  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    Type ptype = _primary.typeCheck(stable);
    boolean canOptimize = _primary instanceof KeyCall;
    
    if (!(ptype instanceof NodeSetType)) {
      if ((ptype instanceof ReferenceType)) {
        _primary = new CastExpr(_primary, Type.NodeSet);
      }
      else {
        throw new TypeCheckError(this);
      }
    }
    

    int n = _predicates.size();
    for (int i = 0; i < n; i++) {
      Predicate pred = (Predicate)_predicates.elementAt(i);
      
      if (!canOptimize) {
        pred.dontOptimize();
      }
      pred.typeCheck(stable);
    }
    return this._type = Type.NodeSet;
  }
  



  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    if (_predicates.size() > 0) {
      translatePredicates(classGen, methodGen);
    }
    else {
      _primary.translate(classGen, methodGen);
    }
  }
  






  public void translatePredicates(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    if (_predicates.size() == 0) {
      translate(classGen, methodGen);
    }
    else {
      Predicate predicate = (Predicate)_predicates.lastElement();
      _predicates.remove(predicate);
      

      translatePredicates(classGen, methodGen);
      
      if (predicate.isNthPositionFilter()) {
        int nthIteratorIdx = cpg.addMethodref("org.apache.xalan.xsltc.dom.NthIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)V");
        













        LocalVariableGen iteratorTemp = methodGen.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        


        iteratorTemp.setStart(il.append(new ASTORE(iteratorTemp.getIndex())));
        

        predicate.translate(classGen, methodGen);
        LocalVariableGen predicateValueTemp = methodGen.addLocalVariable("filter_expr_tmp2", Util.getJCRefType("I"), null, null);
        


        predicateValueTemp.setStart(il.append(new ISTORE(predicateValueTemp.getIndex())));
        

        il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.NthIterator")));
        il.append(DUP);
        iteratorTemp.setEnd(il.append(new ALOAD(iteratorTemp.getIndex())));
        
        predicateValueTemp.setEnd(il.append(new ILOAD(predicateValueTemp.getIndex())));
        
        il.append(new INVOKESPECIAL(nthIteratorIdx));
      }
      else {
        int initCNLI = cpg.addMethodref("org.apache.xalan.xsltc.dom.CurrentNodeListIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;ZLorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;ILorg/apache/xalan/xsltc/runtime/AbstractTranslet;)V");
        
















        LocalVariableGen nodeIteratorTemp = methodGen.addLocalVariable("filter_expr_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
        


        nodeIteratorTemp.setStart(il.append(new ASTORE(nodeIteratorTemp.getIndex())));
        

        predicate.translate(classGen, methodGen);
        LocalVariableGen filterTemp = methodGen.addLocalVariable("filter_expr_tmp2", Util.getJCRefType("Lorg/apache/xalan/xsltc/dom/CurrentNodeListFilter;"), null, null);
        


        filterTemp.setStart(il.append(new ASTORE(filterTemp.getIndex())));
        


        il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.CurrentNodeListIterator")));
        il.append(DUP);
        

        nodeIteratorTemp.setEnd(il.append(new ALOAD(nodeIteratorTemp.getIndex())));
        
        il.append(ICONST_1);
        filterTemp.setEnd(il.append(new ALOAD(filterTemp.getIndex())));
        il.append(methodGen.loadCurrentNode());
        il.append(classGen.loadTranslet());
        il.append(new INVOKESPECIAL(initCNLI));
      }
    }
  }
}

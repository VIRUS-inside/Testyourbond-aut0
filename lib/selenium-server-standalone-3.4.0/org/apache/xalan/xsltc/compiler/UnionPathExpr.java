package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xml.dtm.Axis;



























final class UnionPathExpr
  extends Expression
{
  private final Expression _pathExpr;
  private final Expression _rest;
  private boolean _reverse = false;
  
  private Expression[] _components;
  
  public UnionPathExpr(Expression pathExpr, Expression rest)
  {
    _pathExpr = pathExpr;
    _rest = rest;
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    
    Vector components = new Vector();
    flatten(components);
    int size = components.size();
    _components = ((Expression[])components.toArray(new Expression[size]));
    for (int i = 0; i < size; i++) {
      _components[i].setParser(parser);
      _components[i].setParent(this);
      if ((_components[i] instanceof Step)) {
        Step step = (Step)_components[i];
        int axis = step.getAxis();
        int type = step.getNodeType();
        
        if ((axis == 2) || (type == 2)) {
          _components[i] = _components[0];
          _components[0] = step;
        }
        
        if (Axis.isReverse(axis)) { _reverse = true;
        }
      }
    }
    if ((getParent() instanceof Expression)) _reverse = false;
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    int length = _components.length;
    for (int i = 0; i < length; i++) {
      if (_components[i].typeCheck(stable) != Type.NodeSet) {
        _components[i] = new CastExpr(_components[i], Type.NodeSet);
      }
    }
    return this._type = Type.NodeSet;
  }
  
  public String toString() {
    return "union(" + _pathExpr + ", " + _rest + ')';
  }
  
  private void flatten(Vector components) {
    components.addElement(_pathExpr);
    if (_rest != null) {
      if ((_rest instanceof UnionPathExpr)) {
        ((UnionPathExpr)_rest).flatten(components);
      }
      else {
        components.addElement(_rest);
      }
    }
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    int init = cpg.addMethodref("org.apache.xalan.xsltc.dom.UnionIterator", "<init>", "(Lorg/apache/xalan/xsltc/DOM;)V");
    

    int iter = cpg.addMethodref("org.apache.xalan.xsltc.dom.UnionIterator", "addIterator", "(Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/apache/xalan/xsltc/dom/UnionIterator;");
    



    il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.UnionIterator")));
    il.append(DUP);
    il.append(methodGen.loadDOM());
    il.append(new INVOKESPECIAL(init));
    

    int length = _components.length;
    for (int i = 0; i < length; i++) {
      _components[i].translate(classGen, methodGen);
      il.append(new INVOKEVIRTUAL(iter));
    }
    

    if (_reverse) {
      int order = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
      

      il.append(methodGen.loadDOM());
      il.append(SWAP);
      il.append(methodGen.loadContextNode());
      il.append(new INVOKEINTERFACE(order, 3));
    }
  }
}

package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;









































final class KeyCall
  extends FunctionCall
{
  private Expression _name;
  private Expression _value;
  private Type _valueType;
  private QName _resolvedQName = null;
  











  public KeyCall(QName fname, Vector arguments)
  {
    super(fname, arguments);
    switch (argumentCount()) {
    case 1: 
      _name = null;
      _value = argument(0);
      break;
    case 2: 
      _name = argument(0);
      _value = argument(1);
      break;
    default: 
      _name = (this._value = null);
    }
    
  }
  












  public void addParentDependency()
  {
    if (_resolvedQName == null) { return;
    }
    SyntaxTreeNode node = this;
    while ((node != null) && (!(node instanceof TopLevelElement))) {
      node = node.getParent();
    }
    
    TopLevelElement parent = (TopLevelElement)node;
    if (parent != null) {
      parent.addDependency(getSymbolTable().getKey(_resolvedQName));
    }
  }
  





  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    Type returnType = super.typeCheck(stable);
    


    if (_name != null) {
      Type nameType = _name.typeCheck(stable);
      
      if ((_name instanceof LiteralExpr)) {
        LiteralExpr literal = (LiteralExpr)_name;
        _resolvedQName = getParser().getQNameIgnoreDefaultNs(literal.getValue());

      }
      else if (!(nameType instanceof StringType)) {
        _name = new CastExpr(_name, Type.String);
      }
    }
    









    _valueType = _value.typeCheck(stable);
    
    if ((_valueType != Type.NodeSet) && (_valueType != Type.Reference) && (_valueType != Type.String))
    {

      _value = new CastExpr(_value, Type.String);
      _valueType = _value.typeCheck(stable);
    }
    

    addParentDependency();
    
    return returnType;
  }
  








  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    int getKeyIndex = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getKeyIndex", "(Ljava/lang/String;)Lorg/apache/xalan/xsltc/dom/KeyIndex;");
    




    int keyDom = cpg.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "setDom", "(Lorg/apache/xalan/xsltc/DOM;)V");
    



    int getKeyIterator = cpg.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "getKeyIndexIterator", "(" + _valueType.toSignature() + "Z)" + "Lorg/apache/xalan/xsltc/dom/KeyIndex$KeyIndexIterator;");
    





    il.append(classGen.loadTranslet());
    if (_name == null) {
      il.append(new PUSH(cpg, "##id"));
    } else if (_resolvedQName != null) {
      il.append(new PUSH(cpg, _resolvedQName.toString()));
    } else {
      _name.translate(classGen, methodGen);
    }
    







    il.append(new INVOKEVIRTUAL(getKeyIndex));
    il.append(DUP);
    il.append(methodGen.loadDOM());
    il.append(new INVOKEVIRTUAL(keyDom));
    
    _value.translate(classGen, methodGen);
    il.append(_name != null ? ICONST_1 : ICONST_0);
    il.append(new INVOKEVIRTUAL(getKeyIterator));
  }
}

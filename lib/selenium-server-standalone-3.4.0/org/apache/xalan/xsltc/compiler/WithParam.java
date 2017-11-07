package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;













































final class WithParam
  extends Instruction
{
  private QName _name;
  protected String _escapedName;
  private Expression _select;
  private boolean _doParameterOptimization = false;
  
  WithParam() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("with-param " + _name);
    if (_select != null) {
      indent(indent + 4);
      Util.println("select " + _select.toString());
    }
    displayContents(indent + 4);
  }
  


  public String getEscapedName()
  {
    return _escapedName;
  }
  


  public QName getName()
  {
    return _name;
  }
  


  public void setName(QName name)
  {
    _name = name;
    _escapedName = Util.escape(name.getStringRep());
  }
  


  public void setDoParameterOptimization(boolean flag)
  {
    _doParameterOptimization = flag;
  }
  



  public void parseContents(Parser parser)
  {
    String name = getAttribute("name");
    if (name.length() > 0) {
      if (!XML11Char.isXML11ValidQName(name)) {
        ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
        
        parser.reportError(3, err);
      }
      setName(parser.getQNameIgnoreDefaultNs(name));
    }
    else {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
    }
    
    String select = getAttribute("select");
    if (select.length() > 0) {
      _select = parser.parseExpression(this, "select", null);
    }
    
    parseChildren(parser);
  }
  


  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    if (_select != null) {
      Type tselect = _select.typeCheck(stable);
      if (!(tselect instanceof ReferenceType)) {
        _select = new CastExpr(_select, Type.Reference);
      }
    }
    else {
      typeCheckContents(stable);
    }
    return Type.Void;
  }
  





  public void translateValue(ClassGenerator classGen, MethodGenerator methodGen)
  {
    if (_select != null) {
      _select.translate(classGen, methodGen);
      _select.startIterator(classGen, methodGen);

    }
    else if (hasContents()) {
      compileResultTree(classGen, methodGen);
    }
    else
    {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      il.append(new PUSH(cpg, ""));
    }
  }
  




  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    if (_doParameterOptimization) {
      translateValue(classGen, methodGen);
      return;
    }
    

    String name = Util.escape(getEscapedName());
    

    il.append(classGen.loadTranslet());
    

    il.append(new PUSH(cpg, name));
    
    translateValue(classGen, methodGen);
    
    il.append(new PUSH(cpg, false));
    
    il.append(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addParameter", "(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/Object;")));
    

    il.append(POP);
  }
}

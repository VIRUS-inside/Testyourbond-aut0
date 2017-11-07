package org.apache.xalan.xsltc.compiler;

import java.io.PrintStream;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;































class VariableBase
  extends TopLevelElement
{
  protected QName _name;
  protected String _escapedName;
  protected org.apache.xalan.xsltc.compiler.util.Type _type;
  protected boolean _isLocal;
  protected LocalVariableGen _local;
  protected Instruction _loadInstruction;
  protected Instruction _storeInstruction;
  protected Expression _select;
  protected String select;
  protected Vector _refs = new Vector(2);
  

  protected Vector _dependencies = null;
  

  protected boolean _ignore = false;
  
  VariableBase() {}
  
  public void disable()
  {
    _ignore = true;
  }
  



  public void addReference(VariableRefBase vref)
  {
    _refs.addElement(vref);
  }
  


  public void mapRegister(MethodGenerator methodGen)
  {
    if (_local == null) {
      String name = getEscapedName();
      org.apache.bcel.generic.Type varType = _type.toJCType();
      _local = methodGen.addLocalVariable2(name, varType, null);
    }
  }
  



  public void unmapRegister(MethodGenerator methodGen)
  {
    if (_local != null) {
      _local.setEnd(methodGen.getInstructionList().getEnd());
      methodGen.removeLocalVariable(_local);
      _refs = null;
      _local = null;
    }
  }
  



  public Instruction loadInstruction()
  {
    Instruction instr = _loadInstruction;
    if (_loadInstruction == null) {
      _loadInstruction = _type.LOAD(_local.getIndex());
    }
    return _loadInstruction;
  }
  



  public Instruction storeInstruction()
  {
    Instruction instr = _storeInstruction;
    if (_storeInstruction == null) {
      _storeInstruction = _type.STORE(_local.getIndex());
    }
    return _storeInstruction;
  }
  


  public Expression getExpression()
  {
    return _select;
  }
  


  public String toString()
  {
    return "variable(" + _name + ")";
  }
  


  public void display(int indent)
  {
    indent(indent);
    System.out.println("Variable " + _name);
    if (_select != null) {
      indent(indent + 4);
      System.out.println("select " + _select.toString());
    }
    displayContents(indent + 4);
  }
  


  public org.apache.xalan.xsltc.compiler.util.Type getType()
  {
    return _type;
  }
  



  public QName getName()
  {
    return _name;
  }
  


  public String getEscapedName()
  {
    return _escapedName;
  }
  


  public void setName(QName name)
  {
    _name = name;
    _escapedName = Util.escape(name.getStringRep());
  }
  


  public boolean isLocal()
  {
    return _isLocal;
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
    
    VariableBase other = parser.lookupVariable(_name);
    if ((other != null) && (other.getParent() == getParent())) {
      reportError(this, parser, "VARIABLE_REDEF_ERR", name);
    }
    
    select = getAttribute("select");
    if (select.length() > 0) {
      _select = getParser().parseExpression(this, "select", null);
      if (_select.isDummy()) {
        reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
        return;
      }
    }
    

    parseChildren(parser);
  }
  





  public void translateValue(ClassGenerator classGen, MethodGenerator methodGen)
  {
    if (_select != null) {
      _select.translate(classGen, methodGen);
      

      if ((_select.getType() instanceof NodeSetType)) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        
        int initCNI = cpg.addMethodref("org.apache.xalan.xsltc.dom.CachedNodeListIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;)V");
        



        il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.CachedNodeListIterator")));
        il.append(DUP_X1);
        il.append(SWAP);
        
        il.append(new INVOKESPECIAL(initCNI));
      }
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
}

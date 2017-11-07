package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFEQ;
import org.apache.bcel.generic.IFGE;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;












































final class Key
  extends TopLevelElement
{
  private QName _name;
  private Pattern _match;
  private Expression _use;
  private Type _useType;
  
  Key() {}
  
  public void parseContents(Parser parser)
  {
    String name = getAttribute("name");
    if (!XML11Char.isXML11ValidQName(name)) {
      ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
      parser.reportError(3, err);
    }
    

    _name = parser.getQNameIgnoreDefaultNs(name);
    getSymbolTable().addKey(_name, this);
    
    _match = parser.parsePattern(this, "match", null);
    _use = parser.parseExpression(this, "use", null);
    

    if (_name == null) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
      return;
    }
    if (_match.isDummy()) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "match");
      return;
    }
    if (_use.isDummy()) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "use");
      return;
    }
  }
  



  public String getName()
  {
    return _name.toString();
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError
  {
    _match.typeCheck(stable);
    

    _useType = _use.typeCheck(stable);
    if ((!(_useType instanceof StringType)) && (!(_useType instanceof NodeSetType)))
    {

      _use = new CastExpr(_use, Type.String);
    }
    
    return Type.Void;
  }
  






  public void traverseNodeSet(ClassGenerator classGen, MethodGenerator methodGen, int buildKeyIndex)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    int getNodeValue = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getStringValueX", "(I)Ljava/lang/String;");
    


    int getNodeIdent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
    



    int keyDom = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lorg/apache/xalan/xsltc/DOM;)V");
    






    LocalVariableGen parentNode = methodGen.addLocalVariable("parentNode", Util.getJCRefType("I"), null, null);
    




    parentNode.setStart(il.append(new ISTORE(parentNode.getIndex())));
    

    il.append(methodGen.loadCurrentNode());
    il.append(methodGen.loadIterator());
    

    _use.translate(classGen, methodGen);
    _use.startIterator(classGen, methodGen);
    il.append(methodGen.storeIterator());
    
    BranchHandle nextNode = il.append(new GOTO(null));
    InstructionHandle loop = il.append(NOP);
    

    il.append(classGen.loadTranslet());
    il.append(new PUSH(cpg, _name.toString()));
    parentNode.setEnd(il.append(new ILOAD(parentNode.getIndex())));
    

    il.append(methodGen.loadDOM());
    il.append(methodGen.loadCurrentNode());
    il.append(new INVOKEINTERFACE(getNodeValue, 2));
    

    il.append(new INVOKEVIRTUAL(buildKeyIndex));
    
    il.append(classGen.loadTranslet());
    il.append(new PUSH(cpg, getName()));
    il.append(methodGen.loadDOM());
    il.append(new INVOKEVIRTUAL(keyDom));
    
    nextNode.setTarget(il.append(methodGen.loadIterator()));
    il.append(methodGen.nextNode());
    
    il.append(DUP);
    il.append(methodGen.storeCurrentNode());
    il.append(new IFGE(loop));
    

    il.append(methodGen.storeIterator());
    il.append(methodGen.storeCurrentNode());
  }
  




  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int current = methodGen.getLocalIndex("current");
    

    int key = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "buildKeyIndex", "(Ljava/lang/String;ILjava/lang/Object;)V");
    



    int keyDom = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "setKeyIndexDom", "(Ljava/lang/String;Lorg/apache/xalan/xsltc/DOM;)V");
    


    int getNodeIdent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
    



    int git = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getAxisIterator", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
    


    il.append(methodGen.loadCurrentNode());
    il.append(methodGen.loadIterator());
    

    il.append(methodGen.loadDOM());
    il.append(new PUSH(cpg, 4));
    il.append(new INVOKEINTERFACE(git, 2));
    

    il.append(methodGen.loadCurrentNode());
    il.append(methodGen.setStartNode());
    il.append(methodGen.storeIterator());
    

    BranchHandle nextNode = il.append(new GOTO(null));
    InstructionHandle loop = il.append(NOP);
    

    il.append(methodGen.loadCurrentNode());
    _match.translate(classGen, methodGen);
    _match.synthesize(classGen, methodGen);
    BranchHandle skipNode = il.append(new IFEQ(null));
    

    if ((_useType instanceof NodeSetType))
    {
      il.append(methodGen.loadCurrentNode());
      traverseNodeSet(classGen, methodGen, key);
    }
    else {
      il.append(classGen.loadTranslet());
      il.append(DUP);
      il.append(new PUSH(cpg, _name.toString()));
      il.append(DUP_X1);
      il.append(methodGen.loadCurrentNode());
      _use.translate(classGen, methodGen);
      il.append(new INVOKEVIRTUAL(key));
      
      il.append(methodGen.loadDOM());
      il.append(new INVOKEVIRTUAL(keyDom));
    }
    

    InstructionHandle skip = il.append(NOP);
    
    il.append(methodGen.loadIterator());
    il.append(methodGen.nextNode());
    il.append(DUP);
    il.append(methodGen.storeCurrentNode());
    il.append(new IFGT(loop));
    

    il.append(methodGen.storeIterator());
    il.append(methodGen.storeCurrentNode());
    
    nextNode.setTarget(skip);
    skipNode.setTarget(skip);
  }
}

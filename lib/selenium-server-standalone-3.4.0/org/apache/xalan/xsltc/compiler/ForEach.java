package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFGT;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeSetType;
import org.apache.xalan.xsltc.compiler.util.NodeType;
import org.apache.xalan.xsltc.compiler.util.ReferenceType;
import org.apache.xalan.xsltc.compiler.util.ResultTreeType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
























final class ForEach
  extends Instruction
{
  private Expression _select;
  private Type _type;
  
  ForEach() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("ForEach");
    indent(indent + 4);
    Util.println("select " + _select.toString());
    displayContents(indent + 4);
  }
  
  public void parseContents(Parser parser) {
    _select = parser.parseExpression(this, "select", null);
    
    parseChildren(parser);
    

    if (_select.isDummy()) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "select");
    }
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    _type = _select.typeCheck(stable);
    
    if (((_type instanceof ReferenceType)) || ((_type instanceof NodeType))) {
      _select = new CastExpr(_select, Type.NodeSet);
      typeCheckContents(stable);
      return Type.Void;
    }
    if (((_type instanceof NodeSetType)) || ((_type instanceof ResultTreeType))) {
      typeCheckContents(stable);
      return Type.Void;
    }
    throw new TypeCheckError(this);
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    il.append(methodGen.loadCurrentNode());
    il.append(methodGen.loadIterator());
    

    Vector sortObjects = new Vector();
    Enumeration children = elements();
    while (children.hasMoreElements()) {
      Object child = children.nextElement();
      if ((child instanceof Sort)) {
        sortObjects.addElement(child);
      }
    }
    
    if ((_type != null) && ((_type instanceof ResultTreeType)))
    {
      il.append(methodGen.loadDOM());
      

      if (sortObjects.size() > 0) {
        ErrorMsg msg = new ErrorMsg("RESULT_TREE_SORT_ERR", this);
        getParser().reportError(4, msg);
      }
      

      _select.translate(classGen, methodGen);
      
      _type.translateTo(classGen, methodGen, Type.NodeSet);
      
      il.append(SWAP);
      il.append(methodGen.storeDOM());
    }
    else
    {
      if (sortObjects.size() > 0) {
        Sort.translateSortIterator(classGen, methodGen, _select, sortObjects);
      }
      else
      {
        _select.translate(classGen, methodGen);
      }
      
      if (!(_type instanceof ReferenceType)) {
        il.append(methodGen.loadContextNode());
        il.append(methodGen.setStartNode());
      }
    }
    


    il.append(methodGen.storeIterator());
    

    initializeVariables(classGen, methodGen);
    
    BranchHandle nextNode = il.append(new GOTO(null));
    InstructionHandle loop = il.append(NOP);
    
    translateContents(classGen, methodGen);
    
    nextNode.setTarget(il.append(methodGen.loadIterator()));
    il.append(methodGen.nextNode());
    il.append(DUP);
    il.append(methodGen.storeCurrentNode());
    il.append(new IFGT(loop));
    

    if ((_type != null) && ((_type instanceof ResultTreeType))) {
      il.append(methodGen.storeDOM());
    }
    

    il.append(methodGen.storeIterator());
    il.append(methodGen.storeCurrentNode());
  }
  
















  public void initializeVariables(ClassGenerator classGen, MethodGenerator methodGen)
  {
    int n = elementCount();
    for (int i = 0; i < n; i++) {
      Object child = getContents().elementAt(i);
      if ((child instanceof Variable)) {
        Variable var = (Variable)child;
        var.initialize(classGen, methodGen);
      }
    }
  }
}

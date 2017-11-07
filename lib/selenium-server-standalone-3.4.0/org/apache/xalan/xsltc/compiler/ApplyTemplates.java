package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
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
import org.apache.xml.utils.XML11Char;

























final class ApplyTemplates
  extends Instruction
{
  private Expression _select;
  private Type _type = null;
  private QName _modeName;
  
  ApplyTemplates() {}
  
  public void display(int indent) { indent(indent);
    Util.println("ApplyTemplates");
    indent(indent + 4);
    Util.println("select " + _select.toString());
    if (_modeName != null) {
      indent(indent + 4);
      Util.println("mode " + _modeName);
    }
  }
  
  public boolean hasWithParams() {
    return hasContents();
  }
  
  public void parseContents(Parser parser) {
    String select = getAttribute("select");
    String mode = getAttribute("mode");
    
    if (select.length() > 0) {
      _select = parser.parseExpression(this, "select", null);
    }
    

    if (mode.length() > 0) {
      if (!XML11Char.isXML11ValidQName(mode)) {
        ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", mode, this);
        parser.reportError(3, err);
      }
      _modeName = parser.getQNameIgnoreDefaultNs(mode);
    }
    

    _functionName = parser.getTopLevelStylesheet().getMode(_modeName).functionName();
    
    parseChildren(parser);
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    if (_select != null) {
      _type = _select.typeCheck(stable);
      if (((_type instanceof NodeType)) || ((_type instanceof ReferenceType))) {
        _select = new CastExpr(_select, Type.NodeSet);
        _type = Type.NodeSet;
      }
      if (((_type instanceof NodeSetType)) || ((_type instanceof ResultTreeType))) {
        typeCheckContents(stable);
        return Type.Void;
      }
      throw new TypeCheckError(this);
    }
    
    typeCheckContents(stable);
    return Type.Void;
  }
  


  private String _functionName;
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    boolean setStartNodeCalled = false;
    Stylesheet stylesheet = classGen.getStylesheet();
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int current = methodGen.getLocalIndex("current");
    

    Vector sortObjects = new Vector();
    Enumeration children = elements();
    while (children.hasMoreElements()) {
      Object child = children.nextElement();
      if ((child instanceof Sort)) {
        sortObjects.addElement(child);
      }
    }
    

    if ((stylesheet.hasLocalParams()) || (hasContents())) {
      il.append(classGen.loadTranslet());
      int pushFrame = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
      

      il.append(new INVOKEVIRTUAL(pushFrame));
      
      translateContents(classGen, methodGen);
    }
    

    il.append(classGen.loadTranslet());
    

    if ((_type != null) && ((_type instanceof ResultTreeType)))
    {
      if (sortObjects.size() > 0) {
        ErrorMsg err = new ErrorMsg("RESULT_TREE_SORT_ERR", this);
        getParser().reportError(4, err);
      }
      
      _select.translate(classGen, methodGen);
      
      _type.translateTo(classGen, methodGen, Type.NodeSet);
    }
    else {
      il.append(methodGen.loadDOM());
      

      if (sortObjects.size() > 0) {
        Sort.translateSortIterator(classGen, methodGen, _select, sortObjects);
        
        int setStartNode = cpg.addInterfaceMethodref("org.apache.xml.dtm.DTMAxisIterator", "setStartNode", "(I)Lorg/apache/xml/dtm/DTMAxisIterator;");
        


        il.append(methodGen.loadCurrentNode());
        il.append(new INVOKEINTERFACE(setStartNode, 2));
        setStartNodeCalled = true;

      }
      else if (_select == null) {
        Mode.compileGetChildren(classGen, methodGen, current);
      } else {
        _select.translate(classGen, methodGen);
      }
    }
    
    if ((_select != null) && (!setStartNodeCalled)) {
      _select.startIterator(classGen, methodGen);
    }
    

    String className = classGen.getStylesheet().getClassName();
    il.append(methodGen.loadHandler());
    String applyTemplatesSig = classGen.getApplyTemplatesSig();
    int applyTemplates = cpg.addMethodref(className, _functionName, applyTemplatesSig);
    

    il.append(new INVOKEVIRTUAL(applyTemplates));
    

    if ((stylesheet.hasLocalParams()) || (hasContents())) {
      il.append(classGen.loadTranslet());
      int popFrame = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
      

      il.append(new INVOKEVIRTUAL(popFrame));
    }
  }
}

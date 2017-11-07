package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.AttributeSetMethodGenerator;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;






























final class AttributeSet
  extends TopLevelElement
{
  private static final String AttributeSetPrefix = "$as$";
  private QName _name;
  private UseAttributeSets _useSets;
  private AttributeSet _mergeSet;
  private String _method;
  private boolean _ignore = false;
  
  AttributeSet() {}
  
  public QName getName()
  {
    return _name;
  }
  



  public String getMethodName()
  {
    return _method;
  }
  





  public void ignore()
  {
    _ignore = true;
  }
  





  public void parseContents(Parser parser)
  {
    String name = getAttribute("name");
    
    if (!XML11Char.isXML11ValidQName(name)) {
      ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
      parser.reportError(3, err);
    }
    _name = parser.getQNameIgnoreDefaultNs(name);
    if ((_name == null) || (_name.equals(""))) {
      ErrorMsg msg = new ErrorMsg("UNNAMED_ATTRIBSET_ERR", this);
      parser.reportError(3, msg);
    }
    

    String useSets = getAttribute("use-attribute-sets");
    if (useSets.length() > 0) {
      if (!Util.isValidQNames(useSets)) {
        ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", useSets, this);
        parser.reportError(3, err);
      }
      _useSets = new UseAttributeSets(useSets, parser);
    }
    


    Vector contents = getContents();
    int count = contents.size();
    for (int i = 0; i < count; i++) {
      SyntaxTreeNode child = (SyntaxTreeNode)contents.elementAt(i);
      if ((child instanceof XslAttribute)) {
        parser.getSymbolTable().setCurrentNode(child);
        child.parseContents(parser);
      }
      else if (!(child instanceof Text))
      {


        ErrorMsg msg = new ErrorMsg("ILLEGAL_CHILD_ERR", this);
        parser.reportError(3, msg);
      }
    }
    

    parser.getSymbolTable().setCurrentNode(this);
  }
  


  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    if (_ignore) { return Type.Void;
    }
    
    _mergeSet = stable.addAttributeSet(this);
    
    _method = ("$as$" + getXSLTC().nextAttributeSetSerial());
    
    if (_useSets != null) _useSets.typeCheck(stable);
    typeCheckContents(stable);
    return Type.Void;
  }
  



  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    if (_ignore) { return;
    }
    
    methodGen = new AttributeSetMethodGenerator(_method, classGen);
    


    if (_mergeSet != null) {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      String methodName = _mergeSet.getMethodName();
      
      il.append(classGen.loadTranslet());
      il.append(methodGen.loadDOM());
      il.append(methodGen.loadIterator());
      il.append(methodGen.loadHandler());
      int method = cpg.addMethodref(classGen.getClassName(), methodName, ATTR_SET_SIG);
      
      il.append(new INVOKESPECIAL(method));
    }
    


    if (_useSets != null) { _useSets.translate(classGen, methodGen);
    }
    
    Enumeration attributes = elements();
    while (attributes.hasMoreElements()) {
      SyntaxTreeNode element = (SyntaxTreeNode)attributes.nextElement();
      if ((element instanceof XslAttribute)) {
        XslAttribute attribute = (XslAttribute)element;
        attribute.translate(classGen, methodGen);
      }
    }
    InstructionList il = methodGen.getInstructionList();
    il.append(RETURN);
    
    classGen.addMethod(methodGen);
  }
  
  public String toString() {
    StringBuffer buf = new StringBuffer("attribute-set: ");
    
    Enumeration attributes = elements();
    while (attributes.hasMoreElements()) {
      XslAttribute attribute = (XslAttribute)attributes.nextElement();
      
      buf.append(attribute);
    }
    return buf.toString();
  }
}

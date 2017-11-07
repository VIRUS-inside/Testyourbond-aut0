package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;

























final class XslElement
  extends Instruction
{
  private String _prefix;
  private boolean _ignore = false;
  private boolean _isLiteralName = true;
  private AttributeValueTemplate _name;
  private AttributeValueTemplate _namespace;
  
  XslElement() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("Element " + _name);
    displayContents(indent + 4);
  }
  



  public boolean declaresDefaultNS()
  {
    return false;
  }
  
  public void parseContents(Parser parser) {
    SymbolTable stable = parser.getSymbolTable();
    

    String name = getAttribute("name");
    if (name == "") {
      ErrorMsg msg = new ErrorMsg("ILLEGAL_ELEM_NAME_ERR", name, this);
      
      parser.reportError(4, msg);
      parseChildren(parser);
      _ignore = true;
      return;
    }
    

    String namespace = getAttribute("namespace");
    

    _isLiteralName = Util.isLiteral(name);
    if (_isLiteralName) {
      if (!XML11Char.isXML11ValidQName(name)) {
        ErrorMsg msg = new ErrorMsg("ILLEGAL_ELEM_NAME_ERR", name, this);
        
        parser.reportError(4, msg);
        parseChildren(parser);
        _ignore = true;
        return;
      }
      
      QName qname = parser.getQNameSafe(name);
      String prefix = qname.getPrefix();
      String local = qname.getLocalPart();
      
      if (prefix == null) {
        prefix = "";
      }
      
      if (!hasAttribute("namespace")) {
        namespace = lookupNamespace(prefix);
        if (namespace == null) {
          ErrorMsg err = new ErrorMsg("NAMESPACE_UNDEF_ERR", prefix, this);
          
          parser.reportError(4, err);
          parseChildren(parser);
          _ignore = true;
          return;
        }
        _prefix = prefix;
        _namespace = new AttributeValueTemplate(namespace, parser, this);
      }
      else {
        if (prefix == "") {
          if (Util.isLiteral(namespace)) {
            prefix = lookupPrefix(namespace);
            if (prefix == null) {
              prefix = stable.generateNamespacePrefix();
            }
          }
          

          StringBuffer newName = new StringBuffer(prefix);
          if (prefix != "") {
            newName.append(':');
          }
          name = local;
        }
        _prefix = prefix;
        _namespace = new AttributeValueTemplate(namespace, parser, this);
      }
      

    }
    else
    {
      _namespace = (namespace == "" ? null : new AttributeValueTemplate(namespace, parser, this));
    }
    

    _name = new AttributeValueTemplate(name, parser, this);
    
    String useSets = getAttribute("use-attribute-sets");
    if (useSets.length() > 0) {
      if (!Util.isValidQNames(useSets)) {
        ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", useSets, this);
        parser.reportError(3, err);
      }
      setFirstElement(new UseAttributeSets(useSets, parser));
    }
    
    parseChildren(parser);
  }
  

  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    if (!_ignore) {
      _name.typeCheck(stable);
      if (_namespace != null) {
        _namespace.typeCheck(stable);
      }
    }
    typeCheckContents(stable);
    return Type.Void;
  }
  




  public void translateLiteral(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    if (!_ignore) {
      il.append(methodGen.loadHandler());
      _name.translate(classGen, methodGen);
      il.append(DUP2);
      il.append(methodGen.startElement());
      
      if (_namespace != null) {
        il.append(methodGen.loadHandler());
        il.append(new PUSH(cpg, _prefix));
        _namespace.translate(classGen, methodGen);
        il.append(methodGen.namespace());
      }
    }
    
    translateContents(classGen, methodGen);
    
    if (!_ignore) {
      il.append(methodGen.endElement());
    }
  }
  







  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    LocalVariableGen local = null;
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    if (_isLiteralName) {
      translateLiteral(classGen, methodGen);
      return;
    }
    
    if (!_ignore)
    {

      LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
      




      _name.translate(classGen, methodGen);
      nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
      il.append(new ALOAD(nameValue.getIndex()));
      

      int check = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkQName", "(Ljava/lang/String;)V");
      


      il.append(new INVOKESTATIC(check));
      

      il.append(methodGen.loadHandler());
      

      nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
      
      if (_namespace != null) {
        _namespace.translate(classGen, methodGen);



      }
      else
      {


        String transletClassName = getXSLTC().getClassName();
        il.append(DUP);
        il.append(new PUSH(cpg, getNodeIDForStylesheetNSLookup()));
        il.append(new GETSTATIC(cpg.addFieldref(transletClassName, "_sNamespaceAncestorsArray", "[I")));
        


        il.append(new GETSTATIC(cpg.addFieldref(transletClassName, "_sPrefixURIsIdxArray", "[I")));
        


        il.append(new GETSTATIC(cpg.addFieldref(transletClassName, "_sPrefixURIPairsArray", "[Ljava/lang/String;")));
        



        il.append(ICONST_0);
        il.append(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "lookupStylesheetQNameNamespace", "(Ljava/lang/String;I[I[I[Ljava/lang/String;Z)Ljava/lang/String;")));
      }
      





      il.append(methodGen.loadHandler());
      il.append(methodGen.loadDOM());
      il.append(methodGen.loadCurrentNode());
      

      il.append(new INVOKESTATIC(cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "startXslElement", "(Ljava/lang/String;Ljava/lang/String;" + TRANSLET_OUTPUT_SIG + "Lorg/apache/xalan/xsltc/DOM;" + "I)" + "Ljava/lang/String;")));
    }
    







    translateContents(classGen, methodGen);
    
    if (!_ignore) {
      il.append(methodGen.endElement());
    }
  }
  




  public void translateContents(ClassGenerator classGen, MethodGenerator methodGen)
  {
    int n = elementCount();
    for (int i = 0; i < n; i++) {
      SyntaxTreeNode item = (SyntaxTreeNode)getContents().elementAt(i);
      
      if ((!_ignore) || (!(item instanceof XslAttribute))) {
        item.translate(classGen, methodGen);
      }
    }
  }
}

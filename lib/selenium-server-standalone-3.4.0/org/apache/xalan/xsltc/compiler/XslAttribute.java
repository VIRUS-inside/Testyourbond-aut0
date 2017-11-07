package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.serializer.ElemDesc;
import org.apache.xml.utils.XML11Char;
































final class XslAttribute
  extends Instruction
{
  private String _prefix;
  private AttributeValue _name;
  private AttributeValueTemplate _namespace = null;
  private boolean _ignore = false;
  private boolean _isLiteral = false;
  
  XslAttribute() {}
  
  public AttributeValue getName()
  {
    return _name;
  }
  


  public void display(int indent)
  {
    indent(indent);
    Util.println("Attribute " + _name);
    displayContents(indent + 4);
  }
  


  public void parseContents(Parser parser)
  {
    boolean generated = false;
    SymbolTable stable = parser.getSymbolTable();
    
    String name = getAttribute("name");
    String namespace = getAttribute("namespace");
    QName qname = parser.getQName(name, false);
    String prefix = qname.getPrefix();
    
    if (((prefix != null) && (prefix.equals("xmlns"))) || (name.equals("xmlns"))) {
      reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", name);
      return;
    }
    
    _isLiteral = Util.isLiteral(name);
    if ((_isLiteral) && 
      (!XML11Char.isXML11ValidQName(name))) {
      reportError(this, parser, "ILLEGAL_ATTR_NAME_ERR", name);
      return;
    }
    


    SyntaxTreeNode parent = getParent();
    Vector siblings = parent.getContents();
    for (int i = 0; i < parent.elementCount(); i++) {
      SyntaxTreeNode item = (SyntaxTreeNode)siblings.elementAt(i);
      if (item == this) {
        break;
      }
      if ((!(item instanceof XslAttribute)) && 
        (!(item instanceof UseAttributeSets)) && 
        (!(item instanceof LiteralAttribute)) && 
        (!(item instanceof Text)))
      {


        if ((!(item instanceof If)) && 
          (!(item instanceof Choose)) && 
          (!(item instanceof CopyOf)) && 
          (!(item instanceof VariableBase)))
        {

          reportWarning(this, parser, "STRAY_ATTRIBUTE_ERR", name);
        }
      }
    }
    if ((namespace != null) && (namespace != "")) {
      _prefix = lookupPrefix(namespace);
      _namespace = new AttributeValueTemplate(namespace, parser, this);

    }
    else if ((prefix != null) && (prefix != "")) {
      _prefix = prefix;
      namespace = lookupNamespace(prefix);
      if (namespace != null) {
        _namespace = new AttributeValueTemplate(namespace, parser, this);
      }
    }
    

    if (_namespace != null)
    {
      if ((_prefix == null) || (_prefix == "")) {
        if (prefix != null) {
          _prefix = prefix;
        }
        else {
          _prefix = stable.generateNamespacePrefix();
          generated = true;
        }
      }
      else if ((prefix != null) && (!prefix.equals(_prefix))) {
        _prefix = prefix;
      }
      
      name = _prefix + ":" + qname.getLocalPart();
      





      if (((parent instanceof LiteralElement)) && (!generated)) {
        ((LiteralElement)parent).registerNamespace(_prefix, namespace, stable, false);
      }
    }
    


    if ((parent instanceof LiteralElement)) {
      ((LiteralElement)parent).addAttribute(this);
    }
    
    _name = AttributeValue.create(this, name, parser);
    parseChildren(parser);
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    if (!_ignore) {
      _name.typeCheck(stable);
      if (_namespace != null) {
        _namespace.typeCheck(stable);
      }
      typeCheckContents(stable);
    }
    return Type.Void;
  }
  


  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    if (_ignore) return;
    _ignore = true;
    

    if (_namespace != null)
    {
      il.append(methodGen.loadHandler());
      il.append(new PUSH(cpg, _prefix));
      _namespace.translate(classGen, methodGen);
      il.append(methodGen.namespace());
    }
    
    if (!_isLiteral)
    {
      LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
      




      _name.translate(classGen, methodGen);
      nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
      il.append(new ALOAD(nameValue.getIndex()));
      

      int check = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkAttribQName", "(Ljava/lang/String;)V");
      


      il.append(new INVOKESTATIC(check));
      

      il.append(methodGen.loadHandler());
      il.append(DUP);
      

      nameValue.setEnd(il.append(new ALOAD(nameValue.getIndex())));
    }
    else {
      il.append(methodGen.loadHandler());
      il.append(DUP);
      

      _name.translate(classGen, methodGen);
    }
    


    if ((elementCount() == 1) && ((elementAt(0) instanceof Text))) {
      il.append(new PUSH(cpg, ((Text)elementAt(0)).getText()));
    }
    else {
      il.append(classGen.loadTranslet());
      il.append(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;")));
      

      il.append(DUP);
      il.append(methodGen.storeHandler());
      
      translateContents(classGen, methodGen);
      
      il.append(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValue", "()Ljava/lang/String;")));
    }
    


    SyntaxTreeNode parent = getParent();
    if (((parent instanceof LiteralElement)) && (((LiteralElement)parent).allAttributesUnique()))
    {
      int flags = 0;
      ElemDesc elemDesc = ((LiteralElement)parent).getElemDesc();
      

      if ((elemDesc != null) && ((_name instanceof SimpleAttributeValue))) {
        String attrName = ((SimpleAttributeValue)_name).toString();
        if (elemDesc.isAttrFlagSet(attrName, 4)) {
          flags |= 0x2;
        }
        else if (elemDesc.isAttrFlagSet(attrName, 2)) {
          flags |= 0x4;
        }
      }
      il.append(new PUSH(cpg, flags));
      il.append(methodGen.uniqueAttribute());
    }
    else
    {
      il.append(methodGen.attribute());
    }
    

    il.append(methodGen.storeHandler());
  }
}

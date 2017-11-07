package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.utils.XML11Char;






















final class ProcessingInstruction
  extends Instruction
{
  private AttributeValue _name;
  
  ProcessingInstruction() {}
  
  private boolean _isLiteral = false;
  
  public void parseContents(Parser parser) {
    String name = getAttribute("name");
    
    if (name.length() > 0) {
      _isLiteral = Util.isLiteral(name);
      if ((_isLiteral) && 
        (!XML11Char.isXML11ValidNCName(name))) {
        ErrorMsg err = new ErrorMsg("INVALID_NCNAME_ERR", name, this);
        parser.reportError(3, err);
      }
      
      _name = AttributeValue.create(this, name, parser);
    }
    else {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "name");
    }
    if (name.equals("xml")) {
      reportError(this, parser, "ILLEGAL_PI_ERR", "xml");
    }
    parseChildren(parser);
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    _name.typeCheck(stable);
    typeCheckContents(stable);
    return Type.Void;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    if (!_isLiteral)
    {
      LocalVariableGen nameValue = methodGen.addLocalVariable2("nameValue", Util.getJCRefType("Ljava/lang/String;"), null);
      




      _name.translate(classGen, methodGen);
      nameValue.setStart(il.append(new ASTORE(nameValue.getIndex())));
      il.append(new ALOAD(nameValue.getIndex()));
      

      int check = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "checkNCName", "(Ljava/lang/String;)V");
      


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
    

    il.append(classGen.loadTranslet());
    il.append(new GETFIELD(cpg.addFieldref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "stringValueHandler", "Lorg/apache/xalan/xsltc/runtime/StringValueHandler;")));
    

    il.append(DUP);
    il.append(methodGen.storeHandler());
    

    translateContents(classGen, methodGen);
    

    il.append(new INVOKEVIRTUAL(cpg.addMethodref("org.apache.xalan.xsltc.runtime.StringValueHandler", "getValueOfPI", "()Ljava/lang/String;")));
    


    int processingInstruction = cpg.addInterfaceMethodref(TRANSLET_OUTPUT_INTERFACE, "processingInstruction", "(Ljava/lang/String;Ljava/lang/String;)V");
    


    il.append(new INVOKEINTERFACE(processingInstruction, 3));
    
    il.append(methodGen.storeHandler());
  }
}

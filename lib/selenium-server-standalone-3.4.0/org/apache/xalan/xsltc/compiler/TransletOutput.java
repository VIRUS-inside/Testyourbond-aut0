package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.StringType;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;

























final class TransletOutput
  extends Instruction
{
  private Expression _filename;
  private boolean _append;
  
  TransletOutput() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("TransletOutput: " + _filename);
  }
  




  public void parseContents(Parser parser)
  {
    String filename = getAttribute("file");
    


    String append = getAttribute("append");
    

    if ((filename == null) || (filename.equals(""))) {
      reportError(this, parser, "REQUIRED_ATTR_ERR", "file");
    }
    

    _filename = AttributeValue.create(this, filename, parser);
    
    if ((append != null) && ((append.toLowerCase().equals("yes")) || (append.toLowerCase().equals("true"))))
    {
      _append = true;
    }
    else {
      _append = false;
    }
    parseChildren(parser);
  }
  

  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    Type type = _filename.typeCheck(stable);
    if (!(type instanceof StringType)) {
      _filename = new CastExpr(_filename, Type.String);
    }
    typeCheckContents(stable);
    return Type.Void;
  }
  



  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    boolean isSecureProcessing = classGen.getParser().getXSLTC().isSecureProcessing();
    

    if (isSecureProcessing) {
      int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.BasisLibrary", "unallowed_extension_elementF", "(Ljava/lang/String;)V");
      

      il.append(new PUSH(cpg, "redirect"));
      il.append(new INVOKESTATIC(index));
      return;
    }
    

    il.append(methodGen.loadHandler());
    
    int open = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "openOutputHandler", "(Ljava/lang/String;Z)" + TRANSLET_OUTPUT_SIG);
    



    int close = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "closeOutputHandler", "(" + TRANSLET_OUTPUT_SIG + ")V");
    



    il.append(classGen.loadTranslet());
    _filename.translate(classGen, methodGen);
    il.append(new PUSH(cpg, _append));
    il.append(new INVOKEVIRTUAL(open));
    

    il.append(methodGen.storeHandler());
    

    translateContents(classGen, methodGen);
    

    il.append(classGen.loadTranslet());
    il.append(methodGen.loadHandler());
    il.append(new INVOKEVIRTUAL(close));
    

    il.append(methodGen.storeHandler());
  }
}

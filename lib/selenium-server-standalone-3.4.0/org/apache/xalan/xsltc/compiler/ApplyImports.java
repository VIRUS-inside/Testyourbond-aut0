package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;


















final class ApplyImports
  extends Instruction
{
  private QName _modeName;
  private int _precedence;
  
  ApplyImports() {}
  
  public void display(int indent)
  {
    indent(indent);
    Util.println("ApplyTemplates");
    indent(indent + 4);
    if (_modeName != null) {
      indent(indent + 4);
      Util.println("mode " + _modeName);
    }
  }
  


  public boolean hasWithParams()
  {
    return hasContents();
  }
  







  private int getMinPrecedence(int max)
  {
    Stylesheet includeRoot = getStylesheet();
    while (_includedFrom != null) {
      includeRoot = _includedFrom;
    }
    
    return includeRoot.getMinimumDescendantPrecedence();
  }
  




  public void parseContents(Parser parser)
  {
    Stylesheet stylesheet = getStylesheet();
    stylesheet.setTemplateInlining(false);
    

    Template template = getTemplate();
    _modeName = template.getModeName();
    _precedence = template.getImportPrecedence();
    
    parseChildren(parser);
  }
  

  public Type typeCheck(SymbolTable stable)
    throws TypeCheckError
  {
    typeCheckContents(stable);
    return Type.Void;
  }
  



  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    Stylesheet stylesheet = classGen.getStylesheet();
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int current = methodGen.getLocalIndex("current");
    

    il.append(classGen.loadTranslet());
    il.append(methodGen.loadDOM());
    il.append(methodGen.loadIterator());
    il.append(methodGen.loadHandler());
    il.append(methodGen.loadCurrentNode());
    


    if (stylesheet.hasLocalParams()) {
      il.append(classGen.loadTranslet());
      int pushFrame = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "pushParamFrame", "()V");
      

      il.append(new INVOKEVIRTUAL(pushFrame));
    }
    


    int maxPrecedence = _precedence;
    int minPrecedence = getMinPrecedence(maxPrecedence);
    Mode mode = stylesheet.getMode(_modeName);
    


    String functionName = mode.functionName(minPrecedence, maxPrecedence);
    

    String className = classGen.getStylesheet().getClassName();
    String signature = classGen.getApplyTemplatesSigForImport();
    int applyTemplates = cpg.addMethodref(className, functionName, signature);
    

    il.append(new INVOKEVIRTUAL(applyTemplates));
    

    if (stylesheet.hasLocalParams()) {
      il.append(classGen.loadTranslet());
      int pushFrame = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "popParamFrame", "()V");
      

      il.append(new INVOKEVIRTUAL(pushFrame));
    }
  }
}

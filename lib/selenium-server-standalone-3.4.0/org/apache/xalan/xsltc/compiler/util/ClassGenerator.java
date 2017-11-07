package org.apache.xalan.xsltc.compiler.util;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.Instruction;
import org.apache.xalan.xsltc.compiler.Constants;
import org.apache.xalan.xsltc.compiler.Parser;
import org.apache.xalan.xsltc.compiler.Stylesheet;








































public class ClassGenerator
  extends ClassGen
{
  protected static final int TRANSLET_INDEX = 0;
  private Stylesheet _stylesheet;
  private final Parser _parser;
  private final Instruction _aloadTranslet;
  private final String _domClass;
  private final String _domClassSig;
  private final String _applyTemplatesSig;
  private final String _applyTemplatesSigForImport;
  
  public ClassGenerator(String class_name, String super_class_name, String file_name, int access_flags, String[] interfaces, Stylesheet stylesheet)
  {
    super(class_name, super_class_name, file_name, access_flags, interfaces);
    
    _stylesheet = stylesheet;
    _parser = stylesheet.getParser();
    _aloadTranslet = new ALOAD(0);
    
    if (stylesheet.isMultiDocument()) {
      _domClass = "org.apache.xalan.xsltc.dom.MultiDOM";
      _domClassSig = "Lorg/apache/xalan/xsltc/dom/MultiDOM;";
    }
    else {
      _domClass = "org.apache.xalan.xsltc.dom.DOMAdapter";
      _domClassSig = "Lorg/apache/xalan/xsltc/dom/DOMAdapter;";
    }
    _applyTemplatesSig = ("(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + Constants.TRANSLET_OUTPUT_SIG + ")V");
    




    _applyTemplatesSigForImport = ("(Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;" + Constants.TRANSLET_OUTPUT_SIG + "I" + ")V");
  }
  




  public final Parser getParser()
  {
    return _parser;
  }
  
  public final Stylesheet getStylesheet() {
    return _stylesheet;
  }
  



  public final String getClassName()
  {
    return _stylesheet.getClassName();
  }
  
  public Instruction loadTranslet() {
    return _aloadTranslet;
  }
  
  public final String getDOMClass() {
    return _domClass;
  }
  
  public final String getDOMClassSig() {
    return _domClassSig;
  }
  
  public final String getApplyTemplatesSig() {
    return _applyTemplatesSig;
  }
  
  public final String getApplyTemplatesSigForImport() {
    return _applyTemplatesSigForImport;
  }
  



  public boolean isExternal()
  {
    return false;
  }
  
  public void addMethod(MethodGenerator methodGen) {
    Method[] methodsToAdd = methodGen.getGeneratedMethods(this);
    for (int i = 0; i < methodsToAdd.length; i++) {
      addMethod(methodsToAdd[i]);
    }
  }
}

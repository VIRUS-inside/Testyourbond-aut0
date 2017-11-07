package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETSTATIC;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.ErrorMsg;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.runtime.AttributeList;
import org.apache.xml.utils.XML11Char;


























final class DecimalFormatting
  extends TopLevelElement
{
  private static final String DFS_CLASS = "java.text.DecimalFormatSymbols";
  private static final String DFS_SIG = "Ljava/text/DecimalFormatSymbols;";
  private QName _name = null;
  
  DecimalFormatting() {}
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError
  {
    return Type.Void;
  }
  



  public void parseContents(Parser parser)
  {
    String name = getAttribute("name");
    if ((name.length() > 0) && 
      (!XML11Char.isXML11ValidQName(name))) {
      ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", name, this);
      parser.reportError(3, err);
    }
    
    _name = parser.getQNameIgnoreDefaultNs(name);
    if (_name == null) {
      _name = parser.getQNameIgnoreDefaultNs("");
    }
    

    SymbolTable stable = parser.getSymbolTable();
    if (stable.getDecimalFormatting(_name) != null) {
      reportWarning(this, parser, "SYMBOLS_REDEF_ERR", _name.toString());
    }
    else
    {
      stable.addDecimalFormatting(_name, this);
    }
  }
  




  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    


    int init = cpg.addMethodref("java.text.DecimalFormatSymbols", "<init>", "(Ljava/util/Locale;)V");
    


    il.append(classGen.loadTranslet());
    il.append(new PUSH(cpg, _name.toString()));
    




    il.append(new NEW(cpg.addClass("java.text.DecimalFormatSymbols")));
    il.append(DUP);
    il.append(new GETSTATIC(cpg.addFieldref("java.util.Locale", "US", "Ljava/util/Locale;")));
    
    il.append(new INVOKESPECIAL(init));
    
    String tmp = getAttribute("NaN");
    if ((tmp == null) || (tmp.equals(""))) {
      int nan = cpg.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
      
      il.append(DUP);
      il.append(new PUSH(cpg, "NaN"));
      il.append(new INVOKEVIRTUAL(nan));
    }
    
    tmp = getAttribute("infinity");
    if ((tmp == null) || (tmp.equals(""))) {
      int inf = cpg.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
      

      il.append(DUP);
      il.append(new PUSH(cpg, "Infinity"));
      il.append(new INVOKEVIRTUAL(inf));
    }
    
    int nAttributes = _attributes.getLength();
    for (int i = 0; i < nAttributes; i++) {
      String name = _attributes.getQName(i);
      String value = _attributes.getValue(i);
      
      boolean valid = true;
      int method = 0;
      
      if (name.equals("decimal-separator"))
      {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setDecimalSeparator", "(C)V");

      }
      else if (name.equals("grouping-separator")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setGroupingSeparator", "(C)V");

      }
      else if (name.equals("minus-sign")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setMinusSign", "(C)V");

      }
      else if (name.equals("percent")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setPercent", "(C)V");

      }
      else if (name.equals("per-mille")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setPerMill", "(C)V");

      }
      else if (name.equals("zero-digit")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setZeroDigit", "(C)V");

      }
      else if (name.equals("digit")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setDigit", "(C)V");

      }
      else if (name.equals("pattern-separator")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setPatternSeparator", "(C)V");

      }
      else if (name.equals("NaN")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
        
        il.append(DUP);
        il.append(new PUSH(cpg, value));
        il.append(new INVOKEVIRTUAL(method));
        valid = false;
      }
      else if (name.equals("infinity")) {
        method = cpg.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
        

        il.append(DUP);
        il.append(new PUSH(cpg, value));
        il.append(new INVOKEVIRTUAL(method));
        valid = false;
      }
      else {
        valid = false;
      }
      
      if (valid) {
        il.append(DUP);
        il.append(new PUSH(cpg, value.charAt(0)));
        il.append(new INVOKEVIRTUAL(method));
      }
    }
    

    int put = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
    

    il.append(new INVOKEVIRTUAL(put));
  }
  







  public static void translateDefaultDFS(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    int init = cpg.addMethodref("java.text.DecimalFormatSymbols", "<init>", "(Ljava/util/Locale;)V");
    



    il.append(classGen.loadTranslet());
    il.append(new PUSH(cpg, ""));
    





    il.append(new NEW(cpg.addClass("java.text.DecimalFormatSymbols")));
    il.append(DUP);
    il.append(new GETSTATIC(cpg.addFieldref("java.util.Locale", "US", "Ljava/util/Locale;")));
    
    il.append(new INVOKESPECIAL(init));
    
    int nan = cpg.addMethodref("java.text.DecimalFormatSymbols", "setNaN", "(Ljava/lang/String;)V");
    
    il.append(DUP);
    il.append(new PUSH(cpg, "NaN"));
    il.append(new INVOKEVIRTUAL(nan));
    
    int inf = cpg.addMethodref("java.text.DecimalFormatSymbols", "setInfinity", "(Ljava/lang/String;)V");
    

    il.append(DUP);
    il.append(new PUSH(cpg, "Infinity"));
    il.append(new INVOKEVIRTUAL(inf));
    
    int put = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "addDecimalFormat", "(Ljava/lang/String;Ljava/text/DecimalFormatSymbols;)V");
    

    il.append(new INVOKEVIRTUAL(put));
  }
}

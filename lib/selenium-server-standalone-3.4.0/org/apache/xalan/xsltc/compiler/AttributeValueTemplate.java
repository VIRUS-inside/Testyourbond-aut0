package org.apache.xalan.xsltc.compiler;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;




























final class AttributeValueTemplate
  extends AttributeValue
{
  static final int OUT_EXPR = 0;
  static final int IN_EXPR = 1;
  static final int IN_EXPR_SQUOTES = 2;
  static final int IN_EXPR_DQUOTES = 3;
  static final String DELIMITER = "￾";
  
  public AttributeValueTemplate(String value, Parser parser, SyntaxTreeNode parent)
  {
    setParent(parent);
    setParser(parser);
    try
    {
      parseAVTemplate(value, parser);
    }
    catch (NoSuchElementException e) {
      reportError(parent, parser, "ATTR_VAL_TEMPLATE_ERR", value);
    }
  }
  






  private void parseAVTemplate(String text, Parser parser)
  {
    StringTokenizer tokenizer = new StringTokenizer(text, "{}\"'", true);
    






    String t = null;
    String lookahead = null;
    StringBuffer buffer = new StringBuffer();
    int state = 0;
    
    while (tokenizer.hasMoreTokens())
    {
      if (lookahead != null) {
        t = lookahead;
        lookahead = null;
      }
      else {
        t = tokenizer.nextToken();
      }
      
      if (t.length() == 1) {
        switch (t.charAt(0)) {
        case '{': 
          switch (state) {
          case 0: 
            lookahead = tokenizer.nextToken();
            if (lookahead.equals("{")) {
              buffer.append(lookahead);
              lookahead = null;
            }
            else {
              buffer.append("￾");
              state = 1;
            }
            break;
          case 1: 
          case 2: 
          case 3: 
            reportError(getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
          }
          
          
          break;
        case '}': 
          switch (state) {
          case 0: 
            lookahead = tokenizer.nextToken();
            if (lookahead.equals("}")) {
              buffer.append(lookahead);
              lookahead = null;
            }
            else {
              reportError(getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
            }
            
            break;
          case 1: 
            buffer.append("￾");
            state = 0;
            break;
          case 2: 
          case 3: 
            buffer.append(t);
          }
          
          break;
        case '\'': 
          switch (state) {
          case 1: 
            state = 2;
            break;
          case 2: 
            state = 1;
            break;
          }
          
          

          buffer.append(t);
          break;
        case '"': 
          switch (state) {
          case 1: 
            state = 3;
            break;
          case 3: 
            state = 1;
            break;
          }
          
          

          buffer.append(t);
          break;
        default: 
          buffer.append(t);
          break;
        }
        
      } else {
        buffer.append(t);
      }
    }
    

    if (state != 0) {
      reportError(getParent(), parser, "ATTR_VAL_TEMPLATE_ERR", text);
    }
    




    tokenizer = new StringTokenizer(buffer.toString(), "￾", true);
    
    while (tokenizer.hasMoreTokens()) {
      t = tokenizer.nextToken();
      
      if (t.equals("￾")) {
        addElement(parser.parseExpression(this, tokenizer.nextToken()));
        tokenizer.nextToken();
      }
      else {
        addElement(new LiteralExpr(t));
      }
    }
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    Vector contents = getContents();
    int n = contents.size();
    for (int i = 0; i < n; i++) {
      Expression exp = (Expression)contents.elementAt(i);
      if (!exp.typeCheck(stable).identicalTo(Type.String)) {
        contents.setElementAt(new CastExpr(exp, Type.String), i);
      }
    }
    return this._type = Type.String;
  }
  
  public String toString() {
    StringBuffer buffer = new StringBuffer("AVT:[");
    int count = elementCount();
    for (int i = 0; i < count; i++) {
      buffer.append(elementAt(i).toString());
      if (i < count - 1)
        buffer.append(' ');
    }
    return ']';
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    if (elementCount() == 1) {
      Expression exp = (Expression)elementAt(0);
      exp.translate(classGen, methodGen);
    }
    else {
      ConstantPoolGen cpg = classGen.getConstantPool();
      InstructionList il = methodGen.getInstructionList();
      int initBuffer = cpg.addMethodref("java.lang.StringBuffer", "<init>", "()V");
      
      Instruction append = new INVOKEVIRTUAL(cpg.addMethodref("java.lang.StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
      




      int toString = cpg.addMethodref("java.lang.StringBuffer", "toString", "()Ljava/lang/String;");
      

      il.append(new NEW(cpg.addClass("java.lang.StringBuffer")));
      il.append(DUP);
      il.append(new INVOKESPECIAL(initBuffer));
      
      Enumeration elements = elements();
      while (elements.hasMoreElements()) {
        Expression exp = (Expression)elements.nextElement();
        exp.translate(classGen, methodGen);
        il.append(append);
      }
      il.append(new INVOKEVIRTUAL(toString));
    }
  }
}

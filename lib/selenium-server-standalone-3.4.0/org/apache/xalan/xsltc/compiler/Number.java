package org.apache.xalan.xsltc.compiler;

import java.util.ArrayList;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.CHECKCAST;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKESTATIC;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MatchGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.NodeCounterGenerator;
import org.apache.xalan.xsltc.compiler.util.RealType;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xalan.xsltc.runtime.AttributeList;

























final class Number
  extends Instruction
  implements Closure
{
  private static final int LEVEL_SINGLE = 0;
  private static final int LEVEL_MULTIPLE = 1;
  private static final int LEVEL_ANY = 2;
  private static final String[] ClassNames = { "org.apache.xalan.xsltc.dom.SingleNodeCounter", "org.apache.xalan.xsltc.dom.MultipleNodeCounter", "org.apache.xalan.xsltc.dom.AnyNodeCounter" };
  




  private static final String[] FieldNames = { "___single_node_counter", "___multiple_node_counter", "___any_node_counter" };
  




  private Pattern _from = null;
  private Pattern _count = null;
  private Expression _value = null;
  
  private AttributeValueTemplate _lang = null;
  private AttributeValueTemplate _format = null;
  private AttributeValueTemplate _letterValue = null;
  private AttributeValueTemplate _groupingSeparator = null;
  private AttributeValueTemplate _groupingSize = null;
  
  private int _level = 0;
  private boolean _formatNeeded = false;
  
  private String _className = null;
  private ArrayList _closureVars = null;
  


  Number() {}
  

  public boolean inInnerClass()
  {
    return _className != null;
  }
  


  public Closure getParentClosure()
  {
    return null;
  }
  



  public String getInnerClassName()
  {
    return _className;
  }
  


  public void addVariable(VariableRefBase variableRef)
  {
    if (_closureVars == null) {
      _closureVars = new ArrayList();
    }
    

    if (!_closureVars.contains(variableRef)) {
      _closureVars.add(variableRef);
    }
  }
  

  public void parseContents(Parser parser)
  {
    int count = _attributes.getLength();
    
    for (int i = 0; i < count; i++) {
      String name = _attributes.getQName(i);
      String value = _attributes.getValue(i);
      
      if (name.equals("value")) {
        _value = parser.parseExpression(this, name, null);
      }
      else if (name.equals("count")) {
        _count = parser.parsePattern(this, name, null);
      }
      else if (name.equals("from")) {
        _from = parser.parsePattern(this, name, null);
      }
      else if (name.equals("level")) {
        if (value.equals("single")) {
          _level = 0;
        }
        else if (value.equals("multiple")) {
          _level = 1;
        }
        else if (value.equals("any")) {
          _level = 2;
        }
      }
      else if (name.equals("format")) {
        _format = new AttributeValueTemplate(value, parser, this);
        _formatNeeded = true;
      }
      else if (name.equals("lang")) {
        _lang = new AttributeValueTemplate(value, parser, this);
        _formatNeeded = true;
      }
      else if (name.equals("letter-value")) {
        _letterValue = new AttributeValueTemplate(value, parser, this);
        _formatNeeded = true;
      }
      else if (name.equals("grouping-separator")) {
        _groupingSeparator = new AttributeValueTemplate(value, parser, this);
        _formatNeeded = true;
      }
      else if (name.equals("grouping-size")) {
        _groupingSize = new AttributeValueTemplate(value, parser, this);
        _formatNeeded = true;
      }
    }
  }
  
  public org.apache.xalan.xsltc.compiler.util.Type typeCheck(SymbolTable stable) throws TypeCheckError {
    if (_value != null) {
      org.apache.xalan.xsltc.compiler.util.Type tvalue = _value.typeCheck(stable);
      if (!(tvalue instanceof RealType)) {
        _value = new CastExpr(_value, org.apache.xalan.xsltc.compiler.util.Type.Real);
      }
    }
    if (_count != null) {
      _count.typeCheck(stable);
    }
    if (_from != null) {
      _from.typeCheck(stable);
    }
    if (_format != null) {
      _format.typeCheck(stable);
    }
    if (_lang != null) {
      _lang.typeCheck(stable);
    }
    if (_letterValue != null) {
      _letterValue.typeCheck(stable);
    }
    if (_groupingSeparator != null) {
      _groupingSeparator.typeCheck(stable);
    }
    if (_groupingSize != null) {
      _groupingSize.typeCheck(stable);
    }
    return org.apache.xalan.xsltc.compiler.util.Type.Void;
  }
  


  public boolean hasValue()
  {
    return _value != null;
  }
  



  public boolean isDefault()
  {
    return (_from == null) && (_count == null);
  }
  

  private void compileDefault(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    int[] fieldIndexes = getXSLTC().getNumberFieldIndexes();
    
    if (fieldIndexes[_level] == -1) {
      Field defaultNode = new Field(2, cpg.addUtf8(FieldNames[_level]), cpg.addUtf8("Lorg/apache/xalan/xsltc/dom/NodeCounter;"), null, cpg.getConstantPool());
      





      classGen.addField(defaultNode);
      

      fieldIndexes[_level] = cpg.addFieldref(classGen.getClassName(), FieldNames[_level], "Lorg/apache/xalan/xsltc/dom/NodeCounter;");
    }
    



    il.append(classGen.loadTranslet());
    il.append(new GETFIELD(fieldIndexes[_level]));
    BranchHandle ifBlock1 = il.append(new IFNONNULL(null));
    

    int index = cpg.addMethodref(ClassNames[_level], "getDefaultNodeCounter", "(Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;)Lorg/apache/xalan/xsltc/dom/NodeCounter;");
    




    il.append(classGen.loadTranslet());
    il.append(methodGen.loadDOM());
    il.append(methodGen.loadIterator());
    il.append(new INVOKESTATIC(index));
    il.append(DUP);
    

    il.append(classGen.loadTranslet());
    il.append(SWAP);
    il.append(new PUTFIELD(fieldIndexes[_level]));
    BranchHandle ifBlock2 = il.append(new GOTO(null));
    

    ifBlock1.setTarget(il.append(classGen.loadTranslet()));
    il.append(new GETFIELD(fieldIndexes[_level]));
    
    ifBlock2.setTarget(il.append(NOP));
  }
  





  private void compileConstructor(ClassGenerator classGen)
  {
    InstructionList il = new InstructionList();
    ConstantPoolGen cpg = classGen.getConstantPool();
    
    MethodGenerator cons = new MethodGenerator(1, org.apache.bcel.generic.Type.VOID, new org.apache.bcel.generic.Type[] { Util.getJCRefType("Lorg/apache/xalan/xsltc/Translet;"), Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;") }, new String[] { "dom", "translet", "iterator" }, "<init>", _className, il, cpg);
    












    il.append(ALOAD_0);
    il.append(ALOAD_1);
    il.append(ALOAD_2);
    il.append(new ALOAD(3));
    
    int index = cpg.addMethodref(ClassNames[_level], "<init>", "(Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
    




    il.append(new INVOKESPECIAL(index));
    il.append(RETURN);
    
    classGen.addMethod(cons);
  }
  








  private void compileLocals(NodeCounterGenerator nodeCounterGen, MatchGenerator matchGen, InstructionList il)
  {
    ConstantPoolGen cpg = nodeCounterGen.getConstantPool();
    

    LocalVariableGen local = matchGen.addLocalVariable("iterator", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    

    int field = cpg.addFieldref("org.apache.xalan.xsltc.dom.NodeCounter", "_iterator", "Lorg/apache/xml/dtm/DTMAxisIterator;");
    
    il.append(ALOAD_0);
    il.append(new GETFIELD(field));
    local.setStart(il.append(new ASTORE(local.getIndex())));
    matchGen.setIteratorIndex(local.getIndex());
    

    local = matchGen.addLocalVariable("translet", Util.getJCRefType("Lorg/apache/xalan/xsltc/runtime/AbstractTranslet;"), null, null);
    

    field = cpg.addFieldref("org.apache.xalan.xsltc.dom.NodeCounter", "_translet", "Lorg/apache/xalan/xsltc/Translet;");
    
    il.append(ALOAD_0);
    il.append(new GETFIELD(field));
    il.append(new CHECKCAST(cpg.addClass("org.apache.xalan.xsltc.runtime.AbstractTranslet")));
    local.setStart(il.append(new ASTORE(local.getIndex())));
    nodeCounterGen.setTransletIndex(local.getIndex());
    

    local = matchGen.addLocalVariable("document", Util.getJCRefType("Lorg/apache/xalan/xsltc/DOM;"), null, null);
    

    field = cpg.addFieldref(_className, "_document", "Lorg/apache/xalan/xsltc/DOM;");
    il.append(ALOAD_0);
    il.append(new GETFIELD(field));
    
    local.setStart(il.append(new ASTORE(local.getIndex())));
    matchGen.setDomIndex(local.getIndex());
  }
  







  private void compilePatterns(ClassGenerator classGen, MethodGenerator methodGen)
  {
    _className = getXSLTC().getHelperClassName();
    NodeCounterGenerator nodeCounterGen = new NodeCounterGenerator(_className, ClassNames[_level], toString(), 33, null, classGen.getStylesheet());
    




    InstructionList il = null;
    ConstantPoolGen cpg = nodeCounterGen.getConstantPool();
    

    int closureLen = _closureVars == null ? 0 : _closureVars.size();
    

    for (int i = 0; i < closureLen; i++) {
      VariableBase var = ((VariableRefBase)_closureVars.get(i)).getVariable();
      

      nodeCounterGen.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
    }
    




    compileConstructor(nodeCounterGen);
    



    if (_from != null) {
      il = new InstructionList();
      MatchGenerator matchGen = new MatchGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.INT }, new String[] { "node" }, "matchesFrom", _className, il, cpg);
      









      compileLocals(nodeCounterGen, matchGen, il);
      

      il.append(matchGen.loadContextNode());
      _from.translate(nodeCounterGen, matchGen);
      _from.synthesize(nodeCounterGen, matchGen);
      il.append(IRETURN);
      
      nodeCounterGen.addMethod(matchGen);
    }
    



    if (_count != null) {
      il = new InstructionList();
      MatchGenerator matchGen = new MatchGenerator(17, org.apache.bcel.generic.Type.BOOLEAN, new org.apache.bcel.generic.Type[] { org.apache.bcel.generic.Type.INT }, new String[] { "node" }, "matchesCount", _className, il, cpg);
      








      compileLocals(nodeCounterGen, matchGen, il);
      

      il.append(matchGen.loadContextNode());
      _count.translate(nodeCounterGen, matchGen);
      _count.synthesize(nodeCounterGen, matchGen);
      
      il.append(IRETURN);
      
      nodeCounterGen.addMethod(matchGen);
    }
    
    getXSLTC().dumpClass(nodeCounterGen.getJavaClass());
    

    cpg = classGen.getConstantPool();
    il = methodGen.getInstructionList();
    
    int index = cpg.addMethodref(_className, "<init>", "(Lorg/apache/xalan/xsltc/Translet;Lorg/apache/xalan/xsltc/DOM;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
    



    il.append(new NEW(cpg.addClass(_className)));
    il.append(DUP);
    il.append(classGen.loadTranslet());
    il.append(methodGen.loadDOM());
    il.append(methodGen.loadIterator());
    il.append(new INVOKESPECIAL(index));
    

    for (int i = 0; i < closureLen; i++) {
      VariableRefBase varRef = (VariableRefBase)_closureVars.get(i);
      VariableBase var = varRef.getVariable();
      org.apache.xalan.xsltc.compiler.util.Type varType = var.getType();
      

      il.append(DUP);
      il.append(var.loadInstruction());
      il.append(new PUTFIELD(cpg.addFieldref(_className, var.getEscapedName(), varType.toSignature())));
    }
  }
  


  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    il.append(classGen.loadTranslet());
    
    if (hasValue()) {
      compileDefault(classGen, methodGen);
      _value.translate(classGen, methodGen);
      

      il.append(new PUSH(cpg, 0.5D));
      il.append(DADD);
      int index = cpg.addMethodref("java.lang.Math", "floor", "(D)D");
      il.append(new INVOKESTATIC(index));
      

      index = cpg.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "setValue", "(D)Lorg/apache/xalan/xsltc/dom/NodeCounter;");
      

      il.append(new INVOKEVIRTUAL(index));
    }
    else if (isDefault()) {
      compileDefault(classGen, methodGen);
    }
    else {
      compilePatterns(classGen, methodGen);
    }
    

    if (!hasValue()) {
      il.append(methodGen.loadContextNode());
      int index = cpg.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "setStartNode", "(I)Lorg/apache/xalan/xsltc/dom/NodeCounter;");
      

      il.append(new INVOKEVIRTUAL(index));
    }
    

    if (_formatNeeded) {
      if (_format != null) {
        _format.translate(classGen, methodGen);
      }
      else {
        il.append(new PUSH(cpg, "1"));
      }
      
      if (_lang != null) {
        _lang.translate(classGen, methodGen);
      }
      else {
        il.append(new PUSH(cpg, "en"));
      }
      
      if (_letterValue != null) {
        _letterValue.translate(classGen, methodGen);
      }
      else {
        il.append(new PUSH(cpg, ""));
      }
      
      if (_groupingSeparator != null) {
        _groupingSeparator.translate(classGen, methodGen);
      }
      else {
        il.append(new PUSH(cpg, ""));
      }
      
      if (_groupingSize != null) {
        _groupingSize.translate(classGen, methodGen);
      }
      else {
        il.append(new PUSH(cpg, "0"));
      }
      
      int index = cpg.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "getCounter", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
      


      il.append(new INVOKEVIRTUAL(index));
    }
    else {
      index = cpg.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "setDefaultFormatting", "()Lorg/apache/xalan/xsltc/dom/NodeCounter;");
      
      il.append(new INVOKEVIRTUAL(index));
      
      index = cpg.addMethodref("org.apache.xalan.xsltc.dom.NodeCounter", "getCounter", "()Ljava/lang/String;");
      
      il.append(new INVOKEVIRTUAL(index));
    }
    

    il.append(methodGen.loadHandler());
    int index = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "characters", CHARACTERSW_SIG);
    

    il.append(new INVOKEVIRTUAL(index));
  }
}

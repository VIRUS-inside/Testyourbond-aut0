package org.apache.xalan.xsltc.compiler;

import java.util.Vector;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.BranchHandle;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GETFIELD;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.GOTO_W;
import org.apache.bcel.generic.IFLT;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.IF_ICMPEQ;
import org.apache.bcel.generic.IF_ICMPLT;
import org.apache.bcel.generic.IF_ICMPNE;
import org.apache.bcel.generic.ILOAD;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.ISTORE;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.PUTFIELD;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;
import org.apache.xml.dtm.Axis;






























class StepPattern
  extends RelativePathPattern
{
  private static final int NO_CONTEXT = 0;
  private static final int SIMPLE_CONTEXT = 1;
  private static final int GENERAL_CONTEXT = 2;
  protected final int _axis;
  protected final int _nodeType;
  protected Vector _predicates;
  private Step _step = null;
  private boolean _isEpsilon = false;
  
  private int _contextCase;
  private double _priority = Double.MAX_VALUE;
  
  public StepPattern(int axis, int nodeType, Vector predicates) {
    _axis = axis;
    _nodeType = nodeType;
    _predicates = predicates;
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    if (_predicates != null) {
      int n = _predicates.size();
      for (int i = 0; i < n; i++) {
        Predicate exp = (Predicate)_predicates.elementAt(i);
        exp.setParser(parser);
        exp.setParent(this);
      }
    }
  }
  
  public int getNodeType() {
    return _nodeType;
  }
  
  public void setPriority(double priority) {
    _priority = priority;
  }
  
  public StepPattern getKernelPattern() {
    return this;
  }
  
  public boolean isWildcard() {
    return (_isEpsilon) && (!hasPredicates());
  }
  
  public StepPattern setPredicates(Vector predicates) {
    _predicates = predicates;
    return this;
  }
  
  protected boolean hasPredicates() {
    return (_predicates != null) && (_predicates.size() > 0);
  }
  
  public double getDefaultPriority() {
    if (_priority != Double.MAX_VALUE) {
      return _priority;
    }
    
    if (hasPredicates()) {
      return 0.5D;
    }
    
    switch (_nodeType) {
    case -1: 
      return -0.5D;
    case 0: 
      return 0.0D;
    }
    return _nodeType >= 14 ? 0.0D : -0.5D;
  }
  

  public int getAxis()
  {
    return _axis;
  }
  
  public void reduceKernelPattern() {
    _isEpsilon = true;
  }
  
  public String toString() {
    StringBuffer buffer = new StringBuffer("stepPattern(\"");
    buffer.append(Axis.getNames(_axis)).append("\", ").append(_isEpsilon ? "epsilon{" + Integer.toString(_nodeType) + "}" : Integer.toString(_nodeType));
    



    if (_predicates != null)
      buffer.append(", ").append(_predicates.toString());
    return ')';
  }
  
  private int analyzeCases() {
    boolean noContext = true;
    int n = _predicates.size();
    
    for (int i = 0; (i < n) && (noContext); i++) {
      Predicate pred = (Predicate)_predicates.elementAt(i);
      if ((pred.isNthPositionFilter()) || (pred.hasPositionCall()) || (pred.hasLastCall()))
      {


        noContext = false;
      }
    }
    
    if (noContext) {
      return 0;
    }
    if (n == 1) {
      return 1;
    }
    return 2;
  }
  
  private String getNextFieldName() {
    return "__step_pattern_iter_" + getXSLTC().nextStepPatternSerial();
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    if (hasPredicates())
    {
      int n = _predicates.size();
      for (int i = 0; i < n; i++) {
        Predicate pred = (Predicate)_predicates.elementAt(i);
        pred.typeCheck(stable);
      }
      

      _contextCase = analyzeCases();
      
      Step step = null;
      

      if (_contextCase == 1) {
        Predicate pred = (Predicate)_predicates.elementAt(0);
        if (pred.isNthPositionFilter()) {
          _contextCase = 2;
          step = new Step(_axis, _nodeType, _predicates);
        } else {
          step = new Step(_axis, _nodeType, null);
        }
      } else if (_contextCase == 2) {
        int len = _predicates.size();
        for (int i = 0; i < len; i++) {
          ((Predicate)_predicates.elementAt(i)).dontOptimize();
        }
        
        step = new Step(_axis, _nodeType, _predicates);
      }
      
      if (step != null) {
        step.setParser(getParser());
        step.typeCheck(stable);
        _step = step;
      }
    }
    return _axis == 3 ? Type.Element : Type.Attribute;
  }
  
  private void translateKernel(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    if (_nodeType == 1) {
      int check = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "isElement", "(I)Z");
      
      il.append(methodGen.loadDOM());
      il.append(SWAP);
      il.append(new INVOKEINTERFACE(check, 2));
      

      BranchHandle icmp = il.append(new IFNE(null));
      _falseList.add(il.append(new GOTO_W(null)));
      icmp.setTarget(il.append(NOP));
    }
    else if (_nodeType == 2) {
      int check = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "isAttribute", "(I)Z");
      
      il.append(methodGen.loadDOM());
      il.append(SWAP);
      il.append(new INVOKEINTERFACE(check, 2));
      

      BranchHandle icmp = il.append(new IFNE(null));
      _falseList.add(il.append(new GOTO_W(null)));
      icmp.setTarget(il.append(NOP));
    }
    else
    {
      int getEType = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getExpandedTypeID", "(I)I");
      

      il.append(methodGen.loadDOM());
      il.append(SWAP);
      il.append(new INVOKEINTERFACE(getEType, 2));
      il.append(new PUSH(cpg, _nodeType));
      

      BranchHandle icmp = il.append(new IF_ICMPEQ(null));
      _falseList.add(il.append(new GOTO_W(null)));
      icmp.setTarget(il.append(NOP));
    }
  }
  
  private void translateNoContext(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    il.append(methodGen.loadCurrentNode());
    il.append(SWAP);
    

    il.append(methodGen.storeCurrentNode());
    

    if (!_isEpsilon) {
      il.append(methodGen.loadCurrentNode());
      translateKernel(classGen, methodGen);
    }
    

    int n = _predicates.size();
    for (int i = 0; i < n; i++) {
      Predicate pred = (Predicate)_predicates.elementAt(i);
      Expression exp = pred.getExpr();
      exp.translateDesynthesized(classGen, methodGen);
      _trueList.append(_trueList);
      _falseList.append(_falseList);
    }
    


    InstructionHandle restore = il.append(methodGen.storeCurrentNode());
    backPatchTrueList(restore);
    BranchHandle skipFalse = il.append(new GOTO(null));
    

    restore = il.append(methodGen.storeCurrentNode());
    backPatchFalseList(restore);
    _falseList.add(il.append(new GOTO(null)));
    

    skipFalse.setTarget(il.append(NOP));
  }
  

  private void translateSimpleContext(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    


    LocalVariableGen match = methodGen.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
    

    match.setStart(il.append(new ISTORE(match.getIndex())));
    

    if (!_isEpsilon) {
      il.append(new ILOAD(match.getIndex()));
      translateKernel(classGen, methodGen);
    }
    

    il.append(methodGen.loadCurrentNode());
    il.append(methodGen.loadIterator());
    

    int index = cpg.addMethodref("org.apache.xalan.xsltc.dom.MatchingIterator", "<init>", "(ILorg/apache/xml/dtm/DTMAxisIterator;)V");
    










    _step.translate(classGen, methodGen);
    LocalVariableGen stepIteratorTemp = methodGen.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    


    stepIteratorTemp.setStart(il.append(new ASTORE(stepIteratorTemp.getIndex())));
    

    il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.MatchingIterator")));
    il.append(DUP);
    il.append(new ILOAD(match.getIndex()));
    stepIteratorTemp.setEnd(il.append(new ALOAD(stepIteratorTemp.getIndex())));
    
    il.append(new INVOKESPECIAL(index));
    

    il.append(methodGen.loadDOM());
    il.append(new ILOAD(match.getIndex()));
    index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
    il.append(new INVOKEINTERFACE(index, 2));
    

    il.append(methodGen.setStartNode());
    

    il.append(methodGen.storeIterator());
    match.setEnd(il.append(new ILOAD(match.getIndex())));
    il.append(methodGen.storeCurrentNode());
    

    Predicate pred = (Predicate)_predicates.elementAt(0);
    Expression exp = pred.getExpr();
    exp.translateDesynthesized(classGen, methodGen);
    

    InstructionHandle restore = il.append(methodGen.storeIterator());
    il.append(methodGen.storeCurrentNode());
    exp.backPatchTrueList(restore);
    BranchHandle skipFalse = il.append(new GOTO(null));
    

    restore = il.append(methodGen.storeIterator());
    il.append(methodGen.storeCurrentNode());
    exp.backPatchFalseList(restore);
    _falseList.add(il.append(new GOTO(null)));
    

    skipFalse.setTarget(il.append(NOP));
  }
  
  private void translateGeneralContext(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    int iteratorIndex = 0;
    BranchHandle ifBlock = null;
    
    String iteratorName = getNextFieldName();
    

    LocalVariableGen node = methodGen.addLocalVariable("step_pattern_tmp1", Util.getJCRefType("I"), null, null);
    

    node.setStart(il.append(new ISTORE(node.getIndex())));
    

    LocalVariableGen iter = methodGen.addLocalVariable("step_pattern_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    



    if (!classGen.isExternal()) {
      Field iterator = new Field(2, cpg.addUtf8(iteratorName), cpg.addUtf8("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, cpg.getConstantPool());
      



      classGen.addField(iterator);
      iteratorIndex = cpg.addFieldref(classGen.getClassName(), iteratorName, "Lorg/apache/xml/dtm/DTMAxisIterator;");
      


      il.append(classGen.loadTranslet());
      il.append(new GETFIELD(iteratorIndex));
      il.append(DUP);
      iter.setStart(il.append(new ASTORE(iter.getIndex())));
      ifBlock = il.append(new IFNONNULL(null));
      il.append(classGen.loadTranslet());
    }
    

    _step.translate(classGen, methodGen);
    InstructionHandle iterStore = il.append(new ASTORE(iter.getIndex()));
    

    if (!classGen.isExternal()) {
      il.append(new ALOAD(iter.getIndex()));
      il.append(new PUTFIELD(iteratorIndex));
      ifBlock.setTarget(il.append(NOP));
    }
    else
    {
      iter.setStart(iterStore);
    }
    

    il.append(methodGen.loadDOM());
    il.append(new ILOAD(node.getIndex()));
    int index = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getParent", "(I)I");
    
    il.append(new INVOKEINTERFACE(index, 2));
    

    il.append(new ALOAD(iter.getIndex()));
    il.append(SWAP);
    il.append(methodGen.setStartNode());
    










    LocalVariableGen node2 = methodGen.addLocalVariable("step_pattern_tmp3", Util.getJCRefType("I"), null, null);
    


    BranchHandle skipNext = il.append(new GOTO(null));
    InstructionHandle next = il.append(new ALOAD(iter.getIndex()));
    node2.setStart(next);
    InstructionHandle begin = il.append(methodGen.nextNode());
    il.append(DUP);
    il.append(new ISTORE(node2.getIndex()));
    _falseList.add(il.append(new IFLT(null)));
    
    il.append(new ILOAD(node2.getIndex()));
    il.append(new ILOAD(node.getIndex()));
    iter.setEnd(il.append(new IF_ICMPLT(next)));
    
    node2.setEnd(il.append(new ILOAD(node2.getIndex())));
    node.setEnd(il.append(new ILOAD(node.getIndex())));
    _falseList.add(il.append(new IF_ICMPNE(null)));
    
    skipNext.setTarget(begin);
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    
    if (hasPredicates()) {
      switch (_contextCase) {
      case 0: 
        translateNoContext(classGen, methodGen);
        break;
      
      case 1: 
        translateSimpleContext(classGen, methodGen);
        break;
      
      default: 
        translateGeneralContext(classGen, methodGen);
        break;
      }
      
    } else if (isWildcard()) {
      il.append(POP);
    }
    else {
      translateKernel(classGen, methodGen);
    }
  }
}

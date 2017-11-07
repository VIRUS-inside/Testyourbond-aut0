package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ASTORE;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.INVOKEINTERFACE;
import org.apache.bcel.generic.INVOKESPECIAL;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LocalVariableGen;
import org.apache.bcel.generic.NEW;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;
import org.apache.xalan.xsltc.compiler.util.Util;


























final class ParentLocationPath
  extends RelativeLocationPath
{
  private Expression _step;
  private final RelativeLocationPath _path;
  private Type stype;
  private boolean _orderNodes = false;
  private boolean _axisMismatch = false;
  
  public ParentLocationPath(RelativeLocationPath path, Expression step) {
    _path = path;
    _step = step;
    _path.setParent(this);
    _step.setParent(this);
    
    if ((_step instanceof Step)) {
      _axisMismatch = checkAxisMismatch();
    }
  }
  
  public void setAxis(int axis) {
    _path.setAxis(axis);
  }
  
  public int getAxis() {
    return _path.getAxis();
  }
  
  public RelativeLocationPath getPath() {
    return _path;
  }
  
  public Expression getStep() {
    return _step;
  }
  
  public void setParser(Parser parser) {
    super.setParser(parser);
    _step.setParser(parser);
    _path.setParser(parser);
  }
  
  public String toString() {
    return "ParentLocationPath(" + _path + ", " + _step + ')';
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    stype = _step.typeCheck(stable);
    _path.typeCheck(stable);
    
    if (_axisMismatch) { enableNodeOrdering();
    }
    return this._type = Type.NodeSet;
  }
  
  public void enableNodeOrdering() {
    SyntaxTreeNode parent = getParent();
    if ((parent instanceof ParentLocationPath)) {
      ((ParentLocationPath)parent).enableNodeOrdering();
    } else {
      _orderNodes = true;
    }
  }
  





  public boolean checkAxisMismatch()
  {
    int left = _path.getAxis();
    int right = ((Step)_step).getAxis();
    
    if (((left == 0) || (left == 1)) && ((right == 3) || (right == 4) || (right == 5) || (right == 10) || (right == 11) || (right == 12)))
    {





      return true;
    }
    if (((left == 3) && (right == 0)) || (right == 1) || (right == 10) || (right == 11))
    {



      return true;
    }
    if ((left == 4) || (left == 5)) {
      return true;
    }
    if (((left == 6) || (left == 7)) && ((right == 6) || (right == 10) || (right == 11) || (right == 12)))
    {



      return true;
    }
    if (((left == 11) || (left == 12)) && ((right == 4) || (right == 5) || (right == 6) || (right == 7) || (right == 10) || (right == 11) || (right == 12)))
    {






      return true;
    }
    if ((right == 6) && (left == 3))
    {



      if ((_path instanceof Step)) {
        int type = ((Step)_path).getNodeType();
        if (type == 2) { return true;
        }
      }
    }
    return false;
  }
  
  public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    










    _path.translate(classGen, methodGen);
    LocalVariableGen pathTemp = methodGen.addLocalVariable("parent_location_path_tmp1", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    


    pathTemp.setStart(il.append(new ASTORE(pathTemp.getIndex())));
    
    _step.translate(classGen, methodGen);
    LocalVariableGen stepTemp = methodGen.addLocalVariable("parent_location_path_tmp2", Util.getJCRefType("Lorg/apache/xml/dtm/DTMAxisIterator;"), null, null);
    


    stepTemp.setStart(il.append(new ASTORE(stepTemp.getIndex())));
    

    int initSI = cpg.addMethodref("org.apache.xalan.xsltc.dom.StepIterator", "<init>", "(Lorg/apache/xml/dtm/DTMAxisIterator;Lorg/apache/xml/dtm/DTMAxisIterator;)V");
    




    il.append(new NEW(cpg.addClass("org.apache.xalan.xsltc.dom.StepIterator")));
    il.append(DUP);
    
    pathTemp.setEnd(il.append(new ALOAD(pathTemp.getIndex())));
    stepTemp.setEnd(il.append(new ALOAD(stepTemp.getIndex())));
    

    il.append(new INVOKESPECIAL(initSI));
    

    Expression stp = _step;
    if ((stp instanceof ParentLocationPath)) {
      stp = ((ParentLocationPath)stp).getStep();
    }
    if (((_path instanceof Step)) && ((stp instanceof Step))) {
      int path = ((Step)_path).getAxis();
      int step = ((Step)stp).getAxis();
      if (((path == 5) && (step == 3)) || ((path == 11) && (step == 10)))
      {
        int incl = cpg.addMethodref("org.apache.xml.dtm.ref.DTMAxisIteratorBase", "includeSelf", "()Lorg/apache/xml/dtm/DTMAxisIterator;");
        

        il.append(new INVOKEVIRTUAL(incl));
      }
    }
    






    if (_orderNodes) {
      int order = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "orderNodes", "(Lorg/apache/xml/dtm/DTMAxisIterator;I)Lorg/apache/xml/dtm/DTMAxisIterator;");
      

      il.append(methodGen.loadDOM());
      il.append(SWAP);
      il.append(methodGen.loadContextNode());
      il.append(new INVOKEINTERFACE(order, 3));
    }
  }
}

package org.apache.xalan.xsltc.compiler;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNE;
import org.apache.bcel.generic.INVOKEVIRTUAL;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.PUSH;
import org.apache.xalan.xsltc.compiler.util.ClassGenerator;
import org.apache.xalan.xsltc.compiler.util.MethodGenerator;
import org.apache.xalan.xsltc.compiler.util.Type;
import org.apache.xalan.xsltc.compiler.util.TypeCheckError;

























abstract class IdKeyPattern
  extends LocationPathPattern
{
  protected RelativePathPattern _left = null;
  private String _index = null;
  private String _value = null;
  
  public IdKeyPattern(String index, String value) {
    _index = index;
    _value = value;
  }
  
  public String getIndexName() {
    return _index;
  }
  
  public Type typeCheck(SymbolTable stable) throws TypeCheckError {
    return Type.NodeSet;
  }
  
  public boolean isWildcard() {
    return false;
  }
  
  public void setLeft(RelativePathPattern left) {
    _left = left;
  }
  
  public StepPattern getKernelPattern() {
    return null;
  }
  
  public void reduceKernelPattern() {}
  
  public String toString() {
    return "id/keyPattern(" + _index + ", " + _value + ')';
  }
  





  public void translate(ClassGenerator classGen, MethodGenerator methodGen)
  {
    ConstantPoolGen cpg = classGen.getConstantPool();
    InstructionList il = methodGen.getInstructionList();
    

    int getKeyIndex = cpg.addMethodref("org.apache.xalan.xsltc.runtime.AbstractTranslet", "getKeyIndex", "(Ljava/lang/String;)Lorg/apache/xalan/xsltc/dom/KeyIndex;");
    




    int lookupId = cpg.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "containsID", "(ILjava/lang/Object;)I");
    

    int lookupKey = cpg.addMethodref("org/apache/xalan/xsltc/dom/KeyIndex", "containsKey", "(ILjava/lang/Object;)I");
    

    int getNodeIdent = cpg.addInterfaceMethodref("org.apache.xalan.xsltc.DOM", "getNodeIdent", "(I)I");
    




    il.append(classGen.loadTranslet());
    il.append(new PUSH(cpg, _index));
    il.append(new INVOKEVIRTUAL(getKeyIndex));
    


    il.append(SWAP);
    il.append(new PUSH(cpg, _value));
    if ((this instanceof IdPattern))
    {
      il.append(new INVOKEVIRTUAL(lookupId));
    }
    else
    {
      il.append(new INVOKEVIRTUAL(lookupKey));
    }
    
    _trueList.add(il.append(new IFNE(null)));
    _falseList.add(il.append(new GOTO(null)));
  }
}

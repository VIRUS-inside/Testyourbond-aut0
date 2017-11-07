package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import net.sourceforge.htmlunit.corejs.javascript.Node;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;

public final class OptFunctionNode
{
  public final FunctionNode fnode;
  private boolean[] numberVarFlags;
  
  OptFunctionNode(FunctionNode fnode)
  {
    this.fnode = fnode;
    fnode.setCompilerData(this);
  }
  
  public static OptFunctionNode get(net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode scriptOrFn, int i) {
    FunctionNode fnode = scriptOrFn.getFunctionNode(i);
    return (OptFunctionNode)fnode.getCompilerData();
  }
  
  public static OptFunctionNode get(net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode scriptOrFn) {
    return (OptFunctionNode)scriptOrFn.getCompilerData();
  }
  
  public boolean isTargetOfDirectCall() {
    return directTargetIndex >= 0;
  }
  
  public int getDirectTargetIndex() {
    return directTargetIndex;
  }
  
  void setDirectTargetIndex(int directTargetIndex)
  {
    if ((directTargetIndex < 0) || (this.directTargetIndex >= 0))
      net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug();
    this.directTargetIndex = directTargetIndex;
  }
  
  void setParameterNumberContext(boolean b) {
    itsParameterNumberContext = b;
  }
  
  public boolean getParameterNumberContext() {
    return itsParameterNumberContext;
  }
  
  public int getVarCount() {
    return fnode.getParamAndVarCount();
  }
  
  public boolean isParameter(int varIndex) {
    return varIndex < fnode.getParamCount();
  }
  
  public boolean isNumberVar(int varIndex) {
    varIndex -= fnode.getParamCount();
    if ((varIndex >= 0) && (numberVarFlags != null)) {
      return numberVarFlags[varIndex];
    }
    return false;
  }
  
  void setIsNumberVar(int varIndex) {
    varIndex -= fnode.getParamCount();
    
    if (varIndex < 0)
      net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug();
    if (numberVarFlags == null) {
      int size = fnode.getParamAndVarCount() - fnode.getParamCount();
      numberVarFlags = new boolean[size];
    }
    numberVarFlags[varIndex] = true;
  }
  
  public int getVarIndex(Node n) {
    int index = n.getIntProp(7, -1);
    if (index == -1)
    {
      int type = n.getType();
      Node node; if (type == 55) {
        node = n; } else { Node node;
        if ((type == 56) || (type == 156)) {
          node = n.getFirstChild();
        } else
          throw net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug(); }
      Node node;
      index = fnode.getIndexForNameNode(node);
      if (index < 0)
        throw net.sourceforge.htmlunit.corejs.javascript.Kit.codeBug();
      n.putIntProp(7, index);
    }
    return index;
  }
  



  private int directTargetIndex = -1;
  private boolean itsParameterNumberContext;
  boolean itsContainsCalls0;
  boolean itsContainsCalls1;
}

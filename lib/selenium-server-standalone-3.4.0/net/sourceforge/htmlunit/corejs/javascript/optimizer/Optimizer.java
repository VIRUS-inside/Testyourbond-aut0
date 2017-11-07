package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import net.sourceforge.htmlunit.corejs.javascript.Node;
import net.sourceforge.htmlunit.corejs.javascript.ObjArray;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;

class Optimizer
{
  static final int NoType = 0;
  static final int NumberType = 1;
  static final int AnyType = 3;
  private boolean inDirectCallFunction;
  OptFunctionNode theFunction;
  private boolean parameterUsedInNumberContext;
  
  Optimizer() {}
  
  void optimize(ScriptNode scriptOrFn)
  {
    int functionCount = scriptOrFn.getFunctionCount();
    for (int i = 0; i != functionCount; i++) {
      OptFunctionNode f = OptFunctionNode.get(scriptOrFn, i);
      optimizeFunction(f);
    }
  }
  
  private void optimizeFunction(OptFunctionNode theFunction) {
    if (fnode.requiresActivation()) {
      return;
    }
    inDirectCallFunction = theFunction.isTargetOfDirectCall();
    this.theFunction = theFunction;
    
    ObjArray statementsArray = new ObjArray();
    buildStatementList_r(fnode, statementsArray);
    Node[] theStatementNodes = new Node[statementsArray.size()];
    statementsArray.toArray(theStatementNodes);
    
    Block.runFlowAnalyzes(theFunction, theStatementNodes);
    
    if (!fnode.requiresActivation())
    {





      parameterUsedInNumberContext = false;
      for (Node theStatementNode : theStatementNodes) {
        rewriteForNumberVariables(theStatementNode, 1);
      }
      theFunction.setParameterNumberContext(parameterUsedInNumberContext);
    }
  }
  



























  private void markDCPNumberContext(Node n)
  {
    if ((inDirectCallFunction) && (n.getType() == 55)) {
      int varIndex = theFunction.getVarIndex(n);
      if (theFunction.isParameter(varIndex)) {
        parameterUsedInNumberContext = true;
      }
    }
  }
  
  private boolean convertParameter(Node n) {
    if ((inDirectCallFunction) && (n.getType() == 55)) {
      int varIndex = theFunction.getVarIndex(n);
      if (theFunction.isParameter(varIndex)) {
        n.removeProp(8);
        return true;
      }
    }
    return false;
  }
  
  private int rewriteForNumberVariables(Node n, int desired) {
    switch (n.getType()) {
    case 133: 
      Node child = n.getFirstChild();
      int type = rewriteForNumberVariables(child, 1);
      if (type == 1)
        n.putIntProp(8, 0);
      return 0;
    
    case 40: 
      n.putIntProp(8, 0);
      return 1;
    
    case 55: 
      int varIndex = theFunction.getVarIndex(n);
      if ((inDirectCallFunction) && (theFunction.isParameter(varIndex)) && (desired == 1))
      {
        n.putIntProp(8, 0);
        return 1; }
      if (theFunction.isNumberVar(varIndex)) {
        n.putIntProp(8, 0);
        return 1;
      }
      return 0;
    

    case 106: 
    case 107: 
      Node child = n.getFirstChild();
      int type = rewriteForNumberVariables(child, 1);
      if (child.getType() == 55) {
        if ((type == 1) && (!convertParameter(child))) {
          n.putIntProp(8, 0);
          markDCPNumberContext(child);
          return 1;
        }
        return 0; }
      if ((child.getType() == 36) || 
        (child.getType() == 33)) {
        return type;
      }
      return 0;
    
    case 56: 
    case 156: 
      Node lChild = n.getFirstChild();
      Node rChild = lChild.getNext();
      int rType = rewriteForNumberVariables(rChild, 1);
      int varIndex = theFunction.getVarIndex(n);
      if ((inDirectCallFunction) && (theFunction.isParameter(varIndex))) {
        if (rType == 1) {
          if (!convertParameter(rChild)) {
            n.putIntProp(8, 0);
            return 1;
          }
          markDCPNumberContext(rChild);
          return 0;
        }
        return rType; }
      if (theFunction.isNumberVar(varIndex)) {
        if (rType != 1) {
          n.removeChild(rChild);
          n.addChildToBack(new Node(150, rChild));
        }
        n.putIntProp(8, 0);
        markDCPNumberContext(rChild);
        return 1;
      }
      if ((rType == 1) && 
        (!convertParameter(rChild))) {
        n.removeChild(rChild);
        n.addChildToBack(new Node(149, rChild));
      }
      
      return 0;
    

    case 14: 
    case 15: 
    case 16: 
    case 17: 
      Node lChild = n.getFirstChild();
      Node rChild = lChild.getNext();
      int lType = rewriteForNumberVariables(lChild, 1);
      int rType = rewriteForNumberVariables(rChild, 1);
      markDCPNumberContext(lChild);
      markDCPNumberContext(rChild);
      
      if (convertParameter(lChild)) {
        if (convertParameter(rChild))
          return 0;
        if (rType == 1) {
          n.putIntProp(8, 2);
        }
      } else if (convertParameter(rChild)) {
        if (lType == 1) {
          n.putIntProp(8, 1);
        }
      }
      else if (lType == 1) {
        if (rType == 1) {
          n.putIntProp(8, 0);
        } else {
          n.putIntProp(8, 1);
        }
      }
      else if (rType == 1) {
        n.putIntProp(8, 2);
      }
      


      return 0;
    

    case 21: 
      Node lChild = n.getFirstChild();
      Node rChild = lChild.getNext();
      int lType = rewriteForNumberVariables(lChild, 1);
      int rType = rewriteForNumberVariables(rChild, 1);
      
      if (convertParameter(lChild)) {
        if (convertParameter(rChild)) {
          return 0;
        }
        if (rType == 1) {
          n.putIntProp(8, 2);
        }
        
      }
      else if (convertParameter(rChild)) {
        if (lType == 1) {
          n.putIntProp(8, 1);
        }
      }
      else if (lType == 1) {
        if (rType == 1) {
          n.putIntProp(8, 0);
          return 1;
        }
        n.putIntProp(8, 1);

      }
      else if (rType == 1) {
        n.putIntProp(8, 2);
      }
      


      return 0;
    

    case 9: 
    case 10: 
    case 11: 
    case 18: 
    case 19: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
      Node lChild = n.getFirstChild();
      Node rChild = lChild.getNext();
      int lType = rewriteForNumberVariables(lChild, 1);
      int rType = rewriteForNumberVariables(rChild, 1);
      markDCPNumberContext(lChild);
      markDCPNumberContext(rChild);
      if (lType == 1) {
        if (rType == 1) {
          n.putIntProp(8, 0);
          return 1;
        }
        if (!convertParameter(rChild)) {
          n.removeChild(rChild);
          n.addChildToBack(new Node(150, rChild));
          n.putIntProp(8, 0);
        }
        return 1;
      }
      
      if (rType == 1) {
        if (!convertParameter(lChild)) {
          n.removeChild(lChild);
          n.addChildToFront(new Node(150, lChild));
          n.putIntProp(8, 0);
        }
        return 1;
      }
      if (!convertParameter(lChild)) {
        n.removeChild(lChild);
        n.addChildToFront(new Node(150, lChild));
      }
      if (!convertParameter(rChild)) {
        n.removeChild(rChild);
        n.addChildToBack(new Node(150, rChild));
      }
      n.putIntProp(8, 0);
      return 1;
    


    case 37: 
    case 140: 
      Node arrayBase = n.getFirstChild();
      Node arrayIndex = arrayBase.getNext();
      Node rValue = arrayIndex.getNext();
      int baseType = rewriteForNumberVariables(arrayBase, 1);
      if ((baseType == 1) && 
        (!convertParameter(arrayBase))) {
        n.removeChild(arrayBase);
        n.addChildToFront(new Node(149, arrayBase));
      }
      
      int indexType = rewriteForNumberVariables(arrayIndex, 1);
      if ((indexType == 1) && 
        (!convertParameter(arrayIndex)))
      {


        n.putIntProp(8, 1);
      }
      
      int rValueType = rewriteForNumberVariables(rValue, 1);
      if ((rValueType == 1) && 
        (!convertParameter(rValue))) {
        n.removeChild(rValue);
        n.addChildToBack(new Node(149, rValue));
      }
      
      return 0;
    
    case 36: 
      Node arrayBase = n.getFirstChild();
      Node arrayIndex = arrayBase.getNext();
      int baseType = rewriteForNumberVariables(arrayBase, 1);
      if ((baseType == 1) && 
        (!convertParameter(arrayBase))) {
        n.removeChild(arrayBase);
        n.addChildToFront(new Node(149, arrayBase));
      }
      
      int indexType = rewriteForNumberVariables(arrayIndex, 1);
      if ((indexType == 1) && 
        (!convertParameter(arrayIndex)))
      {


        n.putIntProp(8, 2);
      }
      
      return 0;
    
    case 38: 
      Node child = n.getFirstChild();
      
      rewriteAsObjectChildren(child, child.getFirstChild());
      child = child.getNext();
      

      OptFunctionNode target = (OptFunctionNode)n.getProp(9);
      if (target != null)
      {



        while (child != null) {
          int type = rewriteForNumberVariables(child, 1);
          if (type == 1) {
            markDCPNumberContext(child);
          }
          child = child.getNext();
        }
      }
      rewriteAsObjectChildren(n, child);
      
      return 0;
    }
    
    rewriteAsObjectChildren(n, n.getFirstChild());
    return 0;
  }
  


  private void rewriteAsObjectChildren(Node n, Node child)
  {
    while (child != null) {
      Node nextChild = child.getNext();
      int type = rewriteForNumberVariables(child, 0);
      if ((type == 1) && 
        (!convertParameter(child))) {
        n.removeChild(child);
        Node nuChild = new Node(149, child);
        if (nextChild == null) {
          n.addChildToBack(nuChild);
        } else {
          n.addChildBefore(nuChild, nextChild);
        }
      }
      child = nextChild;
    }
  }
  
  private static void buildStatementList_r(Node node, ObjArray statements) {
    int type = node.getType();
    if ((type == 129) || (type == 141) || (type == 132) || (type == 109))
    {
      Node child = node.getFirstChild();
      while (child != null) {
        buildStatementList_r(child, statements);
        child = child.getNext();
      }
    } else {
      statements.add(node);
    }
  }
}

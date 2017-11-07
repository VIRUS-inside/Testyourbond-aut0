package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import net.sourceforge.htmlunit.corejs.javascript.Node;

class Block {
  private Block[] itsSuccessors;
  private Block[] itsPredecessors;
  private int itsStartNodeIndex;
  private int itsEndNodeIndex;
  private int itsBlockID;
  private java.util.BitSet itsLiveOnEntrySet;
  private java.util.BitSet itsLiveOnExitSet;
  private java.util.BitSet itsUseBeforeDefSet;
  private java.util.BitSet itsNotDefSet;
  static final boolean DEBUG = false;
  private static int debug_blockCount;
  
  private static class FatBlock {
    private FatBlock() {}
    
    private static Block[] reduceToArray(net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap map) {
      Block[] result = null;
      if (!map.isEmpty()) {
        result = new Block[map.size()];
        int i = 0;
        net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap.Iterator iter = map.newIterator();
        for (iter.start(); !iter.done(); iter.next()) {
          FatBlock fb = (FatBlock)iter.getKey();
          result[(i++)] = realBlock;
        }
      }
      return result;
    }
    
    void addSuccessor(FatBlock b) {
      successors.put(b, 0);
    }
    
    void addPredecessor(FatBlock b) {
      predecessors.put(b, 0);
    }
    
    Block[] getSuccessors() {
      return reduceToArray(successors);
    }
    
    Block[] getPredecessors() {
      return reduceToArray(predecessors);
    }
    

    private net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap successors = new net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap();
    
    private net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap predecessors = new net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap();
    Block realBlock;
  }
  
  Block(int startNodeIndex, int endNodeIndex)
  {
    itsStartNodeIndex = startNodeIndex;
    itsEndNodeIndex = endNodeIndex;
  }
  
  static void runFlowAnalyzes(OptFunctionNode fn, Node[] statementNodes) {
    int paramCount = fnode.getParamCount();
    int varCount = fnode.getParamAndVarCount();
    int[] varTypes = new int[varCount];
    
    for (int i = 0; i != paramCount; i++) {
      varTypes[i] = 3;
    }
    

    for (int i = paramCount; i != varCount; i++) {
      varTypes[i] = 0;
    }
    
    Block[] theBlocks = buildBlocks(statementNodes);
    









    reachingDefDataFlow(fn, statementNodes, theBlocks, varTypes);
    typeFlow(fn, statementNodes, theBlocks, varTypes);
    











    for (int i = paramCount; i != varCount; i++) {
      if (varTypes[i] == 1) {
        fn.setIsNumberVar(i);
      }
    }
  }
  

  private static Block[] buildBlocks(Node[] statementNodes)
  {
    java.util.Map<Node, FatBlock> theTargetBlocks = new java.util.HashMap();
    net.sourceforge.htmlunit.corejs.javascript.ObjArray theBlocks = new net.sourceforge.htmlunit.corejs.javascript.ObjArray();
    

    int beginNodeIndex = 0;
    
    for (int i = 0; i < statementNodes.length; i++) {
      switch (statementNodes[i].getType()) {
      case 131: 
        if (i != beginNodeIndex) {
          FatBlock fb = newFatBlock(beginNodeIndex, i - 1);
          
          if (statementNodes[beginNodeIndex].getType() == 131) {
            theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
          }
          theBlocks.add(fb);
          
          beginNodeIndex = i; }
        break;
      

      case 5: 
      case 6: 
      case 7: 
        FatBlock fb = newFatBlock(beginNodeIndex, i);
        if (statementNodes[beginNodeIndex].getType() == 131) {
          theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
        }
        theBlocks.add(fb);
        
        beginNodeIndex = i + 1;
      }
      
    }
    

    if (beginNodeIndex != statementNodes.length) {
      FatBlock fb = newFatBlock(beginNodeIndex, statementNodes.length - 1);
      
      if (statementNodes[beginNodeIndex].getType() == 131) {
        theTargetBlocks.put(statementNodes[beginNodeIndex], fb);
      }
      theBlocks.add(fb);
    }
    


    for (int i = 0; i < theBlocks.size(); i++) {
      FatBlock fb = (FatBlock)theBlocks.get(i);
      
      Node blockEndNode = statementNodes[realBlock.itsEndNodeIndex];
      int blockEndNodeType = blockEndNode.getType();
      
      if ((blockEndNodeType != 5) && 
        (i < theBlocks.size() - 1)) {
        FatBlock fallThruTarget = (FatBlock)theBlocks.get(i + 1);
        fb.addSuccessor(fallThruTarget);
        fallThruTarget.addPredecessor(fb);
      }
      
      if ((blockEndNodeType == 7) || (blockEndNodeType == 6) || (blockEndNodeType == 5))
      {

        Node target = target;
        FatBlock branchTargetBlock = (FatBlock)theTargetBlocks.get(target);
        target.putProp(6, realBlock);
        
        fb.addSuccessor(branchTargetBlock);
        branchTargetBlock.addPredecessor(fb);
      }
    }
    
    Block[] result = new Block[theBlocks.size()];
    
    for (int i = 0; i < theBlocks.size(); i++) {
      FatBlock fb = (FatBlock)theBlocks.get(i);
      Block b = realBlock;
      itsSuccessors = fb.getSuccessors();
      itsPredecessors = fb.getPredecessors();
      itsBlockID = i;
      result[i] = b;
    }
    
    return result;
  }
  
  private static FatBlock newFatBlock(int startNodeIndex, int endNodeIndex) {
    FatBlock fb = new FatBlock(null);
    realBlock = new Block(startNodeIndex, endNodeIndex);
    return fb;
  }
  
  private static String toString(Block[] blockList, Node[] statementNodes)
  {
    return null;
  }
  





































  private static void reachingDefDataFlow(OptFunctionNode fn, Node[] statementNodes, Block[] theBlocks, int[] varTypes)
  {
    for (int i = 0; i < theBlocks.length; i++) {
      theBlocks[i].initLiveOnEntrySets(fn, statementNodes);
    }
    




    boolean[] visit = new boolean[theBlocks.length];
    boolean[] doneOnce = new boolean[theBlocks.length];
    int vIndex = theBlocks.length - 1;
    boolean needRescan = false;
    visit[vIndex] = true;
    for (;;) {
      if ((visit[vIndex] != 0) || (doneOnce[vIndex] == 0)) {
        doneOnce[vIndex] = true;
        visit[vIndex] = false;
        if (theBlocks[vIndex].doReachedUseDataFlow()) {
          Block[] pred = itsPredecessors;
          if (pred != null) {
            for (int i = 0; i < pred.length; i++) {
              int index = itsBlockID;
              visit[index] = true;
              needRescan |= index > vIndex;
            }
          }
        }
      }
      if (vIndex == 0) {
        if (!needRescan) break;
        vIndex = theBlocks.length - 1;
        needRescan = false;

      }
      else
      {
        vIndex--;
      }
    }
    





    theBlocks[0].markAnyTypeVariables(varTypes);
  }
  
  private static void typeFlow(OptFunctionNode fn, Node[] statementNodes, Block[] theBlocks, int[] varTypes)
  {
    boolean[] visit = new boolean[theBlocks.length];
    boolean[] doneOnce = new boolean[theBlocks.length];
    int vIndex = 0;
    boolean needRescan = false;
    visit[vIndex] = true;
    for (;;) {
      if ((visit[vIndex] != 0) || (doneOnce[vIndex] == 0)) {
        doneOnce[vIndex] = true;
        visit[vIndex] = false;
        if (theBlocks[vIndex].doTypeFlow(fn, statementNodes, varTypes))
        {
          Block[] succ = itsSuccessors;
          if (succ != null) {
            for (int i = 0; i < succ.length; i++) {
              int index = itsBlockID;
              visit[index] = true;
              needRescan |= index < vIndex;
            }
          }
        }
      }
      if (vIndex == theBlocks.length - 1) {
        if (!needRescan) break;
        vIndex = 0;
        needRescan = false;

      }
      else
      {
        vIndex++;
      }
    }
  }
  
  private static boolean assignType(int[] varTypes, int index, int type) {
    int prev = varTypes[index];
    return prev != (varTypes[index] |= type);
  }
  
  private void markAnyTypeVariables(int[] varTypes) {
    for (int i = 0; i != varTypes.length; i++) {
      if (itsLiveOnEntrySet.get(i)) {
        assignType(varTypes, i, 3);
      }
    }
  }
  







  private void lookForVariableAccess(OptFunctionNode fn, Node n)
  {
    switch (n.getType())
    {

    case 137: 
      int varIndex = fnode.getIndexForNameNode(n);
      if ((varIndex > -1) && (!itsNotDefSet.get(varIndex))) {
        itsUseBeforeDefSet.set(varIndex);
      }
      break;
    case 106: 
    case 107: 
      Node child = n.getFirstChild();
      if (child.getType() == 55) {
        int varIndex = fn.getVarIndex(child);
        if (!itsNotDefSet.get(varIndex))
          itsUseBeforeDefSet.set(varIndex);
        itsNotDefSet.set(varIndex);
      } else {
        lookForVariableAccess(fn, child);
      }
      
      break;
    case 56: 
    case 156: 
      Node lhs = n.getFirstChild();
      Node rhs = lhs.getNext();
      lookForVariableAccess(fn, rhs);
      itsNotDefSet.set(fn.getVarIndex(n));
      
      break;
    case 55: 
      int varIndex = fn.getVarIndex(n);
      if (!itsNotDefSet.get(varIndex)) {
        itsUseBeforeDefSet.set(varIndex);
      }
      break;
    default: 
      Node child = n.getFirstChild();
      while (child != null) {
        lookForVariableAccess(fn, child);
        child = child.getNext();
      }
    }
    
  }
  




  private void initLiveOnEntrySets(OptFunctionNode fn, Node[] statementNodes)
  {
    int listLength = fn.getVarCount();
    itsUseBeforeDefSet = new java.util.BitSet(listLength);
    itsNotDefSet = new java.util.BitSet(listLength);
    itsLiveOnEntrySet = new java.util.BitSet(listLength);
    itsLiveOnExitSet = new java.util.BitSet(listLength);
    for (int i = itsStartNodeIndex; i <= itsEndNodeIndex; i++) {
      Node n = statementNodes[i];
      lookForVariableAccess(fn, n);
    }
    itsNotDefSet.flip(0, listLength);
  }
  





  private boolean doReachedUseDataFlow()
  {
    itsLiveOnExitSet.clear();
    if (itsSuccessors != null) {
      for (int i = 0; i < itsSuccessors.length; i++) {
        itsLiveOnExitSet.or(itsSuccessors[i].itsLiveOnEntrySet);
      }
    }
    return updateEntrySet(itsLiveOnEntrySet, itsLiveOnExitSet, itsUseBeforeDefSet, itsNotDefSet);
  }
  

  private boolean updateEntrySet(java.util.BitSet entrySet, java.util.BitSet exitSet, java.util.BitSet useBeforeDef, java.util.BitSet notDef)
  {
    int card = entrySet.cardinality();
    entrySet.or(exitSet);
    entrySet.and(notDef);
    entrySet.or(useBeforeDef);
    return entrySet.cardinality() != card;
  }
  




  private static int findExpressionType(OptFunctionNode fn, Node n, int[] varTypes)
  {
    switch (n.getType()) {
    case 40: 
      return 1;
    
    case 30: 
    case 38: 
    case 70: 
      return 3;
    
    case 33: 
    case 36: 
    case 39: 
    case 43: 
      return 3;
    
    case 55: 
      return varTypes[fn.getVarIndex(n)];
    
    case 9: 
    case 10: 
    case 11: 
    case 18: 
    case 19: 
    case 20: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 27: 
    case 28: 
    case 29: 
    case 106: 
    case 107: 
      return 1;
    

    case 126: 
      return 3;
    

    case 12: 
    case 13: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 26: 
    case 31: 
    case 44: 
    case 45: 
    case 46: 
    case 47: 
    case 52: 
    case 53: 
    case 69: 
      return 3;
    

    case 32: 
    case 41: 
    case 137: 
      return 3;
    
    case 42: 
    case 48: 
    case 65: 
    case 66: 
    case 157: 
      return 3;
    



    case 21: 
      Node child = n.getFirstChild();
      int lType = findExpressionType(fn, child, varTypes);
      int rType = findExpressionType(fn, child.getNext(), varTypes);
      return lType | rType;
    

    case 102: 
      Node ifTrue = n.getFirstChild().getNext();
      Node ifFalse = ifTrue.getNext();
      int ifTrueType = findExpressionType(fn, ifTrue, varTypes);
      int ifFalseType = findExpressionType(fn, ifFalse, varTypes);
      return ifTrueType | ifFalseType;
    

    case 8: 
    case 35: 
    case 37: 
    case 56: 
    case 89: 
    case 156: 
      return findExpressionType(fn, n.getLastChild(), varTypes);
    
    case 104: 
    case 105: 
      Node child = n.getFirstChild();
      int lType = findExpressionType(fn, child, varTypes);
      int rType = findExpressionType(fn, child.getNext(), varTypes);
      return lType | rType;
    }
    
    
    return 3;
  }
  
  private static boolean findDefPoints(OptFunctionNode fn, Node n, int[] varTypes)
  {
    boolean result = false;
    Node first = n.getFirstChild();
    for (Node next = first; next != null; next = next.getNext()) {
      result |= findDefPoints(fn, next, varTypes);
    }
    switch (n.getType()) {
    case 106: 
    case 107: 
      if (first.getType() == 55)
      {
        int i = fn.getVarIndex(first);
        if (fnode.getParamAndVarConst()[i] == 0)
          result |= assignType(varTypes, i, 1);
      }
      break;
    
    case 56: 
    case 156: 
      Node rValue = first.getNext();
      int theType = findExpressionType(fn, rValue, varTypes);
      int i = fn.getVarIndex(n);
      if ((n.getType() != 56) || 
        (fnode.getParamAndVarConst()[i] == 0)) {
        result |= assignType(varTypes, i, theType);
      }
      break;
    }
    
    return result;
  }
  
  private boolean doTypeFlow(OptFunctionNode fn, Node[] statementNodes, int[] varTypes)
  {
    boolean changed = false;
    
    for (int i = itsStartNodeIndex; i <= itsEndNodeIndex; i++) {
      Node n = statementNodes[i];
      if (n != null) {
        changed |= findDefPoints(fn, n, varTypes);
      }
    }
    
    return changed;
  }
  
  private void printLiveOnEntrySet(OptFunctionNode fn) {}
}

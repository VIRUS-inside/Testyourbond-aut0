package net.sourceforge.htmlunit.corejs.javascript;

import net.sourceforge.htmlunit.corejs.javascript.ast.AstRoot;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.Jump;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.VariableInitializer;














class CodeGenerator
  extends Icode
{
  private static final int MIN_LABEL_TABLE_SIZE = 32;
  private static final int MIN_FIXUP_TABLE_SIZE = 40;
  private CompilerEnvirons compilerEnv;
  private boolean itsInFunctionFlag;
  private boolean itsInTryFlag;
  private InterpreterData itsData;
  private ScriptNode scriptOrFn;
  private int iCodeTop;
  private int stackDepth;
  private int lineNumber;
  private int doubleTableTop;
  private ObjToIntMap strings = new ObjToIntMap(20);
  
  private int localTop;
  
  private int[] labelTable;
  private int labelTableTop;
  private long[] fixupTable;
  private int fixupTableTop;
  private ObjArray literalIds = new ObjArray();
  
  private int exceptionTableTop;
  private static final int ECF_TAIL = 1;
  
  CodeGenerator() {}
  
  public InterpreterData compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction)
  {
    this.compilerEnv = compilerEnv;
    





    new NodeTransformer().transform(tree);
    





    if (returnFunction) {
      scriptOrFn = tree.getFunctionNode(0);
    } else {
      scriptOrFn = tree;
    }
    

    itsData = new InterpreterData(compilerEnv.getLanguageVersion(), scriptOrFn.getSourceName(), encodedSource, ((AstRoot)tree).isInStrictMode());
    itsData.topLevel = true;
    
    if (returnFunction) {
      generateFunctionICode();
    } else {
      generateICodeFromTree(scriptOrFn);
    }
    return itsData;
  }
  
  private void generateFunctionICode() {
    itsInFunctionFlag = true;
    
    FunctionNode theFunction = (FunctionNode)scriptOrFn;
    
    itsData.itsFunctionType = theFunction.getFunctionType();
    itsData.itsNeedsActivation = theFunction.requiresActivation();
    if (theFunction.getFunctionName() != null) {
      itsData.itsName = theFunction.getName();
    }
    if (theFunction.isGenerator()) {
      addIcode(-62);
      addUint16(theFunction.getBaseLineno() & 0xFFFF);
    }
    
    itsData.declaredAsVar = (theFunction.getParent() instanceof VariableInitializer);
    
    generateICodeFromTree(theFunction.getLastChild());
  }
  
  private void generateICodeFromTree(Node tree) {
    generateNestedFunctions();
    
    generateRegExpLiterals();
    
    visitStatement(tree, 0);
    fixLabelGotos();
    
    if (itsData.itsFunctionType == 0) {
      addToken(64);
    }
    
    if (itsData.itsICode.length != iCodeTop)
    {

      byte[] tmp = new byte[iCodeTop];
      System.arraycopy(itsData.itsICode, 0, tmp, 0, iCodeTop);
      itsData.itsICode = tmp;
    }
    if (strings.size() == 0) {
      itsData.itsStringTable = null;
    } else {
      itsData.itsStringTable = new String[strings.size()];
      ObjToIntMap.Iterator iter = strings.newIterator();
      for (iter.start(); !iter.done(); iter.next()) {
        String str = (String)iter.getKey();
        int index = iter.getValue();
        if (itsData.itsStringTable[index] != null)
          Kit.codeBug();
        itsData.itsStringTable[index] = str;
      }
    }
    if (doubleTableTop == 0) {
      itsData.itsDoubleTable = null;
    } else if (itsData.itsDoubleTable.length != doubleTableTop) {
      double[] tmp = new double[doubleTableTop];
      System.arraycopy(itsData.itsDoubleTable, 0, tmp, 0, doubleTableTop);
      itsData.itsDoubleTable = tmp;
    }
    if ((exceptionTableTop != 0) && (itsData.itsExceptionTable.length != exceptionTableTop))
    {
      int[] tmp = new int[exceptionTableTop];
      System.arraycopy(itsData.itsExceptionTable, 0, tmp, 0, exceptionTableTop);
      
      itsData.itsExceptionTable = tmp;
    }
    
    itsData.itsMaxVars = scriptOrFn.getParamAndVarCount();
    

    itsData.itsMaxFrameArray = (itsData.itsMaxVars + itsData.itsMaxLocals + itsData.itsMaxStack);
    

    itsData.argNames = scriptOrFn.getParamAndVarNames();
    itsData.argIsConst = scriptOrFn.getParamAndVarConst();
    itsData.argCount = scriptOrFn.getParamCount();
    
    itsData.encodedSourceStart = scriptOrFn.getEncodedSourceStart();
    itsData.encodedSourceEnd = scriptOrFn.getEncodedSourceEnd();
    
    if (literalIds.size() != 0) {
      itsData.literalIds = literalIds.toArray();
    }
  }
  


  private void generateNestedFunctions()
  {
    int functionCount = scriptOrFn.getFunctionCount();
    if (functionCount == 0) {
      return;
    }
    InterpreterData[] array = new InterpreterData[functionCount];
    for (int i = 0; i != functionCount; i++) {
      FunctionNode fn = scriptOrFn.getFunctionNode(i);
      CodeGenerator gen = new CodeGenerator();
      compilerEnv = compilerEnv;
      scriptOrFn = fn;
      itsData = new InterpreterData(itsData);
      gen.generateFunctionICode();
      array[i] = itsData;
    }
    itsData.itsNestedFunctions = array;
  }
  
  private void generateRegExpLiterals() {
    int N = scriptOrFn.getRegexpCount();
    if (N == 0) {
      return;
    }
    Context cx = Context.getContext();
    RegExpProxy rep = ScriptRuntime.checkRegExpProxy(cx);
    Object[] array = new Object[N];
    for (int i = 0; i != N; i++) {
      String string = scriptOrFn.getRegexpString(i);
      String flags = scriptOrFn.getRegexpFlags(i);
      array[i] = rep.compileRegExp(cx, string, flags);
    }
    itsData.itsRegExpLiterals = array;
  }
  
  private void updateLineNumber(Node node) {
    int lineno = node.getLineno();
    if ((lineno != lineNumber) && (lineno >= 0)) {
      if (itsData.firstLinePC < 0) {
        itsData.firstLinePC = lineno;
      }
      lineNumber = lineno;
      addIcode(-26);
      addUint16(lineno & 0xFFFF);
    }
  }
  
  private RuntimeException badTree(Node node) {
    throw new RuntimeException(node.toString());
  }
  
  private void visitStatement(Node node, int initialStackDepth) {
    int type = node.getType();
    Node child = node.getFirstChild();
    switch (type)
    {
    case 109: 
      int fnIndex = node.getExistingIntProp(1);
      int fnType = scriptOrFn.getFunctionNode(fnIndex).getFunctionType();
      





      if (fnType == 3) {
        addIndexOp(-20, fnIndex);
      }
      else if (fnType != 1) {
        throw Kit.codeBug();
      }
      





      if (!itsInFunctionFlag) {
        addIndexOp(-19, fnIndex);
        stackChange(1);
        addIcode(-5);
        stackChange(-1);
      }
      
      break;
    
    case 123: 
    case 128: 
    case 129: 
    case 130: 
    case 132: 
      updateLineNumber(node);
    case 136: case 2: case 3: case 141: case 160: case 114: case 131: case 6: case 7: case 5: case 135: case 125: case 133: 
    case 134: case 81: case 57: case 50: case 51: case 4: case 64: case 58: case 59: case 60: case -62: default: 
      while (child != null) {
        visitStatement(child, initialStackDepth);
        child = child.getNext(); continue;
        



        visitExpression(child, 0);
        addToken(2);
        stackChange(-1);
        break;
        

        addToken(3);
        break;
        

        int local = allocLocal();
        node.putIntProp(2, local);
        updateLineNumber(node);
        while (child != null) {
          visitStatement(child, initialStackDepth);
          child = child.getNext();
        }
        addIndexOp(-56, local);
        releaseLocal(local);
        
        break;
        

        addIcode(-64);
        break;
        

        updateLineNumber(node);
        


        visitExpression(child, 0);
        
        for (Jump caseNode = (Jump)child.getNext(); caseNode != null; 
            caseNode = (Jump)caseNode.getNext()) {
          if (caseNode.getType() != 115)
            throw badTree(caseNode);
          Node test = caseNode.getFirstChild();
          addIcode(-1);
          stackChange(1);
          visitExpression(test, 0);
          addToken(46);
          stackChange(-1);
          

          addGoto(target, -6);
          stackChange(-1);
        }
        addIcode(-4);
        stackChange(-1);
        
        break;
        

        markTargetLabel(node);
        break;
        


        Node target = target;
        visitExpression(child, 0);
        addGoto(target, type);
        stackChange(-1);
        
        break;
        

        Node target = target;
        addGoto(target, type);
        
        break;
        

        Node target = target;
        addGoto(target, -23);
        
        break;
        


        stackChange(1);
        int finallyRegister = getLocalBlockRef(node);
        addIndexOp(-24, finallyRegister);
        stackChange(-1);
        while (child != null) {
          visitStatement(child, initialStackDepth);
          child = child.getNext();
        }
        addIndexOp(-25, finallyRegister);
        
        break;
        


        updateLineNumber(node);
        visitExpression(child, 0);
        addIcode(type == 133 ? -4 : -5);
        stackChange(-1);
        break;
        

        Jump tryNode = (Jump)node;
        int exceptionObjectLocal = getLocalBlockRef(tryNode);
        int scopeLocal = allocLocal();
        
        addIndexOp(-13, scopeLocal);
        
        int tryStart = iCodeTop;
        boolean savedFlag = itsInTryFlag;
        itsInTryFlag = true;
        while (child != null) {
          visitStatement(child, initialStackDepth);
          child = child.getNext();
        }
        itsInTryFlag = savedFlag;
        
        Node catchTarget = target;
        if (catchTarget != null) {
          int catchStartPC = labelTable[getTargetLabel(catchTarget)];
          addExceptionHandler(tryStart, catchStartPC, catchStartPC, false, exceptionObjectLocal, scopeLocal);
        }
        
        Node finallyTarget = tryNode.getFinally();
        if (finallyTarget != null) {
          int finallyStartPC = labelTable[getTargetLabel(finallyTarget)];
          addExceptionHandler(tryStart, finallyStartPC, finallyStartPC, true, exceptionObjectLocal, scopeLocal);
        }
        

        addIndexOp(-56, scopeLocal);
        releaseLocal(scopeLocal);
        
        break;
        

        int localIndex = getLocalBlockRef(node);
        int scopeIndex = node.getExistingIntProp(14);
        String name = child.getString();
        child = child.getNext();
        visitExpression(child, 0);
        addStringPrefix(name);
        addIndexPrefix(localIndex);
        addToken(57);
        addUint8(scopeIndex != 0 ? 1 : 0);
        stackChange(-1);
        
        break;
        

        updateLineNumber(node);
        visitExpression(child, 0);
        addToken(50);
        addUint16(lineNumber & 0xFFFF);
        stackChange(-1);
        break;
        

        updateLineNumber(node);
        addIndexOp(51, getLocalBlockRef(node));
        break;
        

        updateLineNumber(node);
        if (node.getIntProp(20, 0) != 0)
        {
          addIcode(-63);
          addUint16(lineNumber & 0xFFFF);
        } else if (child != null) {
          visitExpression(child, 1);
          addToken(4);
          stackChange(-1);
        } else {
          addIcode(-22);
          
          break;
          

          updateLineNumber(node);
          addToken(64);
          break;
          



          visitExpression(child, 0);
          addIndexOp(type, getLocalBlockRef(node));
          stackChange(-1);
          break;
          

          break;
          

          throw badTree(node);
        }
      } }
    if (stackDepth != initialStackDepth) {
      throw Kit.codeBug();
    }
  }
  
  private void visitExpression(Node node, int contextFlags) {
    int type = node.getType();
    Node child = node.getFirstChild();
    int savedStackDepth = stackDepth;
    switch (type)
    {
    case 109: 
      int fnIndex = node.getExistingIntProp(1);
      FunctionNode fn = scriptOrFn.getFunctionNode(fnIndex);
      
      if (fn.getFunctionType() != 2) {
        throw Kit.codeBug();
      }
      addIndexOp(-19, fnIndex);
      stackChange(1);
      
      break;
    
    case 54: 
      int localIndex = getLocalBlockRef(node);
      addIndexOp(54, localIndex);
      stackChange(1);
      
      break;
    
    case 89: 
      Node lastChild = node.getLastChild();
      while (child != lastChild) {
        visitExpression(child, 0);
        addIcode(-4);
        stackChange(-1);
        child = child.getNext();
      }
      
      visitExpression(child, contextFlags & 0x1);
      
      break;
    


    case 138: 
      stackChange(1);
      break;
    
    case 30: 
    case 38: 
    case 70: 
      if (type == 30) {
        visitExpression(child, 0);
      } else {
        generateCallFunAndThis(child);
      }
      int argCount = 0;
      while ((child = child.getNext()) != null) {
        visitExpression(child, 0);
        argCount++;
      }
      int callType = node.getIntProp(10, 0);
      
      if ((type != 70) && (callType != 0))
      {
        addIndexOp(-21, argCount);
        addUint8(callType);
        addUint8(type == 30 ? 1 : 0);
        addUint16(lineNumber & 0xFFFF);

      }
      else
      {
        if ((type == 38) && ((contextFlags & 0x1) != 0) && 
          (!compilerEnv.isGenerateDebugInfo()) && (!itsInTryFlag))
        {
          type = -55;
        }
        addIndexOp(type, argCount);
      }
      
      if (type == 30)
      {
        stackChange(-argCount);
      }
      else
      {
        stackChange(-1 - argCount);
      }
      if (argCount > itsData.itsMaxCalleeArgs) {
        itsData.itsMaxCalleeArgs = argCount;
      }
      
      break;
    
    case 104: 
    case 105: 
      visitExpression(child, 0);
      addIcode(-1);
      stackChange(1);
      int afterSecondJumpStart = iCodeTop;
      int jump = type == 105 ? 7 : 6;
      addGotoOp(jump);
      stackChange(-1);
      addIcode(-4);
      stackChange(-1);
      child = child.getNext();
      
      visitExpression(child, contextFlags & 0x1);
      resolveForwardGoto(afterSecondJumpStart);
      
      break;
    
    case 102: 
      Node ifThen = child.getNext();
      Node ifElse = ifThen.getNext();
      visitExpression(child, 0);
      int elseJumpStart = iCodeTop;
      addGotoOp(7);
      stackChange(-1);
      
      visitExpression(ifThen, contextFlags & 0x1);
      int afterElseJumpStart = iCodeTop;
      addGotoOp(5);
      resolveForwardGoto(elseJumpStart);
      stackDepth = savedStackDepth;
      
      visitExpression(ifElse, contextFlags & 0x1);
      resolveForwardGoto(afterElseJumpStart);
      
      break;
    
    case 33: 
    case 34: 
      visitExpression(child, 0);
      child = child.getNext();
      addStringOp(type, child.getString());
      break;
    
    case 31: 
      boolean isName = child.getType() == 49;
      visitExpression(child, 0);
      child = child.getNext();
      visitExpression(child, 0);
      if (isName)
      {
        addIcode(0);
      } else {
        addToken(31);
      }
      stackChange(-1);
      break;
    
    case 9: 
    case 10: 
    case 11: 
    case 12: 
    case 13: 
    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 18: 
    case 19: 
    case 20: 
    case 21: 
    case 22: 
    case 23: 
    case 24: 
    case 25: 
    case 36: 
    case 46: 
    case 47: 
    case 52: 
    case 53: 
      visitExpression(child, 0);
      child = child.getNext();
      visitExpression(child, 0);
      addToken(type);
      stackChange(-1);
      break;
    
    case 26: 
    case 27: 
    case 28: 
    case 29: 
    case 32: 
    case 126: 
      visitExpression(child, 0);
      if (type == 126) {
        addIcode(-4);
        addIcode(-50);
      } else {
        addToken(type);
      }
      break;
    
    case 67: 
    case 69: 
      visitExpression(child, 0);
      addToken(type);
      break;
    
    case 35: 
    case 139: 
      visitExpression(child, 0);
      child = child.getNext();
      String property = child.getString();
      child = child.getNext();
      if (type == 139) {
        addIcode(-1);
        stackChange(1);
        addStringOp(33, property);
        
        stackChange(-1);
      }
      visitExpression(child, 0);
      addStringOp(35, property);
      stackChange(-1);
      
      break;
    
    case 37: 
    case 140: 
      visitExpression(child, 0);
      child = child.getNext();
      visitExpression(child, 0);
      child = child.getNext();
      if (type == 140) {
        addIcode(-2);
        stackChange(2);
        addToken(36);
        stackChange(-1);
        
        stackChange(-1);
      }
      visitExpression(child, 0);
      addToken(37);
      stackChange(-2);
      break;
    
    case 68: 
    case 142: 
      visitExpression(child, 0);
      child = child.getNext();
      if (type == 142) {
        addIcode(-1);
        stackChange(1);
        addToken(67);
        
        stackChange(-1);
      }
      visitExpression(child, 0);
      addToken(68);
      stackChange(-1);
      break;
    
    case 8: 
    case 73: 
      String name = child.getString();
      visitExpression(child, 0);
      child = child.getNext();
      visitExpression(child, 0);
      addStringOp(type, name);
      stackChange(-1);
      
      break;
    
    case 155: 
      String name = child.getString();
      visitExpression(child, 0);
      child = child.getNext();
      visitExpression(child, 0);
      addStringOp(-59, name);
      stackChange(-1);
      
      break;
    
    case 137: 
      int index = -1;
      

      if ((itsInFunctionFlag) && (!itsData.itsNeedsActivation))
        index = scriptOrFn.getIndexForNameNode(node);
      if (index == -1) {
        addStringOp(-14, node.getString());
        stackChange(1);
      } else {
        addVarOp(55, index);
        stackChange(1);
        addToken(32);
      }
      
      break;
    
    case 39: 
    case 41: 
    case 49: 
      addStringOp(type, node.getString());
      stackChange(1);
      break;
    
    case 106: 
    case 107: 
      visitIncDec(node, child);
      break;
    
    case 40: 
      double num = node.getDouble();
      int inum = (int)num;
      if (inum == num) {
        if (inum == 0) {
          addIcode(-51);
          
          if (1.0D / num < 0.0D) {
            addToken(29);
          }
        } else if (inum == 1) {
          addIcode(-52);
        } else if ((short)inum == inum) {
          addIcode(-27);
          
          addUint16(inum & 0xFFFF);
        } else {
          addIcode(-28);
          addInt(inum);
        }
      } else {
        int index = getDoubleIndex(num);
        addIndexOp(40, index);
      }
      stackChange(1);
      
      break;
    
    case 55: 
      if (itsData.itsNeedsActivation)
        Kit.codeBug();
      int index = scriptOrFn.getIndexForNameNode(node);
      addVarOp(55, index);
      stackChange(1);
      
      break;
    
    case 56: 
      if (itsData.itsNeedsActivation)
        Kit.codeBug();
      int index = scriptOrFn.getIndexForNameNode(child);
      child = child.getNext();
      visitExpression(child, 0);
      addVarOp(56, index);
      
      break;
    
    case 156: 
      if (itsData.itsNeedsActivation)
        Kit.codeBug();
      int index = scriptOrFn.getIndexForNameNode(child);
      child = child.getNext();
      visitExpression(child, 0);
      addVarOp(156, index);
      
      break;
    
    case 42: 
    case 43: 
    case 44: 
    case 45: 
    case 63: 
      addToken(type);
      stackChange(1);
      break;
    
    case 61: 
    case 62: 
      addIndexOp(type, getLocalBlockRef(node));
      stackChange(1);
      break;
    
    case 48: 
      int index = node.getExistingIntProp(4);
      addIndexOp(48, index);
      stackChange(1);
      
      break;
    
    case 65: 
    case 66: 
      visitLiteral(node, child);
      break;
    
    case 157: 
      visitArrayComprehension(node, child, child.getNext());
      break;
    
    case 71: 
      visitExpression(child, 0);
      addStringOp(type, (String)node.getProp(17));
      break;
    
    case 77: 
    case 78: 
    case 79: 
    case 80: 
      int memberTypeFlags = node.getIntProp(16, 0);
      
      int childCount = 0;
      do {
        visitExpression(child, 0);
        childCount++;
        child = child.getNext();
      } while (child != null);
      addIndexOp(type, memberTypeFlags);
      stackChange(1 - childCount);
      
      break;
    

    case 146: 
      updateLineNumber(node);
      visitExpression(child, 0);
      addIcode(-53);
      stackChange(-1);
      int queryPC = iCodeTop;
      visitExpression(child.getNext(), 0);
      addBackwardGoto(-54, queryPC);
      
      break;
    
    case 74: 
    case 75: 
    case 76: 
      visitExpression(child, 0);
      addToken(type);
      break;
    
    case 72: 
      if (child != null) {
        visitExpression(child, 0);
      } else {
        addIcode(-50);
        stackChange(1);
      }
      addToken(72);
      addUint16(node.getLineno() & 0xFFFF);
      break;
    
    case 159: 
      Node enterWith = node.getFirstChild();
      Node with = enterWith.getNext();
      visitExpression(enterWith.getFirstChild(), 0);
      addToken(2);
      stackChange(-1);
      visitExpression(with.getFirstChild(), 0);
      addToken(3);
      break;
    case 50: case 51: case 57: case 58: case 59: case 60: case 64: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 90: case 91: case 92: case 93: case 94: case 95: case 96: case 97: 
    case 98: case 99: case 100: case 101: case 103: case 108: case 110: case 111: case 112: case 113: case 114: case 115: case 116: case 117: case 118: case 119: case 120: case 121: case 122: case 123: case 124: case 125: case 127: 
    case 128: case 129: case 130: case 131: case 132: case 133: case 134: case 135: case 136: case 141: case 143: case 144: case 145: case 147: case 148: case 149: case 150: case 151: case 152: case 153: case 154: case 158: default: 
      throw badTree(node);
    }
    if (savedStackDepth + 1 != stackDepth) {
      Kit.codeBug();
    }
  }
  
  private void generateCallFunAndThis(Node left)
  {
    int type = left.getType();
    switch (type) {
    case 39: 
      String name = left.getString();
      
      addStringOp(-15, name);
      stackChange(2);
      break;
    
    case 33: 
    case 36: 
      Node target = left.getFirstChild();
      visitExpression(target, 0);
      Node id = target.getNext();
      if (type == 33) {
        String property = id.getString();
        
        addStringOp(-16, property);
        stackChange(1);
      } else {
        visitExpression(id, 0);
        
        addIcode(-17);
      }
      break;
    

    default: 
      visitExpression(left, 0);
      
      addIcode(-18);
      stackChange(1);
    }
  }
  
  private void visitIncDec(Node node, Node child)
  {
    int incrDecrMask = node.getExistingIntProp(13);
    int childType = child.getType();
    switch (childType) {
    case 55: 
      if (itsData.itsNeedsActivation)
        Kit.codeBug();
      int i = scriptOrFn.getIndexForNameNode(child);
      addVarOp(-7, i);
      addUint8(incrDecrMask);
      stackChange(1);
      break;
    
    case 39: 
      String name = child.getString();
      addStringOp(-8, name);
      addUint8(incrDecrMask);
      stackChange(1);
      break;
    
    case 33: 
      Node object = child.getFirstChild();
      visitExpression(object, 0);
      String property = object.getNext().getString();
      addStringOp(-9, property);
      addUint8(incrDecrMask);
      break;
    
    case 36: 
      Node object = child.getFirstChild();
      visitExpression(object, 0);
      Node index = object.getNext();
      visitExpression(index, 0);
      addIcode(-10);
      addUint8(incrDecrMask);
      stackChange(-1);
      break;
    
    case 67: 
      Node ref = child.getFirstChild();
      visitExpression(ref, 0);
      addIcode(-11);
      addUint8(incrDecrMask);
      break;
    
    default: 
      throw badTree(node);
    }
  }
  
  private void visitLiteral(Node node, Node child)
  {
    int type = node.getType();
    
    Object[] propertyIds = null;
    if (type == 65) {
      int count = 0;
      for (Node n = child; n != null; n = n.getNext())
        count++;
    } else { int count;
      if (type == 66) {
        propertyIds = (Object[])node.getProp(12);
        count = propertyIds.length;
      } else {
        throw badTree(node); } }
    int count;
    addIndexOp(-29, count);
    stackChange(2);
    while (child != null) {
      int childType = child.getType();
      if (childType == 151) {
        visitExpression(child.getFirstChild(), 0);
        addIcode(-57);
      } else if (childType == 152) {
        visitExpression(child.getFirstChild(), 0);
        addIcode(-58);
      } else {
        visitExpression(child, 0);
        addIcode(-30);
      }
      stackChange(-1);
      child = child.getNext();
    }
    if (type == 65) {
      int[] skipIndexes = (int[])node.getProp(11);
      if (skipIndexes == null) {
        addToken(65);
      } else {
        int index = literalIds.size();
        literalIds.add(skipIndexes);
        addIndexOp(-31, index);
      }
    } else {
      int index = literalIds.size();
      literalIds.add(propertyIds);
      addIndexOp(66, index);
    }
    stackChange(-1);
  }
  




  private void visitArrayComprehension(Node node, Node initStmt, Node expr)
  {
    visitStatement(initStmt, stackDepth);
    visitExpression(expr, 0);
  }
  
  private int getLocalBlockRef(Node node) {
    Node localBlock = (Node)node.getProp(3);
    return localBlock.getExistingIntProp(2);
  }
  
  private int getTargetLabel(Node target) {
    int label = target.labelId();
    if (label != -1) {
      return label;
    }
    label = labelTableTop;
    if ((labelTable == null) || (label == labelTable.length)) {
      if (labelTable == null) {
        labelTable = new int[32];
      } else {
        int[] tmp = new int[labelTable.length * 2];
        System.arraycopy(labelTable, 0, tmp, 0, label);
        labelTable = tmp;
      }
    }
    labelTableTop = (label + 1);
    labelTable[label] = -1;
    
    target.labelId(label);
    return label;
  }
  
  private void markTargetLabel(Node target) {
    int label = getTargetLabel(target);
    if (labelTable[label] != -1)
    {
      Kit.codeBug();
    }
    labelTable[label] = iCodeTop;
  }
  
  private void addGoto(Node target, int gotoOp) {
    int label = getTargetLabel(target);
    if (label >= labelTableTop)
      Kit.codeBug();
    int targetPC = labelTable[label];
    
    if (targetPC != -1) {
      addBackwardGoto(gotoOp, targetPC);
    } else {
      int gotoPC = iCodeTop;
      addGotoOp(gotoOp);
      int top = fixupTableTop;
      if ((fixupTable == null) || (top == fixupTable.length)) {
        if (fixupTable == null) {
          fixupTable = new long[40];
        } else {
          long[] tmp = new long[fixupTable.length * 2];
          System.arraycopy(fixupTable, 0, tmp, 0, top);
          fixupTable = tmp;
        }
      }
      fixupTableTop = (top + 1);
      fixupTable[top] = (label << 32 | gotoPC);
    }
  }
  
  private void fixLabelGotos() {
    for (int i = 0; i < fixupTableTop; i++) {
      long fixup = fixupTable[i];
      int label = (int)(fixup >> 32);
      int jumpSource = (int)fixup;
      int pc = labelTable[label];
      if (pc == -1)
      {
        throw Kit.codeBug();
      }
      resolveGoto(jumpSource, pc);
    }
    fixupTableTop = 0;
  }
  
  private void addBackwardGoto(int gotoOp, int jumpPC) {
    int fromPC = iCodeTop;
    
    if (fromPC <= jumpPC)
      throw Kit.codeBug();
    addGotoOp(gotoOp);
    resolveGoto(fromPC, jumpPC);
  }
  
  private void resolveForwardGoto(int fromPC)
  {
    if (iCodeTop < fromPC + 3)
      throw Kit.codeBug();
    resolveGoto(fromPC, iCodeTop);
  }
  
  private void resolveGoto(int fromPC, int jumpPC) {
    int offset = jumpPC - fromPC;
    
    if ((0 <= offset) && (offset <= 2))
      throw Kit.codeBug();
    int offsetSite = fromPC + 1;
    if (offset != (short)offset) {
      if (itsData.longJumps == null) {
        itsData.longJumps = new UintMap();
      }
      itsData.longJumps.put(offsetSite, jumpPC);
      offset = 0;
    }
    byte[] array = itsData.itsICode;
    array[offsetSite] = ((byte)(offset >> 8));
    array[(offsetSite + 1)] = ((byte)offset);
  }
  
  private void addToken(int token) {
    if (!Icode.validTokenCode(token))
      throw Kit.codeBug();
    addUint8(token);
  }
  
  private void addIcode(int icode) {
    if (!Icode.validIcode(icode)) {
      throw Kit.codeBug();
    }
    addUint8(icode & 0xFF);
  }
  
  private void addUint8(int value) {
    if ((value & 0xFF00) != 0)
      throw Kit.codeBug();
    byte[] array = itsData.itsICode;
    int top = iCodeTop;
    if (top == array.length) {
      array = increaseICodeCapacity(1);
    }
    array[top] = ((byte)value);
    iCodeTop = (top + 1);
  }
  
  private void addUint16(int value) {
    if ((value & 0xFFFF0000) != 0)
      throw Kit.codeBug();
    byte[] array = itsData.itsICode;
    int top = iCodeTop;
    if (top + 2 > array.length) {
      array = increaseICodeCapacity(2);
    }
    array[top] = ((byte)(value >>> 8));
    array[(top + 1)] = ((byte)value);
    iCodeTop = (top + 2);
  }
  
  private void addInt(int i) {
    byte[] array = itsData.itsICode;
    int top = iCodeTop;
    if (top + 4 > array.length) {
      array = increaseICodeCapacity(4);
    }
    array[top] = ((byte)(i >>> 24));
    array[(top + 1)] = ((byte)(i >>> 16));
    array[(top + 2)] = ((byte)(i >>> 8));
    array[(top + 3)] = ((byte)i);
    iCodeTop = (top + 4);
  }
  
  private int getDoubleIndex(double num) {
    int index = doubleTableTop;
    if (index == 0) {
      itsData.itsDoubleTable = new double[64];
    } else if (itsData.itsDoubleTable.length == index) {
      double[] na = new double[index * 2];
      System.arraycopy(itsData.itsDoubleTable, 0, na, 0, index);
      itsData.itsDoubleTable = na;
    }
    itsData.itsDoubleTable[index] = num;
    doubleTableTop = (index + 1);
    return index;
  }
  
  private void addGotoOp(int gotoOp) {
    byte[] array = itsData.itsICode;
    int top = iCodeTop;
    if (top + 3 > array.length) {
      array = increaseICodeCapacity(3);
    }
    array[top] = ((byte)gotoOp);
    
    iCodeTop = (top + 1 + 2);
  }
  
  private void addVarOp(int op, int varIndex) {
    switch (op) {
    case 156: 
      if (varIndex < 128) {
        addIcode(-61);
        addUint8(varIndex);
        return;
      }
      addIndexOp(-60, varIndex);
      return;
    case 55: 
    case 56: 
      if (varIndex < 128) {
        addIcode(op == 55 ? -48 : -49);
        addUint8(varIndex);
        return;
      }
    
    case -7: 
      addIndexOp(op, varIndex);
      return;
    }
    throw Kit.codeBug();
  }
  
  private void addStringOp(int op, String str) {
    addStringPrefix(str);
    if (Icode.validIcode(op)) {
      addIcode(op);
    } else {
      addToken(op);
    }
  }
  
  private void addIndexOp(int op, int index) {
    addIndexPrefix(index);
    if (Icode.validIcode(op)) {
      addIcode(op);
    } else {
      addToken(op);
    }
  }
  
  private void addStringPrefix(String str) {
    int index = strings.get(str, -1);
    if (index == -1) {
      index = strings.size();
      strings.put(str, index);
    }
    if (index < 4) {
      addIcode(-41 - index);
    } else if (index <= 255) {
      addIcode(-45);
      addUint8(index);
    } else if (index <= 65535) {
      addIcode(-46);
      addUint16(index);
    } else {
      addIcode(-47);
      addInt(index);
    }
  }
  
  private void addIndexPrefix(int index) {
    if (index < 0)
      Kit.codeBug();
    if (index < 6) {
      addIcode(-32 - index);
    } else if (index <= 255) {
      addIcode(-38);
      addUint8(index);
    } else if (index <= 65535) {
      addIcode(-39);
      addUint16(index);
    } else {
      addIcode(-40);
      addInt(index);
    }
  }
  

  private void addExceptionHandler(int icodeStart, int icodeEnd, int handlerStart, boolean isFinally, int exceptionObjectLocal, int scopeLocal)
  {
    int top = exceptionTableTop;
    int[] table = itsData.itsExceptionTable;
    if (table == null) {
      if (top != 0)
        Kit.codeBug();
      table = new int[12];
      itsData.itsExceptionTable = table;
    } else if (table.length == top) {
      table = new int[table.length * 2];
      System.arraycopy(itsData.itsExceptionTable, 0, table, 0, top);
      itsData.itsExceptionTable = table;
    }
    table[(top + 0)] = icodeStart;
    table[(top + 1)] = icodeEnd;
    table[(top + 2)] = handlerStart;
    table[(top + 3)] = (isFinally ? 1 : 0);
    table[(top + 4)] = exceptionObjectLocal;
    table[(top + 5)] = scopeLocal;
    
    exceptionTableTop = (top + 6);
  }
  
  private byte[] increaseICodeCapacity(int extraSize) {
    int capacity = itsData.itsICode.length;
    int top = iCodeTop;
    if (top + extraSize <= capacity)
      throw Kit.codeBug();
    capacity *= 2;
    if (top + extraSize > capacity) {
      capacity = top + extraSize;
    }
    byte[] array = new byte[capacity];
    System.arraycopy(itsData.itsICode, 0, array, 0, top);
    itsData.itsICode = array;
    return array;
  }
  
  private void stackChange(int change) {
    if (change <= 0) {
      stackDepth += change;
    } else {
      int newDepth = stackDepth + change;
      if (newDepth > itsData.itsMaxStack) {
        itsData.itsMaxStack = newDepth;
      }
      stackDepth = newDepth;
    }
  }
  
  private int allocLocal() {
    int localSlot = localTop;
    localTop += 1;
    if (localTop > itsData.itsMaxLocals) {
      itsData.itsMaxLocals = localTop;
    }
    return localSlot;
  }
  
  private void releaseLocal(int localSlot) {
    localTop -= 1;
    if (localSlot != localTop) {
      Kit.codeBug();
    }
  }
}

package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.classfile.ClassFileWriter;
import net.sourceforge.htmlunit.corejs.javascript.CompilerEnvirons;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.Node;
import net.sourceforge.htmlunit.corejs.javascript.Token;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.Jump;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































class BodyCodegen
{
  private static final int JAVASCRIPT_EXCEPTION = 0;
  private static final int EVALUATOR_EXCEPTION = 1;
  private static final int ECMAERROR_EXCEPTION = 2;
  private static final int THROWABLE_EXCEPTION = 3;
  private static final int FINALLY_EXCEPTION = 4;
  private static final int EXCEPTION_MAX = 5;
  
  BodyCodegen() {}
  
  void generateBodyCode()
  {
    isGenerator = Codegen.isGenerator(scriptOrFn);
    

    initBodyGeneration();
    
    if (isGenerator)
    {


      String type = "(" + codegen.mainClassSignature + "Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;Ljava/lang/Object;I)Ljava/lang/Object;";
      



      cfw.startMethod(codegen.getBodyMethodName(scriptOrFn) + "_gen", type, (short)10);
    }
    else {
      cfw.startMethod(codegen.getBodyMethodName(scriptOrFn), codegen
        .getBodyMethodSignature(scriptOrFn), (short)10);
    }
    

    generatePrologue();
    Node treeTop;
    Node treeTop; if (fnCurrent != null) {
      treeTop = scriptOrFn.getLastChild();
    } else {
      treeTop = scriptOrFn;
    }
    generateStatement(treeTop);
    generateEpilogue();
    
    cfw.stopMethod((short)(localsMax + 1));
    
    if (isGenerator)
    {

      generateGenerator();
    }
    
    if (literals != null)
    {
      for (int i = 0; i < literals.size(); i++) {
        Node node = (Node)literals.get(i);
        int type = node.getType();
        switch (type) {
        case 66: 
          generateObjectLiteralFactory(node, i + 1);
          break;
        case 65: 
          generateArrayLiteralFactory(node, i + 1);
          break;
        default: 
          Kit.codeBug(Token.typeToName(type));
        }
        
      }
    }
  }
  

  private void generateGenerator()
  {
    cfw.startMethod(codegen.getBodyMethodName(scriptOrFn), codegen
      .getBodyMethodSignature(scriptOrFn), (short)10);
    

    initBodyGeneration();
    argsLocal = (firstFreeLocal++);
    localsMax = firstFreeLocal;
    

    if (fnCurrent != null)
    {

      cfw.addALoad(funObjLocal);
      cfw.addInvoke(185, "net/sourceforge/htmlunit/corejs/javascript/Scriptable", "getParentScope", "()Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      


      cfw.addAStore(variableObjectLocal);
    }
    

    cfw.addALoad(funObjLocal);
    cfw.addALoad(variableObjectLocal);
    cfw.addALoad(argsLocal);
    addScriptRuntimeInvoke("createFunctionActivation", "(Lnet/sourceforge/htmlunit/corejs/javascript/NativeFunction;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    



    cfw.addAStore(variableObjectLocal);
    

    cfw.add(187, codegen.mainClassName);
    
    cfw.add(89);
    cfw.addALoad(variableObjectLocal);
    cfw.addALoad(contextLocal);
    cfw.addPush(scriptOrFnIndex);
    cfw.addInvoke(183, codegen.mainClassName, "<init>", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)V");
    

    generateNestedFunctionInits();
    

    cfw.addALoad(variableObjectLocal);
    cfw.addALoad(thisObjLocal);
    cfw.addLoadConstant(maxLocals);
    cfw.addLoadConstant(maxStack);
    addOptRuntimeInvoke("createNativeGenerator", "(Lnet/sourceforge/htmlunit/corejs/javascript/NativeFunction;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;II)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    




    cfw.add(176);
    cfw.stopMethod((short)(localsMax + 1));
  }
  
  private void generateNestedFunctionInits() {
    int functionCount = scriptOrFn.getFunctionCount();
    for (int i = 0; i != functionCount; i++) {
      OptFunctionNode ofn = OptFunctionNode.get(scriptOrFn, i);
      
      if (fnode.getFunctionType() == 1) {
        visitFunction(ofn, 1);
      }
    }
  }
  
  private void initBodyGeneration() {
    varRegisters = null;
    if (scriptOrFn.getType() == 109) {
      fnCurrent = OptFunctionNode.get(scriptOrFn);
      hasVarsInRegs = (!fnCurrent.fnode.requiresActivation());
      if (hasVarsInRegs) {
        int n = fnCurrent.fnode.getParamAndVarCount();
        if (n != 0) {
          varRegisters = new short[n];
        }
      }
      inDirectCallFunction = fnCurrent.isTargetOfDirectCall();
      if ((inDirectCallFunction) && (!hasVarsInRegs))
        Codegen.badTree();
    } else {
      fnCurrent = null;
      hasVarsInRegs = false;
      inDirectCallFunction = false;
    }
    
    locals = new int['Ѐ'];
    
    funObjLocal = 0;
    contextLocal = 1;
    variableObjectLocal = 2;
    thisObjLocal = 3;
    localsMax = 4;
    firstFreeLocal = 4;
    
    popvLocal = -1;
    argsLocal = -1;
    itsZeroArgArray = -1;
    itsOneArgArray = -1;
    epilogueLabel = -1;
    enterAreaStartLabel = -1;
    generatorStateLocal = -1;
  }
  


  private void generatePrologue()
  {
    if (inDirectCallFunction) {
      int directParameterCount = scriptOrFn.getParamCount();
      



      if (firstFreeLocal != 4)
        Kit.codeBug();
      for (int i = 0; i != directParameterCount; i++) {
        varRegisters[i] = firstFreeLocal;
        
        firstFreeLocal = ((short)(firstFreeLocal + 3));
      }
      if (!fnCurrent.getParameterNumberContext())
      {
        itsForcedObjectParameters = true;
        for (int i = 0; i != directParameterCount; i++) {
          short reg = varRegisters[i];
          cfw.addALoad(reg);
          cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
          
          int isObjectLabel = cfw.acquireLabel();
          cfw.add(166, isObjectLabel);
          cfw.addDLoad(reg + 1);
          addDoubleWrap();
          cfw.addAStore(reg);
          cfw.markLabel(isObjectLabel);
        }
      }
    }
    
    if (fnCurrent != null)
    {
      cfw.addALoad(funObjLocal);
      cfw.addInvoke(185, "net/sourceforge/htmlunit/corejs/javascript/Scriptable", "getParentScope", "()Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      


      cfw.addAStore(variableObjectLocal);
    }
    

    argsLocal = (firstFreeLocal++);
    localsMax = firstFreeLocal;
    

    if (isGenerator)
    {

      operationLocal = (firstFreeLocal++);
      localsMax = firstFreeLocal;
      




      cfw.addALoad(thisObjLocal);
      generatorStateLocal = (firstFreeLocal++);
      localsMax = firstFreeLocal;
      cfw.add(192, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime$GeneratorState");
      cfw.add(89);
      cfw.addAStore(generatorStateLocal);
      cfw.add(180, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime$GeneratorState", "thisObj", "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      

      cfw.addAStore(thisObjLocal);
      
      if (epilogueLabel == -1) {
        epilogueLabel = cfw.acquireLabel();
      }
      

      List<Node> targets = ((FunctionNode)scriptOrFn).getResumptionPoints();
      if (targets != null)
      {
        generateGetGeneratorResumptionPoint();
        

        generatorSwitch = cfw.addTableSwitch(0, targets
          .size() + 0);
        generateCheckForThrowOrClose(-1, false, 0);
      }
    }
    


    if ((fnCurrent == null) && (scriptOrFn.getRegexpCount() != 0)) {
      cfw.addALoad(contextLocal);
      cfw.addInvoke(184, codegen.mainClassName, "_reInit", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)V");
    }
    


    if (compilerEnv.isGenerateObserverCount()) {
      saveCurrentCodeOffset();
    }
    if (hasVarsInRegs)
    {
      int parmCount = scriptOrFn.getParamCount();
      if ((parmCount > 0) && (!inDirectCallFunction))
      {

        cfw.addALoad(argsLocal);
        cfw.add(190);
        cfw.addPush(parmCount);
        int label = cfw.acquireLabel();
        cfw.add(162, label);
        cfw.addALoad(argsLocal);
        cfw.addPush(parmCount);
        addScriptRuntimeInvoke("padArguments", "([Ljava/lang/Object;I)[Ljava/lang/Object;");
        
        cfw.addAStore(argsLocal);
        cfw.markLabel(label);
      }
      
      int paramCount = fnCurrent.fnode.getParamCount();
      int varCount = fnCurrent.fnode.getParamAndVarCount();
      boolean[] constDeclarations = fnCurrent.fnode.getParamAndVarConst();
      


      short firstUndefVar = -1;
      for (int i = 0; i != varCount; i++) {
        short reg = -1;
        if (i < paramCount) {
          if (!inDirectCallFunction) {
            reg = getNewWordLocal();
            cfw.addALoad(argsLocal);
            cfw.addPush(i);
            cfw.add(50);
            cfw.addAStore(reg);
          }
        } else if (fnCurrent.isNumberVar(i)) {
          reg = getNewWordPairLocal(constDeclarations[i]);
          cfw.addPush(0.0D);
          cfw.addDStore(reg);
        } else {
          reg = getNewWordLocal(constDeclarations[i]);
          if (firstUndefVar == -1) {
            Codegen.pushUndefined(cfw);
            firstUndefVar = reg;
          } else {
            cfw.addALoad(firstUndefVar);
          }
          cfw.addAStore(reg);
        }
        if (reg >= 0) {
          if (constDeclarations[i] != 0) {
            cfw.addPush(0);
            cfw.addIStore(reg + (fnCurrent.isNumberVar(i) ? 2 : 1));
          }
          varRegisters[i] = reg;
        }
        

        if (compilerEnv.isGenerateDebugInfo()) {
          String name = fnCurrent.fnode.getParamOrVarName(i);
          String type = fnCurrent.isNumberVar(i) ? "D" : "Ljava/lang/Object;";
          
          int startPC = cfw.getCurrentCodeOffset();
          if (reg < 0) {
            reg = varRegisters[i];
          }
          cfw.addVariableDescriptor(name, type, startPC, reg);
        }
      }
      

      return;
    }
    



    if (isGenerator) {
      return;
    }
    String debugVariableName;
    if (fnCurrent != null) {
      String debugVariableName = "activation";
      cfw.addALoad(funObjLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addALoad(argsLocal);
      addScriptRuntimeInvoke("createFunctionActivation", "(Lnet/sourceforge/htmlunit/corejs/javascript/NativeFunction;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      



      cfw.addAStore(variableObjectLocal);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      addScriptRuntimeInvoke("enterActivationFunction", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)V");

    }
    else
    {
      debugVariableName = "global";
      cfw.addALoad(funObjLocal);
      cfw.addALoad(thisObjLocal);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addPush(0);
      addScriptRuntimeInvoke("initScript", "(Lnet/sourceforge/htmlunit/corejs/javascript/NativeFunction;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Z)V");
    }
    





    enterAreaStartLabel = cfw.acquireLabel();
    epilogueLabel = cfw.acquireLabel();
    cfw.markLabel(enterAreaStartLabel);
    
    generateNestedFunctionInits();
    

    if (compilerEnv.isGenerateDebugInfo()) {
      cfw.addVariableDescriptor(debugVariableName, "Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;", cfw
      
        .getCurrentCodeOffset(), variableObjectLocal);
    }
    
    if (fnCurrent == null)
    {
      popvLocal = getNewWordLocal();
      Codegen.pushUndefined(cfw);
      cfw.addAStore(popvLocal);
      
      int linenum = scriptOrFn.getEndLineno();
      if (linenum != -1) {
        cfw.addLineNumberEntry((short)linenum);
      }
    } else {
      if (fnCurrent.itsContainsCalls0) {
        itsZeroArgArray = getNewWordLocal();
        cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
        

        cfw.addAStore(itsZeroArgArray);
      }
      if (fnCurrent.itsContainsCalls1) {
        itsOneArgArray = getNewWordLocal();
        cfw.addPush(1);
        cfw.add(189, "java/lang/Object");
        cfw.addAStore(itsOneArgArray);
      }
    }
  }
  
  private void generateGetGeneratorResumptionPoint() {
    cfw.addALoad(generatorStateLocal);
    cfw.add(180, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
  }
  

  private void generateSetGeneratorResumptionPoint(int nextState)
  {
    cfw.addALoad(generatorStateLocal);
    cfw.addLoadConstant(nextState);
    cfw.add(181, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime$GeneratorState", "resumptionPoint", "I");
  }
  

  private void generateGetGeneratorStackState()
  {
    cfw.addALoad(generatorStateLocal);
    addOptRuntimeInvoke("getGeneratorStackState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
  }
  
  private void generateEpilogue()
  {
    if (compilerEnv.isGenerateObserverCount())
      addInstructionCount();
    List<Node> nodes; if (isGenerator)
    {

      Map<Node, int[]> liveLocals = ((FunctionNode)scriptOrFn).getLiveLocals();
      if (liveLocals != null)
      {
        nodes = ((FunctionNode)scriptOrFn).getResumptionPoints();
        for (int i = 0; i < nodes.size(); i++) {
          Node node = (Node)nodes.get(i);
          int[] live = (int[])liveLocals.get(node);
          if (live != null) {
            cfw.markTableSwitchCase(generatorSwitch, 
              getNextGeneratorState(node));
            generateGetGeneratorLocalsState();
            for (int j = 0; j < live.length; j++) {
              cfw.add(89);
              cfw.addLoadConstant(j);
              cfw.add(50);
              cfw.addAStore(live[j]);
            }
            cfw.add(87);
            cfw.add(167, getTargetLabel(node));
          }
        }
      }
      

      if (finallys != null) {
        for (Node n : finallys.keySet()) {
          if (n.getType() == 125) {
            FinallyReturnPoint ret = (FinallyReturnPoint)finallys.get(n);
            
            cfw.markLabel(tableLabel, (short)1);
            

            int startSwitch = cfw.addTableSwitch(0, jsrPoints
              .size() - 1);
            int c = 0;
            cfw.markTableSwitchDefault(startSwitch);
            for (int i = 0; i < jsrPoints.size(); i++)
            {
              cfw.markTableSwitchCase(startSwitch, c);
              cfw.add(167, 
                ((Integer)jsrPoints.get(i)).intValue());
              c++;
            }
          }
        }
      }
    }
    
    if (epilogueLabel != -1) {
      cfw.markLabel(epilogueLabel);
    }
    
    if (hasVarsInRegs) {
      cfw.add(176);
      return; }
    if (isGenerator) {
      if (((FunctionNode)scriptOrFn).getResumptionPoints() != null) {
        cfw.markTableSwitchDefault(generatorSwitch);
      }
      

      generateSetGeneratorResumptionPoint(-1);
      

      cfw.addALoad(variableObjectLocal);
      addOptRuntimeInvoke("throwStopIteration", "(Ljava/lang/Object;)V");
      
      Codegen.pushUndefined(cfw);
      cfw.add(176);
    }
    else if (fnCurrent == null) {
      cfw.addALoad(popvLocal);
      cfw.add(176);
    } else {
      generateActivationExit();
      cfw.add(176);
      



      int finallyHandler = cfw.acquireLabel();
      cfw.markHandler(finallyHandler);
      short exceptionObject = getNewWordLocal();
      cfw.addAStore(exceptionObject);
      


      generateActivationExit();
      
      cfw.addALoad(exceptionObject);
      releaseWordLocal(exceptionObject);
      
      cfw.add(191);
      

      cfw.addExceptionHandler(enterAreaStartLabel, epilogueLabel, finallyHandler, null);
    }
  }
  
  private void generateGetGeneratorLocalsState()
  {
    cfw.addALoad(generatorStateLocal);
    addOptRuntimeInvoke("getGeneratorLocalsState", "(Ljava/lang/Object;)[Ljava/lang/Object;");
  }
  
  private void generateActivationExit()
  {
    if ((fnCurrent == null) || (hasVarsInRegs))
      throw Kit.codeBug();
    cfw.addALoad(contextLocal);
    addScriptRuntimeInvoke("exitActivationFunction", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)V");
  }
  
  private void generateStatement(Node node)
  {
    updateLineNumber(node);
    int type = node.getType();
    Node child = node.getFirstChild();
    switch (type)
    {
    case 123: 
    case 128: 
    case 129: 
    case 130: 
    case 132: 
    case 136: 
      if (compilerEnv.isGenerateObserverCount())
      {

        addInstructionCount(1); }
      break; }
    while (child != null) {
      generateStatement(child);
      child = child.getNext(); continue;
      



      boolean prevLocal = inLocalBlock;
      inLocalBlock = true;
      int local = getNewWordLocal();
      if (isGenerator) {
        cfw.add(1);
        cfw.addAStore(local);
      }
      node.putIntProp(2, local);
      while (child != null) {
        generateStatement(child);
        child = child.getNext();
      }
      releaseWordLocal((short)local);
      node.removeProp(2);
      inLocalBlock = prevLocal;
      break;
      


      int fnIndex = node.getExistingIntProp(1);
      OptFunctionNode ofn = OptFunctionNode.get(scriptOrFn, fnIndex);
      int t = fnode.getFunctionType();
      if (t == 3) {
        visitFunction(ofn, t);
      }
      else if (t != 1) {
        throw Codegen.badTree();
        





        visitTryCatchFinally((Jump)node, child);
        break;
        


        cfw.setStackTop((short)0);
        
        int local = getLocalBlockRegister(node);
        int scopeIndex = node.getExistingIntProp(14);
        
        String name = child.getString();
        child = child.getNext();
        generateExpression(child, node);
        if (scopeIndex == 0) {
          cfw.add(1);
        }
        else {
          cfw.addALoad(local);
        }
        cfw.addPush(name);
        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        
        addScriptRuntimeInvoke("newCatchScope", "(Ljava/lang/Throwable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
        




        cfw.addAStore(local);
        
        break;
        

        generateExpression(child, node);
        if (compilerEnv.isGenerateObserverCount())
          addInstructionCount();
        generateThrowJavaScriptException();
        break;
        

        if (compilerEnv.isGenerateObserverCount())
          addInstructionCount();
        cfw.addALoad(getLocalBlockRegister(node));
        cfw.add(191);
        break;
        


        if (!isGenerator) {
          if (child != null) {
            generateExpression(child, node);
          } else if (type == 4) {
            Codegen.pushUndefined(cfw);
          } else {
            if (popvLocal < 0)
              throw Codegen.badTree();
            cfw.addALoad(popvLocal);
          }
        }
        if (compilerEnv.isGenerateObserverCount())
          addInstructionCount();
        if (epilogueLabel == -1) {
          if (!hasVarsInRegs)
            throw Codegen.badTree();
          epilogueLabel = cfw.acquireLabel();
        }
        cfw.add(167, epilogueLabel);
        break;
        

        if (compilerEnv.isGenerateObserverCount())
          addInstructionCount();
        visitSwitch((Jump)node, child);
        break;
        

        generateExpression(child, node);
        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("enterWith", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
        


        cfw.addAStore(variableObjectLocal);
        incReferenceWordLocal(variableObjectLocal);
        break;
        

        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("leaveWith", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
        

        cfw.addAStore(variableObjectLocal);
        decReferenceWordLocal(variableObjectLocal);
        break;
        



        generateExpression(child, node);
        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        int enumType = type == 59 ? 1 : type == 58 ? 0 : 2;
        



        cfw.addPush(enumType);
        addScriptRuntimeInvoke("enumInit", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Ljava/lang/Object;");
        



        cfw.addAStore(getLocalBlockRegister(node));
        break;
        

        if (child.getType() == 56)
        {


          visitSetVar(child, child.getFirstChild(), false);
        } else if (child.getType() == 156)
        {


          visitSetConstVar(child, child.getFirstChild(), false);
        } else if (child.getType() == 72) {
          generateYieldPoint(child, false);
        } else {
          generateExpression(child, node);
          if (node.getIntProp(8, -1) != -1) {
            cfw.add(88);
          } else {
            cfw.add(87);
            
            break;
            

            generateExpression(child, node);
            if (popvLocal < 0) {
              popvLocal = getNewWordLocal();
            }
            cfw.addAStore(popvLocal);
            break;
            

            if (compilerEnv.isGenerateObserverCount())
              addInstructionCount();
            int label = getTargetLabel(node);
            cfw.markLabel(label);
            if (compilerEnv.isGenerateObserverCount()) {
              saveCurrentCodeOffset();
            }
            break;
            




            if (compilerEnv.isGenerateObserverCount())
              addInstructionCount();
            visitGoto((Jump)node, type, child);
            break;
            






            if (isGenerator)
            {


              if (compilerEnv.isGenerateObserverCount()) {
                saveCurrentCodeOffset();
              }
              
              cfw.setStackTop((short)1);
              

              int finallyRegister = getNewWordLocal();
              
              int finallyStart = cfw.acquireLabel();
              int finallyEnd = cfw.acquireLabel();
              cfw.markLabel(finallyStart);
              
              generateIntegerWrap();
              cfw.addAStore(finallyRegister);
              
              while (child != null) {
                generateStatement(child);
                child = child.getNext();
              }
              
              cfw.addALoad(finallyRegister);
              cfw.add(192, "java/lang/Integer");
              generateIntegerUnwrap();
              FinallyReturnPoint ret = (FinallyReturnPoint)finallys.get(node);
              tableLabel = cfw.acquireLabel();
              cfw.add(167, tableLabel);
              
              releaseWordLocal((short)finallyRegister);
              cfw.markLabel(finallyEnd);
              
              break;
              

              break;
              

              throw Codegen.badTree();
            }
          }
        }
      } } }
  
  private void generateIntegerWrap() { cfw.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;"); }
  

  private void generateIntegerUnwrap()
  {
    cfw.addInvoke(182, "java/lang/Integer", "intValue", "()I");
  }
  
  private void generateThrowJavaScriptException()
  {
    cfw.add(187, "net/sourceforge/htmlunit/corejs/javascript/JavaScriptException");
    
    cfw.add(90);
    cfw.add(95);
    cfw.addPush(scriptOrFn.getSourceName());
    cfw.addPush(itsLineNumber);
    cfw.addInvoke(183, "net/sourceforge/htmlunit/corejs/javascript/JavaScriptException", "<init>", "(Ljava/lang/Object;Ljava/lang/String;I)V");
    

    cfw.add(191);
  }
  
  private int getNextGeneratorState(Node node)
  {
    int nodeIndex = ((FunctionNode)scriptOrFn).getResumptionPoints().indexOf(node);
    return nodeIndex + 1;
  }
  
  private void generateExpression(Node node, Node parent) {
    int type = node.getType();
    Node child = node.getFirstChild();
    switch (type)
    {
    case 138: 
      break;
    case 109: 
      if ((fnCurrent != null) || (parent.getType() != 136)) {
        int fnIndex = node.getExistingIntProp(1);
        OptFunctionNode ofn = OptFunctionNode.get(scriptOrFn, fnIndex);
        int t = fnode.getFunctionType();
        if (t != 2) {
          throw Codegen.badTree();
        }
        visitFunction(ofn, t); }
      break;
    

    case 39: 
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addPush(node.getString());
      addScriptRuntimeInvoke("name", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
      



      break;
    
    case 30: 
    case 38: 
      int specialType = node.getIntProp(10, 0);
      
      if (specialType == 0)
      {
        OptFunctionNode target = (OptFunctionNode)node.getProp(9);
        
        if (target != null) {
          visitOptimizedCall(node, target, type, child);
        } else if (type == 38) {
          visitStandardCall(node, child);
        } else {
          visitStandardNew(node, child);
        }
      } else {
        visitSpecialCall(node, type, specialType, child);
      }
      
      break;
    
    case 70: 
      generateFunctionAndThisObj(child, node);
      
      child = child.getNext();
      generateCallArgArray(node, child, false);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("callRef", "(Lnet/sourceforge/htmlunit/corejs/javascript/Callable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Lnet/sourceforge/htmlunit/corejs/javascript/Ref;");
      




      break;
    
    case 40: 
      double num = node.getDouble();
      if (node.getIntProp(8, -1) != -1) {
        cfw.addPush(num);
      } else {
        codegen.pushNumberAsObject(cfw, num);
      }
      
      break;
    
    case 41: 
      cfw.addPush(node.getString());
      break;
    
    case 43: 
      cfw.addALoad(thisObjLocal);
      break;
    
    case 63: 
      cfw.add(42);
      break;
    
    case 42: 
      cfw.add(1);
      break;
    
    case 45: 
      cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
      
      break;
    
    case 44: 
      cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
      
      break;
    

    case 48: 
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      int i = node.getExistingIntProp(4);
      cfw.add(178, codegen.mainClassName, codegen
        .getCompiledRegexpName(scriptOrFn, i), "Ljava/lang/Object;");
      
      cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "wrapRegExp", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      






      break;
    
    case 89: 
      Node next = child.getNext();
      while (next != null) {
        generateExpression(child, node);
        cfw.add(87);
        child = next;
        next = next.getNext();
      }
      generateExpression(child, node);
      break;
    

    case 61: 
    case 62: 
      int local = getLocalBlockRegister(node);
      cfw.addALoad(local);
      if (type == 61) {
        addScriptRuntimeInvoke("enumNext", "(Ljava/lang/Object;)Ljava/lang/Boolean;");
      }
      else {
        cfw.addALoad(contextLocal);
        addScriptRuntimeInvoke("enumId", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");
      }
      


      break;
    

    case 65: 
      visitArrayLiteral(node, child, false);
      break;
    
    case 66: 
      visitObjectLiteral(node, child, false);
      break;
    
    case 26: 
      int trueTarget = cfw.acquireLabel();
      int falseTarget = cfw.acquireLabel();
      int beyond = cfw.acquireLabel();
      generateIfJump(child, node, trueTarget, falseTarget);
      
      cfw.markLabel(trueTarget);
      cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
      
      cfw.add(167, beyond);
      cfw.markLabel(falseTarget);
      cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
      
      cfw.markLabel(beyond);
      cfw.adjustStackTop(-1);
      break;
    

    case 27: 
      generateExpression(child, node);
      addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
      cfw.addPush(-1);
      cfw.add(130);
      cfw.add(135);
      addDoubleWrap();
      break;
    
    case 126: 
      generateExpression(child, node);
      cfw.add(87);
      Codegen.pushUndefined(cfw);
      break;
    
    case 32: 
      generateExpression(child, node);
      addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
      
      break;
    
    case 137: 
      visitTypeofname(node);
      break;
    
    case 106: 
    case 107: 
      visitIncDec(node);
      break;
    
    case 104: 
    case 105: 
      generateExpression(child, node);
      cfw.add(89);
      addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
      int falseTarget = cfw.acquireLabel();
      if (type == 105) {
        cfw.add(153, falseTarget);
      } else
        cfw.add(154, falseTarget);
      cfw.add(87);
      generateExpression(child.getNext(), node);
      cfw.markLabel(falseTarget);
      
      break;
    
    case 102: 
      Node ifThen = child.getNext();
      Node ifElse = ifThen.getNext();
      generateExpression(child, node);
      addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
      int elseTarget = cfw.acquireLabel();
      cfw.add(153, elseTarget);
      short stack = cfw.getStackTop();
      generateExpression(ifThen, node);
      int afterHook = cfw.acquireLabel();
      cfw.add(167, afterHook);
      cfw.markLabel(elseTarget, stack);
      generateExpression(ifElse, node);
      cfw.markLabel(afterHook);
      
      break;
    
    case 21: 
      generateExpression(child, node);
      generateExpression(child.getNext(), node);
      switch (node.getIntProp(8, -1)) {
      case 0: 
        cfw.add(99);
        break;
      case 1: 
        addOptRuntimeInvoke("add", "(DLjava/lang/Object;)Ljava/lang/Object;");
        
        break;
      case 2: 
        addOptRuntimeInvoke("add", "(Ljava/lang/Object;D)Ljava/lang/Object;");
        
        break;
      default: 
        if (child.getType() == 41) {
          addScriptRuntimeInvoke("add", "(Ljava/lang/CharSequence;Ljava/lang/Object;)Ljava/lang/CharSequence;");

        }
        else if (child.getNext().getType() == 41) {
          addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/CharSequence;)Ljava/lang/CharSequence;");
        }
        else
        {
          cfw.addALoad(contextLocal);
          addScriptRuntimeInvoke("add", "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");
        }
        
        break;
      }
      
      
      break;
    
    case 23: 
      visitArithmetic(node, 107, child, parent);
      break;
    
    case 22: 
      visitArithmetic(node, 103, child, parent);
      break;
    
    case 24: 
    case 25: 
      visitArithmetic(node, type == 24 ? 111 : 115, child, parent);
      

      break;
    
    case 9: 
    case 10: 
    case 11: 
    case 18: 
    case 19: 
    case 20: 
      visitBitOp(node, type, child);
      break;
    
    case 28: 
    case 29: 
      generateExpression(child, node);
      addObjectToDouble();
      if (type == 29) {
        cfw.add(119);
      }
      addDoubleWrap();
      break;
    

    case 150: 
      generateExpression(child, node);
      addObjectToDouble();
      break;
    

    case 149: 
      int prop = -1;
      if (child.getType() == 40) {
        prop = child.getIntProp(8, -1);
      }
      if (prop != -1) {
        child.removeProp(8);
        generateExpression(child, node);
        child.putIntProp(8, prop);
      } else {
        generateExpression(child, node);
        addDoubleWrap();
      }
      break;
    

    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 52: 
    case 53: 
      int trueGOTO = cfw.acquireLabel();
      int falseGOTO = cfw.acquireLabel();
      visitIfJumpRelOp(node, child, trueGOTO, falseGOTO);
      addJumpedBooleanWrap(trueGOTO, falseGOTO);
      break;
    

    case 12: 
    case 13: 
    case 46: 
    case 47: 
      int trueGOTO = cfw.acquireLabel();
      int falseGOTO = cfw.acquireLabel();
      visitIfJumpEqOp(node, child, trueGOTO, falseGOTO);
      addJumpedBooleanWrap(trueGOTO, falseGOTO);
      break;
    

    case 33: 
    case 34: 
      visitGetProp(node, child);
      break;
    
    case 36: 
      generateExpression(child, node);
      generateExpression(child.getNext(), node);
      cfw.addALoad(contextLocal);
      if (node.getIntProp(8, -1) != -1) {
        addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");

      }
      else
      {
        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
      }
      



      break;
    
    case 67: 
      generateExpression(child, node);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("refGet", "(Lnet/sourceforge/htmlunit/corejs/javascript/Ref;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");
      


      break;
    
    case 55: 
      visitGetVar(node);
      break;
    
    case 56: 
      visitSetVar(node, child, true);
      break;
    
    case 8: 
      visitSetName(node, child);
      break;
    
    case 73: 
      visitStrictSetName(node, child);
      break;
    
    case 155: 
      visitSetConst(node, child);
      break;
    
    case 156: 
      visitSetConstVar(node, child, true);
      break;
    
    case 35: 
    case 139: 
      visitSetProp(type, node, child);
      break;
    
    case 37: 
    case 140: 
      visitSetElem(type, node, child);
      break;
    
    case 68: 
    case 142: 
      generateExpression(child, node);
      child = child.getNext();
      if (type == 142) {
        cfw.add(89);
        cfw.addALoad(contextLocal);
        addScriptRuntimeInvoke("refGet", "(Lnet/sourceforge/htmlunit/corejs/javascript/Ref;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");
      }
      


      generateExpression(child, node);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      addScriptRuntimeInvoke("refSet", "(Lnet/sourceforge/htmlunit/corejs/javascript/Ref;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
      





      break;
    
    case 69: 
      generateExpression(child, node);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("refDel", "(Lnet/sourceforge/htmlunit/corejs/javascript/Ref;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");
      


      break;
    
    case 31: 
      boolean isName = child.getType() == 49;
      generateExpression(child, node);
      child = child.getNext();
      generateExpression(child, node);
      cfw.addALoad(contextLocal);
      cfw.addPush(isName);
      addScriptRuntimeInvoke("delete", "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Z)Ljava/lang/Object;");
      


      break;
    
    case 49: 
      while (child != null) {
        generateExpression(child, node);
        child = child.getNext();
      }
      
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addPush(node.getString());
      addScriptRuntimeInvoke("bind", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      




      break;
    
    case 54: 
      cfw.addALoad(getLocalBlockRegister(node));
      break;
    
    case 71: 
      String special = (String)node.getProp(17);
      generateExpression(child, node);
      cfw.addPush(special);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      addScriptRuntimeInvoke("specialRef", "(Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Ref;");
      




      break;
    
    case 77: 
    case 78: 
    case 79: 
    case 80: 
      int memberTypeFlags = node.getIntProp(16, 0);
      do
      {
        generateExpression(child, node);
        child = child.getNext();
      } while (child != null);
      cfw.addALoad(contextLocal);
      String signature;
      String signature; switch (type) {
      case 77: 
        String methodName = "memberRef";
        signature = "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)Lnet/sourceforge/htmlunit/corejs/javascript/Ref;";
        


        break;
      case 78: 
        String methodName = "memberRef";
        signature = "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)Lnet/sourceforge/htmlunit/corejs/javascript/Ref;";
        



        break;
      case 79: 
        String methodName = "nameRef";
        String signature = "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Lnet/sourceforge/htmlunit/corejs/javascript/Ref;";
        



        cfw.addALoad(variableObjectLocal);
        break;
      case 80: 
        String methodName = "nameRef";
        String signature = "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Lnet/sourceforge/htmlunit/corejs/javascript/Ref;";
        



        cfw.addALoad(variableObjectLocal);
        break;
      default: 
        throw Kit.codeBug(); }
      String signature;
      String methodName; cfw.addPush(memberTypeFlags);
      addScriptRuntimeInvoke(methodName, signature);
      
      break;
    
    case 146: 
      visitDotQuery(node, child);
      break;
    
    case 75: 
      generateExpression(child, node);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("escapeAttributeValue", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/String;");
      


      break;
    
    case 76: 
      generateExpression(child, node);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("escapeTextValue", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/String;");
      


      break;
    
    case 74: 
      generateExpression(child, node);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("setDefaultNamespace", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");
      


      break;
    
    case 72: 
      generateYieldPoint(node, true);
      break;
    
    case 159: 
      Node enterWith = child;
      Node with = enterWith.getNext();
      Node leaveWith = with.getNext();
      generateStatement(enterWith);
      generateExpression(with.getFirstChild(), with);
      generateStatement(leaveWith);
      break;
    

    case 157: 
      Node initStmt = child;
      Node expr = child.getNext();
      generateStatement(initStmt);
      generateExpression(expr, node);
      break;
    case 50: case 51: case 57: case 58: case 59: case 60: case 64: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 90: case 91: case 92: case 93: case 94: case 95: case 96: case 97: 
    case 98: case 99: case 100: case 101: case 103: case 108: case 110: case 111: case 112: case 113: case 114: case 115: case 116: case 117: case 118: case 119: case 120: case 121: case 122: case 123: case 124: case 125: 
    case 127: case 128: case 129: case 130: case 131: case 132: case 133: case 134: case 135: case 136: case 141: case 143: case 144: case 145: case 147: case 148: case 151: case 152: case 153: case 154: case 158: default: 
      throw new RuntimeException("Unexpected node type " + type);
    }
    
  }
  
  private void generateYieldPoint(Node node, boolean exprContext)
  {
    int top = cfw.getStackTop();
    maxStack = (maxStack > top ? maxStack : top);
    if (cfw.getStackTop() != 0) {
      generateGetGeneratorStackState();
      for (int i = 0; i < top; i++) {
        cfw.add(90);
        cfw.add(95);
        cfw.addLoadConstant(i);
        cfw.add(95);
        cfw.add(83);
      }
      
      cfw.add(87);
    }
    

    Node child = node.getFirstChild();
    if (child != null) {
      generateExpression(child, node);
    } else {
      Codegen.pushUndefined(cfw);
    }
    
    int nextState = getNextGeneratorState(node);
    generateSetGeneratorResumptionPoint(nextState);
    
    boolean hasLocals = generateSaveLocals(node);
    
    cfw.add(176);
    
    generateCheckForThrowOrClose(getTargetLabel(node), hasLocals, nextState);
    


    if (top != 0) {
      generateGetGeneratorStackState();
      for (int i = 0; i < top; i++) {
        cfw.add(89);
        cfw.addLoadConstant(top - i - 1);
        cfw.add(50);
        cfw.add(95);
      }
      cfw.add(87);
    }
    

    if (exprContext) {
      cfw.addALoad(argsLocal);
    }
  }
  
  private void generateCheckForThrowOrClose(int label, boolean hasLocals, int nextState)
  {
    int throwLabel = cfw.acquireLabel();
    int closeLabel = cfw.acquireLabel();
    

    cfw.markLabel(throwLabel);
    cfw.addALoad(argsLocal);
    generateThrowJavaScriptException();
    

    cfw.markLabel(closeLabel);
    cfw.addALoad(argsLocal);
    cfw.add(192, "java/lang/Throwable");
    cfw.add(191);
    


    if (label != -1)
      cfw.markLabel(label);
    if (!hasLocals)
    {
      cfw.markTableSwitchCase(generatorSwitch, nextState);
    }
    

    cfw.addILoad(operationLocal);
    cfw.addLoadConstant(2);
    cfw.add(159, closeLabel);
    cfw.addILoad(operationLocal);
    cfw.addLoadConstant(1);
    cfw.add(159, throwLabel);
  }
  


  private void generateIfJump(Node node, Node parent, int trueLabel, int falseLabel)
  {
    int type = node.getType();
    Node child = node.getFirstChild();
    
    switch (type) {
    case 26: 
      generateIfJump(child, node, falseLabel, trueLabel);
      break;
    
    case 104: 
    case 105: 
      int interLabel = cfw.acquireLabel();
      if (type == 105) {
        generateIfJump(child, node, interLabel, falseLabel);
      } else {
        generateIfJump(child, node, trueLabel, interLabel);
      }
      cfw.markLabel(interLabel);
      child = child.getNext();
      generateIfJump(child, node, trueLabel, falseLabel);
      break;
    

    case 14: 
    case 15: 
    case 16: 
    case 17: 
    case 52: 
    case 53: 
      visitIfJumpRelOp(node, child, trueLabel, falseLabel);
      break;
    
    case 12: 
    case 13: 
    case 46: 
    case 47: 
      visitIfJumpEqOp(node, child, trueLabel, falseLabel);
      break;
    

    default: 
      generateExpression(node, parent);
      addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
      cfw.add(154, trueLabel);
      cfw.add(167, falseLabel);
    }
  }
  
  private void visitFunction(OptFunctionNode ofn, int functionType) {
    int fnIndex = codegen.getIndex(fnode);
    cfw.add(187, codegen.mainClassName);
    
    cfw.add(89);
    cfw.addALoad(variableObjectLocal);
    cfw.addALoad(contextLocal);
    cfw.addPush(fnIndex);
    cfw.addInvoke(183, codegen.mainClassName, "<init>", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)V");
    

    if (functionType == 2)
    {

      return;
    }
    cfw.addPush(functionType);
    cfw.addALoad(variableObjectLocal);
    cfw.addALoad(contextLocal);
    addOptRuntimeInvoke("initFunction", "(Lnet/sourceforge/htmlunit/corejs/javascript/NativeFunction;ILnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)V");
  }
  




  private int getTargetLabel(Node target)
  {
    int labelId = target.labelId();
    if (labelId == -1) {
      labelId = cfw.acquireLabel();
      target.labelId(labelId);
    }
    return labelId;
  }
  
  private void visitGoto(Jump node, int type, Node child) {
    Node target = target;
    if ((type == 6) || (type == 7)) {
      if (child == null)
        throw Codegen.badTree();
      int targetLabel = getTargetLabel(target);
      int fallThruLabel = cfw.acquireLabel();
      if (type == 6) {
        generateIfJump(child, node, targetLabel, fallThruLabel);
      } else
        generateIfJump(child, node, fallThruLabel, targetLabel);
      cfw.markLabel(fallThruLabel);
    }
    else if (type == 135) {
      if (isGenerator) {
        addGotoWithReturn(target);
      }
      else {
        inlineFinally(target);
      }
    } else {
      addGoto(target, 167);
    }
  }
  
  private void addGotoWithReturn(Node target)
  {
    FinallyReturnPoint ret = (FinallyReturnPoint)finallys.get(target);
    cfw.addLoadConstant(jsrPoints.size());
    addGoto(target, 167);
    int retLabel = cfw.acquireLabel();
    cfw.markLabel(retLabel);
    jsrPoints.add(Integer.valueOf(retLabel));
  }
  
  private void generateArrayLiteralFactory(Node node, int count) {
    String methodName = codegen.getBodyMethodName(scriptOrFn) + "_literal" + count;
    
    initBodyGeneration();
    argsLocal = (firstFreeLocal++);
    localsMax = firstFreeLocal;
    cfw.startMethod(methodName, "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;", (short)2);
    





    visitArrayLiteral(node, node.getFirstChild(), true);
    cfw.add(176);
    cfw.stopMethod((short)(localsMax + 1));
  }
  
  private void generateObjectLiteralFactory(Node node, int count) {
    String methodName = codegen.getBodyMethodName(scriptOrFn) + "_literal" + count;
    
    initBodyGeneration();
    argsLocal = (firstFreeLocal++);
    localsMax = firstFreeLocal;
    cfw.startMethod(methodName, "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;", (short)2);
    





    visitObjectLiteral(node, node.getFirstChild(), true);
    cfw.add(176);
    cfw.stopMethod((short)(localsMax + 1));
  }
  
  private void visitArrayLiteral(Node node, Node child, boolean topLevel) {
    int count = 0;
    for (Node cursor = child; cursor != null; cursor = cursor.getNext()) {
      count++;
    }
    

    if ((!topLevel) && ((count > 10) || (cfw.getCurrentCodeOffset() > 30000)) && (!hasVarsInRegs) && (!isGenerator) && (!inLocalBlock))
    {
      if (literals == null) {
        literals = new LinkedList();
      }
      literals.add(node);
      
      String methodName = codegen.getBodyMethodName(scriptOrFn) + "_literal" + literals.size();
      cfw.addALoad(funObjLocal);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addALoad(thisObjLocal);
      cfw.addALoad(argsLocal);
      cfw.addInvoke(182, codegen.mainClassName, methodName, "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      





      return;
    }
    

    if (isGenerator)
    {

      for (int i = 0; i != count; i++) {
        generateExpression(child, node);
        child = child.getNext();
      }
      addNewObjectArray(count);
      for (int i = 0; i != count; i++) {
        cfw.add(90);
        cfw.add(95);
        cfw.addPush(count - i - 1);
        cfw.add(95);
        cfw.add(83);
      }
    } else {
      addNewObjectArray(count);
      for (int i = 0; i != count; i++) {
        cfw.add(89);
        cfw.addPush(i);
        generateExpression(child, node);
        cfw.add(83);
        child = child.getNext();
      }
    }
    int[] skipIndexes = (int[])node.getProp(11);
    if (skipIndexes == null) {
      cfw.add(1);
      cfw.add(3);
    } else {
      cfw.addPush(OptRuntime.encodeIntArray(skipIndexes));
      cfw.addPush(skipIndexes.length);
    }
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    addOptRuntimeInvoke("newArrayLiteral", "([Ljava/lang/Object;Ljava/lang/String;ILnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
  }
  




  private void addLoadPropertyIds(Object[] properties, int count)
  {
    addNewObjectArray(count);
    for (int i = 0; i != count; i++) {
      cfw.add(89);
      cfw.addPush(i);
      Object id = properties[i];
      if ((id instanceof String)) {
        cfw.addPush((String)id);
      } else {
        cfw.addPush(((Integer)id).intValue());
        addScriptRuntimeInvoke("wrapInt", "(I)Ljava/lang/Integer;");
      }
      cfw.add(83);
    }
  }
  
  private void addLoadPropertyValues(Node node, Node child, int count)
  {
    if (isGenerator)
    {
      for (int i = 0; i != count; i++) {
        int childType = child.getType();
        if ((childType == 151) || (childType == 152)) {
          generateExpression(child.getFirstChild(), node);
        } else {
          generateExpression(child, node);
        }
        child = child.getNext();
      }
      addNewObjectArray(count);
      for (int i = 0; i != count; i++) {
        cfw.add(90);
        cfw.add(95);
        cfw.addPush(count - i - 1);
        cfw.add(95);
        cfw.add(83);
      }
    } else {
      addNewObjectArray(count);
      Node child2 = child;
      for (int i = 0; i != count; i++) {
        cfw.add(89);
        cfw.addPush(i);
        int childType = child2.getType();
        if ((childType == 151) || (childType == 152)) {
          generateExpression(child2.getFirstChild(), node);
        } else {
          generateExpression(child2, node);
        }
        cfw.add(83);
        child2 = child2.getNext();
      }
    }
  }
  
  private void visitObjectLiteral(Node node, Node child, boolean topLevel) {
    Object[] properties = (Object[])node.getProp(12);
    int count = properties.length;
    

    if ((!topLevel) && ((count > 10) || (cfw.getCurrentCodeOffset() > 30000)) && (!hasVarsInRegs) && (!isGenerator) && (!inLocalBlock))
    {
      if (literals == null) {
        literals = new LinkedList();
      }
      literals.add(node);
      
      String methodName = codegen.getBodyMethodName(scriptOrFn) + "_literal" + literals.size();
      cfw.addALoad(funObjLocal);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addALoad(thisObjLocal);
      cfw.addALoad(argsLocal);
      cfw.addInvoke(182, codegen.mainClassName, methodName, "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
      





      return;
    }
    
    if (isGenerator)
    {

      addLoadPropertyValues(node, child, count);
      addLoadPropertyIds(properties, count);
      
      cfw.add(95);
    } else {
      addLoadPropertyIds(properties, count);
      addLoadPropertyValues(node, child, count);
    }
    

    boolean hasGetterSetters = false;
    Node child2 = child;
    for (int i = 0; i != count; i++) {
      int childType = child2.getType();
      if ((childType == 151) || (childType == 152)) {
        hasGetterSetters = true;
        break;
      }
      child2 = child2.getNext();
    }
    
    if (hasGetterSetters) {
      cfw.addPush(count);
      cfw.add(188, 10);
      child2 = child;
      for (int i = 0; i != count; i++) {
        cfw.add(89);
        cfw.addPush(i);
        int childType = child2.getType();
        if (childType == 151) {
          cfw.add(2);
        } else if (childType == 152) {
          cfw.add(4);
        } else {
          cfw.add(3);
        }
        cfw.add(79);
        child2 = child2.getNext();
      }
    } else {
      cfw.add(1);
    }
    
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    addScriptRuntimeInvoke("newObjectLiteral", "([Ljava/lang/Object;[Ljava/lang/Object;[ILnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
  }
  




  private void visitSpecialCall(Node node, int type, int specialType, Node child)
  {
    cfw.addALoad(contextLocal);
    
    if (type == 30) {
      generateExpression(child, node);
    }
    else {
      generateFunctionAndThisObj(child, node);
    }
    
    child = child.getNext();
    
    generateCallArgArray(node, child, false);
    
    String methodName;
    
    String callSignature;
    if (type == 30) {
      String methodName = "newObjectSpecial";
      String callSignature = "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Ljava/lang/Object;[Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Ljava/lang/Object;";
      




      cfw.addALoad(variableObjectLocal);
      cfw.addALoad(thisObjLocal);
      cfw.addPush(specialType);
    } else {
      methodName = "callSpecial";
      callSignature = "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Callable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;ILjava/lang/String;I)Ljava/lang/Object;";
      







      cfw.addALoad(variableObjectLocal);
      cfw.addALoad(thisObjLocal);
      cfw.addPush(specialType);
      String sourceName = scriptOrFn.getSourceName();
      cfw.addPush(sourceName == null ? "" : sourceName);
      cfw.addPush(itsLineNumber);
    }
    
    addOptRuntimeInvoke(methodName, callSignature);
  }
  
  private void visitStandardCall(Node node, Node child) {
    if (node.getType() != 38) {
      throw Codegen.badTree();
    }
    Node firstArgChild = child.getNext();
    int childType = child.getType();
    
    String signature;
    String methodName;
    String signature;
    if (firstArgChild == null) { String signature;
      if (childType == 39)
      {
        String name = child.getString();
        cfw.addPush(name);
        String methodName = "callName0";
        signature = "(Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;";
      }
      else {
        String signature;
        if (childType == 33)
        {
          Node propTarget = child.getFirstChild();
          generateExpression(propTarget, node);
          Node id = propTarget.getNext();
          String property = id.getString();
          cfw.addPush(property);
          String methodName = "callProp0";
          signature = "(Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;";
        }
        else
        {
          if (childType == 34) {
            throw Kit.codeBug();
          }
          generateFunctionAndThisObj(child, node);
          String methodName = "call0";
          signature = "(Lnet/sourceforge/htmlunit/corejs/javascript/Callable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;";
        }
      }
    }
    else
    {
      String signature;
      if (childType == 39)
      {



        String name = child.getString();
        generateCallArgArray(node, firstArgChild, false);
        cfw.addPush(name);
        String methodName = "callName";
        signature = "([Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;";

      }
      else
      {
        int argCount = 0;
        for (Node arg = firstArgChild; arg != null; arg = arg.getNext()) {
          argCount++;
        }
        generateFunctionAndThisObj(child, node);
        String signature;
        if (argCount == 1) {
          generateExpression(firstArgChild, node);
          String methodName = "call1";
          signature = "(Lnet/sourceforge/htmlunit/corejs/javascript/Callable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;";
        }
        else
        {
          String signature;
          
          if (argCount == 2) {
            generateExpression(firstArgChild, node);
            generateExpression(firstArgChild.getNext(), node);
            String methodName = "call2";
            signature = "(Lnet/sourceforge/htmlunit/corejs/javascript/Callable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;";


          }
          else
          {

            generateCallArgArray(node, firstArgChild, false);
            methodName = "callN";
            signature = "(Lnet/sourceforge/htmlunit/corejs/javascript/Callable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;";
          }
        }
      }
    }
    



    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    addOptRuntimeInvoke(methodName, signature);
  }
  
  private void visitStandardNew(Node node, Node child) {
    if (node.getType() != 30) {
      throw Codegen.badTree();
    }
    Node firstArgChild = child.getNext();
    
    generateExpression(child, node);
    
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    
    generateCallArgArray(node, firstArgChild, false);
    addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
  }
  




  private void visitOptimizedCall(Node node, OptFunctionNode target, int type, Node child)
  {
    Node firstArgChild = child.getNext();
    String className = codegen.mainClassName;
    
    short thisObjLocal = 0;
    if (type == 30) {
      generateExpression(child, node);
    } else {
      generateFunctionAndThisObj(child, node);
      thisObjLocal = getNewWordLocal();
      cfw.addAStore(thisObjLocal);
    }
    

    int beyond = cfw.acquireLabel();
    int regularCall = cfw.acquireLabel();
    
    cfw.add(89);
    cfw.add(193, className);
    cfw.add(153, regularCall);
    cfw.add(192, className);
    cfw.add(89);
    cfw.add(180, className, "_id", "I");
    cfw.addPush(codegen.getIndex(fnode));
    cfw.add(160, regularCall);
    

    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    

    if (type == 30) {
      cfw.add(1);
    } else {
      cfw.addALoad(thisObjLocal);
    }
    







    Node argChild = firstArgChild;
    while (argChild != null) {
      int dcp_register = nodeIsDirectCallParameter(argChild);
      if (dcp_register >= 0) {
        cfw.addALoad(dcp_register);
        cfw.addDLoad(dcp_register + 1);
      } else if (argChild.getIntProp(8, -1) == 0)
      {
        cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        
        generateExpression(argChild, node);
      } else {
        generateExpression(argChild, node);
        cfw.addPush(0.0D);
      }
      argChild = argChild.getNext();
    }
    
    cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
    

    cfw.addInvoke(184, codegen.mainClassName, type == 30 ? codegen
      .getDirectCtorName(fnode) : codegen
      .getBodyMethodName(fnode), codegen
      .getBodyMethodSignature(fnode));
    
    cfw.add(167, beyond);
    
    cfw.markLabel(regularCall);
    
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    
    if (type != 30) {
      cfw.addALoad(thisObjLocal);
      releaseWordLocal(thisObjLocal);
    }
    


    generateCallArgArray(node, firstArgChild, true);
    
    if (type == 30) {
      addScriptRuntimeInvoke("newObject", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");

    }
    else
    {

      cfw.addInvoke(185, "net/sourceforge/htmlunit/corejs/javascript/Callable", "call", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
    }
    






    cfw.markLabel(beyond);
  }
  
  private void generateCallArgArray(Node node, Node argChild, boolean directCall)
  {
    int argCount = 0;
    for (Node child = argChild; child != null; child = child.getNext()) {
      argCount++;
    }
    
    if ((argCount == 1) && (itsOneArgArray >= 0)) {
      cfw.addALoad(itsOneArgArray);
    } else {
      addNewObjectArray(argCount);
    }
    
    for (int i = 0; i != argCount; i++)
    {


      if (!isGenerator) {
        cfw.add(89);
        cfw.addPush(i);
      }
      
      if (!directCall) {
        generateExpression(argChild, node);


      }
      else
      {

        int dcp_register = nodeIsDirectCallParameter(argChild);
        if (dcp_register >= 0) {
          dcpLoadAsObject(dcp_register);
        } else {
          generateExpression(argChild, node);
          
          int childNumberFlag = argChild.getIntProp(8, -1);
          if (childNumberFlag == 0) {
            addDoubleWrap();
          }
        }
      }
      



      if (isGenerator) {
        short tempLocal = getNewWordLocal();
        cfw.addAStore(tempLocal);
        cfw.add(192, "[Ljava/lang/Object;");
        cfw.add(89);
        cfw.addPush(i);
        cfw.addALoad(tempLocal);
        releaseWordLocal(tempLocal);
      }
      
      cfw.add(83);
      
      argChild = argChild.getNext();
    }
  }
  
  private void generateFunctionAndThisObj(Node node, Node parent)
  {
    int type = node.getType();
    switch (node.getType()) {
    case 34: 
      throw Kit.codeBug();
    
    case 33: 
    case 36: 
      Node target = node.getFirstChild();
      generateExpression(target, node);
      Node id = target.getNext();
      if (type == 33) {
        String property = id.getString();
        cfw.addPush(property);
        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("getPropFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Callable;");

      }
      else
      {

        generateExpression(id, node);
        if (node.getIntProp(8, -1) != -1)
          addDoubleWrap();
        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("getElemFunctionAndThis", "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Callable;");
      }
      



      break;
    

    case 39: 
      String name = node.getString();
      cfw.addPush(name);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      addScriptRuntimeInvoke("getNameFunctionAndThis", "(Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Callable;");
      



      break;
    case 35: case 37: 
    case 38: 
    default: 
      generateExpression(node, parent);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("getValueFunctionAndThis", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Lnet/sourceforge/htmlunit/corejs/javascript/Callable;");
    }
    
    



    cfw.addALoad(contextLocal);
    addScriptRuntimeInvoke("lastStoredScriptable", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
  }
  

  private void updateLineNumber(Node node)
  {
    itsLineNumber = node.getLineno();
    if (itsLineNumber == -1)
      return;
    cfw.addLineNumberEntry((short)itsLineNumber);
  }
  











  private void visitTryCatchFinally(Jump node, Node child)
  {
    short savedVariableObject = getNewWordLocal();
    cfw.addALoad(variableObjectLocal);
    cfw.addAStore(savedVariableObject);
    






    int startLabel = cfw.acquireLabel();
    cfw.markLabel(startLabel, (short)0);
    
    Node catchTarget = target;
    Node finallyTarget = node.getFinally();
    int[] handlerLabels = new int[5];
    
    exceptionManager.pushExceptionInfo(node);
    if (catchTarget != null) {
      handlerLabels[0] = cfw.acquireLabel();
      handlerLabels[1] = cfw.acquireLabel();
      handlerLabels[2] = cfw.acquireLabel();
      Context cx = Context.getCurrentContext();
      if ((cx != null) && 
        (cx.hasFeature(13))) {
        handlerLabels[3] = cfw.acquireLabel();
      }
    }
    if (finallyTarget != null) {
      handlerLabels[4] = cfw.acquireLabel();
    }
    exceptionManager.setHandlers(handlerLabels, startLabel);
    

    if ((isGenerator) && (finallyTarget != null)) {
      FinallyReturnPoint ret = new FinallyReturnPoint();
      if (finallys == null) {
        finallys = new HashMap();
      }
      
      finallys.put(finallyTarget, ret);
      
      finallys.put(finallyTarget.getNext(), ret);
    }
    
    while (child != null) {
      if (child == catchTarget) {
        int catchLabel = getTargetLabel(catchTarget);
        exceptionManager.removeHandler(0, catchLabel);
        
        exceptionManager.removeHandler(1, catchLabel);
        exceptionManager.removeHandler(2, catchLabel);
        exceptionManager.removeHandler(3, catchLabel);
      }
      generateStatement(child);
      child = child.getNext();
    }
    

    int realEnd = cfw.acquireLabel();
    cfw.add(167, realEnd);
    
    int exceptionLocal = getLocalBlockRegister(node);
    

    if (catchTarget != null)
    {
      int catchLabel = catchTarget.labelId();
      





      generateCatchBlock(0, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[0]);
      





      generateCatchBlock(1, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[1]);
      






      generateCatchBlock(2, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[2]);
      


      Context cx = Context.getCurrentContext();
      if ((cx != null) && 
        (cx.hasFeature(13))) {
        generateCatchBlock(3, savedVariableObject, catchLabel, exceptionLocal, handlerLabels[3]);
      }
    }
    




    if (finallyTarget != null) {
      int finallyHandler = cfw.acquireLabel();
      int finallyEnd = cfw.acquireLabel();
      cfw.markHandler(finallyHandler);
      if (!isGenerator) {
        cfw.markLabel(handlerLabels[4]);
      }
      cfw.addAStore(exceptionLocal);
      

      cfw.addALoad(savedVariableObject);
      cfw.addAStore(variableObjectLocal);
      

      int finallyLabel = finallyTarget.labelId();
      if (isGenerator) {
        addGotoWithReturn(finallyTarget);
      } else {
        inlineFinally(finallyTarget, handlerLabels[4], finallyEnd);
      }
      


      cfw.addALoad(exceptionLocal);
      if (isGenerator)
        cfw.add(192, "java/lang/Throwable");
      cfw.add(191);
      
      cfw.markLabel(finallyEnd);
      
      if (isGenerator) {
        cfw.addExceptionHandler(startLabel, finallyLabel, finallyHandler, null);
      }
    }
    
    releaseWordLocal(savedVariableObject);
    cfw.markLabel(realEnd);
    
    if (!isGenerator) {
      exceptionManager.popExceptionInfo();
    }
  }
  











  private void generateCatchBlock(int exceptionType, short savedVariableObject, int catchLabel, int exceptionLocal, int handler)
  {
    if (handler == 0) {
      handler = cfw.acquireLabel();
    }
    cfw.markHandler(handler);
    

    cfw.addAStore(exceptionLocal);
    

    cfw.addALoad(savedVariableObject);
    cfw.addAStore(variableObjectLocal);
    
    String exceptionName = exceptionTypeToName(exceptionType);
    
    cfw.add(167, catchLabel);
  }
  
  private String exceptionTypeToName(int exceptionType) {
    if (exceptionType == 0)
      return "net/sourceforge/htmlunit/corejs/javascript/JavaScriptException";
    if (exceptionType == 1)
      return "net/sourceforge/htmlunit/corejs/javascript/EvaluatorException";
    if (exceptionType == 2)
      return "net/sourceforge/htmlunit/corejs/javascript/EcmaError";
    if (exceptionType == 3)
      return "java/lang/Throwable";
    if (exceptionType == 4) {
      return null;
    }
    throw Kit.codeBug();
  }
  










  private class ExceptionManager
  {
    private LinkedList<ExceptionInfo> exceptionInfo;
    










    ExceptionManager()
    {
      exceptionInfo = new LinkedList();
    }
    





    void pushExceptionInfo(Jump node)
    {
      Node fBlock = BodyCodegen.this.getFinallyAtTarget(node.getFinally());
      ExceptionInfo ei = new ExceptionInfo(node, fBlock);
      exceptionInfo.add(ei);
    }
    











    void addHandler(int exceptionType, int handlerLabel, int startLabel)
    {
      ExceptionInfo top = getTop();
      handlerLabels[exceptionType] = handlerLabel;
      exceptionStarts[exceptionType] = startLabel;
    }
    









    void setHandlers(int[] handlerLabels, int startLabel)
    {
      ExceptionInfo top = getTop();
      for (int i = 0; i < handlerLabels.length; i++) {
        if (handlerLabels[i] != 0) {
          addHandler(i, handlerLabels[i], startLabel);
        }
      }
    }
    











    int removeHandler(int exceptionType, int endLabel)
    {
      ExceptionInfo top = getTop();
      if (handlerLabels[exceptionType] != 0) {
        int handlerLabel = handlerLabels[exceptionType];
        endCatch(top, exceptionType, endLabel);
        handlerLabels[exceptionType] = 0;
        return handlerLabel;
      }
      return 0;
    }
    


    void popExceptionInfo()
    {
      exceptionInfo.removeLast();
    }
    
























    void markInlineFinallyStart(Node finallyBlock, int finallyStart)
    {
      ListIterator<ExceptionInfo> iter = exceptionInfo.listIterator(exceptionInfo.size());
      while (iter.hasPrevious()) {
        ExceptionInfo ei = (ExceptionInfo)iter.previous();
        for (int i = 0; i < 5; i++) {
          if ((handlerLabels[i] != 0) && (currentFinally == null)) {
            endCatch(ei, i, finallyStart);
            exceptionStarts[i] = 0;
            currentFinally = finallyBlock;
          }
        }
        if (finallyBlock == finallyBlock) {
          break;
        }
      }
    }
    












    void markInlineFinallyEnd(Node finallyBlock, int finallyEnd)
    {
      ListIterator<ExceptionInfo> iter = exceptionInfo.listIterator(exceptionInfo.size());
      while (iter.hasPrevious()) {
        ExceptionInfo ei = (ExceptionInfo)iter.previous();
        for (int i = 0; i < 5; i++) {
          if ((handlerLabels[i] != 0) && (currentFinally == finallyBlock))
          {
            exceptionStarts[i] = finallyEnd;
            currentFinally = null;
          }
        }
        if (finallyBlock == finallyBlock) {
          break;
        }
      }
    }
    







    private void endCatch(ExceptionInfo ei, int exceptionType, int catchEnd)
    {
      if (exceptionStarts[exceptionType] == 0) {
        throw new IllegalStateException("bad exception start");
      }
      
      int currentStart = exceptionStarts[exceptionType];
      int currentStartPC = cfw.getLabelPC(currentStart);
      int catchEndPC = cfw.getLabelPC(catchEnd);
      if (currentStartPC != catchEndPC) {
        cfw.addExceptionHandler(exceptionStarts[exceptionType], catchEnd, handlerLabels[exceptionType], BodyCodegen.this
        
          .exceptionTypeToName(exceptionType));
      }
    }
    

    private ExceptionInfo getTop() { return (ExceptionInfo)exceptionInfo.getLast(); }
    
    private class ExceptionInfo { Jump node;
      Node finallyBlock;
      
      ExceptionInfo(Jump node, Node finallyBlock) { this.node = node;
        this.finallyBlock = finallyBlock;
        handlerLabels = new int[5];
        exceptionStarts = new int[5];
        currentFinally = null;
      }
      


      int[] handlerLabels;
      

      int[] exceptionStarts;
      

      Node currentFinally;
    }
  }
  

  private ExceptionManager exceptionManager = new ExceptionManager();
  
  static final int GENERATOR_TERMINATE = -1;
  
  static final int GENERATOR_START = 0;
  
  static final int GENERATOR_YIELD_START = 1;
  
  ClassFileWriter cfw;
  
  Codegen codegen;
  
  CompilerEnvirons compilerEnv;
  
  ScriptNode scriptOrFn;
  
  public int scriptOrFnIndex;
  
  private int savedCodeOffset;
  
  private OptFunctionNode fnCurrent;
  private static final int MAX_LOCALS = 1024;
  private int[] locals;
  private short firstFreeLocal;
  private short localsMax;
  private int itsLineNumber;
  private boolean hasVarsInRegs;
  private short[] varRegisters;
  
  private void inlineFinally(Node finallyTarget, int finallyStart, int finallyEnd)
  {
    Node fBlock = getFinallyAtTarget(finallyTarget);
    fBlock.resetTargets();
    Node child = fBlock.getFirstChild();
    exceptionManager.markInlineFinallyStart(fBlock, finallyStart);
    while (child != null) {
      generateStatement(child);
      child = child.getNext();
    }
    exceptionManager.markInlineFinallyEnd(fBlock, finallyEnd);
  }
  
  private void inlineFinally(Node finallyTarget) {
    int finallyStart = cfw.acquireLabel();
    int finallyEnd = cfw.acquireLabel();
    cfw.markLabel(finallyStart);
    inlineFinally(finallyTarget, finallyStart, finallyEnd);
    cfw.markLabel(finallyEnd);
  }
  





  private Node getFinallyAtTarget(Node node)
  {
    if (node == null)
      return null;
    if (node.getType() == 125)
      return node;
    if ((node != null) && (node.getType() == 131)) {
      Node fBlock = node.getNext();
      if ((fBlock != null) && (fBlock.getType() == 125)) {
        return fBlock;
      }
    }
    throw Kit.codeBug("bad finally target");
  }
  
  private boolean generateSaveLocals(Node node) {
    int count = 0;
    for (int i = 0; i < firstFreeLocal; i++) {
      if (locals[i] != 0) {
        count++;
      }
    }
    if (count == 0) {
      ((FunctionNode)scriptOrFn).addLiveLocals(node, null);
      return false;
    }
    

    maxLocals = (maxLocals > count ? maxLocals : count);
    

    int[] ls = new int[count];
    int s = 0;
    for (int i = 0; i < firstFreeLocal; i++) {
      if (locals[i] != 0) {
        ls[s] = i;
        s++;
      }
    }
    

    ((FunctionNode)scriptOrFn).addLiveLocals(node, ls);
    

    generateGetGeneratorLocalsState();
    for (int i = 0; i < count; i++) {
      cfw.add(89);
      cfw.addLoadConstant(i);
      cfw.addALoad(ls[i]);
      cfw.add(83);
    }
    
    cfw.add(87);
    
    return true;
  }
  


  private void visitSwitch(Jump switchNode, Node child)
  {
    generateExpression(child, switchNode);
    
    short selector = getNewWordLocal();
    cfw.addAStore(selector);
    

    for (Jump caseNode = (Jump)child.getNext(); caseNode != null; 
        caseNode = (Jump)caseNode.getNext()) {
      if (caseNode.getType() != 115)
        throw Codegen.badTree();
      Node test = caseNode.getFirstChild();
      generateExpression(test, caseNode);
      cfw.addALoad(selector);
      addScriptRuntimeInvoke("shallowEq", "(Ljava/lang/Object;Ljava/lang/Object;)Z");
      
      addGoto(target, 154);
    }
    releaseWordLocal(selector);
  }
  
  private void visitTypeofname(Node node) {
    if (hasVarsInRegs) {
      int varIndex = fnCurrent.fnode.getIndexForNameNode(node);
      if (varIndex >= 0) {
        if (fnCurrent.isNumberVar(varIndex)) {
          cfw.addPush("number");
        } else if (varIsDirectCallParameter(varIndex)) {
          int dcp_register = varRegisters[varIndex];
          cfw.addALoad(dcp_register);
          cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
          
          int isNumberLabel = cfw.acquireLabel();
          cfw.add(165, isNumberLabel);
          short stack = cfw.getStackTop();
          cfw.addALoad(dcp_register);
          addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
          
          int beyond = cfw.acquireLabel();
          cfw.add(167, beyond);
          cfw.markLabel(isNumberLabel, stack);
          cfw.addPush("number");
          cfw.markLabel(beyond);
        } else {
          cfw.addALoad(varRegisters[varIndex]);
          addScriptRuntimeInvoke("typeof", "(Ljava/lang/Object;)Ljava/lang/String;");
        }
        
        return;
      }
    }
    cfw.addALoad(variableObjectLocal);
    cfw.addPush(node.getString());
    addScriptRuntimeInvoke("typeofName", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/String;");
  }
  





  private void saveCurrentCodeOffset()
  {
    savedCodeOffset = cfw.getCurrentCodeOffset();
  }
  





  private void addInstructionCount()
  {
    int count = cfw.getCurrentCodeOffset() - savedCodeOffset;
    


    addInstructionCount(Math.max(count, 1));
  }
  







  private void addInstructionCount(int count)
  {
    cfw.addALoad(contextLocal);
    cfw.addPush(count);
    addScriptRuntimeInvoke("addInstructionCount", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)V");
  }
  

  private void visitIncDec(Node node)
  {
    int incrDecrMask = node.getExistingIntProp(13);
    Node child = node.getFirstChild();
    switch (child.getType()) {
    case 55: 
      if (!hasVarsInRegs)
        Kit.codeBug();
      boolean post = (incrDecrMask & 0x2) != 0;
      int varIndex = fnCurrent.getVarIndex(child);
      short reg = varRegisters[varIndex];
      boolean[] constDeclarations = fnCurrent.fnode.getParamAndVarConst();
      if (constDeclarations[varIndex] != 0) {
        if (node.getIntProp(8, -1) != -1) {
          int offset = varIsDirectCallParameter(varIndex) ? 1 : 0;
          cfw.addDLoad(reg + offset);
          if (!post) {
            cfw.addPush(1.0D);
            if ((incrDecrMask & 0x1) == 0) {
              cfw.add(99);
            } else {
              cfw.add(103);
            }
          }
        } else {
          if (varIsDirectCallParameter(varIndex)) {
            dcpLoadAsObject(reg);
          } else {
            cfw.addALoad(reg);
          }
          if (post) {
            cfw.add(89);
            addObjectToDouble();
            cfw.add(88);
          } else {
            addObjectToDouble();
            cfw.addPush(1.0D);
            if ((incrDecrMask & 0x1) == 0) {
              cfw.add(99);
            } else {
              cfw.add(103);
            }
            addDoubleWrap();
          }
          
        }
      }
      else if (node.getIntProp(8, -1) != -1) {
        int offset = varIsDirectCallParameter(varIndex) ? 1 : 0;
        cfw.addDLoad(reg + offset);
        if (post) {
          cfw.add(92);
        }
        cfw.addPush(1.0D);
        if ((incrDecrMask & 0x1) == 0) {
          cfw.add(99);
        } else {
          cfw.add(103);
        }
        if (!post) {
          cfw.add(92);
        }
        cfw.addDStore(reg + offset);
      } else {
        if (varIsDirectCallParameter(varIndex)) {
          dcpLoadAsObject(reg);
        } else {
          cfw.addALoad(reg);
        }
        if (post) {
          cfw.add(89);
        }
        addObjectToDouble();
        cfw.addPush(1.0D);
        if ((incrDecrMask & 0x1) == 0) {
          cfw.add(99);
        } else {
          cfw.add(103);
        }
        addDoubleWrap();
        if (!post) {
          cfw.add(89);
        }
        cfw.addAStore(reg);
      }
      break;
    case 39: 
      cfw.addALoad(variableObjectLocal);
      cfw.addPush(child.getString());
      cfw.addALoad(contextLocal);
      cfw.addPush(incrDecrMask);
      addScriptRuntimeInvoke("nameIncrDecr", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)Ljava/lang/Object;");
      



      break;
    case 34: 
      throw Kit.codeBug();
    case 33: 
      Node getPropChild = child.getFirstChild();
      generateExpression(getPropChild, node);
      generateExpression(getPropChild.getNext(), node);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addPush(incrDecrMask);
      addScriptRuntimeInvoke("propIncrDecr", "(Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Ljava/lang/Object;");
      



      break;
    
    case 36: 
      Node elemChild = child.getFirstChild();
      generateExpression(elemChild, node);
      generateExpression(elemChild.getNext(), node);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addPush(incrDecrMask);
      if (elemChild.getNext().getIntProp(8, -1) != -1) {
        addOptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;DLnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Ljava/lang/Object;");

      }
      else
      {

        addScriptRuntimeInvoke("elemIncrDecr", "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Ljava/lang/Object;");
      }
      



      break;
    
    case 67: 
      Node refChild = child.getFirstChild();
      generateExpression(refChild, node);
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      cfw.addPush(incrDecrMask);
      addScriptRuntimeInvoke("refIncrDecr", "(Lnet/sourceforge/htmlunit/corejs/javascript/Ref;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;I)Ljava/lang/Object;");
      



      break;
    
    default: 
      Codegen.badTree();
    }
  }
  
  private static boolean isArithmeticNode(Node node) {
    int type = node.getType();
    return (type == 22) || (type == 25) || (type == 24) || (type == 23);
  }
  

  private void visitArithmetic(Node node, int opCode, Node child, Node parent)
  {
    int childNumberFlag = node.getIntProp(8, -1);
    if (childNumberFlag != -1) {
      generateExpression(child, node);
      generateExpression(child.getNext(), node);
      cfw.add(opCode);
    } else {
      boolean childOfArithmetic = isArithmeticNode(parent);
      generateExpression(child, node);
      if (!isArithmeticNode(child))
        addObjectToDouble();
      generateExpression(child.getNext(), node);
      if (!isArithmeticNode(child.getNext()))
        addObjectToDouble();
      cfw.add(opCode);
      if (!childOfArithmetic) {
        addDoubleWrap();
      }
    }
  }
  
  private void visitBitOp(Node node, int type, Node child) {
    int childNumberFlag = node.getIntProp(8, -1);
    generateExpression(child, node);
    



    if (type == 20) {
      addScriptRuntimeInvoke("toUint32", "(Ljava/lang/Object;)J");
      generateExpression(child.getNext(), node);
      addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
      

      cfw.addPush(31);
      cfw.add(126);
      cfw.add(125);
      cfw.add(138);
      addDoubleWrap();
      return;
    }
    if (childNumberFlag == -1) {
      addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
      generateExpression(child.getNext(), node);
      addScriptRuntimeInvoke("toInt32", "(Ljava/lang/Object;)I");
    } else {
      addScriptRuntimeInvoke("toInt32", "(D)I");
      generateExpression(child.getNext(), node);
      addScriptRuntimeInvoke("toInt32", "(D)I");
    }
    switch (type) {
    case 9: 
      cfw.add(128);
      break;
    case 10: 
      cfw.add(130);
      break;
    case 11: 
      cfw.add(126);
      break;
    case 19: 
      cfw.add(122);
      break;
    case 18: 
      cfw.add(120);
      break;
    case 12: case 13: case 14: case 15: case 16: case 17: default: 
      throw Codegen.badTree();
    }
    cfw.add(135);
    if (childNumberFlag == -1) {
      addDoubleWrap();
    }
  }
  
  private int nodeIsDirectCallParameter(Node node) {
    if ((node.getType() == 55) && (inDirectCallFunction) && (!itsForcedObjectParameters))
    {
      int varIndex = fnCurrent.getVarIndex(node);
      if (fnCurrent.isParameter(varIndex)) {
        return varRegisters[varIndex];
      }
    }
    return -1;
  }
  
  private boolean varIsDirectCallParameter(int varIndex) {
    return (fnCurrent.isParameter(varIndex)) && (inDirectCallFunction) && (!itsForcedObjectParameters);
  }
  
  private void genSimpleCompare(int type, int trueGOTO, int falseGOTO)
  {
    if (trueGOTO == -1)
      throw Codegen.badTree();
    switch (type) {
    case 15: 
      cfw.add(152);
      cfw.add(158, trueGOTO);
      break;
    case 17: 
      cfw.add(151);
      cfw.add(156, trueGOTO);
      break;
    case 14: 
      cfw.add(152);
      cfw.add(155, trueGOTO);
      break;
    case 16: 
      cfw.add(151);
      cfw.add(157, trueGOTO);
      break;
    default: 
      throw Codegen.badTree();
    }
    
    if (falseGOTO != -1) {
      cfw.add(167, falseGOTO);
    }
  }
  
  private void visitIfJumpRelOp(Node node, Node child, int trueGOTO, int falseGOTO) {
    if ((trueGOTO == -1) || (falseGOTO == -1))
      throw Codegen.badTree();
    int type = node.getType();
    Node rChild = child.getNext();
    if ((type == 53) || (type == 52)) {
      generateExpression(child, node);
      generateExpression(rChild, node);
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke(type == 53 ? "instanceOf" : "in", "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Z");
      



      cfw.add(154, trueGOTO);
      cfw.add(167, falseGOTO);
      return;
    }
    int childNumberFlag = node.getIntProp(8, -1);
    int left_dcp_register = nodeIsDirectCallParameter(child);
    int right_dcp_register = nodeIsDirectCallParameter(rChild);
    if (childNumberFlag != -1)
    {


      if (childNumberFlag != 2)
      {
        generateExpression(child, node);
      } else if (left_dcp_register != -1) {
        dcpLoadAsNumber(left_dcp_register);
      } else {
        generateExpression(child, node);
        addObjectToDouble();
      }
      
      if (childNumberFlag != 1)
      {
        generateExpression(rChild, node);
      } else if (right_dcp_register != -1) {
        dcpLoadAsNumber(right_dcp_register);
      } else {
        generateExpression(rChild, node);
        addObjectToDouble();
      }
      
      genSimpleCompare(type, trueGOTO, falseGOTO);
    }
    else {
      if ((left_dcp_register != -1) && (right_dcp_register != -1))
      {

        short stack = cfw.getStackTop();
        int leftIsNotNumber = cfw.acquireLabel();
        cfw.addALoad(left_dcp_register);
        cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        
        cfw.add(166, leftIsNotNumber);
        cfw.addDLoad(left_dcp_register + 1);
        dcpLoadAsNumber(right_dcp_register);
        genSimpleCompare(type, trueGOTO, falseGOTO);
        if (stack != cfw.getStackTop()) {
          throw Codegen.badTree();
        }
        cfw.markLabel(leftIsNotNumber);
        int rightIsNotNumber = cfw.acquireLabel();
        cfw.addALoad(right_dcp_register);
        cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        
        cfw.add(166, rightIsNotNumber);
        cfw.addALoad(left_dcp_register);
        addObjectToDouble();
        cfw.addDLoad(right_dcp_register + 1);
        genSimpleCompare(type, trueGOTO, falseGOTO);
        if (stack != cfw.getStackTop()) {
          throw Codegen.badTree();
        }
        cfw.markLabel(rightIsNotNumber);
        
        cfw.addALoad(left_dcp_register);
        cfw.addALoad(right_dcp_register);
      }
      else {
        generateExpression(child, node);
        generateExpression(rChild, node);
      }
      
      if ((type == 17) || (type == 16)) {
        cfw.add(95);
      }
      String routine = (type == 14) || (type == 16) ? "cmp_LT" : "cmp_LE";
      
      addScriptRuntimeInvoke(routine, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
      
      cfw.add(154, trueGOTO);
      cfw.add(167, falseGOTO);
    }
  }
  
  private void visitIfJumpEqOp(Node node, Node child, int trueGOTO, int falseGOTO)
  {
    if ((trueGOTO == -1) || (falseGOTO == -1)) {
      throw Codegen.badTree();
    }
    short stackInitial = cfw.getStackTop();
    int type = node.getType();
    Node rChild = child.getNext();
    

    if ((child.getType() == 42) || (rChild.getType() == 42))
    {
      if (child.getType() == 42) {
        child = rChild;
      }
      generateExpression(child, node);
      if ((type == 46) || (type == 47)) {
        int testCode = type == 46 ? 198 : 199;
        
        cfw.add(testCode, trueGOTO);
      } else {
        if (type != 12)
        {
          if (type != 13)
            throw Codegen.badTree();
          int tmp = trueGOTO;
          trueGOTO = falseGOTO;
          falseGOTO = tmp;
        }
        cfw.add(89);
        int undefCheckLabel = cfw.acquireLabel();
        cfw.add(199, undefCheckLabel);
        short stack = cfw.getStackTop();
        cfw.add(87);
        cfw.add(167, trueGOTO);
        cfw.markLabel(undefCheckLabel, stack);
        Codegen.pushUndefined(cfw);
        cfw.add(165, trueGOTO);
      }
      cfw.add(167, falseGOTO);
    } else {
      int child_dcp_register = nodeIsDirectCallParameter(child);
      if ((child_dcp_register != -1) && 
        (rChild.getType() == 149)) {
        Node convertChild = rChild.getFirstChild();
        if (convertChild.getType() == 40) {
          cfw.addALoad(child_dcp_register);
          cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
          
          int notNumbersLabel = cfw.acquireLabel();
          cfw.add(166, notNumbersLabel);
          cfw.addDLoad(child_dcp_register + 1);
          cfw.addPush(convertChild.getDouble());
          cfw.add(151);
          if (type == 12) {
            cfw.add(153, trueGOTO);
          } else
            cfw.add(154, trueGOTO);
          cfw.add(167, falseGOTO);
          cfw.markLabel(notNumbersLabel);
        }
      }
      

      generateExpression(child, node);
      generateExpression(rChild, node);
      int testCode;
      int testCode;
      int testCode;
      int testCode; switch (type) {
      case 12: 
        String name = "eq";
        testCode = 154;
        break;
      case 13: 
        String name = "eq";
        testCode = 153;
        break;
      case 46: 
        String name = "shallowEq";
        testCode = 154;
        break;
      case 47: 
        String name = "shallowEq";
        testCode = 153;
        break;
      default: 
        throw Codegen.badTree(); }
      int testCode;
      String name; addScriptRuntimeInvoke(name, "(Ljava/lang/Object;Ljava/lang/Object;)Z");
      
      cfw.add(testCode, trueGOTO);
      cfw.add(167, falseGOTO);
    }
    if (stackInitial != cfw.getStackTop())
      throw Codegen.badTree();
  }
  
  private void visitSetName(Node node, Node child) {
    String name = node.getFirstChild().getString();
    while (child != null) {
      generateExpression(child, node);
      child = child.getNext();
    }
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    cfw.addPush(name);
    addScriptRuntimeInvoke("setName", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
  }
  




  private void visitStrictSetName(Node node, Node child)
  {
    String name = node.getFirstChild().getString();
    while (child != null) {
      generateExpression(child, node);
      child = child.getNext();
    }
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    cfw.addPush(name);
    addScriptRuntimeInvoke("strictSetName", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;)Ljava/lang/Object;");
  }
  




  private void visitSetConst(Node node, Node child)
  {
    String name = node.getFirstChild().getString();
    while (child != null) {
      generateExpression(child, node);
      child = child.getNext();
    }
    cfw.addALoad(contextLocal);
    cfw.addPush(name);
    addScriptRuntimeInvoke("setConst", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Ljava/lang/String;)Ljava/lang/Object;");
  }
  



  private void visitGetVar(Node node)
  {
    if (!hasVarsInRegs)
      Kit.codeBug();
    int varIndex = fnCurrent.getVarIndex(node);
    short reg = varRegisters[varIndex];
    if (varIsDirectCallParameter(varIndex))
    {



      if (node.getIntProp(8, -1) != -1) {
        dcpLoadAsNumber(reg);
      } else {
        dcpLoadAsObject(reg);
      }
    } else if (fnCurrent.isNumberVar(varIndex)) {
      cfw.addDLoad(reg);
    } else {
      cfw.addALoad(reg);
    }
  }
  
  private void visitSetVar(Node node, Node child, boolean needValue) {
    if (!hasVarsInRegs)
      Kit.codeBug();
    int varIndex = fnCurrent.getVarIndex(node);
    generateExpression(child.getNext(), node);
    boolean isNumber = node.getIntProp(8, -1) != -1;
    short reg = varRegisters[varIndex];
    boolean[] constDeclarations = fnCurrent.fnode.getParamAndVarConst();
    if (constDeclarations[varIndex] != 0) {
      if (!needValue) {
        if (isNumber) {
          cfw.add(88);
        } else
          cfw.add(87);
      }
    } else if (varIsDirectCallParameter(varIndex)) {
      if (isNumber) {
        if (needValue)
          cfw.add(92);
        cfw.addALoad(reg);
        cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
        
        int isNumberLabel = cfw.acquireLabel();
        int beyond = cfw.acquireLabel();
        cfw.add(165, isNumberLabel);
        short stack = cfw.getStackTop();
        addDoubleWrap();
        cfw.addAStore(reg);
        cfw.add(167, beyond);
        cfw.markLabel(isNumberLabel, stack);
        cfw.addDStore(reg + 1);
        cfw.markLabel(beyond);
      } else {
        if (needValue)
          cfw.add(89);
        cfw.addAStore(reg);
      }
    } else {
      boolean isNumberVar = fnCurrent.isNumberVar(varIndex);
      if (isNumber) {
        if (isNumberVar) {
          cfw.addDStore(reg);
          if (needValue)
            cfw.addDLoad(reg);
        } else {
          if (needValue) {
            cfw.add(92);
          }
          
          addDoubleWrap();
          cfw.addAStore(reg);
        }
      } else {
        if (isNumberVar)
          Kit.codeBug();
        cfw.addAStore(reg);
        if (needValue)
          cfw.addALoad(reg);
      }
    }
  }
  
  private void visitSetConstVar(Node node, Node child, boolean needValue) {
    if (!hasVarsInRegs)
      Kit.codeBug();
    int varIndex = fnCurrent.getVarIndex(node);
    generateExpression(child.getNext(), node);
    boolean isNumber = node.getIntProp(8, -1) != -1;
    short reg = varRegisters[varIndex];
    int beyond = cfw.acquireLabel();
    int noAssign = cfw.acquireLabel();
    if (isNumber) {
      cfw.addILoad(reg + 2);
      cfw.add(154, noAssign);
      short stack = cfw.getStackTop();
      cfw.addPush(1);
      cfw.addIStore(reg + 2);
      cfw.addDStore(reg);
      if (needValue) {
        cfw.addDLoad(reg);
        cfw.markLabel(noAssign, stack);
      } else {
        cfw.add(167, beyond);
        cfw.markLabel(noAssign, stack);
        cfw.add(88);
      }
    } else {
      cfw.addILoad(reg + 1);
      cfw.add(154, noAssign);
      short stack = cfw.getStackTop();
      cfw.addPush(1);
      cfw.addIStore(reg + 1);
      cfw.addAStore(reg);
      if (needValue) {
        cfw.addALoad(reg);
        cfw.markLabel(noAssign, stack);
      } else {
        cfw.add(167, beyond);
        cfw.markLabel(noAssign, stack);
        cfw.add(87);
      }
    }
    cfw.markLabel(beyond);
  }
  
  private void visitGetProp(Node node, Node child) {
    generateExpression(child, node);
    Node nameChild = child.getNext();
    generateExpression(nameChild, node);
    if (node.getType() == 34) {
      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      addScriptRuntimeInvoke("getObjectPropNoWarn", "(Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
      



      return;
    }
    



    int childType = child.getType();
    if ((childType == 43) && (nameChild.getType() == 41)) {
      cfw.addALoad(contextLocal);
      addScriptRuntimeInvoke("getObjectProp", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");

    }
    else
    {

      cfw.addALoad(contextLocal);
      cfw.addALoad(variableObjectLocal);
      addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
    }
  }
  



  private void visitSetProp(int type, Node node, Node child)
  {
    Node objectChild = child;
    generateExpression(child, node);
    child = child.getNext();
    if (type == 139) {
      cfw.add(89);
    }
    Node nameChild = child;
    generateExpression(child, node);
    child = child.getNext();
    if (type == 139)
    {
      cfw.add(90);
      

      if ((objectChild.getType() == 43) && 
        (nameChild.getType() == 41)) {
        cfw.addALoad(contextLocal);
        addScriptRuntimeInvoke("getObjectProp", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Ljava/lang/Object;");

      }
      else
      {

        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("getObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
      }
    }
    



    generateExpression(child, node);
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    addScriptRuntimeInvoke("setObjectProp", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
  }
  




  private void visitSetElem(int type, Node node, Node child)
  {
    generateExpression(child, node);
    child = child.getNext();
    if (type == 140) {
      cfw.add(89);
    }
    generateExpression(child, node);
    child = child.getNext();
    boolean indexIsNumber = node.getIntProp(8, -1) != -1;
    if (type == 140) {
      if (indexIsNumber)
      {

        cfw.add(93);
        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("getObjectIndex", "(Ljava/lang/Object;DLnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");


      }
      else
      {


        cfw.add(90);
        cfw.addALoad(contextLocal);
        cfw.addALoad(variableObjectLocal);
        addScriptRuntimeInvoke("getObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
      }
    }
    



    generateExpression(child, node);
    cfw.addALoad(contextLocal);
    cfw.addALoad(variableObjectLocal);
    if (indexIsNumber) {
      addScriptRuntimeInvoke("setObjectIndex", "(Ljava/lang/Object;DLjava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");

    }
    else
    {

      addScriptRuntimeInvoke("setObjectElem", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
    }
  }
  




  private void visitDotQuery(Node node, Node child)
  {
    updateLineNumber(node);
    generateExpression(child, node);
    cfw.addALoad(variableObjectLocal);
    addScriptRuntimeInvoke("enterDotQuery", "(Ljava/lang/Object;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    cfw.addAStore(variableObjectLocal);
    



    cfw.add(1);
    int queryLoopStart = cfw.acquireLabel();
    cfw.markLabel(queryLoopStart);
    cfw.add(87);
    
    generateExpression(child.getNext(), node);
    addScriptRuntimeInvoke("toBoolean", "(Ljava/lang/Object;)Z");
    cfw.addALoad(variableObjectLocal);
    addScriptRuntimeInvoke("updateDotQuery", "(ZLnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;");
    

    cfw.add(89);
    cfw.add(198, queryLoopStart);
    
    cfw.addALoad(variableObjectLocal);
    addScriptRuntimeInvoke("leaveDotQuery", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    cfw.addAStore(variableObjectLocal);
  }
  
  private int getLocalBlockRegister(Node node) {
    Node localBlock = (Node)node.getProp(3);
    int localSlot = localBlock.getExistingIntProp(2);
    return localSlot;
  }
  
  private void dcpLoadAsNumber(int dcp_register) {
    cfw.addALoad(dcp_register);
    cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
    
    int isNumberLabel = cfw.acquireLabel();
    cfw.add(165, isNumberLabel);
    short stack = cfw.getStackTop();
    cfw.addALoad(dcp_register);
    addObjectToDouble();
    int beyond = cfw.acquireLabel();
    cfw.add(167, beyond);
    cfw.markLabel(isNumberLabel, stack);
    cfw.addDLoad(dcp_register + 1);
    cfw.markLabel(beyond);
  }
  
  private void dcpLoadAsObject(int dcp_register) {
    cfw.addALoad(dcp_register);
    cfw.add(178, "java/lang/Void", "TYPE", "Ljava/lang/Class;");
    
    int isNumberLabel = cfw.acquireLabel();
    cfw.add(165, isNumberLabel);
    short stack = cfw.getStackTop();
    cfw.addALoad(dcp_register);
    int beyond = cfw.acquireLabel();
    cfw.add(167, beyond);
    cfw.markLabel(isNumberLabel, stack);
    cfw.addDLoad(dcp_register + 1);
    addDoubleWrap();
    cfw.markLabel(beyond);
  }
  
  private void addGoto(Node target, int jumpcode) {
    int targetLabel = getTargetLabel(target);
    cfw.add(jumpcode, targetLabel);
  }
  
  private void addObjectToDouble() {
    addScriptRuntimeInvoke("toNumber", "(Ljava/lang/Object;)D");
  }
  
  private void addNewObjectArray(int size) {
    if (size == 0) {
      if (itsZeroArgArray >= 0) {
        cfw.addALoad(itsZeroArgArray);
      } else {
        cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "emptyArgs", "[Ljava/lang/Object;");
      }
    }
    else
    {
      cfw.addPush(size);
      cfw.add(189, "java/lang/Object");
    }
  }
  
  private void addScriptRuntimeInvoke(String methodName, String methodSignature)
  {
    cfw.addInvoke(184, "net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime", methodName, methodSignature);
  }
  


  private void addOptRuntimeInvoke(String methodName, String methodSignature)
  {
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime", methodName, methodSignature);
  }
  

  private void addJumpedBooleanWrap(int trueLabel, int falseLabel)
  {
    cfw.markLabel(falseLabel);
    int skip = cfw.acquireLabel();
    cfw.add(178, "java/lang/Boolean", "FALSE", "Ljava/lang/Boolean;");
    
    cfw.add(167, skip);
    cfw.markLabel(trueLabel);
    cfw.add(178, "java/lang/Boolean", "TRUE", "Ljava/lang/Boolean;");
    
    cfw.markLabel(skip);
    cfw.adjustStackTop(-1);
  }
  
  private void addDoubleWrap() {
    addOptRuntimeInvoke("wrapDouble", "(D)Ljava/lang/Double;");
  }
  







  private short getNewWordPairLocal(boolean isConst)
  {
    return getNewWordIntern(isConst ? 3 : 2);
  }
  
  private short getNewWordLocal(boolean isConst) {
    return getNewWordIntern(isConst ? 2 : 1);
  }
  
  private short getNewWordLocal() {
    return getNewWordIntern(1);
  }
  
  private short getNewWordIntern(int count) {
    assert ((count >= 1) && (count <= 3));
    
    int[] locals = this.locals;
    int result = -1;
    if (count > 1)
    {
      int i = firstFreeLocal; if (i + count <= 1024) {
        for (int j = 0;; j++) { if (j >= count) break label89;
          if (locals[(i + j)] != 0) {
            i += j + 1;
            break;
          } }
        label89:
        result = i;
      }
    }
    else {
      result = firstFreeLocal;
    }
    
    if (result != -1) {
      locals[result] = 1;
      if (count > 1)
        locals[(result + 1)] = 1;
      if (count > 2) {
        locals[(result + 2)] = 1;
      }
      if (result == firstFreeLocal) {
        for (int i = result + count; i < 1024; i++) {
          if (locals[i] == 0) {
            firstFreeLocal = ((short)i);
            if (localsMax < firstFreeLocal)
              localsMax = firstFreeLocal;
            return (short)result;
          }
        }
      } else {
        return (short)result;
      }
    }
    
    throw Context.reportRuntimeError("Program too complex (out of locals)");
  }
  
  private void incReferenceWordLocal(short local)
  {
    locals[local] += 1;
  }
  
  private void decReferenceWordLocal(short local)
  {
    locals[local] -= 1;
  }
  
  private void releaseWordLocal(short local) {
    if (local < firstFreeLocal)
      firstFreeLocal = local;
    locals[local] = 0;
  }
  


  private boolean inDirectCallFunction;
  

  private boolean itsForcedObjectParameters;
  

  private int enterAreaStartLabel;
  

  private int epilogueLabel;
  

  private boolean inLocalBlock;
  

  private short variableObjectLocal;
  

  private short popvLocal;
  

  private short contextLocal;
  
  private short argsLocal;
  
  private short operationLocal;
  
  private short thisObjLocal;
  
  private short funObjLocal;
  
  private short itsZeroArgArray;
  
  private short itsOneArgArray;
  
  private short generatorStateLocal;
  
  private boolean isGenerator;
  
  private int generatorSwitch;
  
  private int maxLocals = 0;
  private int maxStack = 0;
  private Map<Node, FinallyReturnPoint> finallys;
  private List<Node> literals;
  
  static class FinallyReturnPoint
  {
    public List<Integer> jsrPoints = new ArrayList();
    public int tableLabel = 0;
    
    FinallyReturnPoint() {}
  }
}

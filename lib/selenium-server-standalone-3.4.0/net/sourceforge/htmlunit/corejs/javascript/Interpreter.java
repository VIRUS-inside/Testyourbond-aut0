package net.sourceforge.htmlunit.corejs.javascript;

import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame;
import net.sourceforge.htmlunit.corejs.javascript.debug.Debugger;



























public final class Interpreter
  extends Icode
  implements Evaluator
{
  InterpreterData itsData;
  static final int EXCEPTION_TRY_START_SLOT = 0;
  static final int EXCEPTION_TRY_END_SLOT = 1;
  static final int EXCEPTION_HANDLER_SLOT = 2;
  static final int EXCEPTION_TYPE_SLOT = 3;
  static final int EXCEPTION_LOCAL_SLOT = 4;
  static final int EXCEPTION_SCOPE_SLOT = 5;
  static final int EXCEPTION_SLOT_SIZE = 6;
  public Interpreter() {}
  
  private static class CallFrame
    implements Cloneable, Serializable
  {
    static final long serialVersionUID = -2843792508994958978L;
    CallFrame parentFrame;
    int frameIndex;
    boolean frozen;
    InterpretedFunction fnOrScript;
    InterpreterData idata;
    Object[] stack;
    int[] stackAttributes;
    double[] sDbl;
    CallFrame varSource;
    int localShift;
    int emptyStackTop;
    DebugFrame debuggerFrame;
    boolean useActivation;
    boolean isContinuationsTopFrame;
    Scriptable thisObj;
    Object result;
    double resultDbl;
    int pc;
    int pcPrevBranch;
    int pcSourceLineStart;
    Scriptable scope;
    int savedStackTop;
    int savedCallOp;
    Object throwable;
    
    private CallFrame() {}
    
    CallFrame cloneFrozen()
    {
      if (!frozen) {
        Kit.codeBug();
      }
      try
      {
        copy = (CallFrame)clone();
      } catch (CloneNotSupportedException ex) { CallFrame copy;
        throw new IllegalStateException();
      }
      

      CallFrame copy;
      
      stack = ((Object[])stack.clone());
      stackAttributes = ((int[])stackAttributes.clone());
      sDbl = ((double[])sDbl.clone());
      
      frozen = false;
      return copy;
    }
  }
  
  private static final class ContinuationJump implements Serializable
  {
    static final long serialVersionUID = 7687739156004308247L;
    Interpreter.CallFrame capturedFrame;
    Interpreter.CallFrame branchFrame;
    Object result;
    double resultDbl;
    
    ContinuationJump(NativeContinuation c, Interpreter.CallFrame current) {
      capturedFrame = ((Interpreter.CallFrame)c.getImplementation());
      if ((capturedFrame == null) || (current == null))
      {


        branchFrame = null;
      }
      else
      {
        Interpreter.CallFrame chain1 = capturedFrame;
        Interpreter.CallFrame chain2 = current;
        


        int diff = frameIndex - frameIndex;
        if (diff != 0) {
          if (diff < 0)
          {

            chain1 = current;
            chain2 = capturedFrame;
            diff = -diff;
          }
          do {
            chain1 = parentFrame;
            diff--; } while (diff != 0);
          if (frameIndex != frameIndex) {
            Kit.codeBug();
          }
        }
        

        while ((chain1 != chain2) && (chain1 != null)) {
          chain1 = parentFrame;
          chain2 = parentFrame;
        }
        
        branchFrame = chain1;
        if ((branchFrame != null) && (!branchFrame.frozen))
          Kit.codeBug();
      }
    }
  }
  
  private static CallFrame captureFrameForGenerator(CallFrame frame) {
    frozen = true;
    CallFrame result = frame.cloneFrozen();
    frozen = false;
    

    parentFrame = null;
    frameIndex = 0;
    
    return result;
  }
  















  public Object compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction)
  {
    CodeGenerator cgen = new CodeGenerator();
    itsData = cgen.compile(compilerEnv, tree, encodedSource, returnFunction);
    
    return itsData;
  }
  
  public Script createScriptObject(Object bytecode, Object staticSecurityDomain)
  {
    if (bytecode != itsData) {
      Kit.codeBug();
    }
    return InterpretedFunction.createScript(itsData, staticSecurityDomain);
  }
  
  public void setEvalScriptFlag(Script script) {
    idata.evalScriptFlag = true;
  }
  
  public Function createFunctionObject(Context cx, Scriptable scope, Object bytecode, Object staticSecurityDomain)
  {
    if (bytecode != itsData) {
      Kit.codeBug();
    }
    return InterpretedFunction.createFunction(cx, scope, itsData, staticSecurityDomain);
  }
  
  private static int getShort(byte[] iCode, int pc)
  {
    return iCode[pc] << 8 | iCode[(pc + 1)] & 0xFF;
  }
  
  private static int getIndex(byte[] iCode, int pc) {
    return (iCode[pc] & 0xFF) << 8 | iCode[(pc + 1)] & 0xFF;
  }
  
  private static int getInt(byte[] iCode, int pc) {
    return iCode[pc] << 24 | (iCode[(pc + 1)] & 0xFF) << 16 | (iCode[(pc + 2)] & 0xFF) << 8 | iCode[(pc + 3)] & 0xFF;
  }
  

  private static int getExceptionHandler(CallFrame frame, boolean onlyFinally)
  {
    int[] exceptionTable = idata.itsExceptionTable;
    if (exceptionTable == null)
    {
      return -1;
    }
    



    int pc = frame.pc - 1;
    

    int best = -1;int bestStart = 0;int bestEnd = 0;
    for (int i = 0; i != exceptionTable.length; i += 6) {
      int start = exceptionTable[(i + 0)];
      int end = exceptionTable[(i + 1)];
      if ((start <= pc) && (pc < end))
      {

        if ((!onlyFinally) || (exceptionTable[(i + 3)] == 1))
        {

          if (best >= 0)
          {


            if (bestEnd < end) {
              continue;
            }
            
            if (bestStart > start)
              Kit.codeBug();
            if (bestEnd == end)
              Kit.codeBug();
          }
          best = i;
          bestStart = start;
          bestEnd = end;
        } } }
    return best;
  }
  







































































































  static void dumpICode(InterpreterData idata) {}
  







































































































  private static int bytecodeSpan(int bytecode)
  {
    switch (bytecode)
    {
    case -63: 
    case -62: 
    case 50: 
    case 72: 
      return 3;
    

    case -54: 
    case -23: 
    case -6: 
    case 5: 
    case 6: 
    case 7: 
      return 3;
    



    case -21: 
      return 5;
    

    case 57: 
      return 2;
    

    case -11: 
    case -10: 
    case -9: 
    case -8: 
    case -7: 
      return 2;
    

    case -27: 
      return 3;
    

    case -28: 
      return 5;
    

    case -38: 
      return 2;
    

    case -39: 
      return 3;
    

    case -40: 
      return 5;
    

    case -45: 
      return 2;
    

    case -46: 
      return 3;
    

    case -47: 
      return 5;
    

    case -61: 
    case -49: 
    case -48: 
      return 2;
    

    case -26: 
      return 3;
    }
    if (!validBytecode(bytecode))
      throw Kit.codeBug();
    return 1;
  }
  
  static int[] getLineNumbers(InterpreterData data) {
    UintMap presentLines = new UintMap();
    
    byte[] iCode = itsICode;
    int iCodeLength = iCode.length;
    for (int pc = 0; pc != iCodeLength;) {
      int bytecode = iCode[pc];
      int span = bytecodeSpan(bytecode);
      if (bytecode == -26) {
        if (span != 3)
          Kit.codeBug();
        int line = getIndex(iCode, pc + 1);
        presentLines.put(line, 0);
      }
      pc += span;
    }
    
    return presentLines.getKeys();
  }
  
  public void captureStackInfo(RhinoException ex) {
    Context cx = Context.getCurrentContext();
    if ((cx == null) || (lastInterpreterFrame == null))
    {
      interpreterStackInfo = null;
      interpreterLineData = null; return;
    }
    
    CallFrame[] array;
    CallFrame[] array;
    if ((previousInterpreterInvocations == null) || 
      (previousInterpreterInvocations.size() == 0)) {
      array = new CallFrame[1];
    } else {
      int previousCount = previousInterpreterInvocations.size();
      
      if (previousInterpreterInvocations.peek() == lastInterpreterFrame)
      {



        previousCount--;
      }
      array = new CallFrame[previousCount + 1];
      previousInterpreterInvocations.toArray(array);
    }
    array[(array.length - 1)] = ((CallFrame)lastInterpreterFrame);
    
    int interpreterFrameCount = 0;
    for (int i = 0; i != array.length; i++) {
      interpreterFrameCount += 1 + frameIndex;
    }
    
    int[] linePC = new int[interpreterFrameCount];
    

    int linePCIndex = interpreterFrameCount;
    for (int i = array.length; i != 0;) {
      i--;
      CallFrame frame = array[i];
      while (frame != null) {
        linePCIndex--;
        linePC[linePCIndex] = pcSourceLineStart;
        frame = parentFrame;
      }
    }
    if (linePCIndex != 0) {
      Kit.codeBug();
    }
    interpreterStackInfo = array;
    interpreterLineData = linePC;
  }
  
  public String getSourcePositionFromStack(Context cx, int[] linep) {
    CallFrame frame = (CallFrame)lastInterpreterFrame;
    InterpreterData idata = idata;
    if (pcSourceLineStart >= 0) {
      linep[0] = getIndex(itsICode, pcSourceLineStart);
    } else {
      linep[0] = 0;
    }
    return itsSourceFile;
  }
  
  public String getPatchedStack(RhinoException ex, String nativeStackTrace) {
    String tag = "net.sourceforge.htmlunit.corejs.javascript.Interpreter.interpretLoop";
    StringBuilder sb = new StringBuilder(nativeStackTrace.length() + 1000);
    
    String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
    
    CallFrame[] array = (CallFrame[])interpreterStackInfo;
    int[] linePC = interpreterLineData;
    int arrayIndex = array.length;
    int linePCIndex = linePC.length;
    int offset = 0;
    while (arrayIndex != 0) {
      arrayIndex--;
      int pos = nativeStackTrace.indexOf(tag, offset);
      if (pos < 0) {
        break;
      }
      

      pos += tag.length();
      for (; 
          pos != nativeStackTrace.length(); pos++) {
        char c = nativeStackTrace.charAt(pos);
        if ((c == '\n') || (c == '\r')) {
          break;
        }
      }
      sb.append(nativeStackTrace.substring(offset, pos));
      offset = pos;
      
      CallFrame frame = array[arrayIndex];
      while (frame != null) {
        if (linePCIndex == 0)
          Kit.codeBug();
        linePCIndex--;
        InterpreterData idata = idata;
        sb.append(lineSeparator);
        sb.append("\tat script");
        if ((itsName != null) && (itsName.length() != 0)) {
          sb.append('.');
          sb.append(itsName);
        }
        sb.append('(');
        sb.append(itsSourceFile);
        int pc = linePC[linePCIndex];
        if (pc >= 0)
        {
          sb.append(':');
          sb.append(getIndex(itsICode, pc));
        }
        sb.append(')');
        frame = parentFrame;
      }
    }
    sb.append(nativeStackTrace.substring(offset));
    
    return sb.toString();
  }
  
  public List<String> getScriptStack(RhinoException ex) {
    ScriptStackElement[][] stack = getScriptStackElements(ex);
    List<String> list = new ArrayList(stack.length);
    
    String lineSeparator = SecurityUtilities.getSystemProperty("line.separator");
    for (ScriptStackElement[] group : stack) {
      StringBuilder sb = new StringBuilder();
      for (ScriptStackElement elem : group) {
        elem.renderJavaStyle(sb);
        sb.append(lineSeparator);
      }
      list.add(sb.toString());
    }
    return list;
  }
  
  public ScriptStackElement[][] getScriptStackElements(RhinoException ex) {
    if (interpreterStackInfo == null) {
      return (ScriptStackElement[][])null;
    }
    
    List<ScriptStackElement[]> list = new ArrayList();
    
    CallFrame[] array = (CallFrame[])interpreterStackInfo;
    int[] linePC = interpreterLineData;
    int arrayIndex = array.length;
    int linePCIndex = linePC.length;
    while (arrayIndex != 0) {
      arrayIndex--;
      CallFrame frame = array[arrayIndex];
      List<ScriptStackElement> group = new ArrayList();
      while (frame != null) {
        if (linePCIndex == 0)
          Kit.codeBug();
        linePCIndex--;
        InterpreterData idata = idata;
        String fileName = itsSourceFile;
        String functionName = null;
        int lineNumber = -1;
        int pc = linePC[linePCIndex];
        if (pc >= 0) {
          lineNumber = getIndex(itsICode, pc);
        }
        if ((itsName != null) && (itsName.length() != 0)) {
          functionName = itsName;
        }
        frame = parentFrame;
        group.add(new ScriptStackElement(fileName, functionName, lineNumber));
      }
      
      list.add(group.toArray(new ScriptStackElement[group.size()]));
    }
    return (ScriptStackElement[][])list.toArray(new ScriptStackElement[list.size()][]);
  }
  
  static String getEncodedSource(InterpreterData idata) {
    if (encodedSource == null) {
      return null;
    }
    return encodedSource.substring(encodedSourceStart, encodedSourceEnd);
  }
  


  private static void initFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index)
  {
    InterpretedFunction fn = InterpretedFunction.createFunction(cx, scope, parent, index);
    ScriptRuntime.initFunction(cx, scope, fn, idata.itsFunctionType, idata.evalScriptFlag);
  }
  

  static Object interpret(InterpretedFunction ifun, Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (!ScriptRuntime.hasTopCall(cx)) {
      Kit.codeBug();
    }
    if (interpreterSecurityDomain != securityDomain) {
      Object savedDomain = interpreterSecurityDomain;
      interpreterSecurityDomain = securityDomain;
      try {
        return securityController.callWithDomain(securityDomain, cx, ifun, scope, thisObj, args);
      }
      finally {
        interpreterSecurityDomain = savedDomain;
      }
    }
    
    CallFrame frame = new CallFrame(null);
    initFrame(cx, scope, thisObj, args, null, 0, args.length, ifun, null, frame);
    
    isContinuationsTopFrame = isContinuationsTopCall;
    isContinuationsTopCall = false;
    
    return interpretLoop(cx, frame, null);
  }
  
  static class GeneratorState { int operation;
    
    GeneratorState(int operation, Object value) { this.operation = operation;
      this.value = value;
    }
    

    Object value;
    RuntimeException returnedException;
  }
  
  public static Object resumeGenerator(Context cx, Scriptable scope, int operation, Object savedState, Object value)
  {
    CallFrame frame = (CallFrame)savedState;
    GeneratorState generatorState = new GeneratorState(operation, value);
    if (operation == 2)
      try {
        return interpretLoop(cx, frame, generatorState);
      }
      catch (RuntimeException e) {
        if (e != value) {
          throw e;
        }
        return Undefined.instance;
      }
    Object result = interpretLoop(cx, frame, generatorState);
    if (returnedException != null)
      throw returnedException;
    return result;
  }
  
  public static Object restartContinuation(NativeContinuation c, Context cx, Scriptable scope, Object[] args)
  {
    if (!ScriptRuntime.hasTopCall(cx)) {
      return ScriptRuntime.doTopCall(c, cx, scope, null, args);
    }
    Object arg;
    Object arg;
    if (args.length == 0) {
      arg = Undefined.instance;
    } else {
      arg = args[0];
    }
    
    CallFrame capturedFrame = (CallFrame)c.getImplementation();
    if (capturedFrame == null)
    {
      return arg;
    }
    
    ContinuationJump cjump = new ContinuationJump(c, null);
    
    result = arg;
    return interpretLoop(cx, null, cjump);
  }
  




  private static Object interpretLoop(Context cx, CallFrame frame, Object throwable)
  {
    Object DBL_MRK = UniqueTag.DOUBLE_MARK;
    Object undefined = Undefined.instance;
    
    boolean instructionCounting = instructionThreshold != 0;
    

    int INVOCATION_COST = 100;
    
    int EXCEPTION_COST = 100;
    
    String stringReg = null;
    int indexReg = -1;
    
    if (lastInterpreterFrame != null)
    {

      if (previousInterpreterInvocations == null) {
        previousInterpreterInvocations = new ObjArray();
      }
      previousInterpreterInvocations.push(lastInterpreterFrame);
    }
    







    GeneratorState generatorState = null;
    if (throwable != null) {
      if ((throwable instanceof GeneratorState)) {
        generatorState = (GeneratorState)throwable;
        

        enterFrame(cx, frame, ScriptRuntime.emptyArgs, true);
        throwable = null;
      } else if (!(throwable instanceof ContinuationJump))
      {
        Kit.codeBug();
      }
    }
    
    Object interpreterResult = null;
    double interpreterResultDbl = 0.0D;
    try
    {
      for (;;)
      {
        if (throwable != null)
        {


          frame = processThrowable(cx, throwable, frame, indexReg, instructionCounting);
          
          throwable = throwable;
          throwable = null;
        }
        else if ((generatorState == null) && (frozen)) {
          Kit.codeBug();
        }
        


        Object[] stack = stack;
        double[] sDbl = sDbl;
        Object[] vars = varSource.stack;
        double[] varDbls = varSource.sDbl;
        int[] varAttributes = varSource.stackAttributes;
        byte[] iCode = idata.itsICode;
        String[] strings = idata.itsStringTable;
        




        int stackTop = savedStackTop;
        

        lastInterpreterFrame = frame;
        



        for (;;)
        {
          int op = iCode[(pc++)];
          


          switch (op) {
          case -62: 
            if (!frozen)
            {


              pc -= 1;
              
              CallFrame generatorFrame = captureFrameForGenerator(frame);
              
              frozen = true;
              NativeGenerator generator = new NativeGenerator(scope, fnOrScript, generatorFrame);
              

              result = generator; }
            break;
          





          case 72: 
            if (!frozen) {
              return freezeGenerator(cx, frame, stackTop, generatorState);
            }
            
            Object obj = thawGenerator(frame, stackTop, generatorState, op);
            
            if (obj != Scriptable.NOT_FOUND)
              throwable = obj;
            break;
          




          case -63: 
            frozen = true;
            int sourceLine = getIndex(iCode, pc);
            
            returnedException = new JavaScriptException(NativeIterator.getStopIterationObject(scope), idata.itsSourceFile, sourceLine);
            

            break;
          
          case 50: 
            Object value = stack[stackTop];
            if (value == DBL_MRK)
            {
              value = ScriptRuntime.wrapNumber(sDbl[stackTop]); }
            stackTop--;
            
            int sourceLine = getIndex(iCode, pc);
            throwable = new JavaScriptException(value, idata.itsSourceFile, sourceLine);
            
            break;
          
          case 51: 
            indexReg += localShift;
            throwable = stack[indexReg];
            break;
          
          case 14: 
          case 15: 
          case 16: 
          case 17: 
            stackTop = doCompare(frame, op, stack, sDbl, stackTop);
            
            break;
          
          case 52: 
          case 53: 
            stackTop = doInOrInstanceof(cx, op, stack, sDbl, stackTop);
            
            break;
          
          case 12: 
          case 13: 
            stackTop--;
            boolean valBln = doEquals(stack, sDbl, stackTop);
            valBln ^= op == 13;
            stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
            break;
          
          case 46: 
          case 47: 
            stackTop--;
            boolean valBln = doShallowEquals(stack, sDbl, stackTop);
            
            valBln ^= op == 47;
            stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
            break;
          
          case 7: 
            if (stack_boolean(frame, stackTop--))
              pc += 2;
            break;
          

          case 6: 
            if (!stack_boolean(frame, stackTop--))
              pc += 2;
            break;
          

          case -6: 
            if (!stack_boolean(frame, stackTop--)) {
              pc += 2;
            }
            else
              stack[(stackTop--)] = null;
            break;
          case 5: 
            break;
          case -23: 
            stackTop++;
            stack[stackTop] = DBL_MRK;
            sDbl[stackTop] = (pc + 2);
            break;
          case -24: 
            if (stackTop == emptyStackTop + 1)
            {

              indexReg += localShift;
              stack[indexReg] = stack[stackTop];
              sDbl[indexReg] = sDbl[stackTop];
              stackTop--;



            }
            else if (stackTop != emptyStackTop) {
              Kit.codeBug();
            }
            
            break;
          case -25: 
            if (instructionCounting) {
              addInstructionCount(cx, frame, 0);
            }
            indexReg += localShift;
            Object value = stack[indexReg];
            if (value != DBL_MRK)
            {

              throwable = value;
              
              break label5908;
            }
            pc = ((int)sDbl[indexReg]);
            if (instructionCounting) {
              pcPrevBranch = pc;
            }
            
            break;
          case -4: 
            stack[stackTop] = null;
            stackTop--;
            break;
          case -5: 
            result = stack[stackTop];
            resultDbl = sDbl[stackTop];
            stack[stackTop] = null;
            stackTop--;
            break;
          case -1: 
            stack[(stackTop + 1)] = stack[stackTop];
            sDbl[(stackTop + 1)] = sDbl[stackTop];
            stackTop++;
            break;
          case -2: 
            stack[(stackTop + 1)] = stack[(stackTop - 1)];
            sDbl[(stackTop + 1)] = sDbl[(stackTop - 1)];
            stack[(stackTop + 2)] = stack[stackTop];
            sDbl[(stackTop + 2)] = sDbl[stackTop];
            stackTop += 2;
            break;
          case -3: 
            Object o = stack[stackTop];
            stack[stackTop] = stack[(stackTop - 1)];
            stack[(stackTop - 1)] = o;
            double d = sDbl[stackTop];
            sDbl[stackTop] = sDbl[(stackTop - 1)];
            sDbl[(stackTop - 1)] = d;
            break;
          
          case 4: 
            result = stack[stackTop];
            resultDbl = sDbl[stackTop];
            stackTop--;
            break;
          case 64: 
            break;
          case -22: 
            result = undefined;
            break;
          case 27: 
            int rIntValue = stack_int32(frame, stackTop);
            stack[stackTop] = DBL_MRK;
            sDbl[stackTop] = (rIntValue ^ 0xFFFFFFFF);
            break;
          
          case 9: 
          case 10: 
          case 11: 
          case 18: 
          case 19: 
            stackTop = doBitOp(frame, op, stack, sDbl, stackTop);
            
            break;
          
          case 20: 
            double lDbl = stack_double(frame, stackTop - 1);
            int rIntValue = stack_int32(frame, stackTop) & 0x1F;
            stack[(--stackTop)] = DBL_MRK;
            sDbl[stackTop] = 
              (ScriptRuntime.toUint32(lDbl) >>> rIntValue);
            break;
          
          case 28: 
          case 29: 
            double rDbl = stack_double(frame, stackTop);
            stack[stackTop] = DBL_MRK;
            if (op == 29) {
              rDbl = -rDbl;
            }
            sDbl[stackTop] = rDbl;
            break;
          
          case 21: 
            stackTop--;
            doAdd(stack, sDbl, stackTop, cx);
            break;
          case 22: 
          case 23: 
          case 24: 
          case 25: 
            stackTop = doArithmetic(frame, op, stack, sDbl, stackTop);
            
            break;
          
          case 26: 
            stack[stackTop] = ScriptRuntime.wrapBoolean(
              !stack_boolean(frame, stackTop) ? 1 : false);
            break;
          case 49: 
            stack[(++stackTop)] = ScriptRuntime.bind(cx, scope, stringReg);
            
            break;
          case 8: 
          case 73: 
            Object rhs = stack[stackTop];
            if (rhs == DBL_MRK)
              rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stackTop--;
            Scriptable lhs = (Scriptable)stack[stackTop];
            stack[stackTop] = (op == 8 ? 
              ScriptRuntime.setName(lhs, rhs, cx, scope, stringReg) : 
              
              ScriptRuntime.strictSetName(lhs, rhs, cx, scope, stringReg));
            
            break;
          
          case -59: 
            Object rhs = stack[stackTop];
            if (rhs == DBL_MRK)
              rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stackTop--;
            Scriptable lhs = (Scriptable)stack[stackTop];
            stack[stackTop] = ScriptRuntime.setConst(lhs, rhs, cx, stringReg);
            
            break;
          
          case 0: 
          case 31: 
            stackTop = doDelName(cx, frame, op, stack, sDbl, stackTop);
            
            break;
          
          case 34: 
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stack[stackTop] = ScriptRuntime.getObjectPropNoWarn(lhs, stringReg, cx, scope);
            
            break;
          
          case 33: 
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stack[stackTop] = ScriptRuntime.getObjectProp(lhs, stringReg, cx, scope);
            
            break;
          
          case 35: 
            Object rhs = stack[stackTop];
            if (rhs == DBL_MRK)
              rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stackTop--;
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stack[stackTop] = ScriptRuntime.setObjectProp(lhs, stringReg, rhs, cx, scope);
            
            break;
          
          case -9: 
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stack[stackTop] = ScriptRuntime.propIncrDecr(lhs, stringReg, cx, scope, iCode[pc]);
            

            pc += 1;
            break;
          
          case 36: 
            stackTop = doGetElem(cx, frame, stack, sDbl, stackTop);
            
            break;
          
          case 37: 
            stackTop = doSetElem(cx, frame, stack, sDbl, stackTop);
            
            break;
          
          case -10: 
            stackTop = doElemIncDec(cx, frame, iCode, stack, sDbl, stackTop);
            
            break;
          
          case 67: 
            Ref ref = (Ref)stack[stackTop];
            stack[stackTop] = ScriptRuntime.refGet(ref, cx);
            break;
          
          case 68: 
            Object value = stack[stackTop];
            if (value == DBL_MRK)
            {
              value = ScriptRuntime.wrapNumber(sDbl[stackTop]); }
            stackTop--;
            Ref ref = (Ref)stack[stackTop];
            stack[stackTop] = ScriptRuntime.refSet(ref, value, cx, scope);
            
            break;
          
          case 69: 
            Ref ref = (Ref)stack[stackTop];
            stack[stackTop] = ScriptRuntime.refDel(ref, cx);
            break;
          
          case -11: 
            Ref ref = (Ref)stack[stackTop];
            stack[stackTop] = ScriptRuntime.refIncrDecr(ref, cx, scope, iCode[pc]);
            
            pc += 1;
            break;
          
          case 54: 
            stackTop++;
            indexReg += localShift;
            stack[stackTop] = stack[indexReg];
            sDbl[stackTop] = sDbl[indexReg];
            break;
          case -56: 
            indexReg += localShift;
            stack[indexReg] = null;
            break;
          
          case -15: 
            stackTop++;
            stack[stackTop] = 
              ScriptRuntime.getNameFunctionAndThis(stringReg, cx, scope);
            
            stackTop++;
            stack[stackTop] = 
              ScriptRuntime.lastStoredScriptable(cx);
            break;
          case -16: 
            Object obj = stack[stackTop];
            if (obj == DBL_MRK) {
              obj = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            
            stack[stackTop] = ScriptRuntime.getPropFunctionAndThis(obj, stringReg, cx, scope);
            
            stackTop++;
            stack[stackTop] = 
              ScriptRuntime.lastStoredScriptable(cx);
            break;
          
          case -17: 
            Object obj = stack[(stackTop - 1)];
            if (obj == DBL_MRK)
            {
              obj = ScriptRuntime.wrapNumber(sDbl[(stackTop - 1)]); }
            Object id = stack[stackTop];
            if (id == DBL_MRK) {
              id = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            stack[(stackTop - 1)] = ScriptRuntime.getElemFunctionAndThis(obj, id, cx, scope);
            
            stack[stackTop] = 
              ScriptRuntime.lastStoredScriptable(cx);
            break;
          
          case -18: 
            Object value = stack[stackTop];
            if (value == DBL_MRK)
            {
              value = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            stack[stackTop] = ScriptRuntime.getValueFunctionAndThis(value, cx);
            stackTop++;
            stack[stackTop] = 
              ScriptRuntime.lastStoredScriptable(cx);
            break;
          
          case -21: 
            if (instructionCounting) {
              instructionCount += 100;
            }
            stackTop = doCallSpecial(cx, frame, stack, sDbl, stackTop, iCode, indexReg);
            
            break;
          
          case -55: 
          case 38: 
          case 70: 
            if (instructionCounting) {
              instructionCount += 100;
            }
            


            stackTop -= 1 + indexReg;
            



            Callable fun = (Callable)stack[stackTop];
            Scriptable funThisObj = (Scriptable)stack[(stackTop + 1)];
            
            if (op == 70) {
              Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 2, indexReg);
              
              stack[stackTop] = ScriptRuntime.callRef(fun, funThisObj, outArgs, cx);
            }
            else
            {
              Scriptable calleeScope = scope;
              if (useActivation)
              {
                calleeScope = ScriptableObject.getTopLevelScope(scope);
              }
              if ((fun instanceof InterpretedFunction)) {
                InterpretedFunction ifun = (InterpretedFunction)fun;
                if (fnOrScript.securityDomain == securityDomain) {
                  CallFrame callParentFrame = frame;
                  CallFrame calleeFrame = new CallFrame(null);
                  if (op == -55)
                  {



























                    callParentFrame = parentFrame;
                    


                    exitFrame(cx, frame, null);
                  }
                  initFrame(cx, calleeScope, funThisObj, stack, sDbl, stackTop + 2, indexReg, ifun, callParentFrame, calleeFrame);
                  

                  if (op != -55) {
                    savedStackTop = stackTop;
                    savedCallOp = op;
                  }
                  frame = calleeFrame;
                  break;
                }
              }
              
              if ((fun instanceof NativeContinuation))
              {

                ContinuationJump cjump = new ContinuationJump((NativeContinuation)fun, frame);
                




                if (indexReg == 0) {
                  result = undefined;
                } else {
                  result = stack[(stackTop + 2)];
                  resultDbl = sDbl[(stackTop + 2)];
                }
                

                throwable = cjump;
                
                break label5908;
              }
              if ((fun instanceof IdFunctionObject)) {
                IdFunctionObject ifun = (IdFunctionObject)fun;
                
                if (NativeContinuation.isContinuationConstructor(ifun)) {
                  stack[stackTop] = captureContinuation(cx, parentFrame, false);
                  
                  continue;
                }
                



                if (BaseFunction.isApplyOrCall(ifun))
                {
                  Callable applyCallable = ScriptRuntime.getCallable(funThisObj);
                  if ((applyCallable instanceof InterpretedFunction)) {
                    InterpretedFunction iApplyCallable = (InterpretedFunction)applyCallable;
                    if (fnOrScript.securityDomain == securityDomain) {
                      frame = initFrameForApplyOrCall(cx, frame, indexReg, stack, sDbl, stackTop, op, calleeScope, ifun, iApplyCallable);
                      



                      break;
                    }
                  }
                }
              }
              



              if ((fun instanceof ScriptRuntime.NoSuchMethodShim))
              {
                ScriptRuntime.NoSuchMethodShim noSuchMethodShim = (ScriptRuntime.NoSuchMethodShim)fun;
                Callable noSuchMethodMethod = noSuchMethodMethod;
                

                if ((noSuchMethodMethod instanceof InterpretedFunction)) {
                  InterpretedFunction ifun = (InterpretedFunction)noSuchMethodMethod;
                  if (fnOrScript.securityDomain == securityDomain) {
                    frame = initFrameForNoSuchMethod(cx, frame, indexReg, stack, sDbl, stackTop, op, funThisObj, calleeScope, noSuchMethodShim, ifun);
                    



                    break;
                  }
                }
              }
              
              lastInterpreterFrame = frame;
              savedCallOp = op;
              savedStackTop = stackTop;
              stack[stackTop] = fun.call(cx, calleeScope, funThisObj, 
                getArgsArray(stack, sDbl, stackTop + 2, indexReg));
            }
            
            break;
          
          case 30: 
            if (instructionCounting) {
              instructionCount += 100;
            }
            

            stackTop -= indexReg;
            
            Object lhs = stack[stackTop];
            if ((lhs instanceof InterpretedFunction)) {
              InterpretedFunction f = (InterpretedFunction)lhs;
              if (fnOrScript.securityDomain == securityDomain) {
                Scriptable newInstance = f.createObject(cx, scope);
                
                CallFrame calleeFrame = new CallFrame(null);
                initFrame(cx, scope, newInstance, stack, sDbl, stackTop + 1, indexReg, f, frame, calleeFrame);
                


                stack[stackTop] = newInstance;
                savedStackTop = stackTop;
                savedCallOp = op;
                frame = calleeFrame;
                break;
              }
            }
            if (!(lhs instanceof Function)) {
              if (lhs == DBL_MRK)
              {
                lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]); }
              throw ScriptRuntime.notFunctionError(lhs);
            }
            Function fun = (Function)lhs;
            
            if ((fun instanceof IdFunctionObject)) {
              IdFunctionObject ifun = (IdFunctionObject)fun;
              
              if (NativeContinuation.isContinuationConstructor(ifun)) {
                stack[stackTop] = captureContinuation(cx, parentFrame, false);
                
                continue;
              }
            }
            
            Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 1, indexReg);
            
            stack[stackTop] = fun.construct(cx, scope, outArgs);
            
            break;
          
          case 32: 
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stack[stackTop] = ScriptRuntime.typeof(lhs);
            break;
          

          case -14: 
            stack[(++stackTop)] = ScriptRuntime.typeofName(scope, stringReg);
            break;
          case 41: 
            stack[(++stackTop)] = stringReg;
            break;
          case -27: 
            stackTop++;
            stack[stackTop] = DBL_MRK;
            sDbl[stackTop] = getShort(iCode, pc);
            pc += 2;
            break;
          case -28: 
            stackTop++;
            stack[stackTop] = DBL_MRK;
            sDbl[stackTop] = getInt(iCode, pc);
            pc += 4;
            break;
          case 40: 
            stackTop++;
            stack[stackTop] = DBL_MRK;
            sDbl[stackTop] = idata.itsDoubleTable[indexReg];
            break;
          case 39: 
            stack[(++stackTop)] = ScriptRuntime.name(cx, scope, stringReg);
            
            break;
          case -8: 
            stack[(++stackTop)] = ScriptRuntime.nameIncrDecr(scope, stringReg, cx, iCode[pc]);
            

            pc += 1;
            break;
          case -61: 
            indexReg = iCode[(pc++)];
          
          case 156: 
            stackTop = doSetConstVar(frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg);
            

            break;
          case -49: 
            indexReg = iCode[(pc++)];
          
          case 56: 
            stackTop = doSetVar(frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg);
            
            break;
          case -48: 
            indexReg = iCode[(pc++)];
          
          case 55: 
            stackTop = doGetVar(frame, stack, sDbl, stackTop, vars, varDbls, indexReg);
            
            break;
          case -7: 
            stackTop = doVarIncDec(cx, frame, stack, sDbl, stackTop, vars, varDbls, varAttributes, indexReg);
            

            break;
          
          case -51: 
            stackTop++;
            stack[stackTop] = DBL_MRK;
            sDbl[stackTop] = 0.0D;
            break;
          case -52: 
            stackTop++;
            stack[stackTop] = DBL_MRK;
            sDbl[stackTop] = 1.0D;
            break;
          case 42: 
            stack[(++stackTop)] = null;
            break;
          case 43: 
            stack[(++stackTop)] = thisObj;
            break;
          case 63: 
            stack[(++stackTop)] = fnOrScript;
            break;
          case 44: 
            stack[(++stackTop)] = Boolean.FALSE;
            break;
          case 45: 
            stack[(++stackTop)] = Boolean.TRUE;
            break;
          case -50: 
            stack[(++stackTop)] = undefined;
            break;
          case 2: 
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stackTop--;
            scope = ScriptRuntime.enterWith(lhs, cx, scope);
            
            break;
          
          case 3: 
            scope = ScriptRuntime.leaveWith(scope);
            break;
          


          case 57: 
            stackTop--;
            indexReg += localShift;
            
            boolean afterFirstScope = idata.itsICode[pc] != 0;
            Throwable caughtException = (Throwable)stack[(stackTop + 1)];
            Scriptable lastCatchScope;
            Scriptable lastCatchScope;
            if (!afterFirstScope) {
              lastCatchScope = null;
            } else {
              lastCatchScope = (Scriptable)stack[indexReg];
            }
            stack[indexReg] = ScriptRuntime.newCatchScope(caughtException, lastCatchScope, stringReg, cx, scope);
            

            pc += 1;
            break;
          
          case 58: 
          case 59: 
          case 60: 
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stackTop--;
            indexReg += localShift;
            int enumType = op == 59 ? 1 : op == 58 ? 0 : 2;
            



            stack[indexReg] = ScriptRuntime.enumInit(lhs, cx, scope, enumType);
            
            break;
          
          case 61: 
          case 62: 
            indexReg += localShift;
            Object val = stack[indexReg];
            stackTop++;
            stack[stackTop] = (op == 61 ? 
              ScriptRuntime.enumNext(val) : 
              ScriptRuntime.enumId(val, cx));
            break;
          

          case 71: 
            Object obj = stack[stackTop];
            if (obj == DBL_MRK)
              obj = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stack[stackTop] = ScriptRuntime.specialRef(obj, stringReg, cx, scope);
            
            break;
          

          case 77: 
            stackTop = doRefMember(cx, stack, sDbl, stackTop, indexReg);
            
            break;
          

          case 78: 
            stackTop = doRefNsMember(cx, stack, sDbl, stackTop, indexReg);
            
            break;
          

          case 79: 
            Object name = stack[stackTop];
            if (name == DBL_MRK)
              name = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stack[stackTop] = ScriptRuntime.nameRef(name, cx, scope, indexReg);
            
            break;
          

          case 80: 
            stackTop = doRefNsName(cx, frame, stack, sDbl, stackTop, indexReg);
            
            break;
          
          case -12: 
            indexReg += localShift;
            scope = ((Scriptable)stack[indexReg]);
            break;
          case -13: 
            indexReg += localShift;
            stack[indexReg] = scope;
            break;
          
          case -19: 
            stack[(++stackTop)] = InterpretedFunction.createFunction(cx, scope, fnOrScript, indexReg);
            
            break;
          case -20: 
            initFunction(cx, scope, fnOrScript, indexReg);
            
            break;
          case 48: 
            Object re = idata.itsRegExpLiterals[indexReg];
            stack[(++stackTop)] = ScriptRuntime.wrapRegExp(cx, scope, re);
            
            break;
          
          case -29: 
            stackTop++;
            stack[stackTop] = new int[indexReg];
            stackTop++;
            stack[stackTop] = new Object[indexReg];
            sDbl[stackTop] = 0.0D;
            break;
          case -30: 
            Object value = stack[stackTop];
            if (value == DBL_MRK)
            {
              value = ScriptRuntime.wrapNumber(sDbl[stackTop]); }
            stackTop--;
            int i = (int)sDbl[stackTop];
            ((Object[])stack[stackTop])[i] = value;
            sDbl[stackTop] = (i + 1);
            break;
          
          case -57: 
            Object value = stack[stackTop];
            stackTop--;
            int i = (int)sDbl[stackTop];
            ((Object[])stack[stackTop])[i] = value;
            ((int[])stack[(stackTop - 1)])[i] = -1;
            sDbl[stackTop] = (i + 1);
            break;
          
          case -58: 
            Object value = stack[stackTop];
            stackTop--;
            int i = (int)sDbl[stackTop];
            ((Object[])stack[stackTop])[i] = value;
            ((int[])stack[(stackTop - 1)])[i] = 1;
            sDbl[stackTop] = (i + 1);
            break;
          
          case -31: 
          case 65: 
          case 66: 
            Object[] data = (Object[])stack[stackTop];
            stackTop--;
            int[] getterSetters = (int[])stack[stackTop];
            Object val;
            Object val; if (op == 66) {
              Object[] ids = (Object[])idata.literalIds[indexReg];
              val = ScriptRuntime.newObjectLiteral(ids, data, getterSetters, cx, scope);
            }
            else {
              int[] skipIndexces = null;
              if (op == -31) {
                skipIndexces = (int[])idata.literalIds[indexReg];
              }
              val = ScriptRuntime.newArrayLiteral(data, skipIndexces, cx, scope);
            }
            
            stack[stackTop] = val;
            break;
          
          case -53: 
            Object lhs = stack[stackTop];
            if (lhs == DBL_MRK)
              lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            stackTop--;
            scope = ScriptRuntime.enterDotQuery(lhs, scope);
            
            break;
          
          case -54: 
            boolean valBln = stack_boolean(frame, stackTop);
            Object x = ScriptRuntime.updateDotQuery(valBln, scope);
            
            if (x != null) {
              stack[stackTop] = x;
              
              scope = ScriptRuntime.leaveDotQuery(scope);
              pc += 2;
            }
            else
            {
              stackTop--; }
            break;
          
          case 74: 
            Object value = stack[stackTop];
            if (value == DBL_MRK)
            {
              value = ScriptRuntime.wrapNumber(sDbl[stackTop]);
            }
            stack[stackTop] = ScriptRuntime.setDefaultNamespace(value, cx);
            break;
          
          case 75: 
            Object value = stack[stackTop];
            if (value != DBL_MRK)
            {
              stack[stackTop] = ScriptRuntime.escapeAttributeValue(value, cx);
            }
            
            break;
          case 76: 
            Object value = stack[stackTop];
            if (value != DBL_MRK)
            {
              stack[stackTop] = ScriptRuntime.escapeTextValue(value, cx);
            }
            
            break;
          case -64: 
            if (debuggerFrame != null) {
              debuggerFrame.onDebuggerStatement(cx);
            }
            break;
          case -26: 
            pcSourceLineStart = pc;
            if (debuggerFrame != null) {
              int line = getIndex(iCode, pc);
              debuggerFrame.onLineChange(cx, line);
            }
            pc += 2;
            break;
          case -32: 
            indexReg = 0;
            break;
          case -33: 
            indexReg = 1;
            break;
          case -34: 
            indexReg = 2;
            break;
          case -35: 
            indexReg = 3;
            break;
          case -36: 
            indexReg = 4;
            break;
          case -37: 
            indexReg = 5;
            break;
          case -38: 
            indexReg = 0xFF & iCode[pc];
            pc += 1;
            break;
          case -39: 
            indexReg = getIndex(iCode, pc);
            pc += 2;
            break;
          case -40: 
            indexReg = getInt(iCode, pc);
            pc += 4;
            break;
          case -41: 
            stringReg = strings[0];
            break;
          case -42: 
            stringReg = strings[1];
            break;
          case -43: 
            stringReg = strings[2];
            break;
          case -44: 
            stringReg = strings[3];
            break;
          case -45: 
            stringReg = strings[(0xFF & iCode[pc])];
            pc += 1;
            break;
          case -46: 
            stringReg = strings[getIndex(iCode, pc)];
            pc += 2;
            break;
          case -47: 
            stringReg = strings[getInt(iCode, pc)];
            pc += 4;
            break;
          case -60: case 1: case 81: case 82: case 83: case 84: case 85: case 86: case 87: case 88: case 89: case 90: case 91: case 92: case 93: case 94: case 95: case 96: case 97: case 98: case 99: case 100: case 101: case 102: case 103: case 104: case 105: case 106: case 107: case 108: case 109: case 110: case 111: case 112: case 113: case 114: case 115: case 116: case 117: case 118: case 119: case 120: case 121: case 122: case 123: case 124: case 125: case 126: case 127: case 128: case 129: case 130: case 131: case 132: case 133: case 134: case 135: case 136: case 137: case 138: case 139: case 140: case 141: case 142: case 143: case 144: case 145: case 146: case 147: case 148: case 149: case 150: case 151: case 152: case 153: case 154: case 155: default: 
            dumpICode(idata);
            throw new RuntimeException("Unknown icode : " + op + " @ pc : " + (pc - 1));
            






            if (instructionCounting) {
              addInstructionCount(cx, frame, 2);
            }
            int offset = getShort(iCode, pc);
            if (offset != 0)
            {
              pc += offset - 1;
            }
            else {
              pc = idata.longJumps.getExistingInt(pc);
            }
            if (instructionCounting) {
              pcPrevBranch = pc;
            }
            break;
          }
          
        }
        exitFrame(cx, frame, null);
        interpreterResult = result;
        interpreterResultDbl = resultDbl;
        if (parentFrame == null) break;
        frame = parentFrame;
        if (frozen) {
          frame = frame.cloneFrozen();
        }
        setCallResult(frame, interpreterResult, interpreterResultDbl);
        
        interpreterResult = null;
      }
    }
    catch (Throwable ex) {
      label5908:
      ContinuationJump cjump;
      for (;;) {
        if (throwable != null)
        {
          ex.printStackTrace(System.err);
          throw new IllegalStateException();
        }
        throwable = ex;
        




        if (throwable == null) {
          Kit.codeBug();
        }
        
        int EX_CATCH_STATE = 2;
        int EX_FINALLY_STATE = 1;
        int EX_NO_JS_STATE = 0;
        

        cjump = null;
        int exState;
        int exState; if ((generatorState != null) && (operation == 2) && (throwable == value))
        {

          exState = 1; } else { int exState;
          if ((throwable instanceof JavaScriptException)) {
            exState = 2; } else { int exState;
            if ((throwable instanceof EcmaError))
            {
              exState = 2; } else { int exState;
              if ((throwable instanceof EvaluatorException)) {
                exState = 2; } else { int exState;
                if ((throwable instanceof ContinuationPending)) {
                  exState = 0; } else { int exState;
                  if ((throwable instanceof RuntimeException)) {
                    exState = cx.hasFeature(13) ? 2 : 1;
                  } else { int exState;
                    if ((throwable instanceof Error)) {
                      exState = cx.hasFeature(13) ? 2 : 0;
                    }
                    else if ((throwable instanceof ContinuationJump))
                    {
                      int exState = 1;
                      cjump = (ContinuationJump)throwable;
                    } else {
                      exState = cx.hasFeature(13) ? 2 : 1;
                    }
                  }
                } } } } }
        if (instructionCounting) {
          try {
            addInstructionCount(cx, frame, 100);
          } catch (RuntimeException ex) {
            throwable = ex;
            exState = 1;
          }
          catch (Error ex)
          {
            throwable = ex;
            cjump = null;
            exState = 0;
          }
        }
        if ((debuggerFrame != null) && ((throwable instanceof RuntimeException)))
        {

          RuntimeException rex = (RuntimeException)throwable;
          try {
            debuggerFrame.onExceptionThrown(cx, rex);
          }
          catch (Throwable ex)
          {
            throwable = ex;
            cjump = null;
            exState = 0;
          }
        }
        do
        {
          if (exState != 0) {
            boolean onlyFinally = exState != 2;
            indexReg = getExceptionHandler(frame, onlyFinally);
            if (indexReg >= 0) {
              break;
            }
          }
          





          exitFrame(cx, frame, throwable);
          
          frame = parentFrame;
          if (frame == null) {
            break label6251;
          }
        } while ((cjump == null) || (branchFrame != frame));
        

        indexReg = -1;
        continue;
        

        label6251:
        
        if (cjump == null) break label6301;
        if (branchFrame != null)
        {
          Kit.codeBug();
        }
        if (capturedFrame == null)
          break;
        indexReg = -1;
      }
      

      interpreterResult = result;
      interpreterResultDbl = resultDbl;
      throwable = null;
    }
    


    label6301:
    

    if ((previousInterpreterInvocations != null) && 
      (previousInterpreterInvocations.size() != 0)) {
      lastInterpreterFrame = previousInterpreterInvocations.pop();
    }
    else {
      lastInterpreterFrame = null;
      
      previousInterpreterInvocations = null;
    }
    
    if (throwable != null) {
      if ((throwable instanceof RuntimeException)) {
        throw ((RuntimeException)throwable);
      }
      
      throw ((Error)throwable);
    }
    

    return interpreterResult != DBL_MRK ? interpreterResult : 
      ScriptRuntime.wrapNumber(interpreterResultDbl);
  }
  
  private static int doInOrInstanceof(Context cx, int op, Object[] stack, double[] sDbl, int stackTop)
  {
    Object rhs = stack[stackTop];
    if (rhs == UniqueTag.DOUBLE_MARK)
      rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stackTop--;
    Object lhs = stack[stackTop];
    if (lhs == UniqueTag.DOUBLE_MARK)
      lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    boolean valBln;
    boolean valBln; if (op == 52) {
      valBln = ScriptRuntime.in(lhs, rhs, cx);
    } else {
      valBln = ScriptRuntime.instanceOf(lhs, rhs, cx);
    }
    stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
    return stackTop;
  }
  
  private static int doCompare(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop)
  {
    stackTop--;
    Object rhs = stack[(stackTop + 1)];
    Object lhs = stack[stackTop];
    
    double lDbl;
    double rDbl;
    double lDbl;
    if (rhs == UniqueTag.DOUBLE_MARK) {
      double rDbl = sDbl[(stackTop + 1)];
      lDbl = stack_double(frame, stackTop);
    } else { if (lhs != UniqueTag.DOUBLE_MARK) break label172;
      rDbl = ScriptRuntime.toNumber(rhs);
      lDbl = sDbl[stackTop]; }
    boolean valBln;
    boolean valBln;
    boolean valBln;
    boolean valBln; switch (op) {
    case 17: 
      valBln = lDbl >= rDbl;
      break;
    case 15: 
      valBln = lDbl <= rDbl;
      break;
    case 16: 
      valBln = lDbl > rDbl;
      break;
    case 14: 
      valBln = lDbl < rDbl;
      break;
    default: 
      throw Kit.codeBug(); }
    label172:
    boolean valBln;
    boolean valBln; boolean valBln; boolean valBln; switch (op) {
    case 17: 
      valBln = ScriptRuntime.cmp_LE(rhs, lhs);
      break;
    case 15: 
      valBln = ScriptRuntime.cmp_LE(lhs, rhs);
      break;
    case 16: 
      valBln = ScriptRuntime.cmp_LT(rhs, lhs);
      break;
    case 14: 
      valBln = ScriptRuntime.cmp_LT(lhs, rhs);
      break;
    default: 
      throw Kit.codeBug();
    }
    boolean valBln;
    stack[stackTop] = ScriptRuntime.wrapBoolean(valBln);
    return stackTop;
  }
  
  private static int doBitOp(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop)
  {
    int lIntValue = stack_int32(frame, stackTop - 1);
    int rIntValue = stack_int32(frame, stackTop);
    stack[(--stackTop)] = UniqueTag.DOUBLE_MARK;
    switch (op) {
    case 11: 
      lIntValue &= rIntValue;
      break;
    case 9: 
      lIntValue |= rIntValue;
      break;
    case 10: 
      lIntValue ^= rIntValue;
      break;
    case 18: 
      lIntValue <<= rIntValue;
      break;
    case 19: 
      lIntValue >>= rIntValue;
    }
    
    sDbl[stackTop] = lIntValue;
    return stackTop;
  }
  
  private static int doDelName(Context cx, CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop)
  {
    Object rhs = stack[stackTop];
    if (rhs == UniqueTag.DOUBLE_MARK)
      rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stackTop--;
    Object lhs = stack[stackTop];
    if (lhs == UniqueTag.DOUBLE_MARK)
      lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stack[stackTop] = ScriptRuntime.delete(lhs, rhs, cx, scope, op == 0 ? 1 : false);
    
    return stackTop;
  }
  
  private static int doGetElem(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop)
  {
    stackTop--;
    Object lhs = stack[stackTop];
    if (lhs == UniqueTag.DOUBLE_MARK) {
      lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    }
    
    Object id = stack[(stackTop + 1)];
    Object value; Object value; if (id != UniqueTag.DOUBLE_MARK) {
      value = ScriptRuntime.getObjectElem(lhs, id, cx, scope);
    } else {
      double d = sDbl[(stackTop + 1)];
      value = ScriptRuntime.getObjectIndex(lhs, d, cx, scope);
    }
    stack[stackTop] = value;
    return stackTop;
  }
  
  private static int doSetElem(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop)
  {
    stackTop -= 2;
    Object rhs = stack[(stackTop + 2)];
    if (rhs == UniqueTag.DOUBLE_MARK) {
      rhs = ScriptRuntime.wrapNumber(sDbl[(stackTop + 2)]);
    }
    Object lhs = stack[stackTop];
    if (lhs == UniqueTag.DOUBLE_MARK) {
      lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    }
    
    Object id = stack[(stackTop + 1)];
    Object value; Object value; if (id != UniqueTag.DOUBLE_MARK) {
      value = ScriptRuntime.setObjectElem(lhs, id, rhs, cx, scope);
    } else {
      double d = sDbl[(stackTop + 1)];
      value = ScriptRuntime.setObjectIndex(lhs, d, rhs, cx, scope);
    }
    stack[stackTop] = value;
    return stackTop;
  }
  
  private static int doElemIncDec(Context cx, CallFrame frame, byte[] iCode, Object[] stack, double[] sDbl, int stackTop)
  {
    Object rhs = stack[stackTop];
    if (rhs == UniqueTag.DOUBLE_MARK)
      rhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stackTop--;
    Object lhs = stack[stackTop];
    if (lhs == UniqueTag.DOUBLE_MARK)
      lhs = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stack[stackTop] = ScriptRuntime.elemIncrDecr(lhs, rhs, cx, scope, iCode[pc]);
    
    pc += 1;
    return stackTop;
  }
  

  private static int doCallSpecial(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, byte[] iCode, int indexReg)
  {
    int callType = iCode[pc] & 0xFF;
    boolean isNew = iCode[(pc + 1)] != 0;
    int sourceLine = getIndex(iCode, pc + 2);
    

    if (isNew)
    {
      stackTop -= indexReg;
      
      Object function = stack[stackTop];
      if (function == UniqueTag.DOUBLE_MARK)
        function = ScriptRuntime.wrapNumber(sDbl[stackTop]);
      Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 1, indexReg);
      
      stack[stackTop] = ScriptRuntime.newSpecial(cx, function, outArgs, scope, callType);
    }
    else
    {
      stackTop -= 1 + indexReg;
      


      Scriptable functionThis = (Scriptable)stack[(stackTop + 1)];
      Callable function = (Callable)stack[stackTop];
      Object[] outArgs = getArgsArray(stack, sDbl, stackTop + 2, indexReg);
      
      stack[stackTop] = ScriptRuntime.callSpecial(cx, function, functionThis, outArgs, scope, thisObj, callType, idata.itsSourceFile, sourceLine);
    }
    

    pc += 4;
    return stackTop;
  }
  

  private static int doSetConstVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg)
  {
    if (!useActivation) {
      if ((varAttributes[indexReg] & 0x1) == 0) {
        throw Context.reportRuntimeError1("msg.var.redecl", idata.argNames[indexReg]);
      }
      
      if ((varAttributes[indexReg] & 0x8) != 0)
      {
        vars[indexReg] = stack[stackTop];
        varAttributes[indexReg] &= 0xFFFFFFF7;
        varDbls[indexReg] = sDbl[stackTop];
      }
    } else {
      Object val = stack[stackTop];
      if (val == UniqueTag.DOUBLE_MARK)
        val = ScriptRuntime.wrapNumber(sDbl[stackTop]);
      String stringReg = idata.argNames[indexReg];
      if ((scope instanceof ConstProperties)) {
        ConstProperties cp = (ConstProperties)scope;
        cp.putConst(stringReg, scope, val);
      } else {
        throw Kit.codeBug();
      } }
    return stackTop;
  }
  

  private static int doSetVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg)
  {
    if (!useActivation) {
      if ((varAttributes[indexReg] & 0x1) == 0) {
        vars[indexReg] = stack[stackTop];
        varDbls[indexReg] = sDbl[stackTop];
      }
    } else {
      Object val = stack[stackTop];
      if (val == UniqueTag.DOUBLE_MARK)
        val = ScriptRuntime.wrapNumber(sDbl[stackTop]);
      String stringReg = idata.argNames[indexReg];
      scope.put(stringReg, scope, val);
    }
    return stackTop;
  }
  
  private static int doGetVar(CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int indexReg)
  {
    
    if (!useActivation) {
      stack[stackTop] = vars[indexReg];
      sDbl[stackTop] = varDbls[indexReg];
    } else {
      String stringReg = idata.argNames[indexReg];
      stack[stackTop] = scope.get(stringReg, scope);
    }
    return stackTop;
  }
  


  private static int doVarIncDec(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, Object[] vars, double[] varDbls, int[] varAttributes, int indexReg)
  {
    stackTop++;
    int incrDecrMask = idata.itsICode[pc];
    if (!useActivation) {
      Object varValue = vars[indexReg];
      double d;
      double d; if (varValue == UniqueTag.DOUBLE_MARK) {
        d = varDbls[indexReg];
      } else {
        d = ScriptRuntime.toNumber(varValue);
      }
      double d2 = (incrDecrMask & 0x1) == 0 ? d + 1.0D : d - 1.0D;
      
      boolean post = (incrDecrMask & 0x2) != 0;
      if ((varAttributes[indexReg] & 0x1) == 0) {
        if (varValue != UniqueTag.DOUBLE_MARK) {
          vars[indexReg] = UniqueTag.DOUBLE_MARK;
        }
        varDbls[indexReg] = d2;
        stack[stackTop] = UniqueTag.DOUBLE_MARK;
        sDbl[stackTop] = (post ? d : d2);
      }
      else if ((post) && (varValue != UniqueTag.DOUBLE_MARK)) {
        stack[stackTop] = varValue;
      } else {
        stack[stackTop] = UniqueTag.DOUBLE_MARK;
        sDbl[stackTop] = (post ? d : d2);
      }
    }
    else {
      String varName = idata.argNames[indexReg];
      stack[stackTop] = ScriptRuntime.nameIncrDecr(scope, varName, cx, incrDecrMask);
    }
    
    pc += 1;
    return stackTop;
  }
  
  private static int doRefMember(Context cx, Object[] stack, double[] sDbl, int stackTop, int flags)
  {
    Object elem = stack[stackTop];
    if (elem == UniqueTag.DOUBLE_MARK)
      elem = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stackTop--;
    Object obj = stack[stackTop];
    if (obj == UniqueTag.DOUBLE_MARK)
      obj = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stack[stackTop] = ScriptRuntime.memberRef(obj, elem, cx, flags);
    return stackTop;
  }
  
  private static int doRefNsMember(Context cx, Object[] stack, double[] sDbl, int stackTop, int flags)
  {
    Object elem = stack[stackTop];
    if (elem == UniqueTag.DOUBLE_MARK)
      elem = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stackTop--;
    Object ns = stack[stackTop];
    if (ns == UniqueTag.DOUBLE_MARK)
      ns = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stackTop--;
    Object obj = stack[stackTop];
    if (obj == UniqueTag.DOUBLE_MARK)
      obj = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stack[stackTop] = ScriptRuntime.memberRef(obj, ns, elem, cx, flags);
    return stackTop;
  }
  
  private static int doRefNsName(Context cx, CallFrame frame, Object[] stack, double[] sDbl, int stackTop, int flags)
  {
    Object name = stack[stackTop];
    if (name == UniqueTag.DOUBLE_MARK)
      name = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stackTop--;
    Object ns = stack[stackTop];
    if (ns == UniqueTag.DOUBLE_MARK)
      ns = ScriptRuntime.wrapNumber(sDbl[stackTop]);
    stack[stackTop] = ScriptRuntime.nameRef(ns, name, cx, scope, flags);
    
    return stackTop;
  }
  






  private static CallFrame initFrameForNoSuchMethod(Context cx, CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable funThisObj, Scriptable calleeScope, ScriptRuntime.NoSuchMethodShim noSuchMethodShim, InterpretedFunction ifun)
  {
    Object[] argsArray = null;
    

    int shift = stackTop + 2;
    Object[] elements = new Object[indexReg];
    for (int i = 0; i < indexReg; shift++) {
      Object val = stack[shift];
      if (val == UniqueTag.DOUBLE_MARK) {
        val = ScriptRuntime.wrapNumber(sDbl[shift]);
      }
      elements[i] = val;i++;
    }
    argsArray = new Object[2];
    argsArray[0] = methodName;
    argsArray[1] = cx.newArray(calleeScope, elements);
    

    CallFrame callParentFrame = frame;
    CallFrame calleeFrame = new CallFrame(null);
    if (op == -55) {
      callParentFrame = parentFrame;
      exitFrame(cx, frame, null);
    }
    

    initFrame(cx, calleeScope, funThisObj, argsArray, null, 0, 2, ifun, callParentFrame, calleeFrame);
    
    if (op != -55) {
      savedStackTop = stackTop;
      savedCallOp = op;
    }
    return calleeFrame;
  }
  
  private static boolean doEquals(Object[] stack, double[] sDbl, int stackTop)
  {
    Object rhs = stack[(stackTop + 1)];
    Object lhs = stack[stackTop];
    if (rhs == UniqueTag.DOUBLE_MARK) {
      if (lhs == UniqueTag.DOUBLE_MARK) {
        return sDbl[stackTop] == sDbl[(stackTop + 1)];
      }
      return ScriptRuntime.eqNumber(sDbl[(stackTop + 1)], lhs);
    }
    
    if (lhs == UniqueTag.DOUBLE_MARK) {
      return ScriptRuntime.eqNumber(sDbl[stackTop], rhs);
    }
    return ScriptRuntime.eq(lhs, rhs);
  }
  


  private static boolean doShallowEquals(Object[] stack, double[] sDbl, int stackTop)
  {
    Object rhs = stack[(stackTop + 1)];
    Object lhs = stack[stackTop];
    Object DBL_MRK = UniqueTag.DOUBLE_MARK;
    
    if (rhs == DBL_MRK) {
      double rdbl = sDbl[(stackTop + 1)];
      double ldbl; if (lhs == DBL_MRK) {
        ldbl = sDbl[stackTop]; } else { double ldbl;
        if ((lhs instanceof Number)) {
          ldbl = ((Number)lhs).doubleValue();
        } else
          return false;
      }
    } else if (lhs == DBL_MRK) {
      double ldbl = sDbl[stackTop];
      double rdbl; if ((rhs instanceof Number)) {
        rdbl = ((Number)rhs).doubleValue();
      } else {
        return false;
      }
    } else {
      return ScriptRuntime.shallowEq(lhs, rhs); }
    double ldbl;
    double rdbl; return ldbl == rdbl;
  }
  



  private static CallFrame processThrowable(Context cx, Object throwable, CallFrame frame, int indexReg, boolean instructionCounting)
  {
    if (indexReg >= 0)
    {


      if (frozen)
      {
        frame = frame.cloneFrozen();
      }
      
      int[] table = idata.itsExceptionTable;
      
      pc = table[(indexReg + 2)];
      if (instructionCounting) {
        pcPrevBranch = pc;
      }
      
      savedStackTop = emptyStackTop;
      int scopeLocal = localShift + table[(indexReg + 5)];
      
      int exLocal = localShift + table[(indexReg + 4)];
      
      scope = ((Scriptable)stack[scopeLocal]);
      stack[exLocal] = throwable;
      
      throwable = null;
    }
    else {
      ContinuationJump cjump = (ContinuationJump)throwable;
      

      throwable = null;
      
      if (branchFrame != frame) {
        Kit.codeBug();
      }
      


      if (capturedFrame == null) {
        Kit.codeBug();
      }
      

      int rewindCount = capturedFrame.frameIndex + 1;
      if (branchFrame != null) {
        rewindCount -= branchFrame.frameIndex;
      }
      
      int enterCount = 0;
      CallFrame[] enterFrames = null;
      
      CallFrame x = capturedFrame;
      for (int i = 0; i != rewindCount; i++) {
        if (!frozen)
          Kit.codeBug();
        if (isFrameEnterExitRequired(x)) {
          if (enterFrames == null)
          {


            enterFrames = new CallFrame[rewindCount - i];
          }
          enterFrames[enterCount] = x;
          enterCount++;
        }
        x = parentFrame;
      }
      
      while (enterCount != 0)
      {


        enterCount--;
        x = enterFrames[enterCount];
        enterFrame(cx, x, ScriptRuntime.emptyArgs, true);
      }
      




      frame = capturedFrame.cloneFrozen();
      setCallResult(frame, result, resultDbl);
    }
    
    throwable = throwable;
    return frame;
  }
  
  private static Object freezeGenerator(Context cx, CallFrame frame, int stackTop, GeneratorState generatorState)
  {
    if (operation == 2)
    {
      throw ScriptRuntime.typeError0("msg.yield.closing");
    }
    
    frozen = true;
    result = stack[stackTop];
    resultDbl = sDbl[stackTop];
    savedStackTop = stackTop;
    pc -= 1;
    ScriptRuntime.exitActivationFunction(cx);
    return result != UniqueTag.DOUBLE_MARK ? result : 
      ScriptRuntime.wrapNumber(resultDbl);
  }
  

  private static Object thawGenerator(CallFrame frame, int stackTop, GeneratorState generatorState, int op)
  {
    frozen = false;
    int sourceLine = getIndex(idata.itsICode, pc);
    pc += 2;
    if (operation == 1)
    {

      return new JavaScriptException(value, idata.itsSourceFile, sourceLine);
    }
    
    if (operation == 2) {
      return value;
    }
    if (operation != 0)
      throw Kit.codeBug();
    if (op == 72)
      stack[stackTop] = value;
    return Scriptable.NOT_FOUND;
  }
  

  private static CallFrame initFrameForApplyOrCall(Context cx, CallFrame frame, int indexReg, Object[] stack, double[] sDbl, int stackTop, int op, Scriptable calleeScope, IdFunctionObject ifun, InterpretedFunction iApplyCallable)
  {
    Scriptable applyThis;
    Scriptable applyThis;
    if (indexReg != 0) {
      Object obj = stack[(stackTop + 2)];
      if (obj == UniqueTag.DOUBLE_MARK)
        obj = ScriptRuntime.wrapNumber(sDbl[(stackTop + 2)]);
      applyThis = ScriptRuntime.toObjectOrNull(cx, obj, scope);
    } else {
      applyThis = null;
    }
    if (applyThis == null)
    {
      applyThis = ScriptRuntime.getTopCallScope(cx);
    }
    if (op == -55) {
      exitFrame(cx, frame, null);
      frame = parentFrame;
    } else {
      savedStackTop = stackTop;
      savedCallOp = op;
    }
    CallFrame calleeFrame = new CallFrame(null);
    if (BaseFunction.isApply(ifun))
    {
      Object[] callArgs = indexReg < 2 ? ScriptRuntime.emptyArgs : ScriptRuntime.getApplyArguments(cx, stack[(stackTop + 3)]);
      initFrame(cx, calleeScope, applyThis, callArgs, null, 0, callArgs.length, iApplyCallable, frame, calleeFrame);
    }
    else
    {
      for (int i = 1; i < indexReg; i++) {
        stack[(stackTop + 1 + i)] = stack[(stackTop + 2 + i)];
        sDbl[(stackTop + 1 + i)] = sDbl[(stackTop + 2 + i)];
      }
      int argCount = indexReg < 2 ? 0 : indexReg - 1;
      initFrame(cx, calleeScope, applyThis, stack, sDbl, stackTop + 2, argCount, iApplyCallable, frame, calleeFrame);
    }
    

    frame = calleeFrame;
    return frame;
  }
  


  private static void initFrame(Context cx, Scriptable callerScope, Scriptable thisObj, Object[] args, double[] argsDbl, int argShift, int argCount, InterpretedFunction fnOrScript, CallFrame parentFrame, CallFrame frame)
  {
    InterpreterData idata = idata;
    
    boolean useActivation = itsNeedsActivation;
    DebugFrame debuggerFrame = null;
    if (debugger != null) {
      debuggerFrame = debugger.getFrame(cx, idata);
      if (debuggerFrame != null) {
        useActivation = true;
      }
    }
    
    if (useActivation)
    {

      if (argsDbl != null) {
        args = getArgsArray(args, argsDbl, argShift, argCount);
      }
      argShift = 0;
      argsDbl = null;
    }
    
    Scriptable scope;
    if (itsFunctionType != 0) {
      Scriptable scope = fnOrScript.getParentScope();
      
      if (useActivation) {
        scope = ScriptRuntime.createFunctionActivation(fnOrScript, scope, args);
      }
    }
    else {
      scope = callerScope;
      ScriptRuntime.initScript(fnOrScript, thisObj, cx, scope, idata.evalScriptFlag);
    }
    

    if (itsNestedFunctions != null) {
      if ((itsFunctionType != 0) && (!itsNeedsActivation))
        Kit.codeBug();
      for (int i = 0; i < itsNestedFunctions.length; i++) {
        InterpreterData fdata = itsNestedFunctions[i];
        if (itsFunctionType == 1) {
          initFunction(cx, scope, fnOrScript, i);
        }
      }
    }
    


    int emptyStackTop = itsMaxVars + itsMaxLocals - 1;
    int maxFrameArray = itsMaxFrameArray;
    if (maxFrameArray != emptyStackTop + itsMaxStack + 1)
      Kit.codeBug();
    double[] sDbl;
    boolean stackReuse;
    Object[] stack;
    int[] stackAttributes;
    double[] sDbl;
    if ((stack != null) && (maxFrameArray <= stack.length))
    {
      boolean stackReuse = true;
      Object[] stack = stack;
      int[] stackAttributes = stackAttributes;
      sDbl = sDbl;
    } else {
      stackReuse = false;
      stack = new Object[maxFrameArray];
      stackAttributes = new int[maxFrameArray];
      sDbl = new double[maxFrameArray];
    }
    
    int varCount = idata.getParamAndVarCount();
    for (int i = 0; i < varCount; i++) {
      if (idata.getParamOrVarConst(i))
        stackAttributes[i] = 13;
    }
    int definedArgs = argCount;
    if (definedArgs > argCount) {
      definedArgs = argCount;
    }
    


    parentFrame = parentFrame;
    frameIndex = (parentFrame == null ? 0 : frameIndex + 1);
    
    if (frameIndex > cx.getMaximumInterpreterStackDepth()) {
      throw Context.reportRuntimeError("Exceeded maximum stack depth");
    }
    frozen = false;
    
    fnOrScript = fnOrScript;
    idata = idata;
    
    stack = stack;
    stackAttributes = stackAttributes;
    sDbl = sDbl;
    varSource = frame;
    localShift = itsMaxVars;
    emptyStackTop = emptyStackTop;
    
    debuggerFrame = debuggerFrame;
    useActivation = useActivation;
    
    thisObj = thisObj;
    


    result = Undefined.instance;
    pc = 0;
    pcPrevBranch = 0;
    pcSourceLineStart = firstLinePC;
    scope = scope;
    
    savedStackTop = emptyStackTop;
    savedCallOp = 0;
    
    System.arraycopy(args, argShift, stack, 0, definedArgs);
    if (argsDbl != null) {
      System.arraycopy(argsDbl, argShift, sDbl, 0, definedArgs);
    }
    for (int i = definedArgs; i != itsMaxVars; i++) {
      stack[i] = Undefined.instance;
    }
    if (stackReuse)
    {

      for (int i = emptyStackTop + 1; i != stack.length; i++) {
        stack[i] = null;
      }
    }
    
    enterFrame(cx, frame, args, false);
  }
  
  private static boolean isFrameEnterExitRequired(CallFrame frame) {
    return (debuggerFrame != null) || (idata.itsNeedsActivation);
  }
  
  private static void enterFrame(Context cx, CallFrame frame, Object[] args, boolean continuationRestart)
  {
    if ((parentFrame != null) && 
      (!parentFrame.fnOrScript.isScript())) {
      fnOrScript.defaultPut("caller", parentFrame.fnOrScript);
      fnOrScript.setAttributes("caller", 2);
    }
    if ((scope instanceof NativeCall)) {
      Object arguments = ScriptableObject.getProperty(scope, "arguments");
      
      if ((arguments instanceof Arguments)) {
        fnOrScript.setArguments((Arguments)arguments);
      }
    }
    
    boolean usesActivation = idata.itsNeedsActivation;
    boolean isDebugged = debuggerFrame != null;
    if ((usesActivation) || (isDebugged)) {
      Scriptable scope = scope;
      if (scope == null) {
        Kit.codeBug();
      } else if (continuationRestart)
      {








        while ((scope instanceof NativeWith)) {
          scope = scope.getParentScope();
          if ((scope == null) || ((parentFrame != null) && (parentFrame.scope == scope)))
          {



            Kit.codeBug();
          }
        }
      }
      





      if (isDebugged) {
        debuggerFrame.onEnter(cx, scope, thisObj, args);
      }
      


      if (usesActivation) {
        ScriptRuntime.enterActivationFunction(cx, scope);
      }
    }
  }
  
  private static void exitFrame(Context cx, CallFrame frame, Object throwable)
  {
    fnOrScript.defaultPut("caller", null);
    fnOrScript.setArguments(null);
    
    if (idata.itsNeedsActivation) {
      ScriptRuntime.exitActivationFunction(cx);
    }
    
    if (debuggerFrame != null) {
      try {
        if ((throwable instanceof Throwable)) {
          debuggerFrame.onExit(cx, true, throwable);
        }
        else {
          ContinuationJump cjump = (ContinuationJump)throwable;
          Object result; Object result; if (cjump == null) {
            result = result;
          } else {
            result = result;
          }
          if (result == UniqueTag.DOUBLE_MARK) { double resultDbl;
            double resultDbl;
            if (cjump == null) {
              resultDbl = resultDbl;
            } else {
              resultDbl = resultDbl;
            }
            result = ScriptRuntime.wrapNumber(resultDbl);
          }
          debuggerFrame.onExit(cx, false, result);
        }
      } catch (Throwable ex) {
        System.err.println("RHINO USAGE WARNING: onExit terminated with exception");
        
        ex.printStackTrace(System.err);
      }
    }
  }
  
  private static void setCallResult(CallFrame frame, Object callResult, double callResultDbl)
  {
    if (savedCallOp == 38) {
      stack[savedStackTop] = callResult;
      sDbl[savedStackTop] = callResultDbl;
    } else if (savedCallOp == 30)
    {


      if ((callResult instanceof Scriptable)) {
        stack[savedStackTop] = callResult;
      }
    } else {
      Kit.codeBug();
    }
    savedCallOp = 0;
  }
  
  public static NativeContinuation captureContinuation(Context cx) {
    if ((lastInterpreterFrame == null) || (!(lastInterpreterFrame instanceof CallFrame)))
    {
      throw new IllegalStateException("Interpreter frames not found");
    }
    return captureContinuation(cx, (CallFrame)lastInterpreterFrame, true);
  }
  

  private static NativeContinuation captureContinuation(Context cx, CallFrame frame, boolean requireContinuationsTopFrame)
  {
    NativeContinuation c = new NativeContinuation();
    ScriptRuntime.setObjectProtoAndParent(c, 
      ScriptRuntime.getTopCallScope(cx));
    

    CallFrame x = frame;
    CallFrame outermost = frame;
    while ((x != null) && (!frozen)) {
      frozen = true;
      
      for (int i = savedStackTop + 1; i != stack.length; i++)
      {
        stack[i] = null;
        stackAttributes[i] = 0;
      }
      if (savedCallOp == 38)
      {
        stack[savedStackTop] = null;
      }
      else if (savedCallOp != 30) {
        Kit.codeBug();
      }
      


      outermost = x;
      x = parentFrame;
    }
    
    if (requireContinuationsTopFrame) {
      while (parentFrame != null) {
        outermost = parentFrame;
      }
      if (!isContinuationsTopFrame) {
        throw new IllegalStateException("Cannot capture continuation from JavaScript code not called directly by executeScriptWithContinuations or callFunctionWithContinuations");
      }
    }
    



    c.initImplementation(frame);
    return c;
  }
  
  private static int stack_int32(CallFrame frame, int i) {
    Object x = stack[i];
    if (x == UniqueTag.DOUBLE_MARK) {
      return ScriptRuntime.toInt32(sDbl[i]);
    }
    return ScriptRuntime.toInt32(x);
  }
  
  private static double stack_double(CallFrame frame, int i)
  {
    Object x = stack[i];
    if (x != UniqueTag.DOUBLE_MARK) {
      return ScriptRuntime.toNumber(x);
    }
    return sDbl[i];
  }
  
  private static boolean stack_boolean(CallFrame frame, int i)
  {
    Object x = stack[i];
    if (x == Boolean.TRUE)
      return true;
    if (x == Boolean.FALSE)
      return false;
    if (x == UniqueTag.DOUBLE_MARK) {
      double d = sDbl[i];
      return (d == d) && (d != 0.0D); }
    if ((x == null) || (x == Undefined.instance))
      return false;
    if ((x instanceof Number)) {
      double d = ((Number)x).doubleValue();
      return (d == d) && (d != 0.0D); }
    if ((x instanceof Boolean)) {
      return ((Boolean)x).booleanValue();
    }
    return ScriptRuntime.toBoolean(x);
  }
  

  private static void doAdd(Object[] stack, double[] sDbl, int stackTop, Context cx)
  {
    Object rhs = stack[(stackTop + 1)];
    Object lhs = stack[stackTop];
    
    boolean leftRightOrder;
    if (rhs == UniqueTag.DOUBLE_MARK) {
      double d = sDbl[(stackTop + 1)];
      if (lhs == UniqueTag.DOUBLE_MARK) {
        sDbl[stackTop] += d;
        return;
      }
      leftRightOrder = true;
    } else { boolean leftRightOrder;
      if (lhs == UniqueTag.DOUBLE_MARK) {
        double d = sDbl[stackTop];
        lhs = rhs;
        leftRightOrder = false;
      }
      else {
        if (((lhs instanceof Scriptable)) || ((rhs instanceof Scriptable))) {
          stack[stackTop] = ScriptRuntime.add(lhs, rhs, cx);
        } else if (((lhs instanceof CharSequence)) || ((rhs instanceof CharSequence)))
        {
          CharSequence lstr = ScriptRuntime.toCharSequence(lhs);
          CharSequence rstr = ScriptRuntime.toCharSequence(rhs);
          stack[stackTop] = new ConsString(lstr, rstr);
        }
        else
        {
          double lDbl = (lhs instanceof Number) ? ((Number)lhs).doubleValue() : ScriptRuntime.toNumber(lhs);
          

          double rDbl = (rhs instanceof Number) ? ((Number)rhs).doubleValue() : ScriptRuntime.toNumber(rhs);
          stack[stackTop] = UniqueTag.DOUBLE_MARK;
          sDbl[stackTop] = (lDbl + rDbl);
        }
        return;
      } }
    boolean leftRightOrder;
    double d;
    if ((lhs instanceof Scriptable)) {
      rhs = ScriptRuntime.wrapNumber(d);
      if (!leftRightOrder) {
        Object tmp = lhs;
        lhs = rhs;
        rhs = tmp;
      }
      stack[stackTop] = ScriptRuntime.add(lhs, rhs, cx);
    } else if ((lhs instanceof CharSequence)) {
      CharSequence lstr = (CharSequence)lhs;
      CharSequence rstr = ScriptRuntime.toCharSequence(Double.valueOf(d));
      if (leftRightOrder) {
        stack[stackTop] = new ConsString(lstr, rstr);
      } else {
        stack[stackTop] = new ConsString(rstr, lstr);
      }
    }
    else {
      double lDbl = (lhs instanceof Number) ? ((Number)lhs).doubleValue() : ScriptRuntime.toNumber(lhs);
      stack[stackTop] = UniqueTag.DOUBLE_MARK;
      sDbl[stackTop] = (lDbl + d);
    }
  }
  
  private static int doArithmetic(CallFrame frame, int op, Object[] stack, double[] sDbl, int stackTop)
  {
    double rDbl = stack_double(frame, stackTop);
    stackTop--;
    double lDbl = stack_double(frame, stackTop);
    stack[stackTop] = UniqueTag.DOUBLE_MARK;
    switch (op) {
    case 22: 
      lDbl -= rDbl;
      break;
    case 23: 
      lDbl *= rDbl;
      break;
    case 24: 
      lDbl /= rDbl;
      break;
    case 25: 
      lDbl %= rDbl;
    }
    
    sDbl[stackTop] = lDbl;
    return stackTop;
  }
  
  private static Object[] getArgsArray(Object[] stack, double[] sDbl, int shift, int count)
  {
    if (count == 0) {
      return ScriptRuntime.emptyArgs;
    }
    Object[] args = new Object[count];
    for (int i = 0; i != count; shift++) {
      Object val = stack[shift];
      if (val == UniqueTag.DOUBLE_MARK) {
        val = ScriptRuntime.wrapNumber(sDbl[shift]);
      }
      args[i] = val;i++;
    }
    return args;
  }
  
  private static void addInstructionCount(Context cx, CallFrame frame, int extra)
  {
    instructionCount += pc - pcPrevBranch + extra;
    if (instructionCount > instructionThreshold) {
      cx.observeInstructionCount(instructionCount);
      instructionCount = 0;
    }
  }
}

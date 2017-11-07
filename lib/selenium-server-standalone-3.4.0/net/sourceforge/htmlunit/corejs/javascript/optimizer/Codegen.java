package net.sourceforge.htmlunit.corejs.javascript.optimizer;

import java.lang.reflect.Constructor;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.classfile.ClassFileWriter;
import net.sourceforge.htmlunit.corejs.classfile.ClassFileWriter.ClassFileFormatException;
import net.sourceforge.htmlunit.corejs.javascript.CompilerEnvirons;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.GeneratedClassLoader;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.corejs.javascript.ObjArray;
import net.sourceforge.htmlunit.corejs.javascript.ObjToIntMap;
import net.sourceforge.htmlunit.corejs.javascript.RhinoException;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ast.FunctionNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.ScriptNode;

public class Codegen implements net.sourceforge.htmlunit.corejs.javascript.Evaluator
{
  static final String DEFAULT_MAIN_METHOD_CLASS = "net.sourceforge.htmlunit.corejs.javascript.optimizer.OptRuntime";
  private static final String SUPER_CLASS_NAME = "net.sourceforge.htmlunit.corejs.javascript.NativeFunction";
  static final String ID_FIELD_NAME = "_id";
  static final String REGEXP_INIT_METHOD_NAME = "_reInit";
  static final String REGEXP_INIT_METHOD_SIGNATURE = "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)V";
  static final String FUNCTION_INIT_SIGNATURE = "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)V";
  static final String FUNCTION_CONSTRUCTOR_SIGNATURE = "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)V";
  
  public Codegen() {}
  
  public void captureStackInfo(RhinoException ex)
  {
    throw new UnsupportedOperationException();
  }
  
  public String getSourcePositionFromStack(Context cx, int[] linep) {
    throw new UnsupportedOperationException();
  }
  
  public String getPatchedStack(RhinoException ex, String nativeStackTrace) {
    throw new UnsupportedOperationException();
  }
  
  public java.util.List<String> getScriptStack(RhinoException ex) {
    throw new UnsupportedOperationException();
  }
  
  public void setEvalScriptFlag(Script script) {
    throw new UnsupportedOperationException();
  }
  
  public Object compile(CompilerEnvirons compilerEnv, ScriptNode tree, String encodedSource, boolean returnFunction)
  {
    int serial;
    synchronized (globalLock) {
      serial = ++globalSerialClassCounter;
    }
    int serial;
    String baseName = "c";
    if (tree.getSourceName().length() > 0) {
      baseName = tree.getSourceName().replaceAll("\\W", "_");
      if (!Character.isJavaIdentifierStart(baseName.charAt(0))) {
        baseName = "_" + baseName;
      }
    }
    
    String mainClassName = "net.sourceforge.htmlunit.corejs.javascript.gen." + baseName + "_" + serial;
    

    byte[] mainClassBytes = compileToClassFile(compilerEnv, mainClassName, tree, encodedSource, returnFunction);
    

    return new Object[] { mainClassName, mainClassBytes };
  }
  
  public Script createScriptObject(Object bytecode, Object staticSecurityDomain)
  {
    Class<?> cl = defineClass(bytecode, staticSecurityDomain);
    
    try
    {
      script = (Script)cl.newInstance();
    } catch (Exception ex) {
      Script script;
      throw new RuntimeException("Unable to instantiate compiled class:" + ex.toString()); }
    Script script;
    return script;
  }
  
  public net.sourceforge.htmlunit.corejs.javascript.Function createFunctionObject(Context cx, Scriptable scope, Object bytecode, Object staticSecurityDomain)
  {
    Class<?> cl = defineClass(bytecode, staticSecurityDomain);
    
    try
    {
      Constructor<?> ctor = cl.getConstructors()[0];
      Object[] initArgs = { scope, cx, Integer.valueOf(0) };
      f = (NativeFunction)ctor.newInstance(initArgs);
    } catch (Exception ex) {
      NativeFunction f;
      throw new RuntimeException("Unable to instantiate compiled class:" + ex.toString()); }
    NativeFunction f;
    return f;
  }
  
  private Class<?> defineClass(Object bytecode, Object staticSecurityDomain) {
    Object[] nameBytesPair = (Object[])bytecode;
    String className = (String)nameBytesPair[0];
    byte[] classBytes = (byte[])nameBytesPair[1];
    


    ClassLoader rhinoLoader = getClass().getClassLoader();
    
    GeneratedClassLoader loader = net.sourceforge.htmlunit.corejs.javascript.SecurityController.createLoader(rhinoLoader, staticSecurityDomain);
    Exception e;
    try
    {
      Class<?> cl = loader.defineClass(className, classBytes);
      loader.linkClass(cl);
      return cl;
    } catch (SecurityException x) {
      e = x;
    } catch (IllegalArgumentException x) { Exception e;
      e = x;
    }
    throw new RuntimeException("Malformed optimizer package " + e);
  }
  

  public byte[] compileToClassFile(CompilerEnvirons compilerEnv, String mainClassName, ScriptNode scriptOrFn, String encodedSource, boolean returnFunction)
  {
    this.compilerEnv = compilerEnv;
    
    transform(scriptOrFn);
    




    if (returnFunction) {
      scriptOrFn = scriptOrFn.getFunctionNode(0);
    }
    
    initScriptNodesData(scriptOrFn);
    
    this.mainClassName = mainClassName;
    
    mainClassSignature = ClassFileWriter.classNameToSignature(mainClassName);
    try
    {
      return generateCode(encodedSource);
    } catch (ClassFileWriter.ClassFileFormatException e) {
      throw reportClassFileFormatException(scriptOrFn, e.getMessage());
    }
  }
  



  private RuntimeException reportClassFileFormatException(ScriptNode scriptOrFn, String message)
  {
    String msg = (scriptOrFn instanceof FunctionNode) ? net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.getMessage2("msg.while.compiling.fn", ((FunctionNode)scriptOrFn).getFunctionName(), message) : net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.getMessage1("msg.while.compiling.script", message);
    
    return Context.reportRuntimeError(msg, scriptOrFn.getSourceName(), scriptOrFn
      .getLineno(), null, 0);
  }
  
  private void transform(ScriptNode tree) {
    initOptFunctions_r(tree);
    
    int optLevel = compilerEnv.getOptimizationLevel();
    
    Map<String, OptFunctionNode> possibleDirectCalls = null;
    if (optLevel > 0)
    {




      if (tree.getType() == 136) {
        int functionCount = tree.getFunctionCount();
        for (int i = 0; i != functionCount; i++) {
          OptFunctionNode ofn = OptFunctionNode.get(tree, i);
          
          if (fnode.getFunctionType() == 1) {
            String name = fnode.getName();
            if (name.length() != 0) {
              if (possibleDirectCalls == null) {
                possibleDirectCalls = new java.util.HashMap();
              }
              possibleDirectCalls.put(name, ofn);
            }
          }
        }
      }
    }
    
    if (possibleDirectCalls != null) {
      directCallTargets = new ObjArray();
    }
    
    OptTransformer ot = new OptTransformer(possibleDirectCalls, directCallTargets);
    
    ot.transform(tree);
    
    if (optLevel > 0) {
      new Optimizer().optimize(tree);
    }
  }
  
  private static void initOptFunctions_r(ScriptNode scriptOrFn) {
    int i = 0; for (int N = scriptOrFn.getFunctionCount(); i != N; i++) {
      FunctionNode fn = scriptOrFn.getFunctionNode(i);
      new OptFunctionNode(fn);
      initOptFunctions_r(fn);
    }
  }
  
  private void initScriptNodesData(ScriptNode scriptOrFn) {
    ObjArray x = new ObjArray();
    collectScriptNodes_r(scriptOrFn, x);
    
    int count = x.size();
    scriptOrFnNodes = new ScriptNode[count];
    x.toArray(scriptOrFnNodes);
    
    scriptOrFnIndexes = new ObjToIntMap(count);
    for (int i = 0; i != count; i++) {
      scriptOrFnIndexes.put(scriptOrFnNodes[i], i);
    }
  }
  
  private static void collectScriptNodes_r(ScriptNode n, ObjArray x) {
    x.add(n);
    int nestedCount = n.getFunctionCount();
    for (int i = 0; i != nestedCount; i++) {
      collectScriptNodes_r(n.getFunctionNode(i), x);
    }
  }
  
  private byte[] generateCode(String encodedSource) {
    boolean hasScript = scriptOrFnNodes[0].getType() == 136;
    boolean hasFunctions = (scriptOrFnNodes.length > 1) || (!hasScript);
    
    String sourceFile = null;
    if (compilerEnv.isGenerateDebugInfo()) {
      sourceFile = scriptOrFnNodes[0].getSourceName();
    }
    
    ClassFileWriter cfw = new ClassFileWriter(mainClassName, "net.sourceforge.htmlunit.corejs.javascript.NativeFunction", sourceFile);
    
    cfw.addField("_id", "I", (short)2);
    
    if (hasFunctions) {
      generateFunctionConstructor(cfw);
    }
    
    if (hasScript) {
      cfw.addInterface("net/sourceforge/htmlunit/corejs/javascript/Script");
      
      generateScriptCtor(cfw);
      generateMain(cfw);
      generateExecute(cfw);
    }
    
    generateCallMethod(cfw);
    generateResumeGenerator(cfw);
    
    generateNativeFunctionOverrides(cfw, encodedSource);
    
    int count = scriptOrFnNodes.length;
    for (int i = 0; i != count; i++) {
      ScriptNode n = scriptOrFnNodes[i];
      
      BodyCodegen bodygen = new BodyCodegen();
      cfw = cfw;
      codegen = this;
      compilerEnv = compilerEnv;
      scriptOrFn = n;
      scriptOrFnIndex = i;
      try
      {
        bodygen.generateBodyCode();
      } catch (ClassFileWriter.ClassFileFormatException e) {
        throw reportClassFileFormatException(n, e.getMessage());
      }
      
      if (n.getType() == 109) {
        OptFunctionNode ofn = OptFunctionNode.get(n);
        generateFunctionInit(cfw, ofn);
        if (ofn.isTargetOfDirectCall()) {
          emitDirectConstructor(cfw, ofn);
        }
      }
    }
    
    emitRegExpInit(cfw);
    emitConstantDudeInitializers(cfw);
    
    return cfw.toByteArray();
  }
  







  private void emitDirectConstructor(ClassFileWriter cfw, OptFunctionNode ofn)
  {
    cfw.startMethod(getDirectCtorName(fnode), 
      getBodyMethodSignature(fnode), (short)10);
    

    int argCount = fnode.getParamCount();
    int firstLocal = 4 + argCount * 3 + 1;
    
    cfw.addALoad(0);
    cfw.addALoad(1);
    cfw.addALoad(2);
    cfw.addInvoke(182, "net/sourceforge/htmlunit/corejs/javascript/BaseFunction", "createObject", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    




    cfw.addAStore(firstLocal);
    
    cfw.addALoad(0);
    cfw.addALoad(1);
    cfw.addALoad(2);
    cfw.addALoad(firstLocal);
    for (int i = 0; i < argCount; i++) {
      cfw.addALoad(4 + i * 3);
      cfw.addDLoad(5 + i * 3);
    }
    cfw.addALoad(4 + argCount * 3);
    cfw.addInvoke(184, mainClassName, 
      getBodyMethodName(fnode), 
      getBodyMethodSignature(fnode));
    int exitLabel = cfw.acquireLabel();
    cfw.add(89);
    cfw.add(193, "net/sourceforge/htmlunit/corejs/javascript/Scriptable");
    
    cfw.add(153, exitLabel);
    
    cfw.add(192, "net/sourceforge/htmlunit/corejs/javascript/Scriptable");
    
    cfw.add(176);
    cfw.markLabel(exitLabel);
    
    cfw.addALoad(firstLocal);
    cfw.add(176);
    
    cfw.stopMethod((short)(firstLocal + 1));
  }
  
  static boolean isGenerator(ScriptNode node) {
    return (node.getType() == 109) && 
      (((FunctionNode)node).isGenerator());
  }
  











  private void generateResumeGenerator(ClassFileWriter cfw)
  {
    boolean hasGenerators = false;
    for (int i = 0; i < scriptOrFnNodes.length; i++) {
      if (isGenerator(scriptOrFnNodes[i])) {
        hasGenerators = true;
      }
    }
    

    if (!hasGenerators) {
      return;
    }
    cfw.startMethod("resumeGenerator", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;ILjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
    






    cfw.addALoad(0);
    cfw.addALoad(1);
    cfw.addALoad(2);
    cfw.addALoad(4);
    cfw.addALoad(5);
    cfw.addILoad(3);
    
    cfw.addLoadThis();
    cfw.add(180, cfw.getClassName(), "_id", "I");
    
    int startSwitch = cfw.addTableSwitch(0, scriptOrFnNodes.length - 1);
    cfw.markTableSwitchDefault(startSwitch);
    int endlabel = cfw.acquireLabel();
    
    for (int i = 0; i < scriptOrFnNodes.length; i++) {
      ScriptNode n = scriptOrFnNodes[i];
      cfw.markTableSwitchCase(startSwitch, i, 6);
      if (isGenerator(n)) {
        String type = "(" + mainClassSignature + "Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Ljava/lang/Object;Ljava/lang/Object;I)Ljava/lang/Object;";
        



        cfw.addInvoke(184, mainClassName, 
          getBodyMethodName(n) + "_gen", type);
        cfw.add(176);
      } else {
        cfw.add(167, endlabel);
      }
    }
    
    cfw.markLabel(endlabel);
    pushUndefined(cfw);
    cfw.add(176);
    

    cfw.stopMethod((short)6);
  }
  
  private void generateCallMethod(ClassFileWriter cfw) {
    cfw.startMethod("call", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;", (short)17);
    










    int nonTopCallLabel = cfw.acquireLabel();
    cfw.addALoad(1);
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "hasTopCall", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Z");
    


    cfw.add(154, nonTopCallLabel);
    cfw.addALoad(0);
    cfw.addALoad(1);
    cfw.addALoad(2);
    cfw.addALoad(3);
    cfw.addALoad(4);
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "doTopCall", "(Lnet/sourceforge/htmlunit/corejs/javascript/Callable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
    






    cfw.add(176);
    cfw.markLabel(nonTopCallLabel);
    

    cfw.addALoad(0);
    cfw.addALoad(1);
    cfw.addALoad(2);
    cfw.addALoad(3);
    cfw.addALoad(4);
    
    int end = scriptOrFnNodes.length;
    boolean generateSwitch = 2 <= end;
    
    int switchStart = 0;
    int switchStackTop = 0;
    if (generateSwitch) {
      cfw.addLoadThis();
      cfw.add(180, cfw.getClassName(), "_id", "I");
      

      switchStart = cfw.addTableSwitch(1, end - 1);
    }
    
    for (int i = 0; i != end; i++) {
      ScriptNode n = scriptOrFnNodes[i];
      if (generateSwitch) {
        if (i == 0) {
          cfw.markTableSwitchDefault(switchStart);
          switchStackTop = cfw.getStackTop();
        } else {
          cfw.markTableSwitchCase(switchStart, i - 1, switchStackTop);
        }
      }
      if (n.getType() == 109) {
        OptFunctionNode ofn = OptFunctionNode.get(n);
        if (ofn.isTargetOfDirectCall()) {
          int pcount = fnode.getParamCount();
          if (pcount != 0)
          {

            for (int p = 0; p != pcount; p++) {
              cfw.add(190);
              cfw.addPush(p);
              int undefArg = cfw.acquireLabel();
              int beyond = cfw.acquireLabel();
              cfw.add(164, undefArg);
              
              cfw.addALoad(4);
              cfw.addPush(p);
              cfw.add(50);
              cfw.add(167, beyond);
              cfw.markLabel(undefArg);
              pushUndefined(cfw);
              cfw.markLabel(beyond);
              
              cfw.adjustStackTop(-1);
              cfw.addPush(0.0D);
              
              cfw.addALoad(4);
            }
          }
        }
      }
      cfw.addInvoke(184, mainClassName, 
        getBodyMethodName(n), getBodyMethodSignature(n));
      cfw.add(176);
    }
    cfw.stopMethod((short)5);
  }
  
  private void generateMain(ClassFileWriter cfw)
  {
    cfw.startMethod("main", "([Ljava/lang/String;)V", (short)9);
    


    cfw.add(187, cfw.getClassName());
    cfw.add(89);
    cfw.addInvoke(183, cfw.getClassName(), "<init>", "()V");
    

    cfw.add(42);
    
    cfw.addInvoke(184, mainMethodClass, "main", "(Lnet/sourceforge/htmlunit/corejs/javascript/Script;[Ljava/lang/String;)V");
    
    cfw.add(177);
    
    cfw.stopMethod((short)1);
  }
  
  private void generateExecute(ClassFileWriter cfw) {
    cfw.startMethod("exec", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)Ljava/lang/Object;", (short)17);
    




    int CONTEXT_ARG = 1;
    int SCOPE_ARG = 2;
    
    cfw.addLoadThis();
    cfw.addALoad(1);
    cfw.addALoad(2);
    cfw.add(89);
    cfw.add(1);
    cfw.addInvoke(182, cfw.getClassName(), "call", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
    




    cfw.add(176);
    
    cfw.stopMethod((short)3);
  }
  
  private void generateScriptCtor(ClassFileWriter cfw) {
    cfw.startMethod("<init>", "()V", (short)1);
    
    cfw.addLoadThis();
    cfw.addInvoke(183, "net.sourceforge.htmlunit.corejs.javascript.NativeFunction", "<init>", "()V");
    

    cfw.addLoadThis();
    cfw.addPush(0);
    cfw.add(181, cfw.getClassName(), "_id", "I");
    
    cfw.add(177);
    
    cfw.stopMethod((short)1);
  }
  
  private void generateFunctionConstructor(ClassFileWriter cfw) {
    int SCOPE_ARG = 1;
    int CONTEXT_ARG = 2;
    int ID_ARG = 3;
    
    cfw.startMethod("<init>", "(Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Context;I)V", (short)1);
    cfw.addALoad(0);
    cfw.addInvoke(183, "net.sourceforge.htmlunit.corejs.javascript.NativeFunction", "<init>", "()V");
    

    cfw.addLoadThis();
    cfw.addILoad(3);
    cfw.add(181, cfw.getClassName(), "_id", "I");
    
    cfw.addLoadThis();
    cfw.addALoad(2);
    cfw.addALoad(1);
    
    int start = scriptOrFnNodes[0].getType() == 136 ? 1 : 0;
    int end = scriptOrFnNodes.length;
    if (start == end)
      throw badTree();
    boolean generateSwitch = 2 <= end - start;
    
    int switchStart = 0;
    int switchStackTop = 0;
    if (generateSwitch) {
      cfw.addILoad(3);
      

      switchStart = cfw.addTableSwitch(start + 1, end - 1);
    }
    
    for (int i = start; i != end; i++) {
      if (generateSwitch) {
        if (i == start) {
          cfw.markTableSwitchDefault(switchStart);
          switchStackTop = cfw.getStackTop();
        } else {
          cfw.markTableSwitchCase(switchStart, i - 1 - start, switchStackTop);
        }
      }
      
      OptFunctionNode ofn = OptFunctionNode.get(scriptOrFnNodes[i]);
      cfw.addInvoke(183, mainClassName, 
        getFunctionInitMethodName(ofn), "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)V");
      cfw.add(177);
    }
    

    cfw.stopMethod((short)4);
  }
  
  private void generateFunctionInit(ClassFileWriter cfw, OptFunctionNode ofn)
  {
    int CONTEXT_ARG = 1;
    int SCOPE_ARG = 2;
    cfw.startMethod(getFunctionInitMethodName(ofn), "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)V", (short)18);
    


    cfw.addLoadThis();
    cfw.addALoad(1);
    cfw.addALoad(2);
    cfw.addInvoke(182, "net/sourceforge/htmlunit/corejs/javascript/NativeFunction", "initScriptFunction", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;)V");
    






    if (fnode.getRegexpCount() != 0) {
      cfw.addALoad(1);
      cfw.addInvoke(184, mainClassName, "_reInit", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)V");
    }
    

    cfw.add(177);
    
    cfw.stopMethod((short)3);
  }
  



  private void generateNativeFunctionOverrides(ClassFileWriter cfw, String encodedSource)
  {
    cfw.startMethod("getLanguageVersion", "()I", (short)1);
    
    cfw.addPush(compilerEnv.getLanguageVersion());
    cfw.add(172);
    

    cfw.stopMethod((short)1);
    



    int Do_getFunctionName = 0;
    int Do_getParamCount = 1;
    int Do_getParamAndVarCount = 2;
    int Do_getParamOrVarName = 3;
    int Do_getEncodedSource = 4;
    int Do_getParamOrVarConst = 5;
    int SWITCH_COUNT = 6;
    
    for (int methodIndex = 0; methodIndex != 6; methodIndex++) {
      if ((methodIndex != 4) || (encodedSource != null))
      {








        switch (methodIndex) {
        case 0: 
          short methodLocals = 1;
          cfw.startMethod("getFunctionName", "()Ljava/lang/String;", (short)1);
          
          break;
        case 1: 
          short methodLocals = 1;
          cfw.startMethod("getParamCount", "()I", (short)1);
          break;
        case 2: 
          short methodLocals = 1;
          cfw.startMethod("getParamAndVarCount", "()I", (short)1);
          break;
        case 3: 
          short methodLocals = 2;
          cfw.startMethod("getParamOrVarName", "(I)Ljava/lang/String;", (short)1);
          
          break;
        case 5: 
          short methodLocals = 3;
          cfw.startMethod("getParamOrVarConst", "(I)Z", (short)1);
          break;
        case 4: 
          short methodLocals = 1;
          cfw.startMethod("getEncodedSource", "()Ljava/lang/String;", (short)1);
          
          cfw.addPush(encodedSource);
          break;
        default: 
          throw Kit.codeBug();
        }
        short methodLocals;
        int count = scriptOrFnNodes.length;
        
        int switchStart = 0;
        int switchStackTop = 0;
        if (count > 1)
        {

          cfw.addLoadThis();
          cfw.add(180, cfw.getClassName(), "_id", "I");
          


          switchStart = cfw.addTableSwitch(1, count - 1);
        }
        
        for (int i = 0; i != count; i++) {
          ScriptNode n = scriptOrFnNodes[i];
          if (i == 0) {
            if (count > 1) {
              cfw.markTableSwitchDefault(switchStart);
              switchStackTop = cfw.getStackTop();
            }
          } else {
            cfw.markTableSwitchCase(switchStart, i - 1, switchStackTop);
          }
          

          switch (methodIndex)
          {
          case 0: 
            if (n.getType() == 136) {
              cfw.addPush("");
            } else {
              String name = ((FunctionNode)n).getName();
              cfw.addPush(name);
            }
            cfw.add(176);
            break;
          

          case 1: 
            cfw.addPush(n.getParamCount());
            cfw.add(172);
            break;
          

          case 2: 
            cfw.addPush(n.getParamAndVarCount());
            cfw.add(172);
            break;
          


          case 3: 
            int paramAndVarCount = n.getParamAndVarCount();
            if (paramAndVarCount == 0)
            {


              cfw.add(1);
              cfw.add(176);
            } else if (paramAndVarCount == 1)
            {

              cfw.addPush(n.getParamOrVarName(0));
              cfw.add(176);
            }
            else {
              cfw.addILoad(1);
              

              int paramSwitchStart = cfw.addTableSwitch(1, paramAndVarCount - 1);
              
              for (int j = 0; j != paramAndVarCount; j++) {
                if (cfw.getStackTop() != 0)
                  Kit.codeBug();
                String s = n.getParamOrVarName(j);
                if (j == 0) {
                  cfw.markTableSwitchDefault(paramSwitchStart);
                } else {
                  cfw.markTableSwitchCase(paramSwitchStart, j - 1, 0);
                }
                
                cfw.addPush(s);
                cfw.add(176);
              }
            }
            break;
          


          case 5: 
            int paramAndVarCount = n.getParamAndVarCount();
            boolean[] constness = n.getParamAndVarConst();
            if (paramAndVarCount == 0)
            {


              cfw.add(3);
              cfw.add(172);
            } else if (paramAndVarCount == 1)
            {

              cfw.addPush(constness[0]);
              cfw.add(172);
            }
            else {
              cfw.addILoad(1);
              

              int paramSwitchStart = cfw.addTableSwitch(1, paramAndVarCount - 1);
              
              for (int j = 0; j != paramAndVarCount; j++) {
                if (cfw.getStackTop() != 0)
                  Kit.codeBug();
                if (j == 0) {
                  cfw.markTableSwitchDefault(paramSwitchStart);
                } else {
                  cfw.markTableSwitchCase(paramSwitchStart, j - 1, 0);
                }
                
                cfw.addPush(constness[j]);
                cfw.add(172);
              }
            }
            break;
          


          case 4: 
            cfw.addPush(n.getEncodedSourceStart());
            cfw.addPush(n.getEncodedSourceEnd());
            cfw.addInvoke(182, "java/lang/String", "substring", "(II)Ljava/lang/String;");
            
            cfw.add(176);
            break;
          
          default: 
            throw Kit.codeBug();
          }
          
        }
        cfw.stopMethod(methodLocals);
      }
    }
  }
  
  private void emitRegExpInit(ClassFileWriter cfw)
  {
    int totalRegCount = 0;
    for (int i = 0; i != scriptOrFnNodes.length; i++) {
      totalRegCount += scriptOrFnNodes[i].getRegexpCount();
    }
    if (totalRegCount == 0) {
      return;
    }
    
    cfw.startMethod("_reInit", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)V", (short)10);
    
    cfw.addField("_reInitDone", "Z", (short)74);
    
    cfw.add(178, mainClassName, "_reInitDone", "Z");
    int doInit = cfw.acquireLabel();
    cfw.add(153, doInit);
    cfw.add(177);
    cfw.markLabel(doInit);
    

    cfw.addALoad(0);
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "checkRegExpProxy", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;)Lnet/sourceforge/htmlunit/corejs/javascript/RegExpProxy;");
    



    cfw.addAStore(1);
    


    for (int i = 0; i != scriptOrFnNodes.length; i++) {
      ScriptNode n = scriptOrFnNodes[i];
      int regCount = n.getRegexpCount();
      for (int j = 0; j != regCount; j++) {
        String reFieldName = getCompiledRegexpName(n, j);
        String reFieldType = "Ljava/lang/Object;";
        String reString = n.getRegexpString(j);
        String reFlags = n.getRegexpFlags(j);
        cfw.addField(reFieldName, reFieldType, (short)10);
        
        cfw.addALoad(1);
        cfw.addALoad(0);
        cfw.addPush(reString);
        if (reFlags == null) {
          cfw.add(1);
        } else {
          cfw.addPush(reFlags);
        }
        cfw.addInvoke(185, "net/sourceforge/htmlunit/corejs/javascript/RegExpProxy", "compileRegExp", "(Lnet/sourceforge/htmlunit/corejs/javascript/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
        




        cfw.add(179, mainClassName, reFieldName, reFieldType);
      }
    }
    

    cfw.addPush(1);
    cfw.add(179, mainClassName, "_reInitDone", "Z");
    cfw.add(177);
    cfw.stopMethod((short)2);
  }
  
  private void emitConstantDudeInitializers(ClassFileWriter cfw) {
    int N = itsConstantListSize;
    if (N == 0) {
      return;
    }
    cfw.startMethod("<clinit>", "()V", (short)24);
    
    double[] array = itsConstantList;
    for (int i = 0; i != N; i++) {
      double num = array[i];
      String constantName = "_k" + i;
      String constantType = getStaticConstantWrapperType(num);
      cfw.addField(constantName, constantType, (short)10);
      
      int inum = (int)num;
      if (inum == num) {
        cfw.addPush(inum);
        cfw.addInvoke(184, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;");
      }
      else {
        cfw.addPush(num);
        addDoubleWrap(cfw);
      }
      cfw.add(179, mainClassName, constantName, constantType);
    }
    

    cfw.add(177);
    cfw.stopMethod((short)0);
  }
  
  void pushNumberAsObject(ClassFileWriter cfw, double num) {
    if (num == 0.0D) {
      if (1.0D / num > 0.0D)
      {
        cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime", "zeroObj", "Ljava/lang/Double;");
      }
      else
      {
        cfw.addPush(num);
        addDoubleWrap(cfw);
      }
    } else {
      if (num == 1.0D) {
        cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime", "oneObj", "Ljava/lang/Double;");
        

        return;
      }
      if (num == -1.0D) {
        cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime", "minusOneObj", "Ljava/lang/Double;");


      }
      else if (num != num) {
        cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/ScriptRuntime", "NaNobj", "Ljava/lang/Double;");


      }
      else if (itsConstantListSize >= 2000)
      {



        cfw.addPush(num);
        addDoubleWrap(cfw);
      }
      else {
        int N = itsConstantListSize;
        int index = 0;
        if (N == 0) {
          itsConstantList = new double[64];
        } else {
          double[] array = itsConstantList;
          while ((index != N) && (array[index] != num)) {
            index++;
          }
          if (N == array.length) {
            array = new double[N * 2];
            System.arraycopy(itsConstantList, 0, array, 0, N);
            itsConstantList = array;
          }
        }
        if (index == N) {
          itsConstantList[N] = num;
          itsConstantListSize = (N + 1);
        }
        String constantName = "_k" + index;
        String constantType = getStaticConstantWrapperType(num);
        cfw.add(178, mainClassName, constantName, constantType);
      }
    }
  }
  
  private static void addDoubleWrap(ClassFileWriter cfw) {
    cfw.addInvoke(184, "net/sourceforge/htmlunit/corejs/javascript/optimizer/OptRuntime", "wrapDouble", "(D)Ljava/lang/Double;");
  }
  

  private static String getStaticConstantWrapperType(double num)
  {
    int inum = (int)num;
    if (inum == num) {
      return "Ljava/lang/Integer;";
    }
    return "Ljava/lang/Double;";
  }
  
  static void pushUndefined(ClassFileWriter cfw)
  {
    cfw.add(178, "net/sourceforge/htmlunit/corejs/javascript/Undefined", "instance", "Ljava/lang/Object;");
  }
  

  int getIndex(ScriptNode n)
  {
    return scriptOrFnIndexes.getExisting(n);
  }
  
  String getDirectCtorName(ScriptNode n) {
    return "_n" + getIndex(n);
  }
  
  String getBodyMethodName(ScriptNode n) {
    return "_c_" + cleanName(n) + "_" + getIndex(n);
  }
  


  String cleanName(ScriptNode n)
  {
    String result = "";
    if ((n instanceof FunctionNode)) {
      net.sourceforge.htmlunit.corejs.javascript.ast.Name name = ((FunctionNode)n).getFunctionName();
      if (name == null) {
        result = "anonymous";
      } else {
        result = name.getIdentifier();
      }
    } else {
      result = "script";
    }
    return result;
  }
  
  String getBodyMethodSignature(ScriptNode n) {
    StringBuilder sb = new StringBuilder();
    sb.append('(');
    sb.append(mainClassSignature);
    sb.append("Lnet/sourceforge/htmlunit/corejs/javascript/Context;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;");
    

    if (n.getType() == 109) {
      OptFunctionNode ofn = OptFunctionNode.get(n);
      if (ofn.isTargetOfDirectCall()) {
        int pCount = fnode.getParamCount();
        for (int i = 0; i != pCount; i++) {
          sb.append("Ljava/lang/Object;D");
        }
      }
    }
    sb.append("[Ljava/lang/Object;)Ljava/lang/Object;");
    return sb.toString();
  }
  
  String getFunctionInitMethodName(OptFunctionNode ofn) {
    return "_i" + getIndex(fnode);
  }
  
  String getCompiledRegexpName(ScriptNode n, int regexpIndex) {
    return "_re" + getIndex(n) + "_" + regexpIndex;
  }
  
  static RuntimeException badTree() {
    throw new RuntimeException("Bad tree in codegen");
  }
  
  public void setMainMethodClass(String className) {
    mainMethodClass = className;
  }
  















  private static final Object globalLock = new Object();
  
  private static int globalSerialClassCounter;
  
  private CompilerEnvirons compilerEnv;
  
  private ObjArray directCallTargets;
  ScriptNode[] scriptOrFnNodes;
  private ObjToIntMap scriptOrFnIndexes;
  private String mainMethodClass = "net.sourceforge.htmlunit.corejs.javascript.optimizer.OptRuntime";
  String mainClassName;
  String mainClassSignature;
  private double[] itsConstantList;
  private int itsConstantListSize;
}

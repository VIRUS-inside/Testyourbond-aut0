package net.sourceforge.htmlunit.corejs.javascript;

import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;






final class InterpretedFunction
  extends NativeFunction
  implements Script
{
  static final long serialVersionUID = 541475680333911468L;
  InterpreterData idata;
  SecurityController securityController;
  Object securityDomain;
  private Arguments arguments;
  
  private InterpretedFunction(InterpreterData idata, Object staticSecurityDomain)
  {
    this.idata = idata;
    



    Context cx = Context.getContext();
    SecurityController sc = cx.getSecurityController();
    Object dynamicDomain;
    Object dynamicDomain; if (sc != null) {
      dynamicDomain = sc.getDynamicSecurityDomain(staticSecurityDomain);
    } else {
      if (staticSecurityDomain != null) {
        throw new IllegalArgumentException();
      }
      dynamicDomain = null;
    }
    
    securityController = sc;
    securityDomain = dynamicDomain;
  }
  
  private InterpretedFunction(InterpretedFunction parent, int index) {
    idata = idata.itsNestedFunctions[index];
    securityController = securityController;
    securityDomain = securityDomain;
  }
  




  static InterpretedFunction createScript(InterpreterData idata, Object staticSecurityDomain)
  {
    InterpretedFunction f = new InterpretedFunction(idata, staticSecurityDomain);
    return f;
  }
  




  static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpreterData idata, Object staticSecurityDomain)
  {
    InterpretedFunction f = new InterpretedFunction(idata, staticSecurityDomain);
    f.initScriptFunction(cx, scope);
    return f;
  }
  



  static InterpretedFunction createFunction(Context cx, Scriptable scope, InterpretedFunction parent, int index)
  {
    InterpretedFunction f = new InterpretedFunction(parent, index);
    f.initScriptFunction(cx, scope);
    return f;
  }
  
  public String getFunctionName()
  {
    return idata.itsName == null ? "" : idata.itsName;
  }
  















  public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    if (!ScriptRuntime.hasTopCall(cx)) {
      return ScriptRuntime.doTopCall(this, cx, scope, thisObj, args);
    }
    return Interpreter.interpret(this, cx, scope, thisObj, args);
  }
  
  public Object exec(Context cx, Scriptable scope) {
    if (!isScript())
    {
      throw new IllegalStateException();
    }
    if (!ScriptRuntime.hasTopCall(cx))
    {
      return ScriptRuntime.doTopCall(this, cx, scope, scope, ScriptRuntime.emptyArgs);
    }
    
    return Interpreter.interpret(this, cx, scope, scope, ScriptRuntime.emptyArgs);
  }
  
  public boolean isScript()
  {
    return idata.itsFunctionType == 0;
  }
  
  public String getEncodedSource()
  {
    return Interpreter.getEncodedSource(idata);
  }
  
  public DebuggableScript getDebuggableView()
  {
    return idata;
  }
  

  public Object resumeGenerator(Context cx, Scriptable scope, int operation, Object state, Object value)
  {
    return Interpreter.resumeGenerator(cx, scope, operation, state, value);
  }
  
  protected int getLanguageVersion()
  {
    return idata.languageVersion;
  }
  
  protected int getParamCount()
  {
    return idata.argCount;
  }
  
  protected int getParamAndVarCount()
  {
    return idata.argNames.length;
  }
  
  protected String getParamOrVarName(int index)
  {
    return idata.argNames[index];
  }
  
  protected boolean getParamOrVarConst(int index)
  {
    return idata.argIsConst[index];
  }
  




  public String toString()
  {
    return decompile(2, 0);
  }
  
  void setArguments(Arguments arguments) {
    if (arguments == null) {
      this.arguments = null;
      return;
    }
    
    Context currentContext = Context.getCurrentContext();
    
    if (currentContext.hasFeature(104)) {
      this.arguments = new Arguments(arguments)
      {
        public void put(int index, Scriptable start, Object value) {}
        




        public void put(String name, Scriptable start, Object value) {}
        




        public void delete(int index) {}
        



        public void delete(String name) {}
      };
    } else {
      this.arguments = arguments;
    }
  }
  
  public Object get(String name, Scriptable start)
  {
    if ((start == this) && ("arguments".equals(name))) {
      return arguments;
    }
    return super.get(name, start);
  }
}

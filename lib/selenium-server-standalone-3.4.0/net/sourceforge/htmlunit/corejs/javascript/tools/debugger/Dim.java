package net.sourceforge.htmlunit.corejs.javascript.tools.debugger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Kit;
import net.sourceforge.htmlunit.corejs.javascript.ObjArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebugFrame;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableObject;
import net.sourceforge.htmlunit.corejs.javascript.debug.DebuggableScript;
import net.sourceforge.htmlunit.corejs.javascript.debug.Debugger;

public class Dim
{
  public static final int STEP_OVER = 0;
  public static final int STEP_INTO = 1;
  public static final int STEP_OUT = 2;
  public static final int GO = 3;
  public static final int BREAK = 4;
  public static final int EXIT = 5;
  private static final int IPROXY_DEBUG = 0;
  private static final int IPROXY_LISTEN = 1;
  private static final int IPROXY_COMPILE_SCRIPT = 2;
  private static final int IPROXY_EVAL_SCRIPT = 3;
  private static final int IPROXY_STRING_IS_COMPILABLE = 4;
  private static final int IPROXY_OBJECT_TO_STRING = 5;
  private static final int IPROXY_OBJECT_PROPERTY = 6;
  private static final int IPROXY_OBJECT_IDS = 7;
  private GuiCallback callback;
  private boolean breakFlag;
  private ScopeProvider scopeProvider;
  private SourceProvider sourceProvider;
  private int frameIndex;
  private volatile ContextData interruptedContextData;
  private ContextFactory contextFactory;
  private Object monitor;
  private Object eventThreadMonitor;
  private volatile int returnValue;
  private boolean insideInterruptLoop;
  private String evalRequest;
  private StackFrame evalFrame;
  private String evalResult;
  private boolean breakOnExceptions;
  private boolean breakOnEnter;
  private boolean breakOnReturn;
  private final Map<String, SourceInfo> urlToSourceInfo;
  private final Map<String, FunctionSource> functionNames;
  
  public Dim()
  {
    frameIndex = -1;
    














    monitor = new Object();
    




    eventThreadMonitor = new Object();
    



    returnValue = -1;
    









































    urlToSourceInfo = Collections.synchronizedMap(new HashMap());
    




    functionNames = Collections.synchronizedMap(new HashMap());
  }
  



  private final Map<DebuggableScript, FunctionSource> functionToSource = Collections.synchronizedMap(new HashMap());
  


  private DimIProxy listener;
  



  public void setGuiCallback(GuiCallback callback)
  {
    this.callback = callback;
  }
  


  public void setBreak()
  {
    breakFlag = true;
  }
  


  public void setScopeProvider(ScopeProvider scopeProvider)
  {
    this.scopeProvider = scopeProvider;
  }
  


  public void setSourceProvider(SourceProvider sourceProvider)
  {
    this.sourceProvider = sourceProvider;
  }
  


  public void contextSwitch(int frameIndex)
  {
    this.frameIndex = frameIndex;
  }
  


  public void setBreakOnExceptions(boolean breakOnExceptions)
  {
    this.breakOnExceptions = breakOnExceptions;
  }
  


  public void setBreakOnEnter(boolean breakOnEnter)
  {
    this.breakOnEnter = breakOnEnter;
  }
  


  public void setBreakOnReturn(boolean breakOnReturn)
  {
    this.breakOnReturn = breakOnReturn;
  }
  


  public void attachTo(ContextFactory factory)
  {
    detach();
    contextFactory = factory;
    listener = new DimIProxy(this, 1, null);
    factory.addListener(listener);
  }
  


  public void detach()
  {
    if (listener != null) {
      contextFactory.removeListener(listener);
      contextFactory = null;
      listener = null;
    }
  }
  


  public void dispose()
  {
    detach();
  }
  


  private FunctionSource getFunctionSource(DebuggableScript fnOrScript)
  {
    FunctionSource fsource = functionSource(fnOrScript);
    if (fsource == null) {
      String url = getNormalizedUrl(fnOrScript);
      SourceInfo si = sourceInfo(url);
      if ((si == null) && 
        (!fnOrScript.isGeneratedScript()))
      {
        String source = loadSource(url);
        if (source != null) {
          DebuggableScript top = fnOrScript;
          for (;;) {
            DebuggableScript parent = top.getParent();
            if (parent == null) {
              break;
            }
            top = parent;
          }
          registerTopScript(top, source);
          fsource = functionSource(fnOrScript);
        }
      }
    }
    
    return fsource;
  }
  


  private String loadSource(String sourceUrl)
  {
    String source = null;
    int hash = sourceUrl.indexOf('#');
    if (hash >= 0) {
      sourceUrl = sourceUrl.substring(0, hash);
    }
    
    try
    {
      if (sourceUrl.indexOf(':') < 0)
      {
        try {
          if (sourceUrl.startsWith("~/"))
          {
            String home = net.sourceforge.htmlunit.corejs.javascript.SecurityUtilities.getSystemProperty("user.home");
            if (home != null) {
              String pathFromHome = sourceUrl.substring(2);
              File f = new File(new File(home), pathFromHome);
              if (f.exists()) {
                InputStream is = new FileInputStream(f);
                break label233;
              }
            }
          }
          File f = new File(sourceUrl);
          if (f.exists()) {
            InputStream is = new FileInputStream(f);
            
            break label233;
          }
        }
        catch (SecurityException localSecurityException) {}
        if (sourceUrl.startsWith("//")) {
          sourceUrl = "http:" + sourceUrl;
        } else if (sourceUrl.startsWith("/")) {
          sourceUrl = "http://127.0.0.1" + sourceUrl;
        } else {
          sourceUrl = "http://" + sourceUrl;
        }
      }
      
      InputStream is = new URL(sourceUrl).openStream();
      try
      {
        label233:
        source = Kit.readReader(new java.io.InputStreamReader(is));
      } finally {
        is.close();
      }
    } catch (IOException ex) {
      System.err.println("Failed to load source from " + sourceUrl + ": " + ex);
    }
    
    return source;
  }
  


  private void registerTopScript(DebuggableScript topScript, String source)
  {
    if (!topScript.isTopLevel()) {
      throw new IllegalArgumentException();
    }
    String url = getNormalizedUrl(topScript);
    DebuggableScript[] functions = getAllFunctions(topScript);
    if (sourceProvider != null) {
      String providedSource = sourceProvider.getSource(topScript);
      if (providedSource != null) {
        source = providedSource;
      }
    }
    
    SourceInfo sourceInfo = new SourceInfo(source, functions, url, null);
    
    synchronized (urlToSourceInfo) {
      SourceInfo old = (SourceInfo)urlToSourceInfo.get(url);
      if (old != null) {
        sourceInfo.copyBreakpointsFrom(old);
      }
      urlToSourceInfo.put(url, sourceInfo);
      for (int i = 0; i != sourceInfo.functionSourcesTop(); i++) {
        FunctionSource fsource = sourceInfo.functionSource(i);
        String name = fsource.name();
        if (name.length() != 0) {
          functionNames.put(name, fsource);
        }
      }
    }
    
    synchronized (functionToSource) {
      for (int i = 0; i != functions.length; i++) {
        FunctionSource fsource = sourceInfo.functionSource(i);
        functionToSource.put(functions[i], fsource);
      }
    }
    
    callback.updateSourceText(sourceInfo);
  }
  


  private FunctionSource functionSource(DebuggableScript fnOrScript)
  {
    return (FunctionSource)functionToSource.get(fnOrScript);
  }
  


  public String[] functionNames()
  {
    synchronized (urlToSourceInfo) {
      return 
        (String[])functionNames.keySet().toArray(new String[functionNames.size()]);
    }
  }
  


  public FunctionSource functionSourceByName(String functionName)
  {
    return (FunctionSource)functionNames.get(functionName);
  }
  


  public SourceInfo sourceInfo(String url)
  {
    return (SourceInfo)urlToSourceInfo.get(url);
  }
  


  private String getNormalizedUrl(DebuggableScript fnOrScript)
  {
    String url = fnOrScript.getSourceName();
    if (url == null) {
      url = "<stdin>";

    }
    else
    {

      char evalSeparator = '#';
      StringBuilder sb = null;
      int urlLength = url.length();
      int cursor = 0;
      for (;;) {
        int searchStart = url.indexOf(evalSeparator, cursor);
        if (searchStart < 0) {
          break;
        }
        String replace = null;
        int i = searchStart + 1;
        while (i != urlLength) {
          int c = url.charAt(i);
          if ((48 > c) || (c > 57)) {
            break;
          }
          i++;
        }
        if (i != searchStart + 1)
        {
          if ("(eval)".regionMatches(0, url, i, 6)) {
            cursor = i + 6;
            replace = "(eval)";
          }
        }
        if (replace == null) {
          break;
        }
        if (sb == null) {
          sb = new StringBuilder();
          sb.append(url.substring(0, searchStart));
        }
        sb.append(replace);
      }
      if (sb != null) {
        if (cursor != urlLength) {
          sb.append(url.substring(cursor));
        }
        url = sb.toString();
      }
    }
    return url;
  }
  



  private static DebuggableScript[] getAllFunctions(DebuggableScript function)
  {
    ObjArray functions = new ObjArray();
    collectFunctions_r(function, functions);
    DebuggableScript[] result = new DebuggableScript[functions.size()];
    functions.toArray(result);
    return result;
  }
  



  private static void collectFunctions_r(DebuggableScript function, ObjArray array)
  {
    array.add(function);
    for (int i = 0; i != function.getFunctionCount(); i++) {
      collectFunctions_r(function.getFunction(i), array);
    }
  }
  


  public void clearAllBreakpoints()
  {
    for (SourceInfo si : urlToSourceInfo.values()) {
      si.removeAllBreakpoints();
    }
  }
  


  private void handleBreakpointHit(StackFrame frame, Context cx)
  {
    breakFlag = false;
    interrupted(cx, frame, null);
  }
  



  private void handleExceptionThrown(Context cx, Throwable ex, StackFrame frame)
  {
    if (breakOnExceptions) {
      ContextData cd = frame.contextData();
      if (lastProcessedException != ex) {
        interrupted(cx, frame, ex);
        lastProcessedException = ex;
      }
    }
  }
  


  public ContextData currentContextData()
  {
    return interruptedContextData;
  }
  


  public void setReturnValue(int returnValue)
  {
    synchronized (monitor) {
      this.returnValue = returnValue;
      monitor.notify();
    }
  }
  


  public void go()
  {
    synchronized (monitor) {
      returnValue = 3;
      monitor.notifyAll();
    }
  }
  


  public String eval(String expr)
  {
    String result = "undefined";
    if (expr == null) {
      return result;
    }
    ContextData contextData = currentContextData();
    if ((contextData == null) || (frameIndex >= contextData.frameCount())) {
      return result;
    }
    StackFrame frame = contextData.getFrame(frameIndex);
    if (eventThreadFlag) {
      Context cx = Context.getCurrentContext();
      result = do_eval(cx, frame, expr);
    } else {
      synchronized (monitor) {
        if (insideInterruptLoop) {
          evalRequest = expr;
          evalFrame = frame;
          monitor.notify();
          do {
            try {
              monitor.wait();
            } catch (InterruptedException exc) {
              Thread.currentThread().interrupt();
              break;
            }
          } while (evalRequest != null);
          result = evalResult;
        }
      }
    }
    return result;
  }
  


  public void compileScript(String url, String text)
  {
    DimIProxy action = new DimIProxy(this, 2, null);
    url = url;
    text = text;
    action.withContext();
  }
  


  public void evalScript(String url, String text)
  {
    DimIProxy action = new DimIProxy(this, 3, null);
    url = url;
    text = text;
    action.withContext();
  }
  


  public String objectToString(Object object)
  {
    DimIProxy action = new DimIProxy(this, 5, null);
    object = object;
    action.withContext();
    return stringResult;
  }
  


  public boolean stringIsCompilableUnit(String str)
  {
    DimIProxy action = new DimIProxy(this, 4, null);
    text = str;
    action.withContext();
    return booleanResult;
  }
  


  public Object getObjectProperty(Object object, Object id)
  {
    DimIProxy action = new DimIProxy(this, 6, null);
    object = object;
    id = id;
    action.withContext();
    return objectResult;
  }
  


  public Object[] getObjectIds(Object object)
  {
    DimIProxy action = new DimIProxy(this, 7, null);
    object = object;
    action.withContext();
    return objectArrayResult;
  }
  


  private Object getObjectPropertyImpl(Context cx, Object object, Object id)
  {
    Scriptable scriptable = (Scriptable)object;
    Object result;
    if ((id instanceof String)) {
      String name = (String)id;
      Object result; if (name.equals("this")) {
        result = scriptable; } else { Object result;
        if (name.equals("__proto__")) {
          result = scriptable.getPrototype(); } else { Object result;
          if (name.equals("__parent__")) {
            result = scriptable.getParentScope();
          } else {
            Object result = ScriptableObject.getProperty(scriptable, name);
            if (result == ScriptableObject.NOT_FOUND)
              result = Undefined.instance;
          }
        }
      }
    } else { int index = ((Integer)id).intValue();
      result = ScriptableObject.getProperty(scriptable, index);
      if (result == ScriptableObject.NOT_FOUND) {
        result = Undefined.instance;
      }
    }
    return result;
  }
  


  private Object[] getObjectIdsImpl(Context cx, Object object)
  {
    if ((!(object instanceof Scriptable)) || (object == Undefined.instance)) {
      return Context.emptyArgs;
    }
    

    Scriptable scriptable = (Scriptable)object;
    Object[] ids; Object[] ids; if ((scriptable instanceof DebuggableObject)) {
      ids = ((DebuggableObject)scriptable).getAllIds();
    } else {
      ids = scriptable.getIds();
    }
    
    Scriptable proto = scriptable.getPrototype();
    Scriptable parent = scriptable.getParentScope();
    int extra = 0;
    if (proto != null) {
      extra++;
    }
    if (parent != null) {
      extra++;
    }
    if (extra != 0) {
      Object[] tmp = new Object[extra + ids.length];
      System.arraycopy(ids, 0, tmp, extra, ids.length);
      ids = tmp;
      extra = 0;
      if (proto != null) {
        ids[(extra++)] = "__proto__";
      }
      if (parent != null) {
        ids[(extra++)] = "__parent__";
      }
    }
    
    return ids;
  }
  



  private void interrupted(Context cx, StackFrame frame, Throwable scriptException)
  {
    ContextData contextData = frame.contextData();
    boolean eventThreadFlag = callback.isGuiEventThread();
    eventThreadFlag = eventThreadFlag;
    
    boolean recursiveEventThreadCall = false;
    
    synchronized (eventThreadMonitor) {
      if (eventThreadFlag) {
        if (interruptedContextData != null) {
          recursiveEventThreadCall = true;
          break label100;
        }
      } else {
        while (interruptedContextData != null) {
          try {
            eventThreadMonitor.wait();
          } catch (InterruptedException exc) {
            return;
          }
        }
      }
      interruptedContextData = contextData;
    }
    label100:
    if (recursiveEventThreadCall)
    {













      return;
    }
    
    if (interruptedContextData == null) {
      Kit.codeBug();
    }
    try
    {
      int frameCount = contextData.frameCount();
      frameIndex = (frameCount - 1);
      
      String threadTitle = Thread.currentThread().toString();
      String alertMessage;
      String alertMessage; if (scriptException == null) {
        alertMessage = null;
      } else {
        alertMessage = scriptException.toString();
      }
      
      int returnValue = -1;
      if (!eventThreadFlag) {
        synchronized (monitor) {
          if (insideInterruptLoop)
            Kit.codeBug();
          insideInterruptLoop = true;
          evalRequest = null;
          this.returnValue = -1;
          callback.enterInterrupt(frame, threadTitle, alertMessage);
          try {
            do {
              for (;;) {
                try {
                  monitor.wait();
                } catch (InterruptedException exc) {
                  Thread.currentThread().interrupt();
                  break label323;
                }
                if (evalRequest == null) break;
                evalResult = null;
                try {
                  evalResult = do_eval(cx, evalFrame, evalRequest);
                }
                finally {
                  evalRequest = null;
                  evalFrame = null;
                  monitor.notify();
                }
                
              }
            } while (this.returnValue == -1);
            returnValue = this.returnValue;
          }
          finally
          {
            label323:
            insideInterruptLoop = false;
          }
        }
      } else {
        this.returnValue = -1;
        callback.enterInterrupt(frame, threadTitle, alertMessage);
        while (this.returnValue == -1) {
          try {
            callback.dispatchNextGuiEvent();
          }
          catch (InterruptedException localInterruptedException1) {}
        }
        returnValue = this.returnValue;
      }
      switch (returnValue) {
      case 0: 
        breakNextLine = true;
        stopAtFrameDepth = contextData.frameCount();
        break;
      case 1: 
        breakNextLine = true;
        stopAtFrameDepth = -1;
        break;
      case 2: 
        if (contextData.frameCount() > 1) {
          breakNextLine = true;
          stopAtFrameDepth = (contextData.frameCount() - 1);
        }
        break;
      }
    }
    finally
    {
      synchronized (eventThreadMonitor) {
        interruptedContextData = null;
        eventThreadMonitor.notifyAll();
      }
    }
  }
  




  private static String do_eval(Context cx, StackFrame frame, String expr)
  {
    Debugger saved_debugger = cx.getDebugger();
    Object saved_data = cx.getDebuggerContextData();
    int saved_level = cx.getOptimizationLevel();
    
    cx.setDebugger(null, null);
    cx.setOptimizationLevel(-1);
    cx.setGeneratingDebug(false);
    try {
      Callable script = (Callable)cx.compileString(expr, "", 0, null);
      Object result = script.call(cx, scope, thisObj, net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.emptyArgs);
      String resultString;
      if (result == Undefined.instance) {
        resultString = "";
      } else
        resultString = net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime.toString(result);
    } catch (Exception exc) {
      String resultString;
      resultString = exc.getMessage();
    } finally { String resultString;
      cx.setGeneratingDebug(true);
      cx.setOptimizationLevel(saved_level);
      cx.setDebugger(saved_debugger, saved_data); }
    String resultString;
    if (resultString == null) {
      resultString = "null";
    }
    return resultString;
  }
  





  private static class DimIProxy
    implements net.sourceforge.htmlunit.corejs.javascript.ContextAction, net.sourceforge.htmlunit.corejs.javascript.ContextFactory.Listener, Debugger
  {
    private Dim dim;
    



    private int type;
    



    private String url;
    



    private String text;
    



    private Object object;
    



    private Object id;
    



    private boolean booleanResult;
    



    private String stringResult;
    



    private Object objectResult;
    



    private Object[] objectArrayResult;
    




    private DimIProxy(Dim dim, int type)
    {
      this.dim = dim;
      this.type = type;
    }
    




    public Object run(Context cx)
    {
      switch (type) {
      case 2: 
        cx.compileString(text, url, 1, null);
        break;
      
      case 3: 
        Scriptable scope = null;
        if (dim.scopeProvider != null) {
          scope = dim.scopeProvider.getScope();
        }
        if (scope == null) {
          scope = new net.sourceforge.htmlunit.corejs.javascript.ImporterTopLevel(cx);
        }
        cx.evaluateString(scope, text, url, 1, null);
        
        break;
      
      case 4: 
        booleanResult = cx.stringIsCompilableUnit(text);
        break;
      
      case 5: 
        if (object == Undefined.instance) {
          stringResult = "undefined";
        } else if (object == null) {
          stringResult = "null";
        } else if ((object instanceof net.sourceforge.htmlunit.corejs.javascript.NativeCall)) {
          stringResult = "[object Call]";
        } else {
          stringResult = Context.toString(object);
        }
        break;
      
      case 6: 
        objectResult = dim.getObjectPropertyImpl(cx, object, id);
        break;
      
      case 7: 
        objectArrayResult = dim.getObjectIdsImpl(cx, object);
        break;
      
      default: 
        throw Kit.codeBug();
      }
      return null;
    }
    



    private void withContext()
    {
      dim.contextFactory.call(this);
    }
    




    public void contextCreated(Context cx)
    {
      if (type != 1)
        Kit.codeBug();
      Dim.ContextData contextData = new Dim.ContextData();
      Debugger debugger = new DimIProxy(dim, 0);
      cx.setDebugger(debugger, contextData);
      cx.setGeneratingDebug(true);
      cx.setOptimizationLevel(-1);
    }
    


    public void contextReleased(Context cx)
    {
      if (type != 1) {
        Kit.codeBug();
      }
    }
    



    public DebugFrame getFrame(Context cx, DebuggableScript fnOrScript)
    {
      if (type != 0) {
        Kit.codeBug();
      }
      Dim.FunctionSource item = dim.getFunctionSource(fnOrScript);
      if (item == null)
      {
        return null;
      }
      return new Dim.StackFrame(cx, dim, item, null);
    }
    



    public void handleCompilationDone(Context cx, DebuggableScript fnOrScript, String source)
    {
      if (type != 0) {
        Kit.codeBug();
      }
      if (!fnOrScript.isTopLevel()) {
        return;
      }
      dim.registerTopScript(fnOrScript, source);
    }
  }
  






  public static class ContextData
  {
    private ObjArray frameStack = new ObjArray();
    




    private boolean breakNextLine;
    



    private int stopAtFrameDepth = -1;
    

    private boolean eventThreadFlag;
    

    private Throwable lastProcessedException;
    


    public ContextData() {}
    


    public static ContextData get(Context cx)
    {
      return (ContextData)cx.getDebuggerContextData();
    }
    


    public int frameCount()
    {
      return frameStack.size();
    }
    


    public Dim.StackFrame getFrame(int frameNumber)
    {
      int num = frameStack.size() - frameNumber - 1;
      return (Dim.StackFrame)frameStack.get(num);
    }
    


    private void pushFrame(Dim.StackFrame frame)
    {
      frameStack.push(frame);
    }
    


    private void popFrame()
    {
      frameStack.pop();
    }
  }
  




  public static class StackFrame
    implements DebugFrame
  {
    private Dim dim;
    



    private Dim.ContextData contextData;
    



    private Scriptable scope;
    



    private Scriptable thisObj;
    



    private Dim.FunctionSource fsource;
    



    private boolean[] breakpoints;
    



    private int lineNumber;
    



    private StackFrame(Context cx, Dim dim, Dim.FunctionSource fsource)
    {
      this.dim = dim;
      contextData = Dim.ContextData.get(cx);
      this.fsource = fsource;
      breakpoints = sourceInfobreakpoints;
      lineNumber = fsource.firstLine();
    }
    



    public void onEnter(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
    {
      Dim.ContextData.access$2600(contextData, this);
      this.scope = scope;
      this.thisObj = thisObj;
      if (dim.breakOnEnter) {
        dim.handleBreakpointHit(this, cx);
      }
    }
    


    public void onLineChange(Context cx, int lineno)
    {
      lineNumber = lineno;
      
      if ((breakpoints[lineno] == 0) && (!dim.breakFlag)) {
        boolean lineBreak = Dim.ContextData.access$1400(contextData);
        if ((lineBreak) && (Dim.ContextData.access$1500(contextData) >= 0))
        {
          lineBreak = contextData.frameCount() <= Dim.ContextData.access$1500(contextData);
        }
        if (!lineBreak) {
          return;
        }
        Dim.ContextData.access$1502(contextData, -1);
        Dim.ContextData.access$1402(contextData, false);
      }
      
      dim.handleBreakpointHit(this, cx);
    }
    


    public void onExceptionThrown(Context cx, Throwable exception)
    {
      dim.handleExceptionThrown(cx, exception, this);
    }
    



    public void onExit(Context cx, boolean byThrow, Object resultOrException)
    {
      if ((dim.breakOnReturn) && (!byThrow)) {
        dim.handleBreakpointHit(this, cx);
      }
      Dim.ContextData.access$3200(contextData);
    }
    


    public void onDebuggerStatement(Context cx)
    {
      dim.handleBreakpointHit(this, cx);
    }
    


    public Dim.SourceInfo sourceInfo()
    {
      return fsource.sourceInfo();
    }
    


    public Dim.ContextData contextData()
    {
      return contextData;
    }
    


    public Object scope()
    {
      return scope;
    }
    


    public Object thisObj()
    {
      return thisObj;
    }
    


    public String getUrl()
    {
      return fsource.sourceInfo().url();
    }
    


    public int getLineNumber()
    {
      return lineNumber;
    }
    


    public String getFunctionName()
    {
      return fsource.name();
    }
  }
  





  public static class FunctionSource
  {
    private Dim.SourceInfo sourceInfo;
    



    private int firstLine;
    



    private String name;
    




    private FunctionSource(Dim.SourceInfo sourceInfo, int firstLine, String name)
    {
      if (name == null)
        throw new IllegalArgumentException();
      this.sourceInfo = sourceInfo;
      this.firstLine = firstLine;
      this.name = name;
    }
    



    public Dim.SourceInfo sourceInfo()
    {
      return sourceInfo;
    }
    


    public int firstLine()
    {
      return firstLine;
    }
    


    public String name()
    {
      return name;
    }
  }
  






  public static class SourceInfo
  {
    private static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
    



    private String source;
    



    private String url;
    



    private boolean[] breakableLines;
    



    private boolean[] breakpoints;
    



    private Dim.FunctionSource[] functionSources;
    



    private SourceInfo(String source, DebuggableScript[] functions, String normilizedUrl)
    {
      this.source = source;
      url = normilizedUrl;
      
      int N = functions.length;
      int[][] lineArrays = new int[N][];
      for (int i = 0; i != N; i++) {
        lineArrays[i] = functions[i].getLineNumbers();
      }
      
      int minAll = 0;int maxAll = -1;
      int[] firstLines = new int[N];
      for (int i = 0; i != N; i++) {
        int[] lines = lineArrays[i];
        if ((lines == null) || (lines.length == 0)) {
          firstLines[i] = -1;
        } else {
          int max;
          int min = max = lines[0];
          for (int j = 1; j != lines.length; j++) {
            int line = lines[j];
            if (line < min) {
              min = line;
            } else if (line > max) {
              max = line;
            }
          }
          firstLines[i] = min;
          if (minAll > maxAll) {
            minAll = min;
            maxAll = max;
          } else {
            if (min < minAll) {
              minAll = min;
            }
            if (max > maxAll) {
              maxAll = max;
            }
          }
        }
      }
      
      if (minAll > maxAll)
      {
        breakableLines = EMPTY_BOOLEAN_ARRAY;
        breakpoints = EMPTY_BOOLEAN_ARRAY;
      } else {
        if (minAll < 0)
        {
          throw new IllegalStateException(String.valueOf(minAll));
        }
        int linesTop = maxAll + 1;
        breakableLines = new boolean[linesTop];
        breakpoints = new boolean[linesTop];
        for (int i = 0; i != N; i++) {
          int[] lines = lineArrays[i];
          if ((lines != null) && (lines.length != 0)) {
            for (int j = 0; j != lines.length; j++) {
              int line = lines[j];
              breakableLines[line] = true;
            }
          }
        }
      }
      functionSources = new Dim.FunctionSource[N];
      for (int i = 0; i != N; i++) {
        String name = functions[i].getFunctionName();
        if (name == null) {
          name = "";
        }
        functionSources[i] = new Dim.FunctionSource(this, firstLines[i], name, null);
      }
    }
    



    public String source()
    {
      return source;
    }
    


    public String url()
    {
      return url;
    }
    


    public int functionSourcesTop()
    {
      return functionSources.length;
    }
    


    public Dim.FunctionSource functionSource(int i)
    {
      return functionSources[i];
    }
    



    private void copyBreakpointsFrom(SourceInfo old)
    {
      int end = breakpoints.length;
      if (end > breakpoints.length) {
        end = breakpoints.length;
      }
      for (int line = 0; line != end; line++) {
        if (breakpoints[line] != 0) {
          breakpoints[line] = true;
        }
      }
    }
    



    public boolean breakableLine(int line)
    {
      return (line < breakableLines.length) && (breakableLines[line] != 0);
    }
    



    public boolean breakpoint(int line)
    {
      if (!breakableLine(line)) {
        throw new IllegalArgumentException(String.valueOf(line));
      }
      return (line < breakpoints.length) && (breakpoints[line] != 0);
    }
    


    public boolean breakpoint(int line, boolean value)
    {
      if (!breakableLine(line)) {
        throw new IllegalArgumentException(String.valueOf(line));
      }
      boolean changed;
      synchronized (breakpoints) { boolean changed;
        if (breakpoints[line] != value) {
          breakpoints[line] = value;
          changed = true;
        } else {
          changed = false;
        } }
      boolean changed;
      return changed;
    }
    


    public void removeAllBreakpoints()
    {
      synchronized (breakpoints) {
        for (int line = 0; line != breakpoints.length; line++) {
          breakpoints[line] = false;
        }
      }
    }
  }
}

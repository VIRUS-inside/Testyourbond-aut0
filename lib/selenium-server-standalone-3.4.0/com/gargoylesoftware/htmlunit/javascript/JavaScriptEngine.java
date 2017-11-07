package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.ConstantInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration.PropertyInfo;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObject;
import com.gargoylesoftware.htmlunit.javascript.host.DateCustom;
import com.gargoylesoftware.htmlunit.javascript.host.Reflect;
import com.gargoylesoftware.htmlunit.javascript.host.StringCustom;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.intl.Intl;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import net.sourceforge.htmlunit.corejs.javascript.BaseFunction;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.FunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.IdFunctionObject;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.UniqueTag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

















































public class JavaScriptEngine
  implements AbstractJavaScriptEngine
{
  private static final Log LOG = LogFactory.getLog(JavaScriptEngine.class);
  

  private WebClient webClient_;
  

  private final HtmlUnitContextFactory contextFactory_;
  

  private final JavaScriptConfiguration jsConfig_;
  

  private transient ThreadLocal<Boolean> javaScriptRunning_;
  

  private transient ThreadLocal<List<PostponedAction>> postponedActions_;
  

  private transient boolean holdPostponedActions_;
  

  private transient JavaScriptExecutor javaScriptExecutor_;
  

  public static final String KEY_STARTING_SCOPE = "startingScope";
  

  public static final String KEY_STARTING_PAGE = "startingPage";
  


  public JavaScriptEngine(WebClient webClient)
  {
    webClient_ = webClient;
    contextFactory_ = new HtmlUnitContextFactory(webClient);
    initTransientFields();
    jsConfig_ = JavaScriptConfiguration.getInstance(webClient.getBrowserVersion());
  }
  



  public final WebClient getWebClient()
  {
    return webClient_;
  }
  



  public HtmlUnitContextFactory getContextFactory()
  {
    return contextFactory_;
  }
  




  public void initialize(final WebWindow webWindow)
  {
    WebAssert.notNull("webWindow", webWindow);
    
    ContextAction action = new ContextAction()
    {
      public Object run(Context cx) {
        try {
          JavaScriptEngine.this.init(webWindow, cx);
        }
        catch (Exception e) {
          JavaScriptEngine.LOG.error("Exception while initializing JavaScript for the page", e);
          throw new ScriptException(null, e);
        }
        
        return null;
      }
      
    };
    getContextFactory().call(action);
  }
  



  public JavaScriptExecutor getJavaScriptExecutor()
  {
    return javaScriptExecutor_;
  }
  




  private void init(WebWindow webWindow, Context context)
    throws Exception
  {
    WebClient webClient = webWindow.getWebClient();
    BrowserVersion browserVersion = webClient.getBrowserVersion();
    Map<Class<? extends Scriptable>, Scriptable> prototypes = new HashMap();
    Map<String, Scriptable> prototypesPerJSName = new HashMap();
    
    Window window = new Window();
    window.setClassName("Window");
    context.initStandardObjects(window);
    
    ClassConfiguration windowConfig = jsConfig_.getClassConfiguration("Window");
    if (windowConfig.getJsConstructor() != null) {
      FunctionObject functionObject = new RecursiveFunctionObject("Window", 
        windowConfig.getJsConstructor(), window);
      ScriptableObject.defineProperty(window, "constructor", functionObject, 
        7);
    }
    else {
      defineConstructor(window, window, new Window());
    }
    

    deleteProperties(window, new String[] { "java", "javax", "org", "com", "edu", "net", 
      "JavaAdapter", "JavaImporter", "Continuation", "Packages", "getClass" });
    if (!browserVersion.hasFeature(BrowserVersionFeatures.JS_XML)) {
      deleteProperties(window, new String[] { "XML", "XMLList", "Namespace", "QName" });
    }
    
    if (!browserVersion.hasFeature(BrowserVersionFeatures.JS_Iterator)) {
      deleteProperties(window, new String[] { "Iterator", "StopIteration" });
    }
    
    ScriptableObject errorObject = (ScriptableObject)ScriptableObject.getProperty(window, "Error");
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_ERROR_STACK_TRACE_LIMIT)) {
      errorObject.defineProperty("stackTraceLimit", Integer.valueOf(10), 0);
    }
    else {
      ScriptableObject.deleteProperty(errorObject, "stackTraceLimit");
    }
    
    Intl intl = new Intl();
    intl.setParentScope(window);
    window.defineProperty(intl.getClassName(), intl, 2);
    intl.defineProperties(browserVersion);
    
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_REFLECT)) {
      Reflect reflect = new Reflect();
      reflect.setParentScope(window);
      window.defineProperty(reflect.getClassName(), reflect, 2);
      reflect.defineProperties();
    }
    
    for (ClassConfiguration config : jsConfig_.getAll()) {
      boolean isWindow = Window.class.getName().equals(config.getHostClass().getName());
      if (isWindow) {
        configureConstantsPropertiesAndFunctions(config, window);
        
        HtmlUnitScriptable prototype = configureClass(config, window, browserVersion);
        prototypesPerJSName.put(config.getClassName(), prototype);
      }
      else {
        HtmlUnitScriptable prototype = configureClass(config, window, browserVersion);
        if (config.isJsObject())
        {
          HtmlUnitScriptable obj = (HtmlUnitScriptable)config.getHostClass().newInstance();
          prototype.defineProperty("__proto__", prototype, 2);
          obj.defineProperty("prototype", prototype, 2);
          obj.setParentScope(window);
          obj.setClassName(config.getClassName());
          ScriptableObject.defineProperty(window, obj.getClassName(), obj, 2);
          
          configureConstants(config, obj);
        }
        prototypes.put(config.getHostClass(), prototype);
        prototypesPerJSName.put(config.getClassName(), prototype);
      } }
    Member jsConstructor;
    label1321:
    for (ClassConfiguration config : jsConfig_.getAll()) {
      jsConstructor = config.getJsConstructor();
      String jsClassName = config.getClassName();
      Scriptable prototype = (Scriptable)prototypesPerJSName.get(jsClassName);
      String hostClassSimpleName = config.getHostClassSimpleName();
      if (("Image".equals(hostClassSimpleName)) && 
        (browserVersion.hasFeature(BrowserVersionFeatures.JS_IMAGE_PROTOTYPE_SAME_AS_HTML_IMAGE))) {
        prototype = (Scriptable)prototypesPerJSName.get("HTMLImageElement");
      }
      if (("Option".equals(hostClassSimpleName)) && 
        (browserVersion.hasFeature(BrowserVersionFeatures.JS_OPTION_PROTOTYPE_SAME_AS_HTML_OPTION))) {
        prototype = (Scriptable)prototypesPerJSName.get("HTMLOptionElement");
      }
      String str1;
      switch ((str1 = hostClassSimpleName).hashCode()) {case -1925471606:  if (str1.equals("webkitIDBRequest")) {} break; case -1777131805:  if (str1.equals("WebKitTransitionEvent")) {} break; case -1672322848:  if (str1.equals("webkitIDBDatabase")) {} break; case -1662882790:  if (str1.equals("webkitOfflineAudioContext")) {} break; case -1519448743:  if (str1.equals("webkitIDBTransaction")) {} break; case -1275975743:  if (str1.equals("WebKitMutationObserver")) break; break; case -1230218981:  if (str1.equals("webkitAudioContext")) {} break; case -944137331:  if (str1.equals("webkitIDBIndex")) {} break; case -653996195:  if (str1.equals("webkitIDBObjectStore")) {} break; case -463655155:  if (str1.equals("webkitURL")) {} break; case 181920005:  if (str1.equals("webkitIDBFactory")) {} break; case 631633915:  if (str1.equals("webkitIDBCursor")) {} break; case 1321737603:  if (str1.equals("webkitIDBKeyRange")) {} case 1722609944:  if ((goto 1321) && (str1.equals("WebKitAnimationEvent")))
        {
          prototype = (Scriptable)prototypesPerJSName.get("AnimationEvent");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("MutationObserver");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("TransitionEvent");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("AudioContext");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBCursor");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBDatabase");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBFactory");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBIndex");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBKeyRange");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBObjectStore");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBRequest");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("IDBTransaction");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("OfflineAudioContext");
          
          break label1321;
          
          prototype = (Scriptable)prototypesPerJSName.get("URL");
        }
        break;
      }
      
      if ((prototype != null) && (config.isJsObject())) {
        if (jsConstructor == null) { ScriptableObject constructor;
          ScriptableObject constructor;
          if ("Window".equals(jsClassName)) {
            constructor = (ScriptableObject)ScriptableObject.getProperty(window, "constructor");
          }
          else {
            constructor = (ScriptableObject)config.getHostClass().newInstance();
            ((SimpleScriptable)constructor).setClassName(config.getClassName());
          }
          defineConstructor(window, prototype, constructor);
          configureConstantsStaticPropertiesAndStaticFunctions(config, constructor);
        } else {
          BaseFunction function;
          BaseFunction function;
          if ("Window".equals(jsClassName)) {
            function = (BaseFunction)ScriptableObject.getProperty(window, "constructor");
          }
          else {
            function = new RecursiveFunctionObject(jsClassName, jsConstructor, window);
          }
          
          if (("WebKitAnimationEvent".equals(hostClassSimpleName)) || 
            ("WebKitMutationObserver".equals(hostClassSimpleName)) || 
            ("WebKitTransitionEvent".equals(hostClassSimpleName)) || 
            ("webkitAudioContext".equals(hostClassSimpleName)) || 
            ("webkitIDBCursor".equals(hostClassSimpleName)) || 
            ("webkitIDBDatabase".equals(hostClassSimpleName)) || 
            ("webkitIDBFactory".equals(hostClassSimpleName)) || 
            ("webkitIDBIndex".equals(hostClassSimpleName)) || 
            ("webkitIDBKeyRange".equals(hostClassSimpleName)) || 
            ("webkitIDBObjectStore".equals(hostClassSimpleName)) || 
            ("webkitIDBRequest".equals(hostClassSimpleName)) || 
            ("webkitIDBTransaction".equals(hostClassSimpleName)) || 
            ("webkitOfflineAudioContext".equals(hostClassSimpleName)) || 
            ("webkitURL".equals(hostClassSimpleName)) || 
            ("Image".equals(hostClassSimpleName)) || 
            ("Option".equals(hostClassSimpleName))) {
            Object prototypeProperty = ScriptableObject.getProperty(window, prototype.getClassName());
            
            if ((function instanceof FunctionObject)) {
              ((FunctionObject)function).addAsConstructor(window, prototype);
            }
            
            ScriptableObject.defineProperty(window, hostClassSimpleName, function, 
              2);
            


            if (!hostClassSimpleName.equals(prototype.getClassName())) {
              if (prototypeProperty == UniqueTag.NOT_FOUND) {
                ScriptableObject.deleteProperty(window, prototype.getClassName());
              }
              else {
                ScriptableObject.defineProperty(window, prototype.getClassName(), 
                  prototypeProperty, 2);
              }
              
            }
          }
          else if ((function instanceof FunctionObject)) {
            ((FunctionObject)function).addAsConstructor(window, prototype);
          }
          

          configureConstantsStaticPropertiesAndStaticFunctions(config, function);
        }
      }
    }
    window.setPrototype((Scriptable)prototypesPerJSName.get(Window.class.getSimpleName()));
    

    Scriptable objectPrototype = ScriptableObject.getObjectPrototype(window);
    for (Object entry : prototypesPerJSName.entrySet()) {
      String name = (String)((Map.Entry)entry).getKey();
      ClassConfiguration config = jsConfig_.getClassConfiguration(name);
      Scriptable prototype = (Scriptable)((Map.Entry)entry).getValue();
      if (!StringUtils.isEmpty(config.getExtendedClassName())) {
        Scriptable parentPrototype = (Scriptable)prototypesPerJSName.get(config.getExtendedClassName());
        prototype.setPrototype(parentPrototype);
      }
      else {
        prototype.setPrototype(objectPrototype);
      }
    }
    




    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_WINDOW_ACTIVEXOBJECT_HIDDEN)) {
      Scriptable prototype = (Scriptable)prototypesPerJSName.get("ActiveXObject");
      if (prototype != null) {
        Method jsConstructor = ActiveXObject.class.getDeclaredMethod("jsConstructor", new Class[] {
          Context.class, [Ljava.lang.Object.class, Function.class, Boolean.TYPE });
        FunctionObject functionObject = new HiddenFunctionObject("ActiveXObject", jsConstructor, window);
        functionObject.addAsConstructor(window, prototype);
      }
    }
    

    removePrototypeProperties(window, "String", new String[] { "equals", "equalsIgnoreCase" });
    if (!browserVersion.hasFeature(BrowserVersionFeatures.STRING_INCLUDES)) {
      removePrototypeProperties(window, "String", new String[] { "includes" });
    }
    if (!browserVersion.hasFeature(BrowserVersionFeatures.STRING_REPEAT)) {
      removePrototypeProperties(window, "String", new String[] { "repeat" });
    }
    if (!browserVersion.hasFeature(BrowserVersionFeatures.STRING_STARTS_ENDS_WITH)) {
      removePrototypeProperties(window, "String", new String[] { "startsWith" });
      removePrototypeProperties(window, "String", new String[] { "endsWith" });
    }
    if (!browserVersion.hasFeature(BrowserVersionFeatures.STRING_TRIM_LEFT_RIGHT)) {
      removePrototypeProperties(window, "String", new String[] { "trimLeft" });
      removePrototypeProperties(window, "String", new String[] { "trimRight" });
    }
    if (browserVersion.hasFeature(BrowserVersionFeatures.STRING_CONTAINS)) {
      ScriptableObject stringPrototype = 
        (ScriptableObject)ScriptableObject.getClassPrototype(window, "String");
      stringPrototype.defineFunctionProperties(new String[] { "contains" }, 
        StringCustom.class, 0);
    }
    

    if (!browserVersion.hasFeature(BrowserVersionFeatures.JS_FUNCTION_TOSOURCE)) {
      deleteProperties(window, new String[] { "uneval" });
      removePrototypeProperties(window, "Object", new String[] { "toSource" });
      removePrototypeProperties(window, "Array", new String[] { "toSource" });
      removePrototypeProperties(window, "Date", new String[] { "toSource" });
      removePrototypeProperties(window, "Function", new String[] { "toSource" });
      removePrototypeProperties(window, "Number", new String[] { "toSource" });
      removePrototypeProperties(window, "String", new String[] { "toSource" });
    }
    if (browserVersion.hasFeature(BrowserVersionFeatures.JS_WINDOW_ACTIVEXOBJECT_HIDDEN)) {
      ((IdFunctionObject)ScriptableObject.getProperty(window, "Object")).delete("assign");
    }
    deleteProperties(window, new String[] { "isXMLName" });
    
    NativeFunctionToStringFunction.installFix(window, webClient.getBrowserVersion());
    
    ScriptableObject datePrototype = (ScriptableObject)ScriptableObject.getClassPrototype(window, "Date");
    datePrototype.defineFunctionProperties(new String[] { "toLocaleDateString", "toLocaleTimeString" }, 
      DateCustom.class, 2);
    
    window.setPrototypes(prototypes, prototypesPerJSName);
    window.initialize(webWindow);
  }
  
  private static void defineConstructor(Window window, Scriptable prototype, ScriptableObject constructor)
  {
    constructor.setParentScope(window);
    ScriptableObject.defineProperty(prototype, "constructor", constructor, 
      7);
    ScriptableObject.defineProperty(constructor, "prototype", prototype, 
      7);
    window.defineProperty(constructor.getClassName(), constructor, 2);
  }
  




  private static void deleteProperties(Scriptable scope, String... propertiesToDelete)
  {
    for (String property : propertiesToDelete) {
      scope.delete(property);
    }
  }
  






  private static void removePrototypeProperties(Scriptable scope, String className, String... properties)
  {
    ScriptableObject prototype = (ScriptableObject)ScriptableObject.getClassPrototype(scope, className);
    for (String property : properties) {
      prototype.delete(property);
    }
  }
  










  public static HtmlUnitScriptable configureClass(ClassConfiguration config, Scriptable window, BrowserVersion browserVersion)
    throws InstantiationException, IllegalAccessException
  {
    HtmlUnitScriptable prototype = (HtmlUnitScriptable)config.getHostClass().newInstance();
    prototype.setParentScope(window);
    prototype.setClassName(config.getClassName());
    
    configureConstantsPropertiesAndFunctions(config, prototype);
    
    return prototype;
  }
  





  private static void configureConstantsStaticPropertiesAndStaticFunctions(ClassConfiguration config, ScriptableObject scriptable)
  {
    configureConstants(config, scriptable);
    configureStaticProperties(config, scriptable);
    configureStaticFunctions(config, scriptable);
  }
  





  private static void configureConstantsPropertiesAndFunctions(ClassConfiguration config, ScriptableObject scriptable)
  {
    configureConstants(config, scriptable);
    configureProperties(config, scriptable);
    configureFunctions(config, scriptable);
  }
  
  private static void configureFunctions(ClassConfiguration config, ScriptableObject scriptable) {
    int attributes = 0;
    
    for (Map.Entry<String, Method> functionInfo : config.getFunctionEntries()) {
      String functionName = (String)functionInfo.getKey();
      Method method = (Method)functionInfo.getValue();
      FunctionObject functionObject = new FunctionObject(functionName, method, scriptable);
      scriptable.defineProperty(functionName, functionObject, 0);
    }
  }
  
  private static void configureConstants(ClassConfiguration config, ScriptableObject scriptable) {
    for (ClassConfiguration.ConstantInfo constantInfo : config.getConstants()) {
      scriptable.defineProperty(constantInfo.getName(), constantInfo.getValue(), constantInfo.getFlag());
    }
  }
  
  private static void configureProperties(ClassConfiguration config, ScriptableObject scriptable) {
    Map<String, ClassConfiguration.PropertyInfo> propertyMap = config.getPropertyMap();
    for (String propertyName : propertyMap.keySet()) {
      ClassConfiguration.PropertyInfo info = (ClassConfiguration.PropertyInfo)propertyMap.get(propertyName);
      Method readMethod = info.getReadMethod();
      Method writeMethod = info.getWriteMethod();
      scriptable.defineProperty(propertyName, null, readMethod, writeMethod, 0);
    }
  }
  
  private static void configureStaticProperties(ClassConfiguration config, ScriptableObject scriptable)
  {
    Iterator localIterator = config.getStaticPropertyEntries().iterator();
    while (localIterator.hasNext()) {
      Map.Entry<String, ClassConfiguration.PropertyInfo> propertyEntry = (Map.Entry)localIterator.next();
      String propertyName = (String)propertyEntry.getKey();
      Method readMethod = ((ClassConfiguration.PropertyInfo)propertyEntry.getValue()).getReadMethod();
      Method writeMethod = ((ClassConfiguration.PropertyInfo)propertyEntry.getValue()).getWriteMethod();
      int flag = 0;
      
      scriptable.defineProperty(propertyName, null, readMethod, writeMethod, 0);
    }
  }
  
  private static void configureStaticFunctions(ClassConfiguration config, ScriptableObject scriptable)
  {
    for (Map.Entry<String, Method> staticfunctionInfo : config.getStaticFunctionEntries()) {
      String functionName = (String)staticfunctionInfo.getKey();
      Method method = (Method)staticfunctionInfo.getValue();
      FunctionObject staticFunctionObject = new FunctionObject(functionName, method, 
        scriptable);
      scriptable.defineProperty(functionName, staticFunctionObject, 0);
    }
  }
  




  public synchronized void registerWindowAndMaybeStartEventLoop(WebWindow webWindow)
  {
    if (webClient_ != null) {
      if (javaScriptExecutor_ == null) {
        javaScriptExecutor_ = BackgroundJavaScriptFactory.theFactory().createJavaScriptExecutor(webClient_);
      }
      javaScriptExecutor_.addWindow(webWindow);
    }
  }
  





  public int pumpEventLoop(long timeoutMillis)
  {
    if (javaScriptExecutor_ == null) {
      return 0;
    }
    return javaScriptExecutor_.pumpEventLoop(timeoutMillis);
  }
  



  public void shutdown()
  {
    webClient_ = null;
    if (javaScriptExecutor_ != null) {
      javaScriptExecutor_.shutdown();
      javaScriptExecutor_ = null;
    }
    if (postponedActions_ != null) {
      postponedActions_.remove();
    }
    if (javaScriptRunning_ != null) {
      javaScriptRunning_.remove();
    }
    holdPostponedActions_ = false;
  }
  









  public Script compile(HtmlPage page, String sourceCode, String sourceName, int startLine)
  {
    Scriptable scope = getScope(page, null);
    return compile(page, scope, sourceCode, sourceName, startLine);
  }
  










  public Script compile(HtmlPage owningPage, Scriptable scope, String sourceCode, final String sourceName, final int startLine)
  {
    WebAssert.notNull("sourceCode", sourceCode);
    
    if (LOG.isTraceEnabled()) {
      String newline = System.lineSeparator();
      LOG.trace("Javascript compile " + sourceName + newline + sourceCode + newline);
    }
    
    final String source = sourceCode;
    ContextAction action = new HtmlUnitContextAction(this, scope, owningPage)
    {
      public Object doRun(Context cx) {
        return cx.compileString(source, sourceName, startLine, null);
      }
      
      protected String getSourceCode(Context cx)
      {
        return source;
      }
      
    };
    return (Script)getContextFactory().call(action);
  }
  













  public Object execute(HtmlPage page, String sourceCode, String sourceName, int startLine)
  {
    Script script = compile(page, sourceCode, sourceName, startLine);
    if (script == null) {
      return null;
    }
    return execute(page, script);
  }
  






  public Object execute(HtmlPage page, Script script)
  {
    Scriptable scope = getScope(page, null);
    return execute(page, scope, script);
  }
  







  public Object execute(HtmlPage page, final Scriptable scope, final Script script)
  {
    ContextAction action = new HtmlUnitContextAction(this, scope, page)
    {
      public Object doRun(Context cx) {
        return script.exec(cx, scope);
      }
      
      protected String getSourceCode(Context cx)
      {
        return null;
      }
      
    };
    return getContextFactory().call(action);
  }
  














  public Object callFunction(HtmlPage page, Function javaScriptFunction, Scriptable thisObject, Object[] args, DomNode node)
  {
    Scriptable scope = getScope(page, node);
    
    return callFunction(page, javaScriptFunction, scope, thisObject, args);
  }
  










  public Object callFunction(HtmlPage page, final Function function, final Scriptable scope, final Scriptable thisObject, final Object[] args)
  {
    ContextAction action = new HtmlUnitContextAction(this, scope, page)
    {
      public Object doRun(Context cx) {
        if (ScriptRuntime.hasTopCall(cx)) {
          return function.call(cx, scope, thisObject, args);
        }
        return ScriptRuntime.doTopCall(function, cx, scope, thisObject, args);
      }
      
      protected String getSourceCode(Context cx) {
        return cx.decompileFunction(function, 2);
      }
    };
    return getContextFactory().call(action);
  }
  
  private static Scriptable getScope(HtmlPage page, DomNode node) {
    if (node != null) {
      return node.getScriptableObject();
    }
    return page.getEnclosingWindow().getScriptableObject();
  }
  





  public boolean isScriptRunning()
  {
    return Boolean.TRUE.equals(javaScriptRunning_.get());
  }
  

  private abstract class HtmlUnitContextAction
    implements ContextAction
  {
    private final Scriptable scope_;
    
    private final HtmlPage page_;
    
    HtmlUnitContextAction(Scriptable scope, HtmlPage page)
    {
      scope_ = scope;
      page_ = page;
    }
    
    public final Object run(Context cx)
    {
      Boolean javaScriptAlreadyRunning = (Boolean)javaScriptRunning_.get();
      javaScriptRunning_.set(Boolean.TRUE);
      

      try
      {
        Stack<Scriptable> stack = (Stack)cx.getThreadLocal("startingScope");
        if (stack == null) {
          stack = new Stack();
          cx.putThreadLocal("startingScope", stack);
        }
        

        stack.push(scope_);
        try {
          cx.putThreadLocal("startingPage", page_);
          Object response; synchronized (page_) {
            if (page_ != page_.getEnclosingWindow().getEnclosedPage())
            {





              stack.pop();
              





















              return null;
            }
            response = doRun(cx);
          }
        }
        finally {
          stack.pop();
        }
        
        Object response;
        
        if (!holdPostponedActions_) {
          JavaScriptEngine.this.doProcessPostponedActions();
        }
        return response;
      }
      catch (Exception e) {
        handleJavaScriptException(new ScriptException(page_, e, getSourceCode(cx)), true);
        return null;
      }
      catch (TimeoutError e) {
        getWebClient().getJavaScriptErrorListener().timeoutError(page_, e.getAllowedTime(), e.getExecutionTime());
        if (getWebClient().getOptions().isThrowExceptionOnScriptError()) {
          throw new RuntimeException(e);
        }
        JavaScriptEngine.LOG.info("Caught script timeout error", e);
        return null;
      }
      finally {
        javaScriptRunning_.set(javaScriptAlreadyRunning);
      }
    }
    
    protected abstract Object doRun(Context paramContext);
    
    protected abstract String getSourceCode(Context paramContext);
  }
  
  private void doProcessPostponedActions() {
    holdPostponedActions_ = false;
    
    WebClient webClient = getWebClient();
    if (webClient == null) {
      postponedActions_.set(null);
      return;
    }
    try
    {
      webClient.loadDownloadedResponses();
    }
    catch (RuntimeException e) {
      throw e;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
    
    List<PostponedAction> actions = (List)postponedActions_.get();
    if (actions != null) {
      postponedActions_.set(null);
      try {
        for (PostponedAction action : actions) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("Processing PostponedAction " + action);
          }
          

          if (action.isStillAlive()) {
            action.execute();
          }
        }
      }
      catch (Exception e) {
        Context.throwAsScriptRuntimeEx(e);
      }
    }
  }
  




  public void addPostponedAction(PostponedAction action)
  {
    List<PostponedAction> actions = (List)postponedActions_.get();
    if (actions == null) {
      actions = new ArrayList();
      postponedActions_.set(actions);
    }
    actions.add(action);
  }
  





  protected void handleJavaScriptException(ScriptException scriptException, boolean triggerOnError)
  {
    HtmlPage page = scriptException.getPage();
    if ((triggerOnError) && (page != null)) {
      WebWindow window = page.getEnclosingWindow();
      if (window != null) {
        Window w = (Window)window.getScriptableObject();
        if (w != null) {
          try {
            w.triggerOnError(scriptException);
          }
          catch (Exception e) {
            handleJavaScriptException(new ScriptException(page, e, null), false);
          }
        }
      }
    }
    getWebClient().getJavaScriptErrorListener().scriptException(page, scriptException);
    
    if (getWebClient().getOptions().isThrowExceptionOnScriptError()) {
      throw scriptException;
    }
    
    LOG.info("Caught script exception", scriptException);
  }
  



  public void holdPosponedActions()
  {
    holdPostponedActions_ = true;
  }
  




  public void processPostponedActions()
  {
    doProcessPostponedActions();
  }
  

  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException
  {
    in.defaultReadObject();
    initTransientFields();
  }
  
  private void initTransientFields() {
    javaScriptRunning_ = new ThreadLocal();
    postponedActions_ = new ThreadLocal();
    holdPostponedActions_ = false;
  }
  




  public Class<? extends HtmlUnitScriptable> getJavaScriptClass(Class<?> c)
  {
    return (Class)jsConfig_.getDomJavaScriptMapping().get(c);
  }
  




  public JavaScriptConfiguration getJavaScriptConfiguration()
  {
    return jsConfig_;
  }
  




  public long getJavaScriptTimeout()
  {
    return getContextFactory().getTimeout();
  }
  




  public void setJavaScriptTimeout(long timeout)
  {
    getContextFactory().setTimeout(timeout);
  }
}

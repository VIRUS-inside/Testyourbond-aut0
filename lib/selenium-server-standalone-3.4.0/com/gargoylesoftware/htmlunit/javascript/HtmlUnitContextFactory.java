package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.regexp.HtmlUnitRegExpProxy;
import net.sourceforge.htmlunit.corejs.javascript.Callable;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.ErrorReporter;
import net.sourceforge.htmlunit.corejs.javascript.Evaluator;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.WrapFactory;
import net.sourceforge.htmlunit.corejs.javascript.debug.Debugger;





























public class HtmlUnitContextFactory
  extends ContextFactory
{
  private static final int INSTRUCTION_COUNT_THRESHOLD = 10000;
  private final WebClient webClient_;
  private final BrowserVersion browserVersion_;
  private long timeout_;
  private Debugger debugger_;
  private final ErrorReporter errorReporter_;
  private final WrapFactory wrapFactory_ = new HtmlUnitWrapFactory();
  private boolean deminifyFunctionCode_ = false;
  




  public HtmlUnitContextFactory(WebClient webClient)
  {
    webClient_ = webClient;
    browserVersion_ = webClient.getBrowserVersion();
    errorReporter_ = new StrictErrorReporter();
  }
  





  public void setTimeout(long timeout)
  {
    timeout_ = timeout;
  }
  





  public long getTimeout()
  {
    return timeout_;
  }
  






  public void setDebugger(Debugger debugger)
  {
    debugger_ = debugger;
  }
  





  public Debugger getDebugger()
  {
    return debugger_;
  }
  




  public void setDeminifyFunctionCode(boolean deminify)
  {
    deminifyFunctionCode_ = deminify;
  }
  




  public boolean isDeminifyFunctionCode()
  {
    return deminifyFunctionCode_;
  }
  
  private class TimeoutContext extends Context
  {
    private long startTime_;
    
    protected TimeoutContext(ContextFactory factory)
    {
      super();
    }
    
    public void startClock() { startTime_ = System.currentTimeMillis(); }
    
    public void terminateScriptIfNecessary() {
      if (timeout_ > 0L) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - startTime_ > timeout_)
        {

          throw new TimeoutError(timeout_, currentTime - startTime_);
        }
      }
    }
    




    protected Script compileString(String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain)
    {
      boolean isWindowEval = compiler != null;
      

      if (!isWindowEval) {
        String sourceCodeTrimmed = source.trim();
        if (sourceCodeTrimmed.startsWith("<!--")) {
          source = source.replaceFirst("<!--", "// <!--");
        }
        
        if ((browserVersion_.hasFeature(BrowserVersionFeatures.JS_IGNORES_LAST_LINE_CONTAINING_UNCOMMENTED)) && 
          (sourceCodeTrimmed.endsWith("-->"))) {
          int lastDoubleSlash = source.lastIndexOf("//");
          int lastNewLine = Math.max(source.lastIndexOf('\n'), source.lastIndexOf('\r'));
          if (lastNewLine > lastDoubleSlash) {
            source = source.substring(0, lastNewLine);
          }
        }
      }
      

      HtmlPage page = (HtmlPage)Context.getCurrentContext()
        .getThreadLocal("startingPage");
      source = preProcess(page, source, sourceName, lineno, null);
      
      return super.compileString(source, compiler, compilationErrorReporter, 
        sourceName, lineno, securityDomain);
    }
    



    protected Function compileFunction(Scriptable scope, String source, Evaluator compiler, ErrorReporter compilationErrorReporter, String sourceName, int lineno, Object securityDomain)
    {
      if (deminifyFunctionCode_) {
        Function f = super.compileFunction(scope, source, compiler, 
          compilationErrorReporter, sourceName, lineno, securityDomain);
        source = decompileFunction(f, 4).trim().replace("\n    ", "\n");
      }
      return super.compileFunction(scope, source, compiler, 
        compilationErrorReporter, sourceName, lineno, securityDomain);
    }
  }
  















  protected String preProcess(HtmlPage htmlPage, String sourceCode, String sourceName, int lineNumber, HtmlElement htmlElement)
  {
    String newSourceCode = sourceCode;
    ScriptPreProcessor preProcessor = webClient_.getScriptPreProcessor();
    if (preProcessor != null) {
      newSourceCode = preProcessor.preProcess(htmlPage, sourceCode, sourceName, lineNumber, htmlElement);
      if (newSourceCode == null) {
        newSourceCode = "";
      }
    }
    return newSourceCode;
  }
  



  protected Context makeContext()
  {
    TimeoutContext cx = new TimeoutContext(this);
    

    cx.setOptimizationLevel(-1);
    

    cx.setInstructionObserverThreshold(10000);
    
    configureErrorReporter(cx);
    cx.setWrapFactory(wrapFactory_);
    
    if (debugger_ != null) {
      cx.setDebugger(debugger_, null);
    }
    

    ScriptRuntime.setRegExpProxy(cx, new HtmlUnitRegExpProxy(ScriptRuntime.getRegExpProxy(cx), browserVersion_));
    
    cx.setMaximumInterpreterStackDepth(10000);
    
    return cx;
  }
  




  protected void configureErrorReporter(Context context)
  {
    context.setErrorReporter(errorReporter_);
  }
  








  protected void observeInstructionCount(Context cx, int instructionCount)
  {
    TimeoutContext tcx = (TimeoutContext)cx;
    tcx.terminateScriptIfNecessary();
  }
  






  protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
  {
    TimeoutContext tcx = (TimeoutContext)cx;
    tcx.startClock();
    return super.doTopCall(callable, cx, scope, thisObj, args);
  }
  



  protected boolean hasFeature(Context cx, int featureIndex)
  {
    switch (featureIndex) {
    case 3: 
      return true;
    case 1: 
      return false;
    case 100: 
      return true;
    case 101: 
      return false;
    case 102: 
      return false;
    case 103: 
      return true;
    case 104: 
      return browserVersion_.hasFeature(BrowserVersionFeatures.JS_ARGUMENTS_READ_ONLY_ACCESSED_FROM_FUNCTION);
    case 105: 
      return false;
    case 106: 
      return true;
    case 107: 
      return true;
    case 108: 
      return false;
    case 109: 
      return browserVersion_.hasFeature(BrowserVersionFeatures.JS_FUNCTION_DECLARED_FORWARD_IN_BLOCK);
    case 110: 
      return true;
    case 111: 
      return browserVersion_.hasFeature(BrowserVersionFeatures.JS_ENUM_NUMBERS_FIRST);
    case 112: 
      return browserVersion_.hasFeature(BrowserVersionFeatures.JS_GET_PROTOTYPE_OF_STRING);
    }
    return super.hasFeature(cx, featureIndex);
  }
}

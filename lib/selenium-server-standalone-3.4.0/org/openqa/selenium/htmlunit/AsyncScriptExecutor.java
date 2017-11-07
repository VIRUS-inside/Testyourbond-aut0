package org.openqa.selenium.htmlunit;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeJavaObject;
import org.openqa.selenium.ScriptTimeoutException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriverException;
































class AsyncScriptExecutor
{
  private final HtmlPage page;
  private final long timeoutMillis;
  private AsyncScriptResult asyncResult;
  
  AsyncScriptExecutor(HtmlPage page, long timeoutMillis)
  {
    this.page = page;
    this.timeoutMillis = timeoutMillis;
  }
  
  void alertTriggered(String message) {
    asyncResult.alert(message);
  }
  
  /* Error */
  public Object execute(String scriptBody, Object[] parameters)
  {
    // Byte code:
    //   0: aload_0
    //   1: new 30	org/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult
    //   4: dup
    //   5: invokespecial 39	org/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult:<init>	()V
    //   8: putfield 27	org/openqa/selenium/htmlunit/AsyncScriptExecutor:asyncResult	Lorg/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult;
    //   11: aload_0
    //   12: aload_1
    //   13: aload_0
    //   14: getfield 27	org/openqa/selenium/htmlunit/AsyncScriptExecutor:asyncResult	Lorg/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult;
    //   17: invokespecial 40	org/openqa/selenium/htmlunit/AsyncScriptExecutor:createInjectedScriptFunction	(Ljava/lang/String;Lorg/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult;)Lnet/sourceforge/htmlunit/corejs/javascript/Function;
    //   20: astore_3
    //   21: aload_0
    //   22: getfield 17	org/openqa/selenium/htmlunit/AsyncScriptExecutor:page	Lcom/gargoylesoftware/htmlunit/html/HtmlPage;
    //   25: aload_3
    //   26: aload_3
    //   27: aload_2
    //   28: aload_0
    //   29: getfield 17	org/openqa/selenium/htmlunit/AsyncScriptExecutor:page	Lcom/gargoylesoftware/htmlunit/html/HtmlPage;
    //   32: invokevirtual 44	com/gargoylesoftware/htmlunit/html/HtmlPage:getDocumentElement	()Lcom/gargoylesoftware/htmlunit/html/HtmlElement;
    //   35: invokevirtual 50	com/gargoylesoftware/htmlunit/html/HtmlPage:executeJavaScriptFunctionIfPossible	(Lnet/sourceforge/htmlunit/corejs/javascript/Function;Lnet/sourceforge/htmlunit/corejs/javascript/Scriptable;[Ljava/lang/Object;Lcom/gargoylesoftware/htmlunit/html/DomNode;)Lcom/gargoylesoftware/htmlunit/ScriptResult;
    //   38: pop
    //   39: goto +15 -> 54
    //   42: astore 4
    //   44: new 54	org/openqa/selenium/WebDriverException
    //   47: dup
    //   48: aload 4
    //   50: invokespecial 56	org/openqa/selenium/WebDriverException:<init>	(Ljava/lang/Throwable;)V
    //   53: athrow
    //   54: aload_0
    //   55: getfield 27	org/openqa/selenium/htmlunit/AsyncScriptExecutor:asyncResult	Lorg/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult;
    //   58: aload_0
    //   59: getfield 19	org/openqa/selenium/htmlunit/AsyncScriptExecutor:timeoutMillis	J
    //   62: invokevirtual 59	org/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult:waitForResult	(J)Ljava/lang/Object;
    //   65: astore 6
    //   67: aload_0
    //   68: aconst_null
    //   69: putfield 27	org/openqa/selenium/htmlunit/AsyncScriptExecutor:asyncResult	Lorg/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult;
    //   72: aload 6
    //   74: areturn
    //   75: astore 4
    //   77: new 54	org/openqa/selenium/WebDriverException
    //   80: dup
    //   81: aload 4
    //   83: invokespecial 56	org/openqa/selenium/WebDriverException:<init>	(Ljava/lang/Throwable;)V
    //   86: athrow
    //   87: astore 5
    //   89: aload_0
    //   90: aconst_null
    //   91: putfield 27	org/openqa/selenium/htmlunit/AsyncScriptExecutor:asyncResult	Lorg/openqa/selenium/htmlunit/AsyncScriptExecutor$AsyncScriptResult;
    //   94: aload 5
    //   96: athrow
    // Line number table:
    //   Java source line #70	-> byte code offset #0
    //   Java source line #71	-> byte code offset #11
    //   Java source line #74	-> byte code offset #21
    //   Java source line #75	-> byte code offset #28
    //   Java source line #74	-> byte code offset #35
    //   Java source line #76	-> byte code offset #39
    //   Java source line #77	-> byte code offset #44
    //   Java source line #81	-> byte code offset #54
    //   Java source line #87	-> byte code offset #67
    //   Java source line #81	-> byte code offset #72
    //   Java source line #82	-> byte code offset #75
    //   Java source line #83	-> byte code offset #77
    //   Java source line #86	-> byte code offset #87
    //   Java source line #87	-> byte code offset #89
    //   Java source line #88	-> byte code offset #94
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	97	0	this	AsyncScriptExecutor
    //   0	97	1	scriptBody	String
    //   0	97	2	parameters	Object[]
    //   20	7	3	function	Function
    //   42	7	4	e	com.gargoylesoftware.htmlunit.ScriptException
    //   75	7	4	e	InterruptedException
    //   87	8	5	localObject1	Object
    //   65	8	6	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   21	39	42	com/gargoylesoftware/htmlunit/ScriptException
    //   54	67	75	java/lang/InterruptedException
    //   0	67	87	finally
    //   75	87	87	finally
  }
  
  private Function createInjectedScriptFunction(String userScript, AsyncScriptResult asyncResult)
  {
    String script = 
      "function() {  var self = this, timeoutId;  var cleanUp = function() {    window.clearTimeout(timeoutId);    if (window.detachEvent) {      window.detachEvent('onunload', catchUnload);    } else {      window.removeEventListener('unload', catchUnload, false);    }  };  var self = this, timeoutId, catchUnload = function() {    cleanUp();    self.host.unload();  };  arguments = Array.prototype.slice.call(arguments, 0);  arguments.push(function(value) {    cleanUp();    self.host.callback(typeof value == 'undefined' ? null : value);  });  if (window.attachEvent) {    window.attachEvent('onunload', catchUnload);  } else {    window.addEventListener('unload', catchUnload, false);  }  (function() {" + 
      


























      userScript + "}).apply(null, arguments);" + 
      


      "  timeoutId = window.setTimeout(function() {" + 
      "    self.host.timeout();" + 
      "  }, " + timeoutMillis + ");" + 
      "}";
    

    ScriptResult result = page.executeJavaScript(script);
    Function function = (Function)result.getJavaScriptResult();
    

    function.put("host", function, new NativeJavaObject(function, asyncResult, null));
    
    return function;
  }
  








  public static class AsyncScriptResult
  {
    private final CountDownLatch latch = new CountDownLatch(1);
    
    private volatile Object value;
    
    private volatile boolean isTimeout;
    
    private volatile String alertMessage;
    private volatile boolean unloadDetected;
    
    public AsyncScriptResult() {}
    
    Object waitForResult(long timeoutMillis)
      throws InterruptedException
    {
      long startTimeNanos = System.nanoTime();
      latch.await();
      if (isTimeout) {
        long elapsedTimeNanos = System.nanoTime() - startTimeNanos;
        long elapsedTimeMillis = TimeUnit.NANOSECONDS.toMillis(elapsedTimeNanos);
        throw new ScriptTimeoutException(
          "Timed out waiting for async script result after " + elapsedTimeMillis + "ms");
      }
      if (alertMessage != null) {
        throw new UnhandledAlertException("Alert found", alertMessage);
      }
      
      if (unloadDetected) {
        throw new WebDriverException(
          "Detected a page unload event; executeAsyncScript does not work across page loads");
      }
      return value;
    }
    








    public void callback(Object callbackValue)
    {
      if (latch.getCount() > 0L) {
        value = callbackValue;
        latch.countDown();
      }
    }
    







    public void timeout()
    {
      if (latch.getCount() > 0L) {
        isTimeout = true;
        latch.countDown();
      }
    }
    


    private void alert(String message)
    {
      if (latch.getCount() > 0L) {
        alertMessage = message;
        latch.countDown();
      }
    }
    







    public void unload()
    {
      if (latch.getCount() > 0L) {
        unloadDetected = true;
        latch.countDown();
      }
    }
  }
}

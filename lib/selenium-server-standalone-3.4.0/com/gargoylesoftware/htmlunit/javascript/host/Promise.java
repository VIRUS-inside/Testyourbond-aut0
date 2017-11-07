package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.FunctionWrapper;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxStaticFunction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.JavaScriptException;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.TopLevel.Builtins;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;























@JsxClass(browsers={@com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF), @com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser(com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE)})
public class Promise
  extends SimpleScriptable
{
  private Object value_;
  private Promise[] all_;
  private boolean resolve_ = true;
  


  private String exceptionDetails_;
  


  public Promise() {}
  


  public Promise(Window window)
  {
    setParentScope(window);
    setPrototype(window.getPrototype(Promise.class));
  }
  




  @JsxConstructor
  public Promise(Object object)
  {
    if ((object instanceof Promise)) {
      value_ = value_;
    }
    else if ((object instanceof NativeObject)) {
      NativeObject nativeObject = (NativeObject)object;
      value_ = nativeObject.get("then", nativeObject);
    }
    else {
      value_ = object;
    }
  }
  









  @JsxStaticFunction
  public static Promise resolve(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    Promise promise = new Promise(args.length != 0 ? args[0] : Undefined.instance);
    promise.setResolve(true);
    promise.setParentScope(thisObj.getParentScope());
    promise.setPrototype(getWindow(thisObj).getPrototype(promise.getClass()));
    return promise;
  }
  









  @JsxStaticFunction
  public static Promise reject(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    Promise promise = new Promise(args.length != 0 ? args[0] : Undefined.instance);
    promise.setResolve(false);
    promise.setParentScope(thisObj.getParentScope());
    promise.setPrototype(getWindow(thisObj).getPrototype(promise.getClass()));
    return promise;
  }
  
  private void setResolve(boolean resolve) {
    resolve_ = resolve;
  }
  


  private boolean isResolved(Function onRejected)
  {
    if (all_ != null) {
      Object[] values = new Object[all_.length];
      for (int i = 0; i < all_.length; i++) {
        Promise p = all_[i];
        if (!p.isResolved(onRejected)) {
          value_ = value_;
          return false;
        }
        
        if (!(value_ instanceof Function))
        {


          values[i] = value_;
        }
      }
      NativeArray array = new NativeArray(values);
      ScriptRuntime.setBuiltinProtoAndParent(array, getParentScope(), TopLevel.Builtins.Array);
      value_ = array;
    }
    return resolve_;
  }
  










  @JsxStaticFunction
  public static Promise all(Context context, Scriptable thisObj, Object[] args, Function function)
  {
    Promise promise = new Promise();
    promise.setResolve(true);
    if (args.length == 0) {
      all_ = new Promise[0];
    }
    else {
      NativeArray array = (NativeArray)args[0];
      int length = (int)array.getLength();
      all_ = new Promise[length];
      for (int i = 0; i < length; i++) {
        Object o = array.get(i);
        if ((o instanceof Promise)) {
          all_[i] = ((Promise)o);
        }
        else {
          all_[i] = resolve(null, thisObj, new Object[] { o }, null);
        }
      }
    }
    promise.setParentScope(thisObj.getParentScope());
    promise.setPrototype(getWindow(thisObj).getPrototype(promise.getClass()));
    return promise;
  }
  






  @JsxFunction
  public Promise then(final Function onFulfilled, final Function onRejected)
  {
    final Window window = getWindow();
    final Promise promise = new Promise(window);
    final Promise thisPromise = this;
    
    PostponedAction thenAction = new PostponedAction(window.getDocument().getPage(), "Promise.then")
    {
      public void execute() throws Exception
      {
        Context.enter();
        try {
          Object newValue = null;
          Function toExecute = Promise.this.isResolved(onRejected) ? onFulfilled : onRejected;
          if ((value_ instanceof Function)) {
            Promise.WasCalledFunctionWrapper wrapper = new Promise.WasCalledFunctionWrapper(toExecute);
            try {
              ((Function)value_).call(Context.getCurrentContext(), window, thisPromise, 
                new Object[] { wrapper, onRejected });
              if (!Promise.WasCalledFunctionWrapper.access$0(wrapper)) break label210;
              newValue = Promise.WasCalledFunctionWrapper.access$1(wrapper);
            }
            catch (JavaScriptException e)
            {
              if (onRejected == null) {
                promiseexceptionDetails_ = e.details();
                break label210; }
              if (Promise.WasCalledFunctionWrapper.access$0(wrapper)) break label210; }
            newValue = onRejected.call(Context.getCurrentContext(), window, thisPromise, 
              new Object[] { e.getValue() });

          }
          else
          {
            newValue = toExecute.call(Context.getCurrentContext(), window, thisPromise, 
              new Object[] { value_ }); }
          label210:
          promisevalue_ = newValue;
        }
        finally {
          Context.exit();
        }
        
      }
    };
    JavaScriptEngine jsEngine = window.getWebWindow().getWebClient().getJavaScriptEngine();
    jsEngine.addPostponedAction(thenAction);
    
    return promise;
  }
  





  @JsxFunction(functionName="catch")
  public Promise catch_js(final Function onRejected)
  {
    final Window window = getWindow();
    final Promise promise = new Promise(window);
    final Promise thisPromise = this;
    
    PostponedAction thenAction = new PostponedAction(window.getDocument().getPage(), "Promise.catch")
    {
      public void execute() throws Exception
      {
        Context.enter();
        try {
          Object newValue = onRejected.call(Context.getCurrentContext(), window, thisPromise, 
            new Object[] { exceptionDetails_ });
          promisevalue_ = newValue;
        }
        finally {
          Context.exit();
        }
        
      }
    };
    JavaScriptEngine jsEngine = window.getWebWindow().getWebClient().getJavaScriptEngine();
    jsEngine.addPostponedAction(thenAction);
    
    return promise;
  }
  
  private static class WasCalledFunctionWrapper extends FunctionWrapper {
    private boolean wasCalled_;
    private Object value_;
    
    WasCalledFunctionWrapper(Function wrapped) {
      super();
    }
    



    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args)
    {
      wasCalled_ = true;
      value_ = super.call(cx, scope, thisObj, args);
      return value_;
    }
  }
}

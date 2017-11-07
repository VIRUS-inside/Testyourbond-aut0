package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientInternals;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitContextFactory;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.client.WebSocketClient;



















@JsxClass
public class WebSocket
  extends EventTarget
  implements AutoCloseable
{
  private static final Log LOG = LogFactory.getLog(WebSocket.class);
  
  @JsxConstant
  public static final int CONNECTING = 0;
  
  @JsxConstant
  public static final int OPEN = 1;
  
  @JsxConstant
  public static final int CLOSING = 2;
  
  @JsxConstant
  public static final int CLOSED = 3;
  
  private Function closeHandler_;
  
  private Function errorHandler_;
  private Function messageHandler_;
  private Function openHandler_;
  private URI url_;
  private int readyState_ = 0;
  private String binaryType_ = "blob";
  

  private HtmlPage containingPage_;
  

  private WebSocketClient client_;
  
  private volatile Session incomingSession_;
  
  private Session outgoingSession_;
  
  private WebSocketListener listener_;
  

  public WebSocket() {}
  

  public void setWebSocketListener(WebSocketListener listener)
  {
    listener_ = listener;
  }
  




  public WebSocketListener getWebSocketListener()
  {
    return listener_;
  }
  



  private WebSocket(String url, Window window)
  {
    try
    {
      containingPage_ = ((HtmlPage)window.getWebWindow().getEnclosedPage());
      setParentScope(window);
      setDomNode(containingPage_.getBody(), false);
      
      WebClient webClient = window.getWebWindow().getWebClient();
      if (webClient.getOptions().isUseInsecureSSL()) {
        client_ = new WebSocketClient(new SslContextFactory(true));
      }
      else {
        client_ = new WebSocketClient();
      }
      client_.setCookieStore(new WebSocketCookieStore(webClient));
      client_.start();
      containingPage_.addAutoCloseable(this);
      url_ = new URI(url);
      
      webClient.getInternals().created(this);
      
      final Future<Session> connectFuture = client_.connect(new WebSocketImpl(null), url_);
      client_.getExecutor().execute(new Runnable()
      {
        public void run() {
          try {
            readyState_ = 0;
            incomingSession_ = ((Session)connectFuture.get());
          }
          catch (Exception e) {
            WebSocket.LOG.error("WS connect error", e);
          }
        }
      });
    }
    catch (Exception e) {
      LOG.error("WebSocket Error: 'url' parameter '" + url + "' is invalid.", e);
      throw Context.reportRuntimeError("WebSocket Error: 'url' parameter '" + url + "' is invalid.");
    }
  }
  









  @JsxConstructor
  public static Scriptable jsConstructor(Context cx, Object[] args, Function ctorObj, boolean inNewExpr)
  {
    if ((args.length < 1) || (args.length > 2)) {
      throw Context.reportRuntimeError(
        "WebSocket Error: constructor must have one or two String parameters.");
    }
    if (args[0] == Undefined.instance) {
      throw Context.reportRuntimeError("WebSocket Error: 'url' parameter is undefined.");
    }
    if (!(args[0] instanceof String)) {
      throw Context.reportRuntimeError("WebSocket Error: 'url' parameter must be a String.");
    }
    String url = (String)args[0];
    if (StringUtils.isBlank(url)) {
      throw Context.reportRuntimeError("WebSocket Error: 'url' parameter must be not empty.");
    }
    return new WebSocket(url, getWindow(ctorObj));
  }
  



  @JsxGetter
  public Function getOnclose()
  {
    return closeHandler_;
  }
  



  @JsxSetter
  public void setOnclose(Function closeHandler)
  {
    closeHandler_ = closeHandler;
  }
  



  @JsxGetter
  public Function getOnerror()
  {
    return errorHandler_;
  }
  



  @JsxSetter
  public void setOnerror(Function errorHandler)
  {
    errorHandler_ = errorHandler;
  }
  



  @JsxGetter
  public Function getOnmessage()
  {
    return messageHandler_;
  }
  



  @JsxSetter
  public void setOnmessage(Function messageHandler)
  {
    messageHandler_ = messageHandler;
  }
  



  @JsxGetter
  public Function getOnopen()
  {
    return openHandler_;
  }
  



  @JsxSetter
  public void setOnopen(Function openHandler)
  {
    openHandler_ = openHandler;
  }
  




  @JsxGetter
  public int getReadyState()
  {
    return readyState_;
  }
  


  @JsxGetter
  public String getUrl()
  {
    return url_.toString();
  }
  


  @JsxGetter
  public String getProtocol()
  {
    return "";
  }
  


  @JsxGetter
  public long getBufferedAmount()
  {
    return 0L;
  }
  


  @JsxGetter
  public String getBinaryType()
  {
    return binaryType_;
  }
  



  @JsxSetter
  public void setBinaryType(String type)
  {
    if (("arraybuffer".equals(type)) || 
      ("blob".equals(type))) {
      binaryType_ = type;
    }
  }
  


  public void close()
    throws Exception
  {
    close(null, null);
  }
  





  @JsxFunction
  public void close(Object code, Object reason)
  {
    if (readyState_ != 3) {
      if (incomingSession_ != null) {
        incomingSession_.close();
      }
      if (outgoingSession_ != null) {
        outgoingSession_.close();
      }
    }
    try
    {
      if (client_ != null) {
        client_.stop();
        client_ = null;
      }
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  


  @JsxFunction
  public void send(Object content)
  {
    try
    {
      if ((content instanceof String)) {
        outgoingSession_.getRemote().sendString((String)content);
      }
      else if ((content instanceof ArrayBuffer)) {
        byte[] bytes = ((ArrayBuffer)content).getBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        outgoingSession_.getRemote().sendBytes(buffer);
      }
      else {
        throw new IllegalStateException(
          "Not Yet Implemented: WebSocket.send() was used to send non-string value");
      }
    }
    catch (IOException e) {
      LOG.error("WS send error", e);
    }
  }
  
  private void fire(final Event evt) {
    evt.setTarget(this);
    evt.setParentScope(getParentScope());
    evt.setPrototype(getPrototype(evt.getClass()));
    
    JavaScriptEngine engine = containingPage_.getWebClient().getJavaScriptEngine();
    engine.getContextFactory().call(new ContextAction()
    {
      public ScriptResult run(Context cx) {
        return executeEventLocally(evt);
      }
    });
  }
  
  private void callFunction(Function function, Object[] args) {
    if (function == null) {
      return;
    }
    Scriptable scope = function.getParentScope();
    JavaScriptEngine engine = containingPage_.getWebClient().getJavaScriptEngine();
    engine.callFunction(containingPage_, function, scope, this, args);
  }
  
  private class WebSocketImpl extends WebSocketAdapter {
    private WebSocketImpl() {}
    
    public void onWebSocketConnect(Session session) {
      if (listener_ != null) {
        listener_.onWebSocketConnect(session);
      }
      super.onWebSocketConnect(session);
      readyState_ = 1;
      outgoingSession_ = session;
      
      Event openEvent = new Event();
      openEvent.setType("open");
      WebSocket.this.fire(openEvent);
      WebSocket.this.callFunction(openHandler_, ArrayUtils.EMPTY_OBJECT_ARRAY);
    }
    
    public void onWebSocketClose(int statusCode, String reason)
    {
      if (listener_ != null) {
        listener_.onWebSocketClose(statusCode, reason);
      }
      super.onWebSocketClose(statusCode, reason);
      readyState_ = 3;
      outgoingSession_ = null;
      
      CloseEvent closeEvent = new CloseEvent();
      closeEvent.setCode(statusCode);
      closeEvent.setReason(reason);
      closeEvent.setWasClean(true);
      WebSocket.this.fire(closeEvent);
      WebSocket.this.callFunction(closeHandler_, new Object[] { closeEvent });
    }
    
    public void onWebSocketText(String message)
    {
      if (listener_ != null) {
        listener_.onWebSocketText(message);
      }
      super.onWebSocketText(message);
      
      MessageEvent msgEvent = new MessageEvent(message);
      msgEvent.setOrigin(getUrl());
      WebSocket.this.fire(msgEvent);
      WebSocket.this.callFunction(messageHandler_, new Object[] { msgEvent });
    }
    
    public void onWebSocketBinary(byte[] data, int offset, int length)
    {
      if (listener_ != null) {
        listener_.onWebSocketBinary(data, offset, length);
      }
      super.onWebSocketBinary(data, offset, length);
      
      ArrayBuffer buffer = new ArrayBuffer();
      buffer.setParentScope(getParentScope());
      buffer.setPrototype(getPrototype(buffer.getClass()));
      
      buffer.constructor(length);
      buffer.setBytes(0, Arrays.copyOfRange(data, offset, length));
      
      MessageEvent msgEvent = new MessageEvent(buffer);
      msgEvent.setOrigin(getUrl());
      WebSocket.this.fire(msgEvent);
      WebSocket.this.callFunction(messageHandler_, new Object[] { msgEvent });
    }
    
    public void onWebSocketError(Throwable cause)
    {
      if (listener_ != null) {
        listener_.onWebSocketError(cause);
      }
      super.onWebSocketError(cause);
      readyState_ = 3;
      outgoingSession_ = null;
      
      Event errorEvent = new Event();
      errorEvent.setType("error");
      WebSocket.this.fire(errorEvent);
      WebSocket.this.callFunction(errorHandler_, new Object[] { errorEvent });
      
      CloseEvent closeEvent = new CloseEvent();
      closeEvent.setCode(1006);
      closeEvent.setReason(cause.getMessage());
      closeEvent.setWasClean(false);
      WebSocket.this.fire(closeEvent);
      WebSocket.this.callFunction(closeHandler_, new Object[] { closeEvent });
    }
  }
}

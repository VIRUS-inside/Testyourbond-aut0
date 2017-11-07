package org.eclipse.jetty.websocket.common.events;

import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.common.events.annotated.AbstractMethodAnnotationScanner;
import org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
import org.eclipse.jetty.websocket.common.events.annotated.InvalidSignatureException;
import org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;



















public class JettyAnnotatedScanner
  extends AbstractMethodAnnotationScanner<JettyAnnotatedMetadata>
{
  private static final Logger LOG = Log.getLogger(JettyAnnotatedScanner.class);
  















  private static final ParamList validBinaryParams;
  















  private static final ParamList validConnectParams = new ParamList();
  static { validConnectParams.addParams(new Class[] { Session.class });
    
    validCloseParams = new ParamList();
    validCloseParams.addParams(new Class[] { Integer.TYPE, String.class });
    validCloseParams.addParams(new Class[] { Session.class, Integer.TYPE, String.class });
    
    validErrorParams = new ParamList();
    validErrorParams.addParams(new Class[] { Throwable.class });
    validErrorParams.addParams(new Class[] { Session.class, Throwable.class });
    
    validTextParams = new ParamList();
    validTextParams.addParams(new Class[] { String.class });
    validTextParams.addParams(new Class[] { Session.class, String.class });
    validTextParams.addParams(new Class[] { Reader.class });
    validTextParams.addParams(new Class[] { Session.class, Reader.class });
    
    validBinaryParams = new ParamList();
    validBinaryParams.addParams(new Class[] { [B.class, Integer.TYPE, Integer.TYPE });
    validBinaryParams.addParams(new Class[] { Session.class, [B.class, Integer.TYPE, Integer.TYPE });
    validBinaryParams.addParams(new Class[] { InputStream.class });
    validBinaryParams.addParams(new Class[] { Session.class, InputStream.class });
    
    validFrameParams = new ParamList();
    validFrameParams.addParams(new Class[] { Frame.class });
    validFrameParams.addParams(new Class[] { Session.class, Frame.class }); }
  
  private static final ParamList validCloseParams;
  private static final ParamList validErrorParams;
  private static final ParamList validFrameParams;
  private static final ParamList validTextParams;
  public void onMethodAnnotation(JettyAnnotatedMetadata metadata, Class<?> pojo, Method method, Annotation annotation) { if (LOG.isDebugEnabled()) {
      LOG.debug("onMethodAnnotation({}, {}, {}, {})", new Object[] { metadata, pojo, method, annotation });
    }
    if (isAnnotation(annotation, OnWebSocketConnect.class))
    {
      assertValidSignature(method, OnWebSocketConnect.class, validConnectParams);
      assertUnset(onConnect, OnWebSocketConnect.class, method);
      onConnect = new CallableMethod(pojo, method);
      return;
    }
    
    if (isAnnotation(annotation, OnWebSocketMessage.class))
    {
      if (isSignatureMatch(method, validTextParams))
      {

        assertUnset(onText, OnWebSocketMessage.class, method);
        onText = new OptionalSessionCallableMethod(pojo, method);
        return;
      }
      
      if (isSignatureMatch(method, validBinaryParams))
      {


        assertUnset(onBinary, OnWebSocketMessage.class, method);
        onBinary = new OptionalSessionCallableMethod(pojo, method);
        return;
      }
      
      throw InvalidSignatureException.build(method, OnWebSocketMessage.class, new ParamList[] { validTextParams, validBinaryParams });
    }
    
    if (isAnnotation(annotation, OnWebSocketClose.class))
    {
      assertValidSignature(method, OnWebSocketClose.class, validCloseParams);
      assertUnset(onClose, OnWebSocketClose.class, method);
      onClose = new OptionalSessionCallableMethod(pojo, method);
      return;
    }
    
    if (isAnnotation(annotation, OnWebSocketError.class))
    {
      assertValidSignature(method, OnWebSocketError.class, validErrorParams);
      assertUnset(onError, OnWebSocketError.class, method);
      onError = new OptionalSessionCallableMethod(pojo, method);
      return;
    }
    
    if (isAnnotation(annotation, OnWebSocketFrame.class))
    {
      assertValidSignature(method, OnWebSocketFrame.class, validFrameParams);
      assertUnset(onFrame, OnWebSocketFrame.class, method);
      onFrame = new OptionalSessionCallableMethod(pojo, method);
      return;
    }
  }
  
  public JettyAnnotatedMetadata scan(Class<?> pojo)
  {
    JettyAnnotatedMetadata metadata = new JettyAnnotatedMetadata();
    scanMethodAnnotations(metadata, pojo);
    return metadata;
  }
  
  public JettyAnnotatedScanner() {}
}

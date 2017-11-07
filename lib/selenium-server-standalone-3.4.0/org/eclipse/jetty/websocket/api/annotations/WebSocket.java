package org.eclipse.jetty.websocket.api.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.eclipse.jetty.websocket.api.BatchMode;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
public @interface WebSocket
{
  int inputBufferSize() default -2;
  
  int maxBinaryMessageSize() default -2;
  
  int maxIdleTime() default -2;
  
  int maxTextMessageSize() default -2;
  
  BatchMode batchMode() default BatchMode.AUTO;
}

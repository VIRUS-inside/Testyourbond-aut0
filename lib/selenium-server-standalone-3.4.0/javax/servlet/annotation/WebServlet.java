package javax.servlet.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet
{
  String name() default "";
  
  String[] value() default {};
  
  String[] urlPatterns() default {};
  
  int loadOnStartup() default -1;
  
  WebInitParam[] initParams() default {};
  
  boolean asyncSupported() default false;
  
  String smallIcon() default "";
  
  String largeIcon() default "";
  
  String description() default "";
  
  String displayName() default "";
}

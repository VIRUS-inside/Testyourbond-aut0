package org.openqa.selenium.support;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.TYPE})
public @interface FindBy
{
  How how() default How.UNSET;
  
  String using() default "";
  
  String id() default "";
  
  String name() default "";
  
  String className() default "";
  
  String css() default "";
  
  String tagName() default "";
  
  String linkText() default "";
  
  String partialLinkText() default "";
  
  String xpath() default "";
}
